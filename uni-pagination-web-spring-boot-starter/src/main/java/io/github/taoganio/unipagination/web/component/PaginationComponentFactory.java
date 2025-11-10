package io.github.taoganio.unipagination.web.component;

import org.springframework.lang.Nullable;

/**
 * 分页语句注册
 */
public interface PaginationComponentFactory {

    DefaultPaginationComponent DEFAULT_COMPONENT = new DefaultPaginationComponent();


    /**
     * 获取默认分页组件
     *
     * @return 分页组件
     */
    @Nullable
    default PaginationComponent getDefault() {
        return DEFAULT_COMPONENT;
    }

    /**
     * 获取分页组件
     *
     * @param paginationKey 分页键
     * @return 分页组件
     */
    @Nullable
    PaginationComponent getPaginationComponent(String paginationKey);

}
