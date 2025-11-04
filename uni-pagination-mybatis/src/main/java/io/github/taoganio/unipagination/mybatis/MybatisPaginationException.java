package io.github.taoganio.unipagination.mybatis;

import io.github.taoganio.unipagination.exception.PaginationException;

/**
 * MyBatis 分页异常
 */
public class MybatisPaginationException extends PaginationException {
    /**
     * MyBatis 分页异常
     *
     * @param msg 异常消息
     */
    public MybatisPaginationException(String msg) {
        super(msg);
    }
}
