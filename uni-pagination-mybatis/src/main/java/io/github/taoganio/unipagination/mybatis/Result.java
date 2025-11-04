package io.github.taoganio.unipagination.mybatis;

import io.github.taoganio.unipagination.result.set.ColumnMetadata;
import io.github.taoganio.unipagination.result.set.PaginationRow;

import java.util.List;

/**
 * 执行结果
 */
class Result {

    private final List<ColumnMetadata> columns;
    private final List<PaginationRow> rows;

    Result(List<ColumnMetadata> columns, List<PaginationRow> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    public List<PaginationRow> getRows() {
        return rows;
    }
}
