package io.github.taoganio.unipagination.jdbc;

import io.github.taoganio.unipagination.domain.PageImpl;
import io.github.taoganio.unipagination.domain.PageInformation;
import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.executor.PaginationStatementExecutor;
import io.github.taoganio.unipagination.jdbc.dialect.PageDialectSql;
import io.github.taoganio.unipagination.jdbc.dialect.PaginationSqlAutomatedDialect;
import io.github.taoganio.unipagination.jdbc.dialect.PaginationSqlDialect;
import io.github.taoganio.unipagination.jdbc.sql2o.Sql2oPaginationResultSetHandlerFactory;
import io.github.taoganio.unipagination.result.set.*;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import io.github.taoganio.unipagination.util.Assert;
import io.github.taoganio.unipagination.util.CollectionUtils;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.connectionsources.ConnectionSource;
import org.sql2o.connectionsources.DataSourceConnectionSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 命名参数 JDBC 语句执行程序
 */
public class Sql2oPaginationNamedParameterStatementExecutor
        implements PaginationStatementExecutor<NamedParameterJdbcStatement> {

    private PaginationSqlDialect paginationSqlDialect;
    private final Sql2o sql2o;

    public Sql2oPaginationNamedParameterStatementExecutor(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null");
        this.paginationSqlDialect = new PaginationSqlAutomatedDialect(dataSource);
        this.sql2o = new Sql2o(dataSource);
    }

    public Sql2oPaginationNamedParameterStatementExecutor(Sql2o sql2o) {
        Assert.notNull(sql2o, "Sql2o must not be null");
        DataSource dataSource = null;
        ConnectionSource connectionSource = sql2o.getConnectionSource();
        if (connectionSource instanceof DataSourceConnectionSource) {
            dataSource = ((DataSourceConnectionSource) connectionSource).getDataSource();
        }
        this.paginationSqlDialect = new PaginationSqlAutomatedDialect(dataSource);
        this.sql2o = sql2o;
    }

    @Override
    public boolean supports(PaginationStatement statement) {
        return statement instanceof NamedParameterJdbcStatement;
    }

    @Override
    public PageInformation executeForInformation(NamedParameterJdbcStatement statement) throws PaginationException {
        Pageable pageable = statement.getPageable();
        return new PageImpl<>(executeForCount(statement), pageable);
    }

    @Override
    public PaginationResultSet executeForResultSet(NamedParameterJdbcStatement statement) {
        long count = executeForCount(statement);
        Pageable pageable = statement.getPageable();
        if (count <= 0) {
            return new EmptyPaginationResultSet(pageable);
        }

        Map<String, Object> parameterMap = handlerParameters(statement.getParameter());
        PageDialectSql dialectSql = getPaginationSqlDialect().getPageDialectSql(statement);
        // 添加分页sql方言参数
        parameterMap.put(dialectSql.getPage().getName(), dialectSql.getPage().getValue());
        parameterMap.put(dialectSql.getSize().getName(), dialectSql.getSize().getValue());

        try (Connection connection = sql2o.open();
             Query query = connection.createQuery(dialectSql.getSql())) {
            addQueryParameter(query, parameterMap);
            Sql2oPaginationResultSetHandlerFactory factory = new Sql2oPaginationResultSetHandlerFactory(sql2o);
            List<PaginationRow> paginationRows = query.executeAndFetch(factory);
            return new DefaultPaginationResultSet(count,
                    new DefaultPaginationResultSetMetadata(pageable, factory.getColumns()), paginationRows);
        }
    }

    protected long executeForCount(NamedParameterJdbcStatement statement) {
        String countSql = getPaginationSqlDialect().getCountDialectSql(statement);
        try (Connection connection = sql2o.open();
             Query query = connection.createQuery(countSql)) {
            addQueryParameter(query, statement.getParameter());
            return query.executeScalar(Long.class);
        }
    }

    protected Map<String, Object> handlerParameters(Map<String, Object> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            return new HashMap<>(2);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        parameters.entrySet().stream().filter(e -> e.getKey() != null)
                .forEach(e -> hashMap.put(e.getKey(), e.getValue()));
        return hashMap;
    }

    private void addQueryParameter(Query query, Map<String, Object> parameterMap) {
        if (!CollectionUtils.isEmpty(parameterMap)) {
            Map<String, List<Integer>> paramNameToIdxMap = query.getParamNameToIdxMap();
            parameterMap.forEach((k, v) -> {
                if (paramNameToIdxMap.containsKey(k)) {
                    query.addParameter(k, v);
                }
            });
        }
    }

    public void setPaginationSqlDialect(PaginationSqlDialect paginationSqlDialect) {
        Assert.notNull(paginationSqlDialect, "PaginationSqlDialect must not be null");
        this.paginationSqlDialect = paginationSqlDialect;
    }

    public PaginationSqlDialect getPaginationSqlDialect() {
        return paginationSqlDialect;
    }
}
