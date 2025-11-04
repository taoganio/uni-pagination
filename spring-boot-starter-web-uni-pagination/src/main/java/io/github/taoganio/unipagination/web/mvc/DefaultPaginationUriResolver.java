package io.github.taoganio.unipagination.web.mvc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class DefaultPaginationUriResolver implements PaginationUriResolver {

    private static final String delimiter = "/";
    private String basePath = "";

    @Override
    public String constructUri(String pageKey, String action) {
        if (!pageKey.startsWith(delimiter)) {
            pageKey = delimiter + pageKey;
        }
        if (!pageKey.endsWith(delimiter)) {
            pageKey += delimiter;
        }
        return basePath + pageKey + action;
    }

    @Override
    @Nullable
    public PaginationActionRequest resolve(String[] definePaginationKeys, String uri) {
        if (StringUtils.hasText(basePath)) {
            uri = uri.replaceFirst(basePath, "");
        }
        for (String defineKey : definePaginationKeys) {
            String tempKey = defineKey;
            if (!tempKey.startsWith(delimiter)) {
                tempKey = delimiter + tempKey;
            }
            if (!tempKey.endsWith(delimiter)) {
                tempKey = tempKey + delimiter;
            }
            if (uri.startsWith(tempKey)) {
                return new PaginationActionRequest(defineKey, uri.replaceFirst(tempKey, ""));
            }
        }
        return null;
    }

    public void setBasePath(String basePath) {
        if (basePath == null) {
            this.basePath = "";
            return;
        }
        if (!basePath.startsWith(delimiter)) {
            basePath = delimiter + basePath;
        }
        if (basePath.endsWith(delimiter)) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        this.basePath = basePath;
    }

}
