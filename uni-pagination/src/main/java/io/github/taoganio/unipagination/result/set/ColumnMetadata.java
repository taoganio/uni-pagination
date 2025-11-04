package io.github.taoganio.unipagination.result.set;

/**
 * 列元数据
 */
public interface ColumnMetadata extends Comparable<ColumnMetadata> {

    /**
     * 获取列名
     *
     * @return 列名
     */
    String getColumnName();

    /**
     * 获取列类型
     *
     * @return 列类型
     */
    Class<?> getColumnType();

    /**
     * 获取排序
     *
     * @return 排序值
     */
    int order();

    /**
     * 比较
     *
     * @param o 比较对象
     * @return 比较值
     */
    @Override
    default int compareTo(ColumnMetadata o) {
        return Integer.compare(order(), o.order());
    }
}
