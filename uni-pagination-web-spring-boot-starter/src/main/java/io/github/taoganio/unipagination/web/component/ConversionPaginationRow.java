package io.github.taoganio.unipagination.web.component;

import io.github.taoganio.unipagination.result.set.ColumnMetadata;
import io.github.taoganio.unipagination.result.set.PaginationResultSetMetadata;
import io.github.taoganio.unipagination.result.set.PaginationRow;
import lombok.Setter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public abstract class ConversionPaginationRow implements PaginationRow {

    @Nullable
    private PaginationResultSetMetadata metadata;

    @Nullable
    private volatile ConfigurableConversionService conversionService;

    @Setter
    private boolean conversion = true;

    public ConfigurableConversionService getConversionService() {
        ConfigurableConversionService cs = this.conversionService;
        if (cs == null) {
            synchronized (this) {
                cs = this.conversionService;
                if (cs == null) {
                    cs = new DefaultConversionService();
                    converterRegistry(cs);
                    this.conversionService = cs;
                }
            }
        }
        return cs;
    }

    /**
     * 转换器注册表
     *
     * @param converterRegistry 转换器注册表
     */
    protected void converterRegistry(ConverterRegistry converterRegistry) {
    }

    public void setConversionService(ConfigurableConversionService conversionService) {
        Assert.notNull(conversionService, "ConversionService must not be null");
        this.conversionService = conversionService;
    }

    public void setMetadata(@Nullable PaginationResultSetMetadata metadata) {
        Assert.notNull(metadata, "PaginationResultSetMetadata must not be null");
        this.metadata = metadata;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected <T> T convertValueIfNecessary(@Nullable ColumnMetadata columnMetadata,
                                            @Nullable Object value, @Nullable Class<T> targetType) {
        if (!this.conversion) {
            return (T) value;
        }
        if (value == null) {
            return null;
        }
        if (targetType == null) {
            return (T) value;
        }
        ConversionService conversionServiceToUse = getConversionService();
        if (conversionServiceToUse == null) {
            if (ClassUtils.isAssignableValue(targetType, value)) {
                return (T) value;
            }
            conversionServiceToUse = DefaultConversionService.getSharedInstance();
        }
        if (columnMetadata != null) {
            return (T) conversionServiceToUse.convert(value,
                    TypeDescriptor.valueOf(columnMetadata.getColumnType()), TypeDescriptor.valueOf(targetType));
        }
        return conversionServiceToUse.convert(value, targetType);
    }

    @Nullable
    protected ColumnMetadata getColumnMetadata(int columnIndex) {
        if (metadata != null) {
            return metadata.getColumnMetadata(columnIndex);
        }
        return null;
    }

    @Nullable
    protected ColumnMetadata getColumnMetadata(String columnName) {
        if (metadata != null) {
            return metadata.getColumnMetadata(columnName);
        }
        return null;
    }

    @Override
    public byte[] getBytes(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), byte[].class);
    }

    @Override
    public byte[] getBytes(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), byte[].class);
    }

    @Override
    public Byte getByte(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), Byte.class);
    }

    @Override
    public Byte getByte(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), Byte.class);
    }

    @Override
    public Short getShort(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), Short.class);
    }

    @Override
    public Short getShort(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), Short.class);
    }

    @Override
    public Integer getInteger(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), Integer.class);
    }

    @Override
    public Integer getInteger(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), Integer.class);
    }

    @Override
    public Float getFloat(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), Float.class);
    }

    @Override
    public Float getFloat(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), Float.class);
    }

    @Override
    public Double getDouble(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), Double.class);
    }

    @Override
    public Double getDouble(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), Double.class);
    }

    @Override
    public Long getLong(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), Long.class);
    }

    @Override
    public Long getLong(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), Long.class);
    }

    @Override
    public Boolean getBoolean(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), Boolean.class);
    }

    @Override
    public Boolean getBoolean(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), Boolean.class);
    }

    @Override
    public String getString(int columnIndex) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), String.class);
    }

    @Override
    public String getString(String columnName) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), String.class);
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> clazz) {
        return convertValueIfNecessary(getColumnMetadata(columnIndex), getObject(columnIndex), clazz);
    }

    @Override
    public <T> T getObject(String columnName, Class<T> clazz) {
        return convertValueIfNecessary(getColumnMetadata(columnName), getObject(columnName), clazz);
    }
}
