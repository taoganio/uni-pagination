package io.github.taoganio.unipagination.jdbc;

import io.github.taoganio.unipagination.statement.PaginationStatement;
import jakarta.annotation.Nullable;

import java.util.function.Function;


/**
 * JDBC分页语句
 */
public interface JdbcPaginationStatement extends PaginationStatement {

    @Override
    default String getLanguage() {
        return "JDBC";
    }

    /**
     * 返回原生语句
     *
     * @return 原生语句
     */
    String getNativeStatement();

    /**
     * 得到查询参数
     */
    @Nullable
    Object getParameter();

    static JdbcPaginationStatement of(
            Function<NamedParameterJdbcStatement.Builder, NamedParameterJdbcStatement.Builder> function) {
        return NamedParameterJdbcStatement.of(function);
    }

}
