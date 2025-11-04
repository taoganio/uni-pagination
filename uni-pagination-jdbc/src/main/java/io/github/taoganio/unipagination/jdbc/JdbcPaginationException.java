package io.github.taoganio.unipagination.jdbc;


import io.github.taoganio.unipagination.exception.PaginationException;

/**
 * JDBC 分页异常
 */
public class JdbcPaginationException extends PaginationException {

    public JdbcPaginationException(String msg) {
        super(msg);
    }

    public JdbcPaginationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JdbcPaginationException(Throwable cause) {
        super(cause);
    }
}
