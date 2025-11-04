package io.github.taoganio.unipagination.executor;

import io.github.taoganio.unipagination.domain.Page;
import io.github.taoganio.unipagination.domain.PageInformation;
import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import io.github.taoganio.unipagination.exception.MissingPaginationStatementExecutorException;
import io.github.taoganio.unipagination.util.Assert;
import io.github.taoganio.unipagination.result.set.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaginationTemplate implements PaginationOperations {

    private final List<PaginationStatementExecutor<?>> statementExecutors = new ArrayList<>();

    public PaginationTemplate() {
        statementExecutors.add(EmptyPaginationStatementExecutor.getInstance());
    }

    @Override
    public PageInformation queryForInformation(PaginationStatement statement) throws PaginationException {
        return getStatementExecutor(statement).executeForInformation(statement);
    }

    @Override
    public Page<Map<String, Object>> queryForMapResultSet(PaginationStatement statement) throws PaginationException {
        return queryForResultSet(statement, new MapPaginationRowMapper());
    }

    @Override
    public <T> Page<T> queryForResultSet(PaginationStatement statement,
                                         PaginationRowMapper<T> rowMapper) throws PaginationException {
        return queryForResultSet(statement, new PaginationRowMapperResultSetHandler<>(rowMapper));
    }

    @Override
    public <T> Page<T> queryForResultSet(PaginationStatement statement,
                                         PaginationResultSetHandler<T> handler) throws PaginationException {
        try (PaginationResultSet rs = getStatementExecutor(statement).executeForResultSet(statement)) {
            return handler.handle(rs);
        } catch (Exception e) {
            throw new PaginationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected PaginationStatementExecutor<PaginationStatement> getStatementExecutor(PaginationStatement statement) {
        Assert.notNull(statement, "pagination statement must not be null.");
        for (PaginationStatementExecutor<?> statementExecutor : statementExecutors) {
            if (statementExecutor.supports(statement)) {
                return (PaginationStatementExecutor<PaginationStatement>) statementExecutor;
            }
        }
        String message = "PaginationStatementExecutor not found for statement of type [" + statement.getClass().getName() + "].";
        if (statement.getLanguage() != null && !statement.getLanguage().isEmpty()) {
            message = "PaginationStatementExecutor not found for language [" + statement.getLanguage() + "].";
        }
        throw new MissingPaginationStatementExecutorException(message);
    }

    public void addStatementExecutor(PaginationStatementExecutor<?> statementExecutor) {
        Assert.notNull(statementExecutor, "PaginationStatementExecutor must not be null.");
        this.statementExecutors.add(statementExecutor);
    }
}
