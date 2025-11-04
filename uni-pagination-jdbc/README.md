# Uni Pagination JDBC

Uni Pagination JDBC 是一个基于 JDBC 的分页查询框架，它提供了对多种数据库方言的支持，并简化了分页查询的开发。该模块是 Uni-Pagination 框架的核心组件之一，主要负责 SQL 分页查询的执行和结果处理。

## 核心特性

- 多数据库方言支持：内置支持 MySQL、Oracle、PostgreSQL 等主流数据库
- 自动方言识别：根据数据源自动识别并选择合适的分页方言
- 命名参数支持：支持命名参数绑定，提高 SQL 可读性和安全性
- SQL 解析优化：使用 JSqlParser 进行 SQL 解析和优化
- 结果集处理：提供灵活的结果集处理机制

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.taoganio</groupId>
    <artifactId>uni-pagination-jdbc</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 创建分页查询

```java
// 创建分页查询
JdbcPaginationStatement statement = JdbcPaginationStatement.of(builder -> builder
    .sql("SELECT * FROM user WHERE username LIKE :username")
    .paramMap("username", "%test%")
    .pageable(0, 10));

// 执行分页查询
PaginationStatementExecutor<NamedParameterJdbcStatement> 
        executor = new Sql2oPaginationNamedParameterStatementExecutor(dataSource);
PaginationResultSet resultSet = executor.executeForResultSet(statement);
```

## 核心组件

### 1. 分页语句（JdbcPaginationStatement）

分页语句接口定义了分页查询的基本结构：

```java
public interface JdbcPaginationStatement extends PaginationStatement {
    String getNativeStatement();  // 获取原始SQL语句
    Object getParameter();        // 获取查询参数
}
```

### 2. 命名参数语句（NamedParameterJdbcStatement）

支持命名参数的 SQL 语句实现：

```sql
SELECT * FROM user WHERE username LIKE :username
```

### 3. SQL 方言（PaginationSqlDialect）

支持多种数据库的分页方言：

- MySQL 方言
- Oracle 方言
- PostgreSQL 方言

### 4. 注册方言

注册自定义方言：

```java
PaginationSqlAutomatedDialect.registerDialectAlias("custom", CustomDialect.class);
```