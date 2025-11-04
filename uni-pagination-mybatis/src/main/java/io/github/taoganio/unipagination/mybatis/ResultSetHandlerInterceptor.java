package io.github.taoganio.unipagination.mybatis;

import io.github.taoganio.unipagination.jdbc.ResultSetMetadataColumnFactory;
import io.github.taoganio.unipagination.jdbc.Sql2oPaginationRow;
import io.github.taoganio.unipagination.jdbc.sql2o.Sql2oPaginationTableResultSetIterator;
import io.github.taoganio.unipagination.jdbc.sql2o.Sql2oRow;
import io.github.taoganio.unipagination.mybatis.util.ReflectionUtils;
import io.github.taoganio.unipagination.result.set.ColumnMetadata;
import io.github.taoganio.unipagination.result.set.PaginationRow;
import io.github.taoganio.unipagination.util.Assert;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.sql2o.quirks.NoQuirks;
import org.sql2o.quirks.Quirks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 结果集处理程序侦听器
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
})
public class ResultSetHandlerInterceptor implements Interceptor {

    private Quirks quirks = new NoQuirks();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ExecutorContext.Mark executionMark = ExecutorContext.getMark();
        if (executionMark != null && executionMark.isPageMark()) {
            DefaultResultSetHandler handler = (DefaultResultSetHandler) invocation.getTarget();
            MappedStatement mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(handler, "mappedStatement");
            if (!isPageHelperCountSql(mappedStatement)) {
                Statement statement = (Statement) invocation.getArgs()[0];
                List<Result> result = new ArrayList<>(1);
                result.add(this.createResult(statement.getResultSet()));
                return result;
            }
        }
        return invocation.proceed();
    }

    private boolean isPageHelperCountSql(MappedStatement mappedStatement) {
        // 判断是否是 PageHelper 生成的总数查询
        return mappedStatement.getSqlCommandType() == SqlCommandType.SELECT
                && mappedStatement.getId().endsWith("_COUNT");
    }

    private Result createResult(ResultSet resultSet) throws SQLException {
        List<ColumnMetadata> columns = ResultSetMetadataColumnFactory.createColumns(resultSet.getMetaData());
        int size = columns.size();
        List<PaginationRow> rows = new ArrayList<>();
        Sql2oPaginationTableResultSetIterator
                iterator = new Sql2oPaginationTableResultSetIterator(resultSet, false, quirks);
        while (iterator.hasNext()) {
            Sql2oRow row = iterator.next();
            rows.add(new Sql2oPaginationRow(size, row));
        }
        return new Result(columns, rows);
    }

    public void setQuirks(Quirks quirks) {
        Assert.notNull(quirks, "quirks cannot be null");
        this.quirks = quirks;
    }
}

