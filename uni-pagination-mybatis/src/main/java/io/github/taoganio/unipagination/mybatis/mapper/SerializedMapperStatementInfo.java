package io.github.taoganio.unipagination.mybatis.mapper;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class SerializedMapperStatementInfo extends AbstractMapperStatementInfo {

    private Class<?> implClass;
    private String implClassName;
    private String methodName;

    public SerializedMapperStatementInfo(Serializable serializable) {
        init(serializable);
    }

    protected void init(Serializable serializable) {
        SerializedLambda lambda = getSerializedLambda(serializable);
        this.implClassName = lambda.getImplClass().replaceAll("/", ".");
        this.methodName = lambda.getImplMethodName();
    }

    private SerializedLambda getSerializedLambda(Serializable serializable) {
        try {
            Method writeReplace = serializable.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(serializable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMapperStatementId() {
        return getReference();
    }

    @Override
    public Class<?> getClasses() {
        if (this.implClass != null) {
            return implClass;
        }
        try {
            implClass = Class.forName(this.implClassName, true, SerializedMapperStatementInfo.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + implClassName, e);
        }
        return implClass;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public String getImplClassName() {
        return implClassName;
    }

    public String getReference() {
        return getReference("%s.%s");
    }

    public String getReference(String format) {
        return String.format(format, getImplClassName(), getMethodName());
    }


}