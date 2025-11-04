package io.github.taoganio.unipagination.mybatis.mapper;

import java.io.Serializable;

@FunctionalInterface
public interface Function4<T, P1, P2, P3, P4, R> extends Serializable {
    R apply(T t, P1 p1, P2 p2, P3 p3, P4 p4);
}
