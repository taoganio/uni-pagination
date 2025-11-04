package io.github.taoganio.unipagination.jdbc.sqlmodifier;

import io.github.taoganio.unipagination.util.CollectionUtils;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

import java.util.List;
import java.util.stream.Collectors;

public class SelectItemModifier implements SelectSqlModifier {

    private final List<SelectColumn> selectColumns;

    public SelectItemModifier(List<SelectColumn> selectColumn) {
        this.selectColumns = selectColumn;
    }

    @Override
    public String modify(Select select) {
        SelectBody selectBody = select.getSelectBody();
        if (!CollectionUtils.isEmpty(selectColumns) && (selectBody instanceof PlainSelect)) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            List<SelectItem> selectItems = selectColumns.stream()
                    .filter(e -> !e.isIgnore())
                    .map(SelectColumn::getSelect)
                    .distinct()
                    .map(c -> new SelectExpressionItem(new Column(c)))
                    .collect(Collectors.toList());
            plainSelect.setSelectItems(selectItems);
        }
        return selectBody.toString();
    }
}
