package io.github.taoganio.unipagination.web.autoconfigure;

import io.github.taoganio.unipagination.web.component.PaginationComponentFactory;
import io.github.taoganio.unipagination.executor.PaginationStatementExecutor;
import io.github.taoganio.unipagination.executor.PaginationOperations;
import io.github.taoganio.unipagination.executor.PaginationTemplate;
import io.github.taoganio.unipagination.web.component.DefaultPaginationComponentFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

@AutoConfiguration
public class PaginationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PaginationOperations.class)
    public PaginationOperations paginationStatementOperations(Collection<PaginationStatementExecutor<?>> executors) {
        PaginationTemplate template = new PaginationTemplate();
        executors.forEach(template::addStatementExecutor);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(PaginationComponentFactory.class)
    public PaginationComponentFactory paginationComponentFactory() {
        return new DefaultPaginationComponentFactory();
    }

}
