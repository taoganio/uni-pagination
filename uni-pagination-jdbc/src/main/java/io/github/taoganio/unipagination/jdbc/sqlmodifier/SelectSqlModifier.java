package io.github.taoganio.unipagination.jdbc.sqlmodifier;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

/**
 * 查询sql修改器
 */
public interface SelectSqlModifier extends SqlModifier {

    /**
     * 修改
     *
     * @param select 选择
     * @return 修改后的sql
     */
    String modify(Select select);

    /**
     * 修改
     *
     * @param sql sql
     * @return 修改后的sql
     */
    @Override
    default String modify(String sql) {
        //解析SQL
        Statement stmt;
        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        if (!(stmt instanceof Select)) {
            throw new IllegalArgumentException(this.getClass().getName() + " can only handle Select Statement");
        }
        return modify((Select) stmt);
    }
}
