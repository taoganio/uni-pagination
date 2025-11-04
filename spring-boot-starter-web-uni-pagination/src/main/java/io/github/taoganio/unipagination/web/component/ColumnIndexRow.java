package io.github.taoganio.unipagination.web.component;

import io.github.taoganio.unipagination.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 映射器行集
 */
public class ColumnIndexRow extends ConversionPaginationRow {
    /**
     * 行名称索引
     */
    private final Map<String, Integer> columnNameIndex;
    /**
     * 行索引值
     */
    private final Map<Integer, Object> indexValue;

    public ColumnIndexRow(Map<String, Object> row) {
        Map<String, Integer> columnNameIndex = new LinkedCaseInsensitiveMap<>();
        Map<Integer, Object> indexValue = new HashMap<>();
        int index = 0;
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            columnNameIndex.put(entry.getKey(), index);
            indexValue.put(index, entry.getValue());
            index++;
        }
        this.columnNameIndex = columnNameIndex;
        this.indexValue = indexValue;
    }

    public ColumnIndexRow(Map<String, Integer> columnNameIndex, Map<Integer, Object> indexValue) {
        Assert.notNull(indexValue, "indexValue must not be null");
        this.columnNameIndex = Collections.unmodifiableMap(columnNameIndex);
        this.indexValue = indexValue;
    }

    private Object getColumnNameValue(String columnName) {
        Integer index = columnNameIndex.get(columnName);
        Assert.notNull(index, "Invalid column name: " + columnName);
        return getColumnIndexValue(index);
    }

    private Object getColumnIndexValue(Integer index) {
        if (!indexValue.containsKey(index)) {
            throw new IllegalArgumentException("Invalid column index: " + index);
        }
        return indexValue.get(index);
    }

    @Override
    public int getColumnCount() {
        return columnNameIndex.size();
    }

    @Override
    public Object getObject(int columnIndex) {
        return getColumnIndexValue(columnIndex);
    }

    @Override
    public Object getObject(String columnName) {
        return getColumnNameValue(columnName);
    }

}