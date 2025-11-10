package io.github.taoganio.unipagination.web.mvc;

import org.springframework.lang.Nullable;

/**
 * 分页 URL 解析器
 */
public interface PaginationUriResolver {

    /**
     * 构造分页 URL
     */
    String constructUri(String pageKey, String action);

    /**
     * 解析分页请求信息
     *
     * @param definePaginationKeys 定义的分页键
     * @param uri                  URI
     * @return 分页请求信息
     */
    @Nullable
    PaginationActionRequest resolve(String[] definePaginationKeys, String uri);

}