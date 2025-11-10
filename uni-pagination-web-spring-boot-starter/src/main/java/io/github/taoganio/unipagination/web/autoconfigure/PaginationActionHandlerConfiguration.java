package io.github.taoganio.unipagination.web.autoconfigure;

import io.github.taoganio.unipagination.web.mvc.PaginationDataActionHandler;
import io.github.taoganio.unipagination.web.mvc.PaginationExportsActionHandler;
import io.github.taoganio.unipagination.web.mvc.PaginationInfoActionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({PaginationDataActionHandler.class, PaginationExportsActionHandler.class, PaginationInfoActionHandler.class})
public class PaginationActionHandlerConfiguration {

}
