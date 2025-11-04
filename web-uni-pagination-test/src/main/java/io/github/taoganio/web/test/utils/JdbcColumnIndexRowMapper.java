package io.github.taoganio.web.test.utils;

import io.github.taoganio.unipagination.web.component.ColumnIndexRow;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 列行设置映射器
 */
public class JdbcColumnIndexRowMapper implements RowMapper<ColumnIndexRow> {

    @Override
    public ColumnIndexRow mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Integer> columnNameIndex = createColumnNameIndexMap();
        Map<Integer, Object> indexValue = createIndexValueMap();
        for (int i = 1; i <= columnCount; i++) {
            int columnIndex = i - 1;
            String columnName = getColumnName(rsmd, i);
            Object columnValue = getColumnValue(rs, i);
            columnNameIndex.put(columnName, columnIndex);
            indexValue.put(columnIndex, columnValue);
        }
        return createColumnIndexRowSetMapper(columnNameIndex, indexValue);
    }

    protected ColumnIndexRow createColumnIndexRowSetMapper(Map<String, Integer> columnNameIndex,
                                                           Map<Integer, Object> indexValue) {
        return new ColumnIndexRow(columnNameIndex, indexValue);
    }

    protected Map<Integer, Object> createIndexValueMap() {
        return new HashMap<>();
    }

    protected Map<String, Integer> createColumnNameIndexMap() {
        return new LinkedCaseInsensitiveMap<>();
    }

    protected String getColumnName(ResultSetMetaData rsmd, int columnIndex) throws SQLException {
        return JdbcUtils.lookupColumnName(rsmd, columnIndex);
    }

    @Nullable
    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }

}
