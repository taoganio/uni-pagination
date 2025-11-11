package io.github.taoganio.unipagination.util;

import java.util.Arrays;

public class PageSizes {
    /**
     * 总页码
     */
    private final int totalPages;
    /**
     * 每页大小
     */
    private final int[] pageSizes;

    public PageSizes(int totalPages, int[] pageSizes) {
        this.totalPages = totalPages;
        this.pageSizes = pageSizes;
    }

    /**
     * 获取页面大小
     *
     * @param total    总数
     * @param pageSize 页大小
     * @return {@link PageSizes}
     */
    public static PageSizes from(long total, int pageSize) {
        Assert.state(pageSize > 0, "Page size must not be less than one!");

        if (total <= 0) {
            return new PageSizes(0, new int[]{0});
        }

        if (pageSize >= total) {
            return new PageSizes(1, new int[]{(int) total});
        }

        int pages = (int) (total / pageSize);
        int remainder = (int) (total % pageSize);
        int pageCount = pages + (remainder > 0 ? 1 : 0);

        int[] pageSizes = new int[pageCount];
        Arrays.fill(pageSizes, pageSize);

        if (remainder > 0) {
            pageSizes[pages] = remainder;
        }

        return new PageSizes(pageCount, pageSizes);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageSize(int page) {
        if (page < 0 || page >= pageSizes.length) {
            throw new IllegalArgumentException("Page index out of range: " + page);
        }
        return pageSizes[page];
    }

    public int[] getPageSizes() {
        return pageSizes;
    }

    public boolean isEmpty() {
        return totalPages <= 0;
    }

    @Override
    public String toString() {
        return "PageSizes{" +
                ", totalPage=" + totalPages +
                ", pageSizes=" + Arrays.toString(pageSizes) +
                '}';
    }
}