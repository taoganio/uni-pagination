package io.github.taoganio.unipagination.mybatis.mapper;

import io.github.taoganio.unipagination.mybatis.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 抽象映射器语句信息
 */
public abstract class AbstractMapperStatementInfo implements MapperStatementInfo {

    @Override
    public Method getMapperStatementMethod() {
        Method method = ReflectionUtils.findMethod(getClasses(), getMethodName());
        if (method == null) {
            throw new IllegalArgumentException("No [" +
                    getClasses().getName() + "." + getMethodName() + "] mapper method");
        }
        return method;
    }
}
