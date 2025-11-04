package io.github.taoganio.unipagination.web.autoconfigure;

import io.github.taoganio.unipagination.web.mvc.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@AutoConfiguration(after = {PaginationAutoConfiguration.class, WebMvcAutoConfiguration.class})
@Import(PaginationActionHandlerConfiguration.class)
@EnableConfigurationProperties(PaginationWebProperties.class)
@ConditionalOnProperty(prefix = "spring.unipagination.web", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PaginationWebAutoConfiguration {

    @Bean
    PaginationMappingRegistrar statementMappingRegistrar(PaginationUriResolver paginationUriResolver,
                                                         RequestMappingHandlerMapping requestMappingHandlerMapping,
                                                         PaginationHandlerMethodRegistry paginationHandlerMethodRegistry,
                                                         PaginationWebProperties properties) {
        return new PaginationMappingRegistrar(paginationUriResolver, requestMappingHandlerMapping,
                paginationHandlerMethodRegistry, properties);
    }

    @Bean
    @ConditionalOnMissingBean(PaginationUriResolver.class)
    public PaginationUriResolver paginationUriResolver(PaginationWebProperties properties) {
        DefaultPaginationUriResolver resolver = new DefaultPaginationUriResolver();
        resolver.setBasePath(properties.getPath());
        return resolver;
    }

    @Bean
    @ConditionalOnMissingBean(PaginationHandlerMethodRegistry.class)
    public PaginationHandlerMethodRegistry paginationHandlerMethodRegistry() {
        return new DefaultPaginationHandlerMethodRegistry();
    }

    @Bean
    @ConditionalOnMissingBean(name = "paginationResponseAdvice")
    public PaginationResponseAdvice paginationResponseAdvice(PaginationUriResolver paginationUriResolver,
                                                             List<PaginationActionHandler> actionHandlers) {
        PaginationResponseAdvice advice = new PaginationResponseAdvice();
        advice.setPaginationUrlResolver(paginationUriResolver);
        actionHandlers.forEach(advice::addActionHandler);
        return advice;
    }
}
