package io.github.taoganio.unipagination.mongodb;

import io.github.taoganio.unipagination.result.set.PaginationResultSetMetadata;
import io.github.taoganio.unipagination.result.set.PaginationResultSetMetadataSetter;
import io.github.taoganio.unipagination.result.set.PaginationRow;
import org.bson.Document;

/**
 * MongoDB 行
 */
public class MongoPaginationRow implements PaginationRow, PaginationResultSetMetadataSetter {

    private final Document document;
    private PaginationResultSetMetadata metadata;

    public MongoPaginationRow(Document document, PaginationResultSetMetadata metadata) {
        this.document = document;
        this.metadata = metadata;
    }

    @Override
    public void setMetadata(PaginationResultSetMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public int getColumnCount() {
        return metadata.getColumnCount();
    }

    @Override
    public byte[] getBytes(int columnIndex) {
        Object v = getObject(columnIndex);
        return v instanceof byte[] ? (byte[]) v : null;
    }

    @Override
    public byte[] getBytes(String columnName) {
        Object v = getObject(columnName);
        return v instanceof byte[] ? (byte[]) v : null;
    }

    @Override
    public Byte getByte(int columnIndex) {
        return getObject(columnIndex, Byte.class);
    }

    @Override
    public Byte getByte(String columnName) {
        return getObject(columnName, Byte.class);
    }

    @Override
    public Short getShort(int columnIndex) {
        return getObject(columnIndex, Short.class);
    }

    @Override
    public Short getShort(String columnName) {
        return getObject(columnName, Short.class);
    }

    @Override
    public Integer getInteger(int columnIndex) {
        return getObject(columnIndex, Integer.class);
    }

    @Override
    public Integer getInteger(String columnName) {
        return getObject(columnName, Integer.class);
    }

    @Override
    public Float getFloat(int columnIndex) {
        return getObject(columnIndex, Float.class);
    }

    @Override
    public Float getFloat(String columnName) {
        return getObject(columnName, Float.class);
    }

    @Override
    public Double getDouble(int columnIndex) {
        return getObject(columnIndex, Double.class);
    }

    @Override
    public Double getDouble(String columnName) {
        return getObject(columnName, Double.class);
    }

    @Override
    public Long getLong(int columnIndex) {
        return getObject(columnIndex, Long.class);
    }

    @Override
    public Long getLong(String columnName) {
        return getObject(columnName, Long.class);
    }

    @Override
    public Boolean getBoolean(int columnIndex) {
        return getObject(columnIndex, Boolean.class);
    }

    @Override
    public Boolean getBoolean(String columnName) {
        return getObject(columnName, Boolean.class);
    }

    @Override
    public String getString(int columnIndex) {
        Object v = getObject(columnIndex);
        return v != null ? String.valueOf(v) : null;
    }

    @Override
    public String getString(String columnName) {
        Object v = getObject(columnName);
        return v != null ? String.valueOf(v) : null;
    }

    @Override
    public Object getObject(int columnIndex) {
        String name = metadata.getColumnMetadata(columnIndex).getColumnName();
        return document.get(name);
    }

    @Override
    public Object getObject(String columnName) {
        return document.get(columnName);
    }

    @Override
    public <T> T getObject(String columnName, Class<T> clazz) {
        return document.get(columnName, clazz);
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> clazz) {
        String name = metadata.getColumnMetadata(columnIndex).getColumnName();
        return document.get(name, clazz);
    }
}


