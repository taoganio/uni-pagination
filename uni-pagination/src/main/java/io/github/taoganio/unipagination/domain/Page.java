package io.github.taoganio.unipagination.domain;

import java.util.List;

/**
 * 页面
 */
public interface Page<T> extends Iterable<T>, PageInformation {

    /**
     * 返回当前分页元素的数量
     *
     */
    int getNumberOfElements();

    /**
     * 返回分页是否包含任何内容
     *
     */
    List<T> getContent();

    /**
     * 是否包含任何内容
     *
     */
    boolean hasContent();

    /**
     * 根据当前分页内容获取下一页分页信息
     *
     */
    Pageable nextPageable();

    /**
     * 根据当前分页内容获取上一页分页信息
     *
     */
    Pageable previousPageable();

    /**
     * 根据当前分页内容返回下一页或最后一页分页信息
     *
     */
    default Pageable nextOrLastPageable() {
        return hasNext() ? nextPageable() : getPageable();
    }

    /**
     * 根据当前分页内容返回上一页或第一页分页信息
     *
     */
    default Pageable previousOrFirstPageable() {
        return hasPrevious() ? previousPageable() : getPageable();
    }

}
