package io.github.taoganio.unipagination.statement;

import io.github.taoganio.unipagination.domain.Pageable;

/**
 * 分页语句
 */
public interface PaginationStatement {

    /**
     * 获取语言
     *
     * @return 语言
     */
    default String getLanguage() {
        return "";
    }

    /**
     * 返回原生语句
     *
     * @return 原生语句
     */
    Object getNativeStatement();

    /**
     * 返回分页
     *
     * @return 可分页
     */
    Pageable getPageable();

    /**
     * 设置分页
     *
     * @param pageable 可分页
     */
    void setPageable(Pageable pageable);
}
