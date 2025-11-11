package io.github.taoganio.unipagination.mybatis;

import io.github.taoganio.unipagination.mybatis.mapper.*;
import io.github.taoganio.unipagination.mybatis.parameter.MybatisParamNameResolverSupplier;
import io.github.taoganio.unipagination.mybatis.parameter.ParamNameResolverSupplier;
import io.github.taoganio.unipagination.statement.BasePaginationStatement;
import io.github.taoganio.unipagination.statement.BasePaginationStatementBuilder;
import io.github.taoganio.unipagination.util.Assert;
import jakarta.annotation.Nullable;
import org.apache.ibatis.session.Configuration;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Mybatis 分页语句
 */
public class MybatisMapperPaginationStatement extends BasePaginationStatement implements MybatisPaginationStatement {

    private final Class<?> mapperClass;
    /**
     * 映射语句 ID
     */
    private final String mappedStatementId;
    /**
     * 参数
     */
    @Nullable
    private final Object parameter;

    private MybatisMapperPaginationStatement(Builder<?> builder) {
        super(builder.getPageable());
        Assert.notEmpty(builder.mappedStatementId, "mappedStatementId must not be empty!");
        this.mapperClass = builder.mapperClass;
        this.mappedStatementId = builder.mappedStatementId;
        this.parameter = builder.parameterObject;
    }

    public Class<?> getMapperClass() {
        return mapperClass;
    }

    @Override
    public String getNativeStatement() {
        return mappedStatementId;
    }

    @Override
    @Nullable
    public Object getParameter() {
        return parameter;
    }

    @Override
    public String toString() {
        return mappedStatementId;
    }

    public static <T> Builder<T> builder(Class<T> mapperClass) {
        return new Builder<>(mapperClass);
    }

    public static <T> MybatisMapperPaginationStatement of(Class<T> mapperClass, Function<Builder<T>, Builder<T>> function) {
        return function.apply(builder(mapperClass)).build();
    }

    public static class Builder<T>
            extends BasePaginationStatementBuilder<MybatisMapperPaginationStatement, Builder<T>> {

        private final Class<T> mapperClass;
        private Configuration configuration;
        private String mappedStatementId;
        private Object parameterObject;
        private Object[] params;
        @Nullable
        private ParamNameResolverSupplier paramNameResolver;

        public Builder(Class<T> mapperClass) {
            this.mapperClass = mapperClass;
        }

        private Builder<T> mapper(Serializable mappedStatement, Object... params) {
            this.mappedStatementId = getSerializableMapperStatementId(mappedStatement);
            this.params = params;
            return self();
        }

        public <R> Builder<T> reference(Function0<T, R> function) {
            return mapper(function, (Object) null);
        }

        public <P, R> Builder<T> reference(Function1<T, P, R> function, P p) {
            return mapper(function, p);
        }

        public <P1, P2, R> Builder<T> reference(Function2<T, P1, P2, R> function, P1 p1, P2 p2) {
            return mapper(function, p1, p2);
        }

        public <P1, P2, P3, R> Builder<T> reference(Function3<T, P1, P2, P3, R> function,
                                                    P1 p1, P2 p2, P3 p3) {
            return mapper(function, p1, p2, p3);
        }

        public <P1, P2, P3, P4, R> Builder<T> reference(Function4<T, P1, P2, P3, P4, R> function,
                                                        P1 p1, P2 p2, P3 p3, P4 p4) {
            return mapper(function, p1, p2, p3, p4);
        }

        public <P1, P2, P3, P4, P5, R> Builder<T> reference(Function5<T, P1, P2, P3, P4, P5, R> function,
                                                            P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
            return mapper(function, p1, p2, p3, p4, p5);
        }

        public <P1, P2, P3, P4, P5, P6, R> Builder<T> reference(Function6<T, P1, P2, P3, P4, P5, P6, R> function,
                                                                P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6) {
            return mapper(function, p1, p2, p3, p4, p5, p6);
        }

        public <P1, P2, P3, P4, P5, P6, P7, R> Builder<T> reference(Function7<T, P1, P2, P3, P4, P5, P6, P7, R> function,
                                                                    P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7) {
            return mapper(function, p1, p2, p3, p4, p5, p6, p7);
        }

        public <P1, P2, P3, P4, P5, P6, P7, P8, R> Builder<T> reference(Function8<T, P1, P2, P3, P4, P5, P6, P7, P8, R> function,
                                                                        P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8) {
            return mapper(function, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        public <P1, P2, P3, P4, P5, P6, P7, P8, P9, R> Builder<T> reference(Function9<T, P1, P2, P3, P4, P5, P6, P7, P8, P9, R> function,
                                                                            P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9) {
            return mapper(function, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }

        public <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> Builder<T> reference(Function10<T, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> function,
                                                                                 P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10) {
            return mapper(function, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
        }

        public Builder<T> paramNameResolver(@Nullable ParamNameResolverSupplier paramNameResolver) {
            this.paramNameResolver = paramNameResolver;
            return self();
        }

        public Builder<T> configuration(Configuration configuration) {
            this.configuration = configuration;
            return self();
        }

        @Override
        protected Builder<T> self() {
            return this;
        }

        @Override
        public MybatisMapperPaginationStatement build() {
            if (this.params != null) {
                ParamNameResolverSupplier resolver = getParamNameResolver();
                Assert.notNull(resolver, "ParamNameResolverSupplier must not be null.");
                this.parameterObject = resolver.get(new MapperStatementInfoImpl(mappedStatementId), params);
            }
            return new MybatisMapperPaginationStatement(this);
        }

        protected ParamNameResolverSupplier getParamNameResolver() {
            if (this.paramNameResolver != null) {
                return paramNameResolver;
            } else if (configuration != null) {
                MybatisParamNameResolverSupplier supplier = new MybatisParamNameResolverSupplier();
                supplier.setConfiguration(configuration);
                return supplier;
            } else {
                return MybatisParamNameResolverSupplier.getInstance();
            }
        }

        private String getSerializableMapperStatementId(Serializable serializable) {
            SerializedMapperStatementInfo info = new SerializedMapperStatementInfo(serializable);
            return info.getReference();
        }
    }
}
