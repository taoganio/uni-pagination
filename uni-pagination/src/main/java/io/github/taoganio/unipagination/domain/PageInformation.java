package io.github.taoganio.unipagination.domain;

import jakarta.annotation.Nullable;

/**
 * 页面信息
 */
public interface PageInformation {
    /**
     * 返回当前的分页编号
     */
    int getNumber();

    /**
     * 返回当前的分页大小
     */
    int getSize();

    /**
     * 返回是否为第一页
     */
    boolean isFirst();

    /**
     * 返回是否为最后一页
     */
    boolean isLast();

    /**
     * 返回是否有下一页
     */
    boolean hasNext();

    /**
     * 返回是否有上一页
     */
    boolean hasPrevious();

    /**
     * 获取总页数
     */
    int getTotalPages();

    /**
     * 获取总元素数
     */
    long getTotalElements();

    /**
     * 根据当前分页内容获取分页信息
     */
    default Pageable getPageable() {
        return PageRequest.of(getNumber(), getSize(), getSorted());
    }

    /**
     * 返回分页的排序参数
     */
    @Nullable
    Sort getSorted();

}
