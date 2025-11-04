package io.github.taoganio.unipagination.web.component;

import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.result.set.ColumnMetadata;
import io.github.taoganio.unipagination.result.set.PaginationResultSetMetadata;
import io.github.taoganio.unipagination.result.set.PaginationRow;
import io.github.taoganio.unipagination.result.set.PaginationRowMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * 对象属性结果处理程序
 */
public class BeanPropertyPaginationRowMapper<T> implements PaginationRowMapper<T> {

    protected final Log logger = LogFactory.getLog(getClass());
    @Nullable
    private Class<T> mappedClass;
    @Getter
    private boolean checkFullyPopulated = false;
    @Getter
    @Setter
    private boolean primitivesDefaultedForNullValue = false;
    @Nullable
    private ConversionService conversionService = DefaultConversionService.getSharedInstance();
    @Nullable
    private Map<String, PropertyDescriptor> mappedProperties;
    @Nullable
    private Set<String> mappedPropertyNames;

    public BeanPropertyPaginationRowMapper(Class<T> mappedClass) {
        initialize(mappedClass);
    }

    public BeanPropertyPaginationRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public void setMappedClass(Class<T> mappedClass) {
        if (this.mappedClass == null) {
            initialize(mappedClass);
        } else {
            if (this.mappedClass != mappedClass) {
                throw new IllegalArgumentException("The mapped class can not be reassigned to map to " +
                        mappedClass + " since it is already providing mapping for " + this.mappedClass);
            }
        }
    }


    @Nullable
    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    public void setConversionService(@Nullable ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Nullable
    public ConversionService getConversionService() {
        return this.conversionService;
    }


    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedProperties = new HashMap<>();
        this.mappedPropertyNames = new HashSet<>();

        for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(mappedClass)) {
            if (pd.getWriteMethod() != null) {
                String lowerCaseName = lowerCaseName(pd.getName());
                this.mappedProperties.put(lowerCaseName, pd);
                String underscoreName = underscoreName(pd.getName());
                if (!lowerCaseName.equals(underscoreName)) {
                    this.mappedProperties.put(underscoreName, pd);
                }
                this.mappedPropertyNames.add(pd.getName());
            }
        }
    }

    protected void suppressProperty(String propertyName) {
        if (this.mappedProperties != null) {
            this.mappedProperties.remove(lowerCaseName(propertyName));
            this.mappedProperties.remove(underscoreName(propertyName));
        }
    }

    protected String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }


    protected String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(name.charAt(0)));
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_').append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    @Override
    public T mapRow(PaginationResultSetMetadata metadata, PaginationRow prs, int rowNumber) throws PaginationException {
        BeanWrapperImpl bw = new BeanWrapperImpl();
        initBeanWrapper(bw);

        T mappedObject = constructMappedInstance(prs, bw);
        bw.setBeanInstance(mappedObject);

        int columnCount = metadata.getColumnCount();
        Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<>() : null);

        for (int index = 0; index < columnCount; index++) {
            ColumnMetadata columnMetadata = metadata.getColumnMetadata(index);
            String column = columnMetadata.getColumnName();
            String property = lowerCaseName(StringUtils.delete(column, " "));
            PropertyDescriptor pd = (this.mappedProperties != null ? this.mappedProperties.get(property) : null);
            if (pd != null) {
                try {
                    Object value = getColumnValue(metadata, prs, index, property, pd);

                    if (rowNumber == 0 && logger.isDebugEnabled()) {
                        logger.debug("Mapping column '" + column + "' to property '" + pd.getName() +
                                "' of type '" + ClassUtils.getQualifiedName(pd.getPropertyType()) + "'");
                    }
                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (TypeMismatchException ex) {
                        if (value == null && this.primitivesDefaultedForNullValue) {
                            if (logger.isDebugEnabled()) {
                                String propertyType = ClassUtils.getQualifiedName(pd.getPropertyType());
                                logger.debug(String.format(
                                        "Ignoring intercepted TypeMismatchException for row %d and column '%s' " +
                                                "with null value when setting property '%s' of type '%s' on object: %s",
                                        rowNumber, column, pd.getName(), propertyType, mappedObject), ex);
                            }
                        } else {
                            throw ex;
                        }
                    }
                    if (populatedProperties != null) {
                        populatedProperties.add(pd.getName());
                    }
                } catch (NotWritablePropertyException ex) {
                    throw new IllegalArgumentException(
                            "Unable to map column '" + column + "' to property '" + pd.getName() + "'", ex);
                }
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedPropertyNames)) {
            throw new IllegalArgumentException("Given ResultSet does not contain all properties " +
                    "necessary to populate object of " + this.mappedClass + ": " + this.mappedPropertyNames);
        }

        return mappedObject;
    }

    protected Object getColumnValue(PaginationResultSetMetadata metadata, PaginationRow prs,
                                    int index, String name, PropertyDescriptor pd) {
        return prs.getObject(index);
    }

    protected T constructMappedInstance(PaginationRow rs, TypeConverter tc) throws PaginationException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        return BeanUtils.instantiateClass(this.mappedClass);
    }

    protected void initBeanWrapper(BeanWrapper bw) {
        ConversionService cs = getConversionService();
        if (cs != null) {
            bw.setConversionService(cs);
        }
    }

    public static <T> BeanPropertyPaginationRowMapper<T> newInstance(Class<T> mappedClass) {
        return new BeanPropertyPaginationRowMapper<>(mappedClass);
    }

    public static <T> BeanPropertyPaginationRowMapper<T> newInstance(
            Class<T> mappedClass, @Nullable ConversionService conversionService) {

        BeanPropertyPaginationRowMapper<T> rowMapper = newInstance(mappedClass);
        rowMapper.setConversionService(conversionService);
        return rowMapper;
    }
}
