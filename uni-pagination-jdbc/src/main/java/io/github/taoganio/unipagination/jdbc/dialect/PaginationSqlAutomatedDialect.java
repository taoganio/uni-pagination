package io.github.taoganio.unipagination.jdbc.dialect;


import io.github.taoganio.unipagination.jdbc.JdbcPaginationStatement;
import io.github.taoganio.unipagination.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 分页sql自动方言
 */
public class PaginationSqlAutomatedDialect implements PaginationSqlDialect {

    private static final Map<String, Class<? extends PaginationSqlDialect>> dialectAliasCache = new ConcurrentHashMap<>();
    private final Map<Class<? extends PaginationSqlDialect>, PaginationSqlDialect> instanceCache = new ConcurrentHashMap<>();

    static {
        registerDialectAlias("oracle", OracleSqlDialect.class);
        registerDialectAlias("dm", OracleSqlDialect.class);
        registerDialectAlias("edb", OracleSqlDialect.class);
        registerDialectAlias("mysql", MysqlDialect.class);
        registerDialectAlias("mariadb", MysqlDialect.class);
        registerDialectAlias("sqlite", MysqlDialect.class);
        registerDialectAlias("clickhouse", MysqlDialect.class);
        registerDialectAlias("postgresql", PostgresSqlDialect.class);
    }

    private final DataSource dataSource;

    public PaginationSqlAutomatedDialect(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null!");
        this.dataSource = dataSource;
    }

    @Override
    public String getCountDialectSql(JdbcPaginationStatement jdbcStatement) {
        return getDialect(dataSource).getCountDialectSql(jdbcStatement);
    }

    @Override
    public PageDialectSql getPageDialectSql(JdbcPaginationStatement jdbcStatement,
                                            Function<PageDialectSql.ParamPair, PageDialectSql.ParamPair> pageParamPairFunc,
                                            Function<PageDialectSql.ParamPair, PageDialectSql.ParamPair> sizeParamPairFunc) {
        return getDialect(dataSource).getPageDialectSql(jdbcStatement, pageParamPairFunc, sizeParamPairFunc);
    }

    protected PaginationSqlDialect getDialect(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL().toLowerCase();
            for (Map.Entry<String, Class<? extends PaginationSqlDialect>> entry : dialectAliasCache.entrySet()) {
                if (url.contains(":" + entry.getKey() + ":")) {
                    Class<? extends PaginationSqlDialect> dialectClass = entry.getValue();
                    return instanceCache.computeIfAbsent(dialectClass, k -> {
                        try {
                            return dialectClass.getConstructor().newInstance();
                        } catch (InstantiationException | IllegalAccessException
                                 | InvocationTargetException | NoSuchMethodException e) {
                            throw new PaginationSqlDialectException("Failed to create dialect instance: "
                                    + dialectClass.getName(), e);
                        }
                    });
                }
            }
            throw new UnsupportedOperationException("Unsupported database type: " + url);
        } catch (SQLException e) {
            throw new PaginationSqlDialectException("Error getting connection from data source.", e);
        }
    }

    public static void registerDialectAlias(String alias, Class<? extends PaginationSqlDialect> dialectClass) {
        Assert.notEmpty(alias, "Alias must not be empty!");
        Assert.notNull(dialectClass, "Dialect class must not be null!");
        dialectAliasCache.put(alias, dialectClass);
    }
}
