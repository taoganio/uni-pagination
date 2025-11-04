package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.domain.Page;
import io.github.taoganio.unipagination.domain.PageImpl;
import io.github.taoganio.unipagination.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页行映射器结果集处理程序
 */
public class PaginationRowMapperResultSetHandler<T> implements PaginationResultSetHandler<T> {

    private final PaginationRowMapper<T> rowMapper;
    private final int rowsExpected;

    public PaginationRowMapperResultSetHandler(PaginationRowMapper<T> rowMapper) {
        this(rowMapper, 0);
    }

    public PaginationRowMapperResultSetHandler(PaginationRowMapper<T> rowMapper, int rowsExpected) {
        Assert.notNull(rowMapper, "PaginationRowMapper is required");
        this.rowMapper = rowMapper;
        this.rowsExpected = rowsExpected;
    }

    @Override
    public Page<T> handle(PaginationResultSet resultSet) {
        List<T> results = (this.rowsExpected > 0 ? new ArrayList<>(this.rowsExpected) : new ArrayList<>());
        int rowNumber = 0;
        PaginationResultSetMetadata metadata = resultSet.getMetadata();
        for (PaginationRow prs : resultSet) {
            results.add(rowMapper.mapRow(metadata, prs, rowNumber++));
        }
        return new PageImpl<>(resultSet.getTotal(), results, metadata.getPageable());
    }
}
