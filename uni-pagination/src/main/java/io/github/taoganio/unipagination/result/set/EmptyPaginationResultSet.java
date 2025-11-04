package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.util.EmptyIterator;

import java.util.Iterator;

/**
 * 没有任何数据的结果集
 */
public class EmptyPaginationResultSet implements PaginationResultSet {
    public final PaginationResultSetMetadata metadata;

    public EmptyPaginationResultSet(Pageable pageable) {
        this(new EmptyPaginationResultSetMetadata(pageable));
    }

    public EmptyPaginationResultSet(PaginationResultSetMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public long getTotal() {
        return 0;
    }

    @Override
    public boolean isClosed() {
        return true;
    }

    @Override
    public PaginationResultSetMetadata getMetadata() {
        return metadata;
    }


    @Override
    public Iterator<PaginationRow> iterator() {
        return new EmptyIterator<>();
    }

    @Override
    public void close() throws Exception {

    }
}