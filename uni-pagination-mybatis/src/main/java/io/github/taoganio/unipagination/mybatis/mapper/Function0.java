package io.github.taoganio.unipagination.mybatis.mapper;

import java.io.Serializable;

@FunctionalInterface
public interface Function0<T, R> extends Serializable {
    R apply(T t);
}