package io.github.taoganio.unipagination.statement;

import io.github.taoganio.unipagination.domain.Pageable;

/**
 * 分页基础语句
 */
public abstract class BasePaginationStatement implements PaginationStatement {

    private Pageable pageable;

    /**
     * 分页基础语句
     *
     * @param pageable 可分页
     */
    protected BasePaginationStatement(Pageable pageable) {
        this.pageable = pageable;
    }


    @Override
    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
