package io.github.taoganio.unipagination.web.mvc;

import io.github.taoganio.unipagination.statement.PaginationStatement;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 分页操作处理程序
 */
public interface PaginationActionHandler {

    String getAction();

    @Nullable
    Object handle(String paginationKey, PaginationStatement statement,
                  HttpServletRequest request, HttpServletResponse response);
}
