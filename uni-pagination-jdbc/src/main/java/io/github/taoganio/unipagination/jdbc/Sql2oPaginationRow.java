package io.github.taoganio.unipagination.jdbc;

import io.github.taoganio.unipagination.result.set.PaginationRow;
import io.github.taoganio.unipagination.util.Assert;
import jakarta.annotation.Nullable;
import org.sql2o.data.Row;

/**
 * JDBC结果集行
 */
public class Sql2oPaginationRow implements PaginationRow {

    private final int columnCount;
    private final Row row;

    public Sql2oPaginationRow(int columnCount, Row row) {
        Assert.notNull(row, "Row must not be null");
        this.columnCount = columnCount;
        this.row = row;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Byte getByte(int columnIndex) {
        return row.getByte(columnIndex);
    }

    @Override
    public Byte getByte(String columnName) {
        return row.getByte(columnName);
    }

    @Override
    public byte[] getBytes(int columnIndex) {
        return row.getObject(columnIndex, byte[].class);
    }

    @Override
    public byte[] getBytes(String columnName) {
        return row.getObject(columnName, byte[].class);
    }

    @Override
    public Short getShort(int columnIndex) {
        return row.getShort(columnIndex);
    }

    @Override
    public Short getShort(String columnName) {
        return row.getShort(columnName);
    }

    @Override
    public Integer getInteger(int columnIndex) {
        return row.getInteger(columnIndex);
    }

    @Override
    public Integer getInteger(String columnName) {
        return row.getInteger(columnName);
    }

    @Override
    public Float getFloat(int columnIndex) {
        return row.getFloat(columnIndex);
    }

    @Override
    public Float getFloat(String columnName) {
        return row.getFloat(columnName);
    }

    @Override
    public Double getDouble(int columnIndex) {
        return row.getDouble(columnIndex);
    }

    @Override
    public Double getDouble(String columnName) {
        return row.getDouble(columnName);
    }

    @Override
    public Long getLong(int columnIndex) {
        return row.getLong(columnIndex);
    }

    @Override
    public Long getLong(String columnName) {
        return row.getLong(columnName);
    }

    @Override
    public Boolean getBoolean(int columnIndex) {
        return row.getBoolean(columnIndex);
    }

    @Override
    public Boolean getBoolean(String columnName) {
        return row.getBoolean(columnName);
    }

    @Override
    public String getString(int columnIndex) {
        return row.getString(columnIndex);
    }

    @Override
    public String getString(String columnName) {
        return row.getString(columnName);
    }

    @Override
    public Object getObject(int columnIndex) {
        return row.getObject(columnIndex);
    }

    @Override
    public Object getObject(String columnName) {
        return row.getObject(columnName);
    }

    @Nullable
    @Override
    public <T> T getObject(int columnIndex, Class<T> clazz) {
        return row.getObject(columnIndex, clazz);
    }

    @Nullable
    @Override
    public <T> T getObject(String columnName, Class<T> clazz) {
        return row.getObject(columnName, clazz);
    }

}
