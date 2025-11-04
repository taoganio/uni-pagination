package io.github.taoganio.unipagination.jdbc.dialect;

import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.jdbc.JdbcPaginationStatement;
import net.sf.jsqlparser.JSQLParserException;

import java.util.function.Function;

/**
 * MySQL方言
 */
public class MysqlDialect extends AbstractPaginationSqlDialect {

    @Override
    public PageDialectSql getPageDialectSql(JdbcPaginationStatement jdbcStatement,
                                            Function<PageDialectSql.ParamPair, PageDialectSql.ParamPair> pagePairFunc,
                                            Function<PageDialectSql.ParamPair, PageDialectSql.ParamPair> sizePairFunc) {
        String originalSql = jdbcStatement.getNativeStatement();
        Pageable pageable = jdbcStatement.getPageable();

        // 分页参数
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        PageDialectSql.ParamPair pagePair = pagePairFunc
                .apply(new PageDialectSql.ParamPair(PAGE_NUMBER_PARAM_NAME, pageable.getOffset()));
        PageDialectSql.ParamPair sizePair = sizePairFunc
                .apply(new PageDialectSql.ParamPair(PAGE_SIZE_PARAM_NAME, size));

        String modifySql;
        try {
            modifySql = getModifySql(jdbcStatement);
        } catch (JSQLParserException e) {
            logger.warn("Parsing sql [{}] exception", originalSql, e);
            return new PageDialectSql(originalSql, pagePair, sizePair);
        }
        // 分页SQL
        StringBuilder sqlBuilder = new StringBuilder(modifySql.length() + 14);
        sqlBuilder.append(modifySql);

        if (page == 0) {
            sqlBuilder.append(" LIMIT :").append(sizePair.getName());
        } else {
            sqlBuilder.append(" LIMIT :").append(pagePair.getName()).append(", :").append(sizePair.getName());
        }
        return new PageDialectSql(sqlBuilder.toString(), pagePair, sizePair);
    }
}
