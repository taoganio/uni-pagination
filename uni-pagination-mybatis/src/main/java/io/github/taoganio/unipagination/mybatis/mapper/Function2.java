package io.github.taoganio.unipagination.mybatis.mapper;

import java.io.Serializable;

@FunctionalInterface
public interface Function2<T, P1, P2, R> extends Serializable {
    R apply(T t, P1 p1, P2 p2);
}
