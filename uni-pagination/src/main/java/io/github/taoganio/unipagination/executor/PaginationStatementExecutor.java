package io.github.taoganio.unipagination.executor;

import io.github.taoganio.unipagination.domain.PageInformation;
import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.result.set.PaginationResultSet;
import io.github.taoganio.unipagination.statement.PaginationStatement;

/**
 * 分页语句执行程序
 */
public interface PaginationStatementExecutor<Statement extends PaginationStatement> {

    /**
     * 验证是否支持语句的执行
     *
     * @param statement 分页语句
     * @return true 支持
     */
    boolean supports(PaginationStatement statement);

    /**
     * 执行信息
     *
     * @param statement 分页语句
     * @return {@link PageInformation} 分页信息
     */
    PageInformation executeForInformation(Statement statement) throws PaginationException;

    /**
     * 执行分页查询, 通常是直接操作最底层的API封装后, 返回通用的结果集
     * <p>
     * 例如：JDBC ResultSet
     * <p>
     * 例如：MongoDB Bson
     * <p>
     * 例如：ES Json
     *
     * @param statement 分页语句
     * @return {@link PaginationResultSet} 分页结果集
     */
    PaginationResultSet executeForResultSet(Statement statement) throws PaginationException;

}
