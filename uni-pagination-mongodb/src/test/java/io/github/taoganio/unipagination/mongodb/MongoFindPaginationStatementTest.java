package io.github.taoganio.unipagination.mongodb;

import com.mongodb.CursorType;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.Filters;
import io.github.taoganio.unipagination.domain.Page;
import io.github.taoganio.unipagination.domain.PageInformation;
import io.github.taoganio.unipagination.domain.Sort;
import io.github.taoganio.unipagination.executor.PaginationTemplate;
import io.github.taoganio.unipagination.util.PageSizes;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MongoDB 分页查询测试
 * <p>
 * 注意：此测试需要本地运行 MongoDB 实例，默认连接 mongodb://localhost:27017
 * 如果 MongoDB 未运行，测试将失败
 * <p>
 * 运行测试前请确保 MongoDB 已启动，或使用以下命令跳过测试：
 * mvn test -DskipTests
 */
@Disabled("需要 MongoDB 实例运行")
public class MongoFindPaginationStatementTest {

    private MongoClient mongoClient;
    private MongoDatabase testDatabase;
    private MongoCollection<Document> testCollection;
    private PaginationTemplate paginationTemplate;
    private MongoFindPaginationStatementExecutor executor;

    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        // 连接到本地 MongoDB（可根据实际情况修改连接字符串）
        // mongodb://[username:password@] host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database. collection][?options]]
        String username = URLEncoder.encode("admin", StandardCharsets.UTF_8.toString());
        String password = URLEncoder.encode("123456", StandardCharsets.UTF_8.toString());
        String connectionString = "mongodb://" + username + ":" + password + "@localhost:27017";
        mongoClient = MongoClients.create(connectionString);
        testDatabase = mongoClient.getDatabase("test_pagination");
        testCollection = testDatabase.getCollection("users");

        // 清理并插入测试数据
        testCollection.drop();
        insertTestData();

        // 创建执行器和模板
        executor = new MongoFindPaginationStatementExecutor(testDatabase);
        paginationTemplate = new PaginationTemplate();
        paginationTemplate.addStatementExecutor(executor);
    }

    @AfterEach
    void tearDown() {
        if (testCollection != null) {
            testCollection.drop();
        }
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * 插入测试数据
     */
    private void insertTestData() {
        List<Document> documents = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Document doc = new Document("userId", i)
                    .append("username", "user" + i)
                    .append("email", "user" + i + "@example.com")
                    .append("age", 20 + (i % 50))
                    .append("status", i % 2 == 0 ? "active" : "inactive");
            documents.add(doc);
        }
        testCollection.insertMany(documents);
    }

    @Test
    void testBasicPagination() {
        // 创建分页查询：第0页，每页10条
        MongoFindPaginationStatement statement = MongoFindPaginationStatement.builder()
                .collection("users")
                .filter(new Document("status", "active"))
                .pageable(0, 10)
                .build();

        // 查询分页信息
        PageInformation info = paginationTemplate.queryForInformation(statement);
        assertNotNull(info);
        assertEquals(50, info.getTotalElements()); // 50个active状态用户
        assertEquals(10, info.getPageable().getPageSize());

        // 查询结果集
        Page<Map<String, Object>> page = paginationTemplate.queryForMapResultSet(statement);
        assertNotNull(page);
        assertEquals(50, page.getTotalElements());
        assertEquals(10, page.getContent().size());
        assertEquals(0, page.getNumber());
    }

    @Test
    void testPaginationWithSort() {
        // 创建带排序的分页查询
        MongoFindPaginationStatement statement = MongoFindPaginationStatement.builder()
                .collection("users")
                .filter(new Document("status", "active"))
                .pageable(0, 10)
                .sort(Sort.by(Sort.Direction.DESC, "userId"))
                .build();

        Page<Map<String, Object>> page = paginationTemplate.queryForMapResultSet(statement);
        assertNotNull(page);
        assertEquals(10, page.getContent().size());

        // 验证排序：第一个结果应该是最大的userId
        Map<String, Object> first = page.getContent().get(0);
        assertTrue(((Integer) first.get("userId")) >= 90); // 按DESC排序，应该是较大的值
    }

    @Test
    void testPaginationWithFindOptions() {
        // 创建带 FindOptions 的分页查询
        MongoFindPaginationStatement statement = MongoFindPaginationStatement.builder()
                .collection("users")
                .projection(fields(include("userId", "username", "email")))
                .filter(new Document("status", "active"))
                .options(options -> options
                        .batchSize(20)
                        .maxTime(5, TimeUnit.SECONDS)
                )
                .pageable(0, 10)
                .build();

        Page<Map<String, Object>> page = paginationTemplate.queryForMapResultSet(statement);
        assertNotNull(page);
        assertEquals(10, page.getContent().size());

        // 验证投影：应该只包含指定的字段
        Map<String, Object> first = page.getContent().get(0);
        assertTrue(first.containsKey("userId"));
        assertTrue(first.containsKey("username"));
        assertTrue(first.containsKey("email"));
        // 应该不包含其他字段（或为null）
        assertFalse(first.containsKey("age") && first.get("age") != null);
    }

    @Test
    void testPaginationMultiplePages() {
        MongoFindPaginationStatement.Builder builder = MongoFindPaginationStatement.builder()
                .collection("users")
                .filter(new Document("status", "active"));

        PageInformation pageInformation = paginationTemplate.queryForInformation(builder.build());
        long totalElements = pageInformation.getTotalElements();
        assertEquals(50, totalElements);

        PageSizes pageSizes = PageSizes.from(totalElements, 10);
        for (int page = 0; page < pageSizes.getTotalPages(); page++) {
            int pageSize = pageSizes.getPageSize(page);
            builder.pageable(page, pageSize);
            Page<Map<String, Object>> pageResult = paginationTemplate.queryForMapResultSet(builder.build());
            assertNotNull(pageResult);
            assertEquals(50, pageResult.getTotalElements());
            assertEquals(pageSize, pageResult.getContent().size());
            assertEquals(page, pageResult.getNumber());
        }
    }

    @Test
    void testEmptyResult() {
        // 查询不存在的记录
        MongoFindPaginationStatement statement = MongoFindPaginationStatement.builder()
                .collection("users")
                .filter(new Document("status", "nonexistent"))
                .pageable(0, 10)
                .build();

        PageInformation info = paginationTemplate.queryForInformation(statement);
        assertEquals(0, info.getTotalElements());

        Page<Map<String, Object>> page = paginationTemplate.queryForMapResultSet(statement);
        assertEquals(0, page.getTotalElements());
        assertTrue(page.getContent().isEmpty());
    }

    @Test
    void testGetNativeStatement() {
        // 测试 getNativeStatement 返回完整的 runCommand
        MongoFindPaginationStatement statement = MongoFindPaginationStatement.builder()
                .collection("users")
                .filter(Filters.and(
                                Filters.eq("status", "active"),
                                Filters.gt("age", 20)
                        )
                )
                .projection(fields(include("userId")))
                .options(ops -> ops
                                .batchSize(50)
                                .maxTime(10, TimeUnit.SECONDS)
                                .comment("test query")
//                        .maxAwaitTime(100, TimeUnit.MILLISECONDS)
//                        .cursorType(CursorType.NonTailable)
//                        .noCursorTimeout(true)
//                        .oplogReplay(true)
//                        .partial(true)
//                        .collation(Collation.builder().locale("en").build())
//                        .hint(new Document("username", 1))
//                        .let(new Document("x", 1))
//                        .max(new Document("age", 30))
//                        .min(new Document("age", 20))
//                        .returnKey(true)
//                        .showRecordId(true)
//                        .allowDiskUse(true)
                )
                .pageable(1, 20, Sort.by("userId"))
                .build();

        System.out.println(statement);
        Document nativeStatement = statement.getNativeStatement();
        assertNotNull(nativeStatement);
        assertEquals("users", nativeStatement.getString("find"));
        assertNotNull(nativeStatement.get("filter"));
        assertNotNull(nativeStatement.get("projection"));
        assertEquals(20, nativeStatement.getInteger("limit"));
        assertEquals(20, nativeStatement.getInteger("skip")); // 第1页，skip=1*20
        assertNotNull(nativeStatement.get("sort"));
        assertEquals(50, nativeStatement.getInteger("batchSize"));
        assertNotNull(nativeStatement.get("comment"));
    }

    @Test
    void testCustomRowMapper() {
        // 测试自定义 RowMapper
        MongoFindPaginationStatement statement = MongoFindPaginationStatement.of(b -> b
                .collection("users")
                .projection(fields(include("userId", "username", "email")))
                .filter(new Document("status", "active"))
                .pageable(0, 10));

        Page<User> page = paginationTemplate.queryForResultSet(statement, (metadata, row, rowNum) -> {
            MongoPaginationRow mrow = (MongoPaginationRow) row;
            User user = new User();
            user.setUserId(mrow.getInteger("userId"));
            user.setUsername(mrow.getString("username"));
            user.setEmail(mrow.getString("email"));
            return user;
        });

        assertNotNull(page);
        assertEquals(10, page.getContent().size());
        assertNotNull(page.getContent().get(0));
    }

    /**
     * 测试用的用户实体类
     */
    static class User {
        private Integer userId;
        private String username;
        private String email;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

