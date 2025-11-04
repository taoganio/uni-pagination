package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.util.LinkedCaseInsensitiveMap;
import jakarta.annotation.Nullable;

import java.util.Map;

public class MapPaginationRowMapper implements PaginationRowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> mapRow(PaginationResultSetMetadata metadata,
                                      PaginationRow prs,
                                      int rowNum) throws PaginationException {
        int columnCount = metadata.getColumnCount();
        Map<String, Object> mapOfColumnValues = createColumnMap(columnCount);
        for (int i = 0; i < columnCount; i++) {
            ColumnMetadata columnMetadata = metadata.getColumnMetadata(i);
            String column = columnMetadata.getColumnName();
            mapOfColumnValues.putIfAbsent(getColumnKey(column), getColumnValue(prs, i));
        }
        return mapOfColumnValues;
    }

    protected Map<String, Object> createColumnMap(int columnCount) {
        return new LinkedCaseInsensitiveMap<>(columnCount);
    }

    protected String getColumnKey(String columnName) {
        return columnName;
    }

    @Nullable
    protected Object getColumnValue(PaginationRow rs, int index) throws PaginationException {
        return rs.getObject(index);
    }


}
