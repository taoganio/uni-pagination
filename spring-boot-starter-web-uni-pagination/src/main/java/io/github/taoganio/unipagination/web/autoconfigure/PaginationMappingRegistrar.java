package io.github.taoganio.unipagination.web.autoconfigure;

import io.github.taoganio.unipagination.web.mvc.PaginationHandlerMethodRegistry;
import io.github.taoganio.unipagination.web.mvc.PaginationUriResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Slf4j
class PaginationMappingRegistrar
        implements ApplicationListener<ContextRefreshedEvent>, EmbeddedValueResolverAware {

    private final PaginationUriResolver paginationUriResolver;
    private final RequestMappingHandlerMapping handlerMapping;
    private final PaginationHandlerMethodRegistry registry;
    private final PaginationWebProperties paginationWebProperties;
    @Nullable
    private StringValueResolver embeddedValueResolver;

    public PaginationMappingRegistrar(PaginationUriResolver paginationUriResolver,
                                      RequestMappingHandlerMapping handlerMapping,
                                      PaginationHandlerMethodRegistry registry,
                                      PaginationWebProperties paginationWebProperties) {
        this.paginationUriResolver = paginationUriResolver;
        this.handlerMapping = handlerMapping;
        this.registry = registry;
        this.paginationWebProperties = paginationWebProperties;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (String pageKey : registry.getAllPaginationKey()) {
            HandlerMethod statementMethod = registry.getHandler(pageKey);
            for (PaginationWebProperties.Action action : paginationWebProperties.getActions()) {
                // Check if the pageKey matches the include/exclude conditions
                if (!shouldApplyAction(pageKey, action)) {
                    continue;
                }
                String path = paginationUriResolver.constructUri(pageKey, action.getAction());
                Method method = statementMethod.getMethod();
                RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                Assert.notNull(requestMapping, "RequestMapping annotation not found on method " + method);
                RequestMappingInfo newInfo = RequestMappingInfo
                        .paths(resolveEmbeddedValuesInPattern(path))
                        .options(handlerMapping.getBuilderConfiguration())
                        .methods(requestMapping.method())
                        .params(requestMapping.params())
                        .headers(requestMapping.headers())
                        .consumes(requestMapping.consumes())
                        .produces(requestMapping.produces())
                        .mappingName(requestMapping.name()).build();
                handlerMapping.registerMapping(newInfo, statementMethod.getBean(), statementMethod.getMethod());
            }
        }
    }

    private boolean shouldApplyAction(String pageKey, PaginationWebProperties.Action action) {
        boolean included = CollectionUtils.isEmpty(action.getIncludePages())
                || action.getIncludePages().contains(pageKey);
        boolean excluded = !CollectionUtils.isEmpty(action.getExcludePages())
                && action.getExcludePages().contains(pageKey);
        return included && !excluded;
    }

    protected String resolveEmbeddedValuesInPattern(String pattern) {
        if (this.embeddedValueResolver == null) {
            return pattern;
        } else {
            return this.embeddedValueResolver.resolveStringValue(pattern);
        }
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }
}
