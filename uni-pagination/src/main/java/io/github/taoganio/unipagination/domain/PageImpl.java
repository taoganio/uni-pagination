package io.github.taoganio.unipagination.domain;


import io.github.taoganio.unipagination.util.Assert;

import java.util.*;

/**
 * 页面实现
 */
public class PageImpl<T> implements Page<T> {

    private final List<T> content = new ArrayList<>();
    private final Pageable pageable;
    private final Sort sorted;
    private final long total;

    public PageImpl(long total, Pageable pageable) {
        this(total, new ArrayList<>(), pageable);
    }

    public PageImpl(long total, List<T> content, Pageable pageable) {
        Assert.notNull(content, "Content must not be null");
        Assert.notNull(pageable, "Pageable must not be null");
        this.content.addAll(content);
        this.pageable = pageable;
        this.sorted = pageable.getSort() == null ? Sort.unsorted() : pageable.getSort();
        this.total = Optional.of(pageable).filter(it -> !content.isEmpty())//
                .filter(it -> it.getOffset() + it.getPageSize() > total)//
                .map(it -> it.getOffset() + content.size())//
                .orElse(total);
    }

    @Override
    public int getNumber() {
        return pageable.getPageNumber();
    }

    @Override
    public int getSize() {
        return pageable.getPageSize();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSorted() {
        return sorted;
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Unpaged.INSTANCE;
    }

    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Unpaged.INSTANCE;
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0
                ? 1
                : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }


    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
