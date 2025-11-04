package io.github.taoganio.unipagination.jdbc.sql2o;

import io.github.taoganio.unipagination.jdbc.ResultSetMetadataColumnFactory;
import io.github.taoganio.unipagination.jdbc.Sql2oPaginationRow;
import io.github.taoganio.unipagination.result.set.PaginationRow;
import org.sql2o.ResultSetHandler;
import org.sql2o.Sql2oException;
import org.sql2o.data.Column;
import org.sql2o.quirks.Quirks;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Sql2oPaginationResultSetHandler implements ResultSetHandler<PaginationRow> {

    private final Map<String, Integer> columnNameToIdxMap = new HashMap<>();
    private final boolean isCaseSensitive;
    private final Quirks quirks;

    public Sql2oPaginationResultSetHandler(ResultSetMetaData meta, boolean isCaseSensitive, Quirks quirks) {
        try {
            for (int colIdx = 1; colIdx <= meta.getColumnCount(); ++colIdx) {
                Column column = createColumn(meta, colIdx);
                String colName = column.getName();
                String colMapName = isCaseSensitive ? colName : colName.toLowerCase();
                this.columnNameToIdxMap.put(colMapName, colIdx - 1);
            }
            this.isCaseSensitive = isCaseSensitive;
            this.quirks = quirks;
        } catch (SQLException e) {
            throw new Sql2oException("Error while reading metadata from database", e);
        }
    }

    protected Column createColumn(ResultSetMetaData meta, int colIdx) throws SQLException {
        String colName = ResultSetMetadataColumnFactory.lookupColumnName(meta, colIdx);
        String colClassName = meta.getColumnClassName(colIdx);
        return new Column(colName, colIdx - 1, colClassName);
    }

    @Override
    public PaginationRow handle(ResultSet rs) throws SQLException {
        int columnCount = columnNameToIdxMap.size();
        Sql2oRow row = new Sql2oRow(this.columnNameToIdxMap, columnCount, this.isCaseSensitive, this.quirks);
        for (Integer idx : this.columnNameToIdxMap.values()) {
            row.addValue(idx, this.quirks.getRSVal(rs, idx + 1));
        }
        return new Sql2oPaginationRow(columnCount, row);
    }
}
