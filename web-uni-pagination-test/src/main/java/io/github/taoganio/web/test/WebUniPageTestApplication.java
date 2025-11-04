package io.github.taoganio.web.test;

import com.github.pagehelper.PageInterceptor;
import io.github.taoganio.unipagination.jdbc.Sql2oPaginationNamedParameterStatementExecutor;
import io.github.taoganio.unipagination.mybatis.MybatisPaginationInterceptorExecutor;
import io.github.taoganio.unipagination.mybatis.ResultSetHandlerInterceptor;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class WebUniPageTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebUniPageTestApplication.class, args);
    }

    @Bean
    public Sql2oPaginationNamedParameterStatementExecutor sql2oPaginationStatementExecutor(DataSource dataSource) {
        return new Sql2oPaginationNamedParameterStatementExecutor(dataSource);
    }

    @Bean
    public MybatisPaginationInterceptorExecutor mybatisPaginationInterceptorExecutor(SqlSession sqlSession) {
        return new MybatisPaginationInterceptorExecutor(sqlSession);
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.addInterceptor(new ResultSetHandlerInterceptor());
            configuration.addInterceptor(new PageInterceptor());
        };
    }
}
