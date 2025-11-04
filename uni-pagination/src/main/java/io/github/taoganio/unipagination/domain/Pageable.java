package io.github.taoganio.unipagination.domain;


import jakarta.annotation.Nullable;

/**
 * 可分页
 */
public interface Pageable {

    /**
     * 分页页码, 从零开始的页面索引，不得为负数
     */
    int getPageNumber();

    /**
     * 分页大小
     */
    int getPageSize();

    /**
     * 根据分页页码和页大小取的偏移量
     */
    long getOffset();

    /**
     * 返回上一页分页信息
     */
    Pageable first();

    /**
     * 返回下一页分页信息
     */
    Pageable next();

    /**
     * 返回上一页或首页
     */
    Pageable previousOrFirst();

    /**
     * 返回是否有上一页
     */
    boolean hasPrevious();

    /**
     * 排序参数
     */
    @Nullable
    Sort getSort();

}
