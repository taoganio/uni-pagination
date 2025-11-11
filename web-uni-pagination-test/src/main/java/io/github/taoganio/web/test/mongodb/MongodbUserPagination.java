package io.github.taoganio.web.test.mongodb;

import io.github.taoganio.unipagination.mongodb.MongoFindPaginationStatement;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import io.github.taoganio.unipagination.web.mvc.PaginationController;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

@PaginationController
public class MongodbUserPagination {

    @PostMapping("mongodb-users")
    public PaginationStatement user(@RequestParam int page, @RequestParam int size,
                                    @RequestParam(required = false) String name) {
        Bson filter = eq("status", "active");
        if (StringUtils.hasText(name)) {
            filter = and(filter, eq("username", name));
        }
        return MongoFindPaginationStatement.builder()
                .collection("users")
                .projection(fields(include("userId", "username", "email")))
                .filter(filter)
                .pageable(page, size)
                .build();
    }
}
