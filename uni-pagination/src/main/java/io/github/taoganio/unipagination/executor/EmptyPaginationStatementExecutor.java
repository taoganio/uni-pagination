package io.github.taoganio.unipagination.executor;

import io.github.taoganio.unipagination.domain.PageRequest;
import io.github.taoganio.unipagination.result.set.EmptyPaginationResultSet;
import io.github.taoganio.unipagination.domain.PageImpl;
import io.github.taoganio.unipagination.domain.PageInformation;
import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.result.set.PaginationResultSet;
import io.github.taoganio.unipagination.statement.EmptyPaginationStatement;
import io.github.taoganio.unipagination.statement.PaginationStatement;

/**
 * 没有结果的分页语句执行器
 */
public class EmptyPaginationStatementExecutor implements PaginationStatementExecutor<EmptyPaginationStatement> {

    private static final EmptyPaginationStatementExecutor INSTANCE = new EmptyPaginationStatementExecutor();
    private final Pageable pageable = PageRequest.of(0, 1);
    private final PageInformation pageInformation = new PageImpl<>(0, pageable);
    private final EmptyPaginationResultSet resultSet = new EmptyPaginationResultSet(pageable);

    private EmptyPaginationStatementExecutor() {
    }

    public static EmptyPaginationStatementExecutor getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean supports(PaginationStatement paginationStatement) {
        return paginationStatement instanceof EmptyPaginationStatement;
    }

    @Override
    public PageInformation executeForInformation(EmptyPaginationStatement statement) throws PaginationException {
        return pageInformation;
    }

    @Override
    public PaginationResultSet executeForResultSet(EmptyPaginationStatement statement) throws PaginationException {
        return resultSet;
    }
}
