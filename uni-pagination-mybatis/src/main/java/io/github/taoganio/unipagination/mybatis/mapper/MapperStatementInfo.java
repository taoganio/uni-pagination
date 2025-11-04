package io.github.taoganio.unipagination.mybatis.mapper;


import java.lang.reflect.Method;

/**
 * Mapper 语句信息
 */
public interface MapperStatementInfo {
    /**
     * 获取映射器语句 ID
     */
    String getMapperStatementId();

    /**
     * 获取类
     */
    Class<?> getClasses();

    /**
     * 获取方法名称
     */
    String getMethodName();

    /**
     * 获取 Mapper 语句方法, 为对参数进行匹配
     */
    Method getMapperStatementMethod();
}
