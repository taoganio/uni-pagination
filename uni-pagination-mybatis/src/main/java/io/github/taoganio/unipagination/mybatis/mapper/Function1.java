package io.github.taoganio.unipagination.mybatis.mapper;

import java.io.Serializable;

@FunctionalInterface
public interface Function1<T, P, R> extends Serializable {
    R apply(T t, P p);
}