package io.github.taoganio.unipagination.mybatis.mapper;

/**
 * Mapper 语句信息实现类
 */
public class MapperStatementInfoImpl extends AbstractMapperStatementInfo {

    private final String mapperStatementId;
    private String className;
    private Class<?> clazz;
    private String methodName;

    public MapperStatementInfoImpl(String mapperStatementId) {
        this.mapperStatementId = mapperStatementId;
        parseMapperStatementId();
    }

    private void parseMapperStatementId() {
        int lastIndexOf = mapperStatementId.lastIndexOf(".");
        if (lastIndexOf <= 0) {
            throw new IllegalArgumentException("mapperStatementId '" + mapperStatementId + "' is not legal");
        }
        this.className = mapperStatementId.substring(0, lastIndexOf);
        this.methodName = mapperStatementId.substring(lastIndexOf + 1);
    }

    @Override
    public String getMapperStatementId() {
        return mapperStatementId;
    }

    @Override
    public Class<?> getClasses() {
        if (clazz != null) {
            return clazz;
        }
        try {
            this.clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className, e);
        }
        return clazz;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }
}
