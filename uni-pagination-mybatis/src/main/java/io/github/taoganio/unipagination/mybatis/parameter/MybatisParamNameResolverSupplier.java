package io.github.taoganio.unipagination.mybatis.parameter;

import io.github.taoganio.unipagination.mybatis.mapper.MapperStatementInfo;
import jakarta.annotation.Nullable;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;

/**
 * Mybatis 参数名称解析器供应商
 */
public class MybatisParamNameResolverSupplier implements ParamNameResolverSupplier {

    @Nullable
    private Configuration configuration;

    private static class ParamNameResolverSupplierHolder {
        private static final MybatisParamNameResolverSupplier INSTANCE = new MybatisParamNameResolverSupplier();
        private static final Configuration CONFIGURATION = new Configuration();
    }

    public static MybatisParamNameResolverSupplier getInstance() {
        return ParamNameResolverSupplierHolder.INSTANCE;
    }

    public MybatisParamNameResolverSupplier() {
    }

    @Override
    public Object get(MapperStatementInfo info, Object[] parameters) {
        return new ParamNameResolver(configuration != null ? configuration : ParamNameResolverSupplierHolder.CONFIGURATION
                , info.getMapperStatementMethod()).getNamedParams(parameters);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
