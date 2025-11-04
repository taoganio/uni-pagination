package io.github.taoganio.unipagination.mybatis.parameter;

import io.github.taoganio.unipagination.mybatis.mapper.MapperStatementInfo;

/**
 * 参数名称解析器供应商
 */
public interface ParamNameResolverSupplier {
    /**
     * 获取
     *
     * @param info       语句信息
     * @param parameters 参数
     * @return 参数
     */
    Object get(MapperStatementInfo info, Object[] parameters);
}
