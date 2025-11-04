package io.github.taoganio.unipagination.result.set;

import io.github.taoganio.unipagination.exception.PaginationException;
import jakarta.annotation.Nullable;

/**
 * 分页行映射器
 */
public interface PaginationRowMapper<T> {

    @Nullable
    T mapRow(PaginationResultSetMetadata metadata, PaginationRow prs, int rowNum) throws PaginationException;

}
