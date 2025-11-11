package io.github.taoganio.unipagination.mongodb;

import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.domain.Sort;
import io.github.taoganio.unipagination.statement.BasePaginationStatement;
import io.github.taoganio.unipagination.statement.BasePaginationStatementBuilder;
import io.github.taoganio.unipagination.util.Assert;
import org.bson.BsonString;
import org.bson.conversions.Bson;
import org.bson.Document;

import jakarta.annotation.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * MongoDB Find 语句
 */
public class MongoFindPaginationStatement extends BasePaginationStatement implements MongoPaginationStatement {

    private final String collectionName;
    private final Bson filter;
    private final Bson projection;
    @Nullable
    private final MongoFindOptions findOptions;

    private MongoFindPaginationStatement(Builder builder) {
        super(builder.getPageable());
        Assert.notEmpty(builder.collectionName, "collectionName must not be empty!");
        this.collectionName = builder.collectionName;
        this.projection = builder.projection;
        this.filter = builder.filter;
        this.findOptions = builder.findOptions;
    }

    @Override
    public Document getNativeStatement() {
        // 构造 runCommand find 命令
        Document cmd = new Document("find", collectionName);
        if (filter != null) {
            cmd.append("filter", filter.toBsonDocument());
        }
        if (projection != null) {
            cmd.append("projection", projection.toBsonDocument());
        }

        Pageable pageable = getPageable();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int skip = pageNumber * pageSize;
        cmd.append("limit", pageSize);
        cmd.append("skip", skip);
        if (pageable.getSort() != null && pageable.getSort().isSorted()) {
            Document sortDoc = new Document();
            for (Sort.Order order : pageable.getSort()) {
                sortDoc.append(order.getProperty(), order.isAscending() ? 1 : -1);
            }
            cmd.append("sort", sortDoc);
        }

        MongoFindOptions options = this.findOptions;
        if (options != null) {
            if (options.getBatchSize() > 0) {
                cmd.append("batchSize", options.getBatchSize());
            }
            long maxTimeMs = options.getMaxTime(TimeUnit.MILLISECONDS);
            if (maxTimeMs > 0) {
                cmd.append("maxTimeMS", maxTimeMs);
            }
            if (options.getCollation() != null) {
                cmd.append("collation", options.getCollation().asDocument());
            }
            if (options.getHint() != null) {
                cmd.append("hint", options.getHint().toBsonDocument());
            } else if (options.getHintString() != null) {
                cmd.append("hint", new BsonString(options.getHintString()));
            }
            if (options.getComment() != null) {
                cmd.append("comment", options.getComment());
            }
            if (options.isAllowDiskUse() != null) {
                cmd.append("allowDiskUse", options.isAllowDiskUse());
            }
            if (options.isNoCursorTimeout()) {
                cmd.append("noCursorTimeout", true);
            }
            if (options.isPartial()) {
                cmd.append("allowPartialResults", true);
            }
        }

        return cmd;
    }

    @Override
    public String getCollection() {
        return collectionName;
    }

    @Override
    public Bson getFilter() {
        return filter;
    }

    @Override
    public Bson getProjection() {
        return projection;
    }

    @Nullable
    @Override
    public MongoFindOptions getFindOptions() {
        return findOptions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static MongoFindPaginationStatement of(Function<Builder, Builder> builderFunction) {
        return builderFunction.apply(builder()).build();
    }

    @Override
    public String toString() {
        return getNativeStatement().toJson();
    }

    public static class Builder extends BasePaginationStatementBuilder<MongoFindPaginationStatement, Builder> {

        private String collectionName;
        @Nullable
        private Bson filter;
        @Nullable
        private MongoFindOptions findOptions;
        @Nullable
        private Bson projection;

        public Builder collection(String collectionName) {
            this.collectionName = collectionName;
            return self();
        }

        public Builder filter(@Nullable Bson filter) {
            this.filter = filter;
            return self();
        }

        public Builder projection(@Nullable Bson projection) {
            this.projection = projection;
            return self();
        }

        public Builder options(@Nullable MongoFindOptions findOptions) {
            this.findOptions = findOptions;
            return self();
        }

        public Builder options(Function<MongoFindOptions, MongoFindOptions> findOptions) {
            this.findOptions = findOptions.apply(new MongoFindOptions());
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public MongoFindPaginationStatement build() {
            Assert.notEmpty(collectionName, "collectionName must not be empty!");
            return new MongoFindPaginationStatement(this);
        }

    }
}


