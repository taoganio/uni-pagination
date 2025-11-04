package io.github.taoganio.web.test;

import io.github.taoganio.unipagination.domain.Sort;
import io.github.taoganio.unipagination.result.set.PaginationMapResultSetHandler;
import io.github.taoganio.unipagination.web.component.ComponentStatement;
import org.junit.jupiter.api.Test;

import static io.github.taoganio.unipagination.jdbc.JdbcPaginationStatement.of;

public class ComponentStatementTest {
    @Test
    void test() {

        ComponentStatement.of(b -> b
                .statement(
                        of(pb -> pb
                                .sql("select * from o_stock.organization where name like :name order by id desc")
                                .paramMap("name", "%123%")
                                .pageable(0, 10, Sort.by("id"))
                        )
                )

                .resultSetHandler(new PaginationMapResultSetHandler())
        );
    }
}
