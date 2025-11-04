package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 分页结果集元数据实现
 */
public class DefaultPaginationResultSetMetadata implements PaginationResultSetMetadata {

    private final Pageable pageable;
    private final List<ColumnMetadata> columnMetadataList;
    private final Map<String, ColumnMetadata> columnMetadataMap;

    public DefaultPaginationResultSetMetadata(Pageable pageable, List<ColumnMetadata> columnMetadataList) {
        Assert.notNull(pageable, "Pageable must not be null");
        Assert.notNull(columnMetadataList, "ColumnMetadata must not be null");
        this.pageable = pageable;
        this.columnMetadataList = Collections.unmodifiableList(columnMetadataList);
        this.columnMetadataMap = this.columnMetadataList
                .stream().collect(Collectors.toMap(ColumnMetadata::getColumnName, e -> e, (k1, k2) -> k1));
    }

    @Override
    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public int getColumnCount() {
        return columnMetadataList.size();
    }

    @Override
    public ColumnMetadata getColumnMetadata(int columnIndex) {
        if (columnIndex >= columnMetadataList.size()) {
            throw new IllegalArgumentException("No column metadata of " + columnIndex);
        }
        return columnMetadataList.get(columnIndex);
    }

    @Override
    public ColumnMetadata getColumnMetadata(String columnName) {
        ColumnMetadata columnMetadata = columnMetadataMap.get(columnName);
        if (columnMetadata == null) {
            throw new IllegalArgumentException("No column metadata of " + columnName);
        }
        return columnMetadata;
    }


}
