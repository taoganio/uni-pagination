package io.github.taoganio.unipagination.web.mvc;

import io.github.taoganio.unipagination.web.component.ComponentStatement;
import io.github.taoganio.unipagination.web.component.PageInfoRepresentation;
import io.github.taoganio.unipagination.executor.PaginationOperations;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PaginationInfoActionHandler implements PaginationActionHandler {

    private final PaginationOperations paginationOperations;

    public PaginationInfoActionHandler(PaginationOperations paginationOperations) {
        this.paginationOperations = paginationOperations;
    }

    @Override
    public String getAction() {
        return "info";
    }

    @Override
    public Object handle(String paginationKey, PaginationStatement statement,
                         HttpServletRequest request, HttpServletResponse response) {
        if (statement instanceof ComponentStatement) {
            statement = ((ComponentStatement) statement).getStatement();
        }
        return PageInfoRepresentation.of(paginationOperations.queryForInformation(statement));
    }
}
