package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.domain.Page;
import io.github.taoganio.unipagination.domain.PageImpl;
import io.github.taoganio.unipagination.exception.PaginationException;

import java.util.List;

/**
 * 分页内容处理程序
 */
public abstract class AbstractPaginationContentHandler<T> implements PaginationResultSetHandler<T> {

    private final PaginationResultSetHandler<T> handler;

    protected AbstractPaginationContentHandler(PaginationRowMapper<T> rowMapper) {
        this(new PaginationRowMapperResultSetHandler<>(rowMapper));
    }

    protected AbstractPaginationContentHandler(PaginationResultSetHandler<T> handler) {
        this.handler = handler;
    }

    @Override
    public Page<T> handle(PaginationResultSet resultSet) throws PaginationException {
        Page<T> handle = handler.handle(resultSet);
        if (handle.hasContent()) {
            List<T> handleContent = handleContent(handle.getContent());
            if (handle.getContent() == handleContent) {
                return handle;
            } else {
                return new PageImpl<>(handle.getTotalElements(), handleContent, handle.getPageable());
            }
        }
        return handle;
    }

    protected List<T> handleContent(List<T> content) {
        return content;
    }
}
