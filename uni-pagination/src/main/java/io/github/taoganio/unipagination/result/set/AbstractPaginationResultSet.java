package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.exception.PaginationException;

import java.util.Iterator;

/**
 * 抽象分页结果集
 */
public abstract class AbstractPaginationResultSet implements PaginationResultSet {

    private volatile boolean closed;

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() throws Exception {
        if (!closed) {
            synchronized (this) {
                if (!closed) {
                    doClose();
                    closed = true;
                }
            }
        }
    }

    @Override
    public Iterator<PaginationRow> iterator() {
        if (isClosed()) {
            throw new PaginationException("PaginationResultSet is closed");
        }
        return doIterator();
    }

    protected abstract Iterator<PaginationRow> doIterator();

    protected void doClose() throws Exception {

    }
}
