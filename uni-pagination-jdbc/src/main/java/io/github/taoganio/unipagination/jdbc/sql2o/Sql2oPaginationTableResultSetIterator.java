package io.github.taoganio.unipagination.jdbc.sql2o;

import org.sql2o.ResultSetIteratorBase;
import org.sql2o.Sql2oException;
import org.sql2o.data.Column;
import org.sql2o.quirks.Quirks;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Sql2oPaginationTableResultSetIterator extends ResultSetIteratorBase<Sql2oRow> {

    private final Map<String, Integer> columnNameToIdxMap = new HashMap<>();

    public Sql2oPaginationTableResultSetIterator(ResultSet rs, boolean isCaseSensitive, Quirks quirks) {
        super(rs, isCaseSensitive, quirks);
        try {
            for (int colIdx = 1; colIdx <= this.meta.getColumnCount(); ++colIdx) {
                Column column = createColumn(this.meta, colIdx);
                String colName = column.getName();
                String colMapName = isCaseSensitive ? colName : colName.toLowerCase();
                this.columnNameToIdxMap.put(colMapName, colIdx - 1);
            }
        } catch (SQLException e) {
            throw new Sql2oException("Error while reading metadata from database", e);
        }
    }

    protected Column createColumn(ResultSetMetaData meta, int colIdx) throws SQLException {
        String colName = this.getColumnName(colIdx);
        String colClassName = meta.getColumnClassName(colIdx);
        return new Column(colName, colIdx - 1, colClassName);
    }

    protected Sql2oRow readNext() throws SQLException {
        return this.createRow(this.rs);
    }

    protected Sql2oRow createRow(ResultSet rs) throws SQLException {
        int columnCount = columnNameToIdxMap.values().size();
        Sql2oRow row = new Sql2oRow(this.columnNameToIdxMap, columnCount, this.isCaseSensitive, this.quirks);
        for (int index : columnNameToIdxMap.values()) {
            row.addValue(index, this.quirks.getRSVal(rs, index + 1));
        }
        return row;
    }
}
