package io.github.taoganio.unipagination.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序项构建
 */
public class SortItemsBuilder implements SortBuilder {
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private final String splitRegex;
    private final String sortItems;

    /**
     * @param items [id desc,name asc] 排序规则
     */
    public SortItemsBuilder(String items) {
        this(items, ",");
    }

    /**
     * @param items id desc, name asc 等排序规则
     */
    public SortItemsBuilder(String items, String splitRegex) {
        this.sortItems = items;
        this.splitRegex = splitRegex;
    }

    @Override
    public Sort build() {
        if (sortItems == null || sortItems.isEmpty()) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (String item : sortItems.split(splitRegex)) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            String rules;
            String sortField;
            item = item.trim();
            int rulesIdx = item.toLowerCase().indexOf(ASC);
            if (rulesIdx < 0) {
                rulesIdx = item.toLowerCase().indexOf(DESC);
            }
            if (rulesIdx < 0) {
                rules = ASC;
                sortField = item;
            } else {
                sortField = item.substring(0, rulesIdx);
                rules = item.substring(rulesIdx);
            }
            Sort.Direction direction = Sort.Direction.fromOptionalString(rules).orElse(Sort.Direction.ASC);
            orders.add(new Sort.Order(direction, sortField));
        }
        return Sort.by(orders);
    }
}
