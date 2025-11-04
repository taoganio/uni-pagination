package io.github.taoganio.unipagination.mybatis.mapper;

import java.io.Serializable;

@FunctionalInterface
public interface Function10<T, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> extends Serializable {
    R apply(T t, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10);
}