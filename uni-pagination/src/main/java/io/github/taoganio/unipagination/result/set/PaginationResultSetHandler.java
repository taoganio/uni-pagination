package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.result.PaginationResultHandler;
import io.github.taoganio.unipagination.domain.Page;
import io.github.taoganio.unipagination.exception.PaginationException;

/**
 * 分页结果处理程序
 */
public interface PaginationResultSetHandler<T> extends PaginationResultHandler<PaginationResultSet, Page<T>> {
    /**
     * 处理
     *
     * @param resultSet 结果集
     */
    Page<T> handle(PaginationResultSet resultSet) throws PaginationException;
}
