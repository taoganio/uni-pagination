package io.github.taoganio.unipagination.jdbc.sqlmodifier;


/**
 * sql修改器
 */
public interface SqlModifier {

    /**
     * 修改
     *
     * @param sql sql
     * @return 修改后的sql
     */
    String modify(String sql);
}
