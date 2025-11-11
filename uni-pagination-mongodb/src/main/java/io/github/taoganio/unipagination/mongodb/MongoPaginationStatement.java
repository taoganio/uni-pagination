package io.github.taoganio.unipagination.mongodb;

import io.github.taoganio.unipagination.statement.PaginationStatement;
import org.bson.Document;
import org.bson.conversions.Bson;

import jakarta.annotation.Nullable;

import java.util.function.Function;

/**
 * MongoDB 分页语句
 */
public interface MongoPaginationStatement extends PaginationStatement {

    @Override
    default String getLanguage() {
        return "MongoDB";
    }

    /**
     * 获取MongoDb 可执行的原始 runCommand 语句
     */
    Document getNativeStatement();

    /**
     * 获取集合
     */
    String getCollection();

    /**
     * 查询过滤条件
     */
    @Nullable
    Bson getFilter();

    /**
     * 查询字段
     */
    @Nullable
    Bson getProjection();

    /**
     * 选项
     */
    @Nullable
    MongoFindOptions getFindOptions();

    static MongoPaginationStatement of(
            Function<MongoFindPaginationStatement.Builder, MongoFindPaginationStatement.Builder> function) {
        return MongoFindPaginationStatement.of(function);
    }
}


