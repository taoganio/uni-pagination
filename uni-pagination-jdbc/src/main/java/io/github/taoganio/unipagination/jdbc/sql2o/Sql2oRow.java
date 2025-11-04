package io.github.taoganio.unipagination.jdbc.sql2o;

import org.sql2o.Sql2oException;
import org.sql2o.converters.Convert;
import org.sql2o.converters.ConverterException;
import org.sql2o.data.Row;
import org.sql2o.quirks.Quirks;

import java.math.BigDecimal;
import java.util.*;

public class Sql2oRow extends Row {

    private final Object[] values;
    private final boolean isCaseSensitive;
    private final Quirks quirks;
    private final Map<String, Integer> columnNameToIdxMap;

    public Sql2oRow(Map<String, Integer> columnNameToIdxMap, int columnCnt, boolean isCaseSensitive, Quirks quirks) {
        super(columnNameToIdxMap, columnCnt, isCaseSensitive, quirks);
        this.columnNameToIdxMap = Collections.unmodifiableMap(columnNameToIdxMap);
        this.isCaseSensitive = isCaseSensitive;
        this.quirks = quirks;
        this.values = new Object[columnCnt];
    }

    public void addValue(int columnIndex, Object value) {
        this.values[columnIndex] = value;
    }

    public Object getObject(int columnIndex) {
        return this.values[columnIndex];
    }

    public Object getObject(String columnName) {
        Integer index = this.columnNameToIdxMap.get(this.isCaseSensitive ? columnName : columnName.toLowerCase());
        if (index != null) {
            return this.getObject(index);
        } else {
            throw new Sql2oException(String.format("Column with name '%s' does not exist", columnName));
        }
    }

    public <V> V getObject(int columnIndex, Class<V> clazz) {
        try {
            return Convert.throwIfNull(clazz, this.quirks.converterOf(clazz)).convert(this.getObject(columnIndex));
        } catch (ConverterException var4) {
            throw new Sql2oException("Error converting value", var4);
        }
    }

    public <V> V getObject(String columnName, Class<V> clazz) {
        try {
            return Convert.throwIfNull(clazz, this.quirks.converterOf(clazz)).convert(this.getObject(columnName));
        } catch (ConverterException var4) {
            throw new Sql2oException("Error converting value", var4);
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) {
        return this.getObject(columnIndex, BigDecimal.class);
    }

    public BigDecimal getBigDecimal(String columnName) {
        return this.getObject(columnName, BigDecimal.class);
    }

    public Boolean getBoolean(int columnIndex) {
        return this.getObject(columnIndex, Boolean.class);
    }

    public Boolean getBoolean(String columnName) {
        return this.getObject(columnName, Boolean.class);
    }

    public Double getDouble(int columnIndex) {
        return this.getObject(columnIndex, Double.class);
    }

    public Double getDouble(String columnName) {
        return this.getObject(columnName, Double.class);
    }

    public Float getFloat(int columnIndex) {
        return this.getObject(columnIndex, Float.class);
    }

    public Float getFloat(String columnName) {
        return this.getObject(columnName, Float.class);
    }

    public Long getLong(int columnIndex) {
        return this.getObject(columnIndex, Long.class);
    }

    public Long getLong(String columnName) {
        return this.getObject(columnName, Long.class);
    }

    public Integer getInteger(int columnIndex) {
        return this.getObject(columnIndex, Integer.class);
    }

    public Integer getInteger(String columnName) {
        return this.getObject(columnName, Integer.class);
    }

    public Short getShort(int columnIndex) {
        return this.getObject(columnIndex, Short.class);
    }

    public Short getShort(String columnName) {
        return this.getObject(columnName, Short.class);
    }

    public Byte getByte(int columnIndex) {
        return this.getObject(columnIndex, Byte.class);
    }

    public Byte getByte(String columnName) {
        return this.getObject(columnName, Byte.class);
    }

    public Date getDate(int columnIndex) {
        return this.getObject(columnIndex, Date.class);
    }

    public Date getDate(String columnName) {
        return this.getObject(columnName, Date.class);
    }

    public String getString(int columnIndex) {
        return this.getObject(columnIndex, String.class);
    }

    public String getString(String columnName) {
        return this.getObject(columnName, String.class);
    }

    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        Set<String> keys = this.columnNameToIdxMap.keySet();
        for (String key : keys) {
            int index = this.columnNameToIdxMap.get(key);
            map.put(key, this.values[index]);
        }

        return map;
    }
}