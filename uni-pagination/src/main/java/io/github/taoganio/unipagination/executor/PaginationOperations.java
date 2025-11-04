package io.github.taoganio.unipagination.executor;

import io.github.taoganio.unipagination.domain.Page;
import io.github.taoganio.unipagination.domain.PageInformation;
import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.result.set.PaginationResultSetHandler;
import io.github.taoganio.unipagination.result.set.PaginationRowMapper;
import io.github.taoganio.unipagination.statement.PaginationStatement;

import java.util.Map;

/**
 * 分页操作
 */
public interface PaginationOperations {

    /**
     * 查询信息
     *
     * @param statement 语句
     * @return 页面信息
     * @throws PaginationException 分页引发的异常
     */
    PageInformation queryForInformation(PaginationStatement statement) throws PaginationException;

    /**
     * 查询映射结果集
     *
     * @param statement 陈述
     * @return 列表
     * @throws PaginationException 分页引发的异常
     */
    Page<Map<String, Object>> queryForMapResultSet(PaginationStatement statement) throws PaginationException;

    /**
     * 查询结果集
     *
     * @param statement 语句
     * @param rowMapper 行映射器
     * @return 列表
     * @throws PaginationException 分页引发的异常
     */
    <T> Page<T> queryForResultSet(PaginationStatement statement,
                                  PaginationRowMapper<T> rowMapper) throws PaginationException;

    /**
     * 查询结果集
     *
     * @param statement 语句
     * @param handler   处理器
     * @return 列表
     * @throws PaginationException 分页引发的异常
     */
    <T> Page<T> queryForResultSet(PaginationStatement statement,
                                  PaginationResultSetHandler<T> handler) throws PaginationException;
}
