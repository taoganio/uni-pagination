# Uni Pagination Web Spring Boot Starter

Spring Boot Starter Web Uni Pagination 是一个基于 Spring Boot 的分页查询框架，它通过声明式的方式简化了 Web
环境下的分页开发。开发者只需定义分页查询语句，框架就能自动处理分页请求的各个动作，无需编写重复的分页处理代码。

## 核心特性

- 声明式分页查询：通过 `@PaginationController` 注解定义分页控制器
- 自动路由：根据分页语句自动注册到 SpringMVC 中
- 统一响应处理：通过 `PaginationResponseAdvice` 自动处理分页响应
- 灵活的分页操作：支持 info、data、exports 三种标准操作
- 可扩展性：支持自定义分页组件和处理器

## 快速开始

### 1. 添加依赖

```xml

<dependency>
    <groupId>io.github.taoganio</groupId>
    <artifactId>uni-pagination-web-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置属性

```yaml
spring:
  unipagination:
    web:
      path: /unipage  # 分页请求的基础路径
```

### 3. 定义分页控制器

```java

@PaginationController
public class UserPaginationController {

    @RequestMapping("users")
    public PaginationStatement users(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username) {

        return of(b -> b
                .sql("SELECT * FROM user WHERE username LIKE :username")
                .paramMap("username", "%" + username + "%")
                .pageable(page, size));
    }
}
```

### 4. 访问分页接口

- 获取分页信息：`GET /unipage/users/info?page=0&size=10`
- 获取分页数据：`GET /unipage/users/data?page=0&size=10`
- 导出分页数据：`GET /unipage/users/exports?page=0&size=10`

## 核心组件

### 1. 分页控制器

`@PaginationController` 注解用于标识分页控制器类。控制器中的方法需要返回 `PaginationStatement` 类型，方法定义与普通
SpringMVC 接口方法相同。

### 2. 响应处理器

`PaginationResponseAdvice` 负责拦截 `PaginationStatement` 类型的返回值，根据请求的 action 类型调用对应的处理器。

### 3. 分页操作处理器

框架默认提供三种标准操作处理器：

- `PaginationInfoActionHandler`: 处理分页信息查询
- `PaginationDataActionHandler`: 处理分页数据获取
- `PaginationExportsActionHandler`: 处理数据导出

### 4. URL 解析器

`PaginationUriResolver` 负责处理分页请求的路由规则。默认实现 `DefaultPaginationUriResolver` 的 URL 构造规则为：

```
{unipage.web.path}/{paginationKey}/{action}
```

## 扩展机制

### 1. 自定义分页组件

添加 `@PaginationKey` 注解标记对应的自定义分页组件

```java

@Component
@PaginationKey("user")
public class UserPaginationComponent implements PaginationComponent {

    @Override
    public PaginationRowMapper<?> getDataRowMapper() {
        return (metadata, prs, rowNum) -> {
            OrganizationDTO dto = new OrganizationDTO();
            dto.setId(prs.getString("id"));
            dto.setContactInfo(prs.getString("contact_name") + " " + prs.getString("contact_email"));
            return dto;
        };
    }

    @Override
    public void exports(PaginationResultSet resultSet, HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=organizations.xlsx");

        PaginationResultSetMetadata metadata = resultSet.getMetadata();
        int columnCount = metadata.getColumnCount();
        List<List<String>> headers = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            headers.add(Collections.singletonList(metadata.getColumnMetadata(i).getColumnName()));
        }
        List<List<Object>> rows = new ArrayList<>();
        for (PaginationRow paginationRow : resultSet) {
            List<Object> row = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                row.add(paginationRow.getObject(i));
            }
            rows.add(row);
        }
        try {
            FastExcel.write(response.getOutputStream())
                    .head(headers)
                    .sheet()
                    .doWrite(rows);
        } catch (IOException e) {
            throw new PaginationException("Failed to export data to Excel", e);
        }
    }
}
```

### 2. 自定义分页操作处理器

1. 实现 `PaginationActionHandler` 接口
2. 在配置中添加自定义 action
3. 注册自定义处理器

## 最佳实践

1. 分页控制器方法必须返回 `PaginationStatement` 类型
2. 使用 `@PaginationController` 注解标记控制器
3. 分页 URL 遵循 `{basePath}/{paginationKey}/{action}` 格式
4. 合理使用分页组件进行数据转换和导出处理
5. 根据需要扩展自定义分页操作

## 注意事项

1. 默认分页语句处理器名称值开头不能包含 /
2. PaginationComponent 中如果同时定义了 getDataResultSetHandler 和 getDataRowMapper，getDataResultSetHandler 优先级更高。

## 配置属性说明

### UniPageProperties 配置项

| 参数项                              | 参数类型        | 是否必填 | 默认值             | 说明           |
|----------------------------------|-------------|------|-----------------|--------------|
| spring.unipagination.web.enabled | boolean     | 否    | true            | 启用动态注册MVC控制器 |
| spring.unipagination.web.path    | String      | 否    |                 | 分页请求的基础路径    |
| spring.unipagination.web.actions | Set<Action> | 否    | info, data, exports | 支持的分页操作配置    |

### Action 配置项

| 参数项           | 参数类型        | 是否必填 | 默认值 | 说明                         |
|---------------|-------------|------|-----|----------------------------|
| action        | String      | 是    | -   | 分页操作类型，如：info、data、exports |
| include-pages | Set<String> | 否    | -   | 包含的页面列表，为空时表示包含所有页面        |
| exclude-pages | Set<String> | 否    | -   | 排除的页面列表                    |

### 配置示例

```yaml
spring:
  unipagination:
    web:
      path: /unipage  # 自定义分页请求的基础路径
      actions:
        - action: info
          include-pages:
            - user
            - order
        - action: data
          exclude-pages:
            - sensitive
        - action: exports
          include-pages:
            - report
```