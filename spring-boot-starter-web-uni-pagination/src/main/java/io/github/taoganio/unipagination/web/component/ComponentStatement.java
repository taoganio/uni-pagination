package io.github.taoganio.unipagination.web.component;

import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.result.set.PaginationResultSetHandler;
import io.github.taoganio.unipagination.result.set.PaginationRowMapper;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 分页组件包装
 */
@Getter
public class ComponentStatement implements PaginationComponent, PaginationStatement {

    private final PaginationStatement statement;
    private final PaginationRowMapper<?> dataRowMapper;
    private final PaginationResultSetHandler<?> dataResultSetHandler;
    private final PaginationExporter exporter;
    private final Map<String, Object> payload;

    public ComponentStatement(Builder builder) {
        this.statement = builder.statement;
        this.dataRowMapper = builder.dataRowMapper;
        this.dataResultSetHandler = builder.resultSetHandler;
        this.exporter = builder.exporter;
        this.payload = builder.payload;
    }

    @Override
    public PaginationRowMapper<?> getDataRowMapper() {
        return dataRowMapper;
    }

    @Override
    public PaginationResultSetHandler<?> getDataResultSetHandler() {
        return dataResultSetHandler;
    }

    @Override
    public PaginationExporter getExporter() {
        return exporter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ComponentStatement of(Function<Builder, Builder> builderFunction) {
        return builderFunction.apply(new Builder()).build();
    }

    @Override
    public Object getNativeStatement() {
        return statement.getNativeStatement();
    }

    @Override
    public Pageable getPageable() {
        return statement.getPageable();
    }

    @Override
    public void setPageable(Pageable pageable) {
        statement.setPageable(pageable);
    }

    public static class Builder {

        private PaginationStatement statement;
        private PaginationRowMapper<?> dataRowMapper;
        private PaginationResultSetHandler<?> resultSetHandler;
        private PaginationExporter exporter;
        private final Map<String, Object> payload = new HashMap<>();

        public Builder statement(PaginationStatement statement) {
            this.statement = statement;
            return this;
        }

        public Builder rowMapper(PaginationRowMapper<?> dataRowMapper) {
            this.dataRowMapper = dataRowMapper;
            return this;
        }

        public Builder resultSetHandler(PaginationResultSetHandler<?> resultSetHandler) {
            this.resultSetHandler = resultSetHandler;
            return this;
        }

        public Builder exporter(PaginationExporter exporter) {
            this.exporter = exporter;
            return this;
        }

        public Builder payload(String payload, Object value) {
            if (payload != null) {
                this.payload.put(payload, value);
            }
            return this;
        }

        public ComponentStatement build() {
            Assert.notNull(statement, "statement must not be null");
            return new ComponentStatement(this);
        }
    }
}
