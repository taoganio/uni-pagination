package io.github.taoganio.unipagination.exception;


/**
 * 分页引发的异常
 */
public class PaginationException extends RuntimeException {

    public PaginationException(String msg) {
        super(msg);
    }

    public PaginationException(Throwable cause) {
        super(cause);
    }

    public PaginationException(String msg, Throwable cause) {
        super(msg, cause);
    }


}
