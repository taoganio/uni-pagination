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

    private final Supplier<MongoDatabase> databaseProvider;

    public MongoFindPaginationStatementExecutor(MongoDatabase database) {
        Assert.notNull(database, "MongoDatabase must not be null");
        this.databaseProvider = () -> database;
    }

    public MongoFindPaginationStatementExecutor(Supplier<MongoDatabase> databaseProvider) {
        Assert.notNull(databaseProvider, "databaseProvider must not be null");
        this.databaseProvider = databaseProvider;
    }

    @Override
    public boolean supports(PaginationStatement statement) {
        return statement instanceof MongoPaginationStatement;
    }

    @Override
    public PageInformation executeForInformation(MongoPaginationStatement statement) throws PaginationException {
        MongoCollection<Document> collection = getCollection(statement);
        Bson filter = statement.getFilter();
        long total = filter != null ? collection.countDocuments(filter) : collection.countDocuments();
        return new PageImpl<>(total, statement.getPageable());
    }

    @Override
    public PaginationResultSet executeForResultSet(MongoPaginationStatement statement) throws PaginationException {
        MongoCollection<Document> collection = getCollection(statement);
        Pageable pageable = statement.getPageable();
        Bson filter = statement.getFilter();
        long total = filter != null ? collection.countDocuments(filter) : collection.countDocuments();
        if (total <= 0) {
            return new EmptyPaginationResultSet(pageable);
        }

        FindIterable<Document> find = filter != null ? collection.find(filter) : collection.find();
        find = applyFindOptions(statement, find);

        List<Document> documents = new ArrayList<>();
        for (Document doc : find) {
            documents.add(doc);
        }
        if (CollectionUtils.isEmpty(documents)) {
            return new EmptyPaginationResultSet(pageable);
        }
        List<PaginationRow> rows = documents.stream().map(MongoDocumentPaginationRow::new).collect(Collectors.toList());
        return new DefaultPaginationResultSet(total,
                new DefaultPaginationResultSetMetadata(pageable, inferColumns(documents)), rows);
    }

    protected FindIterable<Document> applyFindOptions(MongoPaginationStatement statement, FindIterable<Document> find) {
        Bson projection = statement.getProjection();
        if (projection != null) {
            find = find.projection(projection);
        }

        Pageable pageable = statement.getPageable();
        Sort sort = pageable.getSort();
        if (sort != null && sort.isSorted()) {
            Document sortDoc = new Document();
            for (Sort.Order order : sort) {
                sortDoc.append(order.getProperty(), order.isAscending() ? 1 : -1);
            }
            find = find.sort(sortDoc);
        }
        int skip = pageable.getPageNumber() * pageable.getPageSize();
        find = find.skip(skip).limit(pageable.getPageSize());

        // Options
        MongoFindOptions options = statement.getFindOptions();
        if (options != null) {
            if (options.getBatchSize() > 0) {
                find = find.batchSize(options.getBatchSize());
            }
            long maxTimeMs = options.getMaxTime(TimeUnit.MILLISECONDS);
            if (maxTimeMs > 0) {
                find = find.maxTime(maxTimeMs, TimeUnit.MILLISECONDS);
            }
            long maxAwaitTimeMs = options.getMaxAwaitTime(TimeUnit.MILLISECONDS);
            if (maxAwaitTimeMs > 0) {
                find = find.maxAwaitTime(maxAwaitTimeMs, TimeUnit.MILLISECONDS);
            }
            if (options.getCollation() != null) {
                find = find.collation(options.getCollation());
            }
            if (options.getHint() != null) {
                find = find.hint(options.getHint());
            } else if (options.getHintString() != null) {
                find = find.hintString(options.getHintString());
            }
            if (options.getComment() != null) {
                find = find.comment(options.getComment());
            }
            if (options.getLet() != null) {
                find = find.let(options.getLet());
            }
            if (options.getMin() != null) {
                find = find.min(options.getMin());
            }
            if (options.getMax() != null) {
                find = find.max(options.getMax());
            }
            if (options.getCursorType() != null) {
                find = find.cursorType(options.getCursorType());
            }
            find = find.noCursorTimeout(options.isNoCursorTimeout());
            if (options.isOplogReplay()) {
                find = find.oplogReplay(true);
            }
            if (options.isPartial()) {
                find = find.partial(true);
            }
            if (options.isReturnKey()) {
                find = find.returnKey(true);
            }
            if (options.isShowRecordId()) {
                find = find.showRecordId(true);
            }
            if (options.isAllowDiskUse() != null) {
                find = find.allowDiskUse(options.isAllowDiskUse());
            }
        }
        return find;
    }

    protected MongoCollection<Document> getCollection(MongoPaginationStatement statement) {
        return databaseProvider.get().getCollection(statement.getCollection());
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


