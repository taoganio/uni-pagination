package io.github.taoganio.unipagination.result.set;

import java.util.Map;

/**
 * 分页映射结果集处理程序
 */
public class PaginationMapResultSetHandler extends AbstractPaginationContentHandler<Map<String, Object>> {

    public PaginationMapResultSetHandler() {
        super(new MapPaginationRowMapper());
    }

    public PaginationMapResultSetHandler(PaginationRowMapper<Map<String, Object>> paginationRowMapper) {
        super(paginationRowMapper);
    }

}
