package io.github.taoganio.unipagination.jdbc.dialect;

import io.github.taoganio.unipagination.jdbc.JdbcPaginationStatement;
import io.github.taoganio.unipagination.jdbc.sqlmodifier.CountModifier;
import io.github.taoganio.unipagination.jdbc.sqlmodifier.JdbcStatementSqlModifier;
import io.github.taoganio.unipagination.jdbc.sqlmodifier.SelectSqlModifier;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPaginationSqlDialect implements PaginationSqlDialect {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getCountDialectSql(JdbcPaginationStatement jdbcStatement) {
        String originalSql = jdbcStatement.getNativeStatement();
        //解析SQL
        Statement stmt;
        try {
            stmt = CCJSqlParserUtil.parse(originalSql);
        } catch (JSQLParserException e) {
            logger.error("Parsing sql [" + originalSql + "] exception", e);
            return originalSql;
        }
        return new CountModifier().modify((Select) stmt);
    }

    protected String getModifySql(JdbcPaginationStatement statement) throws JSQLParserException {
        String originalSql = statement.getNativeStatement();
        Statement oracleStmt = CCJSqlParserUtil.parse(originalSql);
        // 修改SQL
        SelectSqlModifier sqlModifier = new JdbcStatementSqlModifier(statement);
        return sqlModifier.modify((Select) oracleStmt);
    }

}
