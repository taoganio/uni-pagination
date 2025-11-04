package io.github.taoganio.unipagination.domain;

import jakarta.annotation.Nullable;

/**
 * 页面请求
 */
public class PageRequest implements Pageable {
    private final int pageNumber;
    private final int pageSize;
    private final Sort sort;

    private PageRequest(int pageNumber, int pageSize, @Nullable Sort sort) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero");
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort == null ? Sort.unsorted() : sort;
    }

    public static Pageable of(int pageNumber, int pageSize) {
        return new PageRequest(pageNumber, pageSize, null);
    }


    public static Pageable of(int pageNumber, int pageSize, Sort sort) {
        return new PageRequest(pageNumber, pageSize, sort);
    }


    public static Pageable of(int pageNumber, int pageSize, Sort.Direction direction, String... properties) {
        return new PageRequest(pageNumber, pageSize, Sort.by(direction, properties));
    }


    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getOffset() {
        return (long) getPageNumber() * (long) getPageSize();
    }

    @Override
    public Pageable first() {
        return PageRequest.of(0, getPageSize());
    }

    @Override
    public Pageable next() {
        return new PageRequest(getPageNumber() + 1, getPageSize(), getSort());
    }

    public Pageable previous() {
        return getPageNumber() == 0 ? this
                : new PageRequest(getPageNumber() - 1, getPageSize(), getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public boolean hasPrevious() {
        return getPageSize() > 0;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    public PageRequest withPage(int pageNumber) {
        return new PageRequest(pageNumber, this.getPageSize(), this.getSort());
    }

    public PageRequest withLimit(int pageLimit) {
        return new PageRequest(this.getPageNumber(), this.getPageSize(), this.getSort());
    }

    public PageRequest withSort(Sort.Direction direction, String... properties) {
        return new PageRequest(this.getPageNumber(), this.getPageSize(), Sort.by(direction, properties));
    }
}
