package io.github.taoganio.unipagination.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.taoganio.unipagination.domain.PageImpl;
import io.github.taoganio.unipagination.domain.PageInformation;
import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.domain.Sort;
import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.executor.PaginationStatementExecutor;
import io.github.taoganio.unipagination.result.set.*;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import io.github.taoganio.unipagination.util.Assert;
import io.github.taoganio.unipagination.util.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * MongoDB 分页语句执行器
 */
public class MongoFindPaginationStatementExecutor implements PaginationStatementExecutor<MongoPaginationStatement> {

    private final Supplier<MongoDatabase> mongoDatabase;

    public MongoFindPaginationStatementExecutor(MongoDatabase mongoDatabase) {
        Assert.notNull(mongoDatabase, "MongoDatabase must not be null");
        this.mongoDatabase = () -> mongoDatabase;
    }

    public MongoFindPaginationStatementExecutor(Supplier<MongoDatabase> mongoDatabase) {
        Assert.notNull(mongoDatabase, "MongoDatabase must not be null");
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public boolean supports(PaginationStatement statement) {
        return statement instanceof MongoPaginationStatement;
    }

    @Override
    public PageInformation executeForInformation(MongoPaginationStatement statement) throws PaginationException {
        MongoCollection<Document> collection = getCollection(statement);
        Bson filter = statement.getFilter();
        long total = collection.countDocuments(filter);
        return new PageImpl<>(total, statement.getPageable());
    }

    @Override
    public PaginationResultSet executeForResultSet(MongoPaginationStatement statement) throws PaginationException {
        MongoCollection<Document> collection = getCollection(statement);
        Pageable pageable = statement.getPageable();
        Bson filter = statement.getFilter();
        long total = collection.countDocuments(filter);
        if (total <= 0) {
            return new EmptyPaginationResultSet(pageable);
        }

        FindIterable<Document> find = collection.find(filter);
        MongoFindOptions options = statement.getFindOptions();

        // 应用 MongoFindOptions
        if (options != null) {
            find = applyFindOptions(options, find);
        }

        Bson sort = toMongoSort(pageable.getSort());
        if (sort != null) {
            find = find.sort(sort);
        }
        int skip = Math.max(pageable.getPageNumber(), 0) * pageable.getPageSize();
        find = find.skip(skip).limit(pageable.getPageSize());

        List<Document> documents = new ArrayList<>();
        for (Document doc : find) {
            documents.add(doc);
        }
        if (CollectionUtils.isEmpty(documents)) {
            return new EmptyPaginationResultSet(pageable);
        }

        List<ColumnMetadata> columns = inferColumns(documents);
        PaginationResultSetMetadata metadata = new DefaultPaginationResultSetMetadata(pageable, columns);
        List<PaginationRow> rows = documents.stream()
                .map(d -> new MongoPaginationRow(d, metadata))
                .collect(Collectors.toList());
        return new DefaultPaginationResultSet(total, metadata, rows);
    }

    private FindIterable<Document> applyFindOptions(MongoFindOptions options, FindIterable<Document> find) {
        // projection
        Bson projection = options.getProjection();
        if (projection != null) {
            find = find.projection(projection);
        }

        // batchSize (>0 生效)
        if (options.getBatchSize() > 0) {
            find = find.batchSize(options.getBatchSize());
        }

        // maxTime / maxAwaitTime (>0 生效)
        long maxTimeMs = options.getMaxTime(TimeUnit.MILLISECONDS);
        if (maxTimeMs > 0) {
            find = find.maxTime(maxTimeMs, TimeUnit.MILLISECONDS);
        }
        long maxAwaitTimeMs = options.getMaxAwaitTime(TimeUnit.MILLISECONDS);
        if (maxAwaitTimeMs > 0) {
            find = find.maxAwaitTime(maxAwaitTimeMs, TimeUnit.MILLISECONDS);
        }

        // collation
        if (options.getCollation() != null) {
            find = find.collation(options.getCollation());
        }

        // hint / hintString（先使用 hint，其次 hintString）
        if (options.getHint() != null) {
            find = find.hint(options.getHint());
        } else if (options.getHintString() != null) {
            find = find.hintString(options.getHintString());
        }

        // comment
        if (options.getComment() != null) {
            find = find.comment(options.getComment());
        }

        // let (variables)
        if (options.getLet() != null) {
            find = find.let(options.getLet());
        }

        // min / max（索引边界）
        if (options.getMin() != null) {
            find = find.min(options.getMin());
        }
        if (options.getMax() != null) {
            find = find.max(options.getMax());
        }

        // cursorType（Tailable/Await/NonTailable）
        if (options.getCursorType() != null) {
            find = find.cursorType(options.getCursorType());
        }

        // noCursorTimeout / oplogReplay / partial
        find = find.noCursorTimeout(options.isNoCursorTimeout());
        if (options.isOplogReplay()) {
            find = find.oplogReplay(true);
        }
        if (options.isPartial()) {
            find = find.partial(true);
        }

        // returnKey / showRecordId
        if (options.isReturnKey()) {
            find = find.returnKey(true);
        }
        if (options.isShowRecordId()) {
            find = find.showRecordId(true);
        }

        // allowDiskUse（Boolean）
        if (options.isAllowDiskUse() != null) {
            find = find.allowDiskUse(options.isAllowDiskUse());
        }

        // limit/skip/sort 由分页器统一处理，这里不应用
        return find;
    }

    private MongoCollection<Document> getCollection(MongoPaginationStatement statement) {
        return mongoDatabase.get().getCollection(statement.getCollection());
    }

    private Bson toMongoSort(Sort sort) {
        if (sort == null || !sort.isSorted()) {
            return null;
        }
        Document sortDoc = new Document();
        for (Sort.Order order : sort) {
            sortDoc.append(order.getProperty(), order.isAscending() ? 1 : -1);
        }
        return sortDoc;
    }

    private List<ColumnMetadata> inferColumns(List<Document> documents) {
        Set<String> names = new HashSet<>();
        for (Document doc : documents) {
            names.addAll(doc.keySet());
        }
        List<ColumnMetadata> columns = new ArrayList<>();
        int idx = 0;
        for (String name : names) {
            Class<?> type = Object.class;
            for (Document doc : documents) {
                if (doc.containsKey(name) && doc.get(name) != null) {
                    type = doc.get(name).getClass();
                    break;
                }
            }
            columns.add(new DefaultColumnMetadata(name, type, idx++));
        }
        return columns;
    }
}


