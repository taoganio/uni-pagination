package io.github.taoganio.unipagination.mongodb;

import io.github.taoganio.unipagination.domain.Sort;
import io.github.taoganio.unipagination.statement.BasePaginationStatement;
import io.github.taoganio.unipagination.statement.BasePaginationStatementBuilder;
import io.github.taoganio.unipagination.util.Assert;
import org.bson.conversions.Bson;
import org.bson.Document;

import jakarta.annotation.Nullable;

/**
 * MongoDB Find 语句
 */
public class MongoFindPaginationStatement extends BasePaginationStatement implements MongoPaginationStatement {

    private final String collectionName;
    private final Bson filter;
    @Nullable
    private final MongoFindOptions findOptions;

    private MongoFindPaginationStatement(Builder builder) {
        super(builder.getPageable());
        Assert.notEmpty(builder.collectionName, "collectionName must not be empty!");
        this.collectionName = builder.collectionName;
        this.filter = builder.filter;
        this.findOptions = builder.findOptions;
    }

    @Override
    public Document getNativeStatement() {
        // 构造完整的 runCommand find 命令（不包含分页与排序：skip/limit/sort 由执行器处理）
        Document cmd = new Document("find", collectionName);
        if (filter != null) {
            cmd.append("filter", filter);
        }

        MongoFindOptions options = this.findOptions;
        if (options != null) {
            // projection
            if (options.getProjection() != null) {
                cmd.append("projection", options.getProjection());
            }
            // batchSize
            if (options.getBatchSize() > 0) {
                cmd.append("batchSize", options.getBatchSize());
            }
            // maxTimeMS / maxAwaitTimeMS
            long maxTimeMs = options.getMaxTime(java.util.concurrent.TimeUnit.MILLISECONDS);
            if (maxTimeMs > 0) {
                cmd.append("maxTimeMS", maxTimeMs);
            }
            long maxAwaitTimeMs = options.getMaxAwaitTime(java.util.concurrent.TimeUnit.MILLISECONDS);
            if (maxAwaitTimeMs > 0) {
                cmd.append("maxAwaitTimeMS", maxAwaitTimeMs);
            }
            // collation
            if (options.getCollation() != null) {
                cmd.append("collation", options.getCollation().asDocument());
            }
            // hint / hintString
            if (options.getHint() != null) {
                cmd.append("hint", options.getHint());
            } else if (options.getHintString() != null) {
                cmd.append("hint", options.getHintString());
            }
            // comment
            if (options.getComment() != null) {
                cmd.append("comment", options.getComment());
            }
            // let
            if (options.getLet() != null) {
                cmd.append("let", options.getLet());
            }
            // min / max
            if (options.getMin() != null) {
                cmd.append("min", options.getMin());
            }
            if (options.getMax() != null) {
                cmd.append("max", options.getMax());
            }
            // returnKey / showRecordId
            if (options.isReturnKey()) {
                cmd.append("returnKey", true);
            }
            if (options.isShowRecordId()) {
                cmd.append("showRecordId", true);
            }
            // allowDiskUse
            if (options.isAllowDiskUse() != null) {
                cmd.append("allowDiskUse", options.isAllowDiskUse());
            }
            // 游标与超时相关
            if (options.isNoCursorTimeout()) {
                cmd.append("noCursorTimeout", true);
            }
            if (options.isOplogReplay()) {
                cmd.append("oplogReplay", true);
            }
            if (options.isPartial()) {
                cmd.append("allowPartialResults", true);
            }
            if (options.getCursorType() != null) {
                switch (options.getCursorType()) {
                    case Tailable:
                        cmd.append("tailable", true);
                        break;
                    case TailableAwait:
                        cmd.append("tailable", true).append("awaitData", true);
                        break;
                    case NonTailable:
                    default:
                        break;
                }
            }
        }

        // 基于 Pageable 标识 limit/skip/sort
        int pageSize = Math.max(getPageable().getPageSize(), 0);
        int pageNumber = Math.max(getPageable().getPageNumber(), 0);
        int skip = pageNumber * pageSize;
        cmd.append("limit", pageSize);
        cmd.append("skip", skip);
        if (getPageable().getSort() != null && getPageable().getSort().isSorted()) {
            Document sortDoc = new Document();
            for (Sort.Order order : getPageable().getSort()) {
                sortDoc.append(order.getProperty(), order.isAscending() ? 1 : -1);
            }
            cmd.append("sort", sortDoc);
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

    @Nullable
    @Override
    public MongoFindOptions getFindOptions() {
        return findOptions;
    }

    public static Builder of() {
        return new Builder();
    }

    public static class Builder extends BasePaginationStatementBuilder<MongoFindPaginationStatement, Builder> {

        private String collectionName;
        @Nullable
        private Bson filter;
        @Nullable
        private MongoFindOptions findOptions;

        public Builder collection(String collectionName) {
            this.collectionName = collectionName;
            return self();
        }

        public Builder filter(@Nullable Bson filter) {
            this.filter = filter;
            return self();
        }

        public Builder findOptions(@Nullable MongoFindOptions findOptions) {
            this.findOptions = findOptions;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public MongoFindPaginationStatement build() {
            return new MongoFindPaginationStatement(this);
        }

    }
}


