package io.github.taoganio.unipagination.statement;

import io.github.taoganio.unipagination.domain.Pageable;

/**
 * 没有结果的分页语句
 */
public class EmptyPaginationStatement implements PaginationStatement {

    private static final EmptyPaginationStatement INSTANCE = new EmptyPaginationStatement();

    private EmptyPaginationStatement() {
    }

    public static EmptyPaginationStatement getInstance() {
        return INSTANCE;
    }

    @Override
    public Object getNativeStatement() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pageable getPageable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPageable(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

}
