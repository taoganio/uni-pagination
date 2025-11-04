package io.github.taoganio.unipagination.web.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaginationResponseAdvice extends AbstractPaginationResponseAdvice {

    private PaginationUriResolver paginationURIResolver = new DefaultPaginationUriResolver();

    @Override
    protected PaginationActionRequest resolvePaginationActionRequest(Object body, MethodParameter returnType,
                                                                  MediaType contentType, Class converterType,
                                                                  ServerHttpRequest serverRequest, ServerHttpResponse serverResponse) {
        RequestMapping annotation = returnType.getMethodAnnotation(RequestMapping.class);
        if (annotation == null) {
            return null;
        }
        return paginationURIResolver.resolve(annotation.value(), serverRequest.getURI().getPath());
    }

    public void setPaginationUrlResolver(PaginationUriResolver paginationURIResolver) {
        Assert.notNull(paginationURIResolver, "PaginationUrlResolver must not be null");
        this.paginationURIResolver = paginationURIResolver;
    }
}
