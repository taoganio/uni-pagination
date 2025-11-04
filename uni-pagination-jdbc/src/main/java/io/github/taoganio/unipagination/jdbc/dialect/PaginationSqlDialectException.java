package io.github.taoganio.unipagination.jdbc.dialect;

import io.github.taoganio.unipagination.exception.PaginationException;

public class PaginationSqlDialectException extends PaginationException {

    public PaginationSqlDialectException(String msg) {
        super(msg);
    }

    public PaginationSqlDialectException(Throwable cause) {
        super(cause);
    }

    public PaginationSqlDialectException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
