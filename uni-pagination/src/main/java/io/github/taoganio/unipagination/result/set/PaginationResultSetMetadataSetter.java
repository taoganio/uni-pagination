package io.github.taoganio.unipagination.result.set;

/**
 * 分页结果集元数据设置器
 */
public interface PaginationResultSetMetadataSetter {

    /**
     * 设置元数据
     *
     * @param metadata 元数据
     */
    void setMetadata(PaginationResultSetMetadata metadata);
}
