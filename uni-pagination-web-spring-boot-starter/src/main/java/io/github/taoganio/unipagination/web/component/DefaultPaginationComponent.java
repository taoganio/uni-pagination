package io.github.taoganio.unipagination.web.component;

import io.github.taoganio.unipagination.result.set.MapPaginationRowMapper;
import io.github.taoganio.unipagination.result.set.PaginationMapResultSetHandler;
import io.github.taoganio.unipagination.result.set.PaginationResultSetHandler;
import io.github.taoganio.unipagination.result.set.PaginationRowMapper;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DefaultPaginationComponent implements PaginationComponent {

    private MapPaginationRowMapper dataRowMapper = new MapPaginationRowMapper();
    private PaginationResultSetHandler<?> dataResultSetHandler = new PaginationMapResultSetHandler(dataRowMapper);
    private PaginationExporter exporter = NullPaginationExporter.INSTANCE;

    @Override
    public PaginationResultSetHandler<?> getDataResultSetHandler() {
        return dataResultSetHandler;
    }

    @Override
    public PaginationRowMapper<?> getDataRowMapper() {
        return dataRowMapper;
    }

    @Override
    public PaginationExporter getExporter() {
        return exporter;
    }
}
