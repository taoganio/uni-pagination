package io.github.taoganio.unipagination.mybatis;

import io.github.taoganio.unipagination.statement.PaginationStatement;
import jakarta.annotation.Nullable;

import java.util.function.Function;

/**
 * Mybatis 分页语句
 */
public interface MybatisPaginationStatement extends PaginationStatement {

    @Override
    default String getLanguage() {
        return "Mybatis";
    }

    /**
     * 获取Mybatis映射语句 ID
     *
     * @return Mybatis映射语句 ID
     */
    String getNativeStatement();

    /**
     * 查询参数
     *
     * @return 参数
     */
    @Nullable
    Object getParameter();

    static <T> MybatisPaginationStatement of(Class<T> mapperClass,
                                             Function<MybatisMapperPaginationStatement.Builder<T>,
                                                     MybatisMapperPaginationStatement.Builder<T>> function) {
        return MybatisMapperPaginationStatement.of(mapperClass, function);
    }
}
