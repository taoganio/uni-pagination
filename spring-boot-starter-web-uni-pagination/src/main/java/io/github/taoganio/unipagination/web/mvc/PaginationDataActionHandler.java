package io.github.taoganio.unipagination.web.mvc;

import io.github.taoganio.unipagination.web.component.ComponentStatement;
import io.github.taoganio.unipagination.web.component.PaginationComponent;
import io.github.taoganio.unipagination.web.component.PaginationComponentFactory;
import io.github.taoganio.unipagination.executor.PaginationOperations;
import io.github.taoganio.unipagination.result.set.PaginationMapResultSetHandler;
import io.github.taoganio.unipagination.result.set.PaginationResultSetHandler;
import io.github.taoganio.unipagination.result.set.PaginationRowMapper;
import io.github.taoganio.unipagination.result.set.PaginationRowMapperResultSetHandler;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PaginationDataActionHandler implements PaginationActionHandler {

    private final PaginationComponentFactory componentFactory;
    private final PaginationOperations paginationOperations;

    private PaginationResultSetHandler<?> defaultDataResultHandler;

    public PaginationDataActionHandler(PaginationComponentFactory paginationComponentFactory,
                                       PaginationOperations paginationOperations) {
        this.componentFactory = paginationComponentFactory;
        this.defaultDataResultHandler = getResultHandler(
                this.componentFactory.getDefault(), new PaginationMapResultSetHandler());
        this.paginationOperations = paginationOperations;
    }

    @Override
    public String getAction() {
        return "data";
    }

    @Override
    public Object handle(String paginationKey, PaginationStatement statement,
                         HttpServletRequest request, HttpServletResponse response) {
        PaginationResultSetHandler<?> resultHandler;
        if (statement instanceof ComponentStatement) {
            ComponentStatement componentStatement = (ComponentStatement) statement;
            statement = componentStatement.getStatement();
            resultHandler = getResultHandler(componentStatement, defaultDataResultHandler);
        } else {
            resultHandler =
                    getResultHandler(componentFactory.getPaginationComponent(paginationKey), defaultDataResultHandler);
        }
        return paginationOperations.queryForResultSet(statement, resultHandler).getContent();
    }

    public void setDefaultDataResultHandler(PaginationResultSetHandler<?> defaultDataResultHandler) {
        Assert.notNull(defaultDataResultHandler, "DefaultDataResultHandler must not be null");
        this.defaultDataResultHandler = defaultDataResultHandler;
    }

    private PaginationResultSetHandler<?> getResultHandler(PaginationComponent paginationComponent,
                                                           PaginationResultSetHandler<?> defaultHandler) {
        if (paginationComponent == null) {
            return defaultHandler;
        }
        PaginationResultSetHandler<?> dataResultSetHandler = paginationComponent.getDataResultSetHandler();
        if (dataResultSetHandler != null) {
            return dataResultSetHandler;
        }
        PaginationRowMapper<?> dataRowMapper = paginationComponent.getDataRowMapper();
        if (dataRowMapper != null) {
            return new PaginationRowMapperResultSetHandler<>(dataRowMapper);
        }
        return defaultHandler;
    }

}
