package io.github.taoganio.unipagination.result.set;

import java.util.Objects;

/**
 * 列元数据默认实现
 */
public class DefaultColumnMetadata implements ColumnMetadata {

    private final String columnName;
    private final Class<?> columnType;
    private final int order;

    public DefaultColumnMetadata(String columnName, Class<?> columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.order = 0;
    }

    public DefaultColumnMetadata(String columnName, Class<?> columnType, int order) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.order = order;
    }


    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Class<?> getColumnType() {
        return columnType;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultColumnMetadata that = (DefaultColumnMetadata) o;
        return Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName);
    }
}
