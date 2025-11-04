package io.github.taoganio.unipagination.jdbc.sqlmodifier;

import io.github.taoganio.unipagination.domain.Sort;
import io.github.taoganio.unipagination.jdbc.JdbcPaginationStatement;
import io.github.taoganio.unipagination.util.Assert;


public class JdbcStatementSqlModifier extends CompositeSqlModifier {

    public JdbcStatementSqlModifier(JdbcPaginationStatement jdbcStatement) {
        Assert.notNull(jdbcStatement, "JdbcStatement must not be null");
        Sort sort = jdbcStatement.getPageable().getSort();
        if (sort != null && sort.isSorted()) {
            addModifier(new SortModifier(sort));
        }
    }
}
