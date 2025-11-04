package io.github.taoganio.unipagination.result.set;

import jakarta.annotation.Nullable;

/**
 * 分页行集
 */
public interface PaginationRow {

    int getColumnCount();

    @Nullable
    byte[] getBytes(int columnIndex);

    @Nullable
    byte[] getBytes(String columnName);

    @Nullable
    Byte getByte(int columnIndex);

    @Nullable
    Byte getByte(String columnName);

    @Nullable
    Short getShort(int columnIndex);

    @Nullable
    Short getShort(String columnName);

    @Nullable
    Integer getInteger(int columnIndex);

    @Nullable
    Integer getInteger(String columnName);

    @Nullable
    Float getFloat(int columnIndex);

    @Nullable
    Float getFloat(String columnName);

    @Nullable
    Double getDouble(int columnIndex);

    @Nullable
    Double getDouble(String columnName);

    @Nullable
    Long getLong(int columnIndex);

    @Nullable
    Long getLong(String columnName);

    @Nullable
    Boolean getBoolean(int columnIndex);

    @Nullable
    Boolean getBoolean(String columnName);

    @Nullable
    String getString(int columnIndex);

    @Nullable
    String getString(String columnName);

    @Nullable
    Object getObject(int columnIndex);

    @Nullable
    Object getObject(String columnName);

    @Nullable
    <T> T getObject(int columnIndex, Class<T> clazz);

    @Nullable
    <T> T getObject(String columnName, Class<T> clazz);
}
