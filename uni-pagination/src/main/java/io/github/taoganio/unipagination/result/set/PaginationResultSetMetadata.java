package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.domain.Pageable;

/**
 * 分页结果集元数据
 */
public interface PaginationResultSetMetadata {

    /**
     * 获得可分页
     */
    Pageable getPageable();

    /**
     * 获得列数
     */
    int getColumnCount();

    /**
     * 获得列元数据
     */
    ColumnMetadata getColumnMetadata(int columnIndex);

    /**
     * 获取列元数据
     */
    ColumnMetadata getColumnMetadata(String columnName);
}
