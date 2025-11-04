package io.github.taoganio.unipagination.web.mvc;

import io.github.taoganio.unipagination.statement.PaginationStatement;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPaginationResponseAdvice implements ResponseBodyAdvice<Object> {

    private final List<PaginationActionHandler> actionHandlers = new ArrayList<>();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return PaginationStatement.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType, Class converterType,
                                  ServerHttpRequest serverRequest, ServerHttpResponse serverResponse) {
        if (!(body instanceof PaginationStatement)) {
            return body;
        }
        PaginationActionRequest actionRequest =
                resolvePaginationActionRequest(body, returnType, contentType, converterType, serverRequest, serverResponse);
        if (actionRequest == null || !actionRequest.isValid()) {
            return null;
        }
        String key = actionRequest.getPaginationKey();
        String action = actionRequest.getAction();
        for (PaginationActionHandler actionHandler : actionHandlers) {
            if (actionHandler.getAction().equals(action)) {
                return actionHandler.handle(key, (PaginationStatement) body,
                        ((ServletServerHttpRequest) serverRequest).getServletRequest(),
                        ((ServletServerHttpResponse) serverResponse).getServletResponse());
            }
        }
        return null;
    }

    protected abstract PaginationActionRequest resolvePaginationActionRequest(Object body, MethodParameter returnType,
                                                                              MediaType contentType, Class converterType,
                                                                              ServerHttpRequest serverRequest, ServerHttpResponse serverResponse);

    public void addActionHandler(PaginationActionHandler actionHandler) {
        Assert.notNull(actionHandler, "PaginationStatementActionHandler must not be null");
        this.actionHandlers.add(actionHandler);
    }

}
