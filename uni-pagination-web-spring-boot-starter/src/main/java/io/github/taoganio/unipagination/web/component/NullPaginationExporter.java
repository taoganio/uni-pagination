package io.github.taoganio.unipagination.web.component;

import io.github.taoganio.unipagination.result.set.PaginationResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NullPaginationExporter implements PaginationExporter {

    public static final NullPaginationExporter INSTANCE = new NullPaginationExporter();

    private NullPaginationExporter() {
    }

    @Override
    public void exports(PaginationResultSet resultSet, HttpServletRequest request, HttpServletResponse response) {
        try {
            resultSet.close();
        } catch (Exception ignored) {
        }
    }
}
