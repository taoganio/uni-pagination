package io.github.taoganio.unipagination.result.set;


/**
 * 分页结果集
 */
public interface PaginationResultSet extends Iterable<PaginationRow>, AutoCloseable {

    /**
     * 获取数据总数
     */
    long getTotal();

    /**
     * 返回当前结果集是否已关闭，如果已关闭，则可能无法再使用该结果集进行查询
     *
     * @return true 关闭，false 未关闭
     */
    boolean isClosed();

    /**
     * 得到元数据
     */
    PaginationResultSetMetadata getMetadata();

}
