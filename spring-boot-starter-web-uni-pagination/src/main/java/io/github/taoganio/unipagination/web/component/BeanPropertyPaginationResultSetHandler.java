package io.github.taoganio.unipagination.web.component;


import io.github.taoganio.unipagination.result.set.AbstractPaginationContentHandler;

/**
 * 对象属性分页结果集处理程序
 */
public class BeanPropertyPaginationResultSetHandler<T> extends AbstractPaginationContentHandler<T> {

    private final Class<T> mapperClass;

    public BeanPropertyPaginationResultSetHandler(Class<T> mapperClass) {
        super(new BeanPropertyPaginationRowMapper<>(mapperClass));
        this.mapperClass = mapperClass;
    }

    public Class<T> getMapperClass() {
        return mapperClass;
    }

    public static <T> BeanPropertyPaginationResultSetHandler<T> newInstance(Class<T> mapperClass) {
        return new BeanPropertyPaginationResultSetHandler<>(mapperClass);
    }
}
