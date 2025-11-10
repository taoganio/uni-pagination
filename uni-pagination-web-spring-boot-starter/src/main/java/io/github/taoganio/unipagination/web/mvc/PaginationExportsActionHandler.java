package io.github.taoganio.unipagination.web.mvc;

import io.github.taoganio.unipagination.web.component.*;
import io.github.taoganio.unipagination.executor.PaginationOperations;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class PaginationExportsActionHandler implements PaginationActionHandler {

    private final PaginationComponentFactory componentFactory;
    private final PaginationOperations paginationOperations;

    private PaginationExporter defaultExporter;

    public PaginationExportsActionHandler(PaginationComponentFactory componentFactory,
                                          PaginationOperations paginationOperations) {
        this.componentFactory = componentFactory;
        this.paginationOperations = paginationOperations;
        this.defaultExporter = getExporter(componentFactory.getDefault(), NullPaginationExporter.INSTANCE);
    }

    @Override
    public String getAction() {
        return "exports";
    }

    @Override
    public Object handle(String paginationKey, PaginationStatement statement,
                         HttpServletRequest request, HttpServletResponse response) {
        PaginationExporter exporter;
        if (statement instanceof ComponentStatement) {
            ComponentStatement componentStatement = (ComponentStatement) statement;
            statement = componentStatement.getStatement();
            exporter = getExporter(componentStatement, defaultExporter);
        } else {
            exporter = getExporter(componentFactory.getPaginationComponent(paginationKey), defaultExporter);
        }
        paginationOperations.queryForResultSet(statement, resultSet -> {
            exporter.exports(resultSet, request, response);
            return null;
        });
        return null;
    }

    public void setDefaultExporter(PaginationExporter defaultExporter) {
        Assert.notNull(defaultExporter, "defaultExporter must not be null");
        this.defaultExporter = defaultExporter;
    }

    private PaginationExporter getExporter(PaginationComponent component, PaginationExporter defaultExporter) {
        return Optional.ofNullable(component).map(PaginationComponent::getExporter).orElse(defaultExporter);
    }
}
