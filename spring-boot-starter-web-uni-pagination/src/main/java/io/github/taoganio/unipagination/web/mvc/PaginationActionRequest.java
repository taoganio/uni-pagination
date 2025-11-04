package io.github.taoganio.unipagination.web.mvc;

import lombok.Data;

@Data
public class PaginationActionRequest {

    private final String paginationKey;
    private final String action;

    public boolean isValid() {
        return paginationKey != null && action != null;
    }

    @Override
    public String toString() {
        return "paginationKey=" + paginationKey + ", action=" + action;
    }
}
