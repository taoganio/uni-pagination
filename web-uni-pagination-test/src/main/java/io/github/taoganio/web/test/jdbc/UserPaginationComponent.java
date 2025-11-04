package io.github.taoganio.web.test.jdbc;

import io.github.taoganio.unipagination.annotation.PaginationKey;
import io.github.taoganio.unipagination.result.set.PaginationRowMapper;
import io.github.taoganio.unipagination.web.component.PaginationComponent;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@PaginationKey("user")
public class UserPaginationComponent implements PaginationComponent {

    @Override
    public PaginationRowMapper<?> getDataRowMapper() {
        return (metadata, prs, rowNum) -> {
            Properties properties = new Properties();
            return properties;
        };
    }
}
