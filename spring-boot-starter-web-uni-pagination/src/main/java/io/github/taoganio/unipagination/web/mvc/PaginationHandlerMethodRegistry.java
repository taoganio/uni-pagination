package io.github.taoganio.unipagination.web.mvc;

import org.springframework.web.method.HandlerMethod;

import java.util.Set;

/**
 * 分页处理程序方法注册表
 */
public interface PaginationHandlerMethodRegistry {

    Set<String> getAllPaginationKey();

    HandlerMethod getHandler(String paginationKey);

    void register(String paginationKey, HandlerMethod handlerMethod);
}
