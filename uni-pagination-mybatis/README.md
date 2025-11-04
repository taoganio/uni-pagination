# Uni Pagination MyBatis

Uni Pagination MyBatis 是 Uni-Pagination 框架的 MyBatis 集成组件，用于支持 MyBatis Mapper 接口的分页查询。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.taoganio</groupId>
    <artifactId>uni-pagination-mybatis</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置 MyBatis 拦截器

> **重要提示**：使用 MyBatis 分页时，需要在 MyBatis 配置中添加以下拦截器，否则分页功能将无法正常工作：
> ```java
> configuration.addInterceptor(new ResultSetHandlerInterceptor());
> configuration.addInterceptor(new PageInterceptor());
> ```

### 3. 使用示例

```java
// 创建分页查询
MybatisPaginationStatement statement = MybatisPaginationStatement
        .of(UserMapper.class, builder -> builder
            .reference(UserMapper::findByUsername, "test")  // 引用Mapper接口中的方法
            .pageable(0, 10));

// 执行分页查询
PaginationStatementExecutor<MybatisPaginationStatement> 
        executor = new MybatisPaginationInterceptorExecutor(sqlSession);
PaginationResultSet resultSet = executor.executeForResultSet(statement);
```

## 核心功能

1. 支持通过 Mapper 接口方法进行分页查询
2. 支持 MyBatis 的参数解析机制
3. 提供统一的分页查询执行和结果处理
4. 拦截 ResultSet 并转换为统一的分页结果类型（PaginationResultSet）

## 使用说明

1. 使用 Mapper 方法引用：
```java
// reference方法用于引用Mapper接口中定义的方法
MybatisPaginationStatement.of(UserMapper.class, builder -> builder
    .reference(UserMapper::findByUsername, "test")  // 第一个参数是Mapper接口中的方法引用
    .pageable(0, 10));
```

2. 多参数查询：
```java
// 支持引用带多个参数的Mapper方法
MybatisPaginationStatement.of(UserMapper.class, builder -> builder
    .reference(UserMapper::findByUsernameAndAge, "test", 18)  // 可以传入多个参数
    .pageable(0, 10));
```

## 结果处理

框架会拦截 MyBatis 的 ResultSet，并将其转换为统一的分页结果类型（PaginationResultSet），而不是返回 Mapper 接口中定义的返回类型。