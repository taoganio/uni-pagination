package io.github.taoganio.web.test.utils;

import io.github.taoganio.unipagination.result.set.ColumnMetadata;
import io.github.taoganio.unipagination.result.set.DefaultColumnMetadata;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 元数据行映射器结果集提取程序
 */
public class MetadataRowMapperResultSetExtractor<T> extends RowMapperResultSetExtractor<T> {

    private final List<ColumnMetadata> columnMetadataList = new ArrayList<>();

    public MetadataRowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        super(rowMapper);
    }

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            int columnIndex = i + 1;
            String columnName = getColumnName(rsmd, columnIndex);
            Class<?> columnType = getColumnType(rsmd, columnIndex);
            columnMetadataList.add(new DefaultColumnMetadata(columnName, columnType, i));
        }
        return super.extractData(rs);
    }

    protected String getColumnName(ResultSetMetaData rsmd, int columnIndex) throws SQLException {
        return JdbcUtils.lookupColumnName(rsmd, columnIndex);
    }

    protected Class<?> getColumnType(ResultSetMetaData rsmd, int columnIndex) throws SQLException {
        try {
            return Class.forName(rsmd.getColumnClassName(columnIndex));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ColumnMetadata> getColumnMetadataList() {
        return Collections.unmodifiableList(columnMetadataList);
    }
}
