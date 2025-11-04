package io.github.taoganio.unipagination.jdbc.dialect;


import io.github.taoganio.unipagination.jdbc.JdbcPaginationStatement;

import java.util.function.Function;

/**
 * 分页sql方言
 */
public interface PaginationSqlDialect {

    String PAGE_NUMBER_PARAM_NAME = "pageNumber";
    String PAGE_SIZE_PARAM_NAME = "pageSize";

    /**
     * 获取计数方言 SQL
     */
    String getCountDialectSql(JdbcPaginationStatement jdbcStatement);

    /**
     * 获取sql方言
     */
    default PageDialectSql getPageDialectSql(JdbcPaginationStatement jdbcStatement) {
        return getPageDialectSql(jdbcStatement, e -> e, e -> e);
    }

    /**
     * 获取分页sql方言
     */
    PageDialectSql getPageDialectSql(JdbcPaginationStatement jdbcStatement,
                                     Function<PageDialectSql.ParamPair, PageDialectSql.ParamPair> pagePairFunc,
                                     Function<PageDialectSql.ParamPair, PageDialectSql.ParamPair> sizePairFunc);


}
