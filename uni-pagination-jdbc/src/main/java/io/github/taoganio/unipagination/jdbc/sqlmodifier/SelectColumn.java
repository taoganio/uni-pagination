package io.github.taoganio.unipagination.jdbc.sqlmodifier;

import java.util.Objects;

public class SelectColumn {
    /**
     * 查询列
     */
    private final String select;
    /**
     * 忽略大小写
     */
    private final boolean ignoreCase;
    /**
     * 忽略
     */
    private boolean ignore;

    public SelectColumn(String select) {
        this(select, true);
    }

    public SelectColumn(String select, boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        this.select = ignoreCase ? select.toLowerCase() : select;
    }

    public String getSelect() {
        return select;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectColumn that = (SelectColumn) o;
        return Objects.equals(select, that.select);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(select);
    }
}
