package io.github.taoganio.unipagination.web.mvc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 默认分页 URI 解析器。
 *
 * <p>URL 约定为 {@code {basePath}/{pageKey}/{action}}：</p>
 * <ul>
 *   <li>{@code basePath}：全局前缀，可为空；</li>
 *   <li>{@code pageKey}：分页语句处理器标识（即 {@code @RequestMapping} 的 value）；</li>
 *   <li>{@code action}：分页操作类型（info / data / exports 等）。</li>
 * </ul>
 *
 * <p>实现原则：</p>
 * <ol>
 *   <li>所有路径片段统一“去首尾空白与斜杠”规范化，再以单个 {@code /} 拼接，避免双斜杠；</li>
 * </ol>
 */
@Getter
@Setter
public class DefaultPaginationUriResolver implements PaginationUriResolver {

    private static final String DELIMITER = "/";

    private String basePath = "";

    /**
     * 构造分页 URL：{@code {basePath}/{pageKey}/{action}}。
     *
     * <p>pageKey、action 均可带或不带前导/尾随斜杠，内部统一规范化。</p>
     */
    @Override
    public String constructUri(String pageKey, String action) {
        StringBuilder uri = new StringBuilder();
        if (StringUtils.hasText(basePath)) {
            // basePath 在 setBasePath 中已规范化为「以 / 开头、无尾斜杠」
            uri.append(basePath);
        }
        appendSegment(uri, pageKey);
        appendSegment(uri, action);
        return uri.toString();
    }

    /**
     * 解析分页请求，得到 (pageKey, action)。
     */
    @Override
    @Nullable
    public PaginationActionRequest resolve(String[] definePaginationKeys, String uri) {
        if (uri == null || definePaginationKeys == null) {
            return null;
        }
        // 1) 剥离 basePath 前缀：仅当 uri 以 basePath 开头时才剥，且按长度精确截取
        if (StringUtils.hasText(basePath) && uri.startsWith(basePath)) {
            uri = uri.substring(basePath.length());
        }
        // 2) 逐个匹配定义的 pageKey（字面量前缀匹配）
        for (String defineKey : definePaginationKeys) {
            String prefix = DELIMITER + normalize(defineKey) + DELIMITER;
            if (uri.startsWith(prefix)) {
                // 3) 剩余部分即 action，去前导斜杠得到权威动作名
                String action = normalize(uri.substring(prefix.length()));
                return new PaginationActionRequest(defineKey, action);
            }
        }
        return null;
    }

    /**
     * 设置基础路径。统一规范化为「以 / 开头、无尾斜杠」；null 或空白等价于空路径。
     */
    public void setBasePath(String basePath) {
        String normalized = normalize(basePath);
        this.basePath = StringUtils.hasText(normalized) ? DELIMITER + normalized : "";
    }

    /**
     * 向 uri 追加一个路径段，自动补单个分隔符；空段忽略。
     */
    private static void appendSegment(StringBuilder uri, String segment) {
        String s = normalize(segment);
        if (!s.isEmpty()) {
            uri.append(DELIMITER).append(s);
        }
    }

    /**
     * 去首尾空白与首尾斜杠，返回权威形态；null 返回空串。
     */
    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String v = value.trim();
        while (v.startsWith(DELIMITER)) {
            v = v.substring(1);
        }
        while (v.endsWith(DELIMITER)) {
            v = v.substring(0, v.length() - 1);
        }
        return v;
    }
}
