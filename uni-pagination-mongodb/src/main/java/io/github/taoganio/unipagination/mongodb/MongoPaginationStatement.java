package io.github.taoganio.unipagination.mongodb;

import io.github.taoganio.unipagination.statement.PaginationStatement;
import org.bson.Document;
import org.bson.conversions.Bson;

import jakarta.annotation.Nullable;

/**
 * MongoDB 分页语句
 */
public interface MongoPaginationStatement extends PaginationStatement {

    @Override
    default String getLanguage() {
        return "MongoDB";
    }

    /**
     * 获取完整可执行的原始语句，例如 runCommand 的命令 Document
     */
    Document getNativeStatement();

    /**
     * 获取集合
     */
    String getCollection();

    /**
     * 查询过滤条件
     */
    Bson getFilter();

    /**
     * 选项
     */
    @Nullable
    MongoFindOptions getFindOptions();
}


