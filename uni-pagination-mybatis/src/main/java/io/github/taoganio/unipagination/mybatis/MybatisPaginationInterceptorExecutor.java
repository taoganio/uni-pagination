package io.github.taoganio.unipagination.mybatis;

import io.github.taoganio.unipagination.domain.PageImpl;
import io.github.taoganio.unipagination.domain.PageInformation;
import io.github.taoganio.unipagination.domain.Sort;
import io.github.taoganio.unipagination.result.set.DefaultPaginationResultSet;
import io.github.taoganio.unipagination.result.set.DefaultPaginationResultSetMetadata;
import io.github.taoganio.unipagination.result.set.EmptyPaginationResultSet;
import io.github.taoganio.unipagination.result.set.PaginationResultSet;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.github.taoganio.unipagination.domain.Pageable;
import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.executor.PaginationStatementExecutor;
import org.apache.ibatis.session.SqlSession;

import java.util.StringJoiner;

/**
 * Mybatis 分页语句执行器
 */
public class MybatisPaginationInterceptorExecutor implements PaginationStatementExecutor<MybatisPaginationStatement> {

    private final SqlSession sqlSession;

    public MybatisPaginationInterceptorExecutor(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public boolean supports(PaginationStatement paginationStatement) {
        return paginationStatement instanceof MybatisPaginationStatement;
    }

    @Override
    public PageInformation executeForInformation(MybatisPaginationStatement statement) throws PaginationException {
        String mappedStatementId = statement.getNativeStatement();
        Object parameter = statement.getParameter();
        Page<?> page;
        try (Page<Object> close = PageMethod.startPage(0, 0)) {
            page = (Page<?>) sqlSession.selectList(mappedStatementId, parameter);
        }
        if (page == null) {
            throw new MybatisPaginationException("The result must be a Page.");
        }
        return new PageImpl<>(page.getTotal(), statement.getPageable());
    }

    @Override
    public PaginationResultSet executeForResultSet(MybatisPaginationStatement statement) throws PaginationException {
        Pageable pageable = statement.getPageable();
        int pageNumber = pageable.getPageNumber() + 1;
        String mappedStatementId = statement.getNativeStatement();
        Page<?> pageResult;
        try (ExecutorContext.Mark mark = ExecutorContext.pageMark();
             Page<Object> page = PageMethod.startPage(pageNumber, pageable.getPageSize())) {
            if (pageable.getSort() != null && pageable.getSort().isSorted()) {
                StringJoiner orderBy = new StringJoiner(",");
                for (Sort.Order order : pageable.getSort()) {
                    orderBy.add(String.format("%s %s", order.getProperty(), order.getDirection()));
                }
                page.setOrderBy(orderBy.toString());
            }
            pageResult = (Page<?>) sqlSession.selectList(mappedStatementId, statement.getParameter());
        }
        if (pageResult == null) {
            throw new MybatisPaginationException("The result must be a Page.");
        }
        if (pageResult.isEmpty()) {
            return new EmptyPaginationResultSet(pageable);
        }
        // ResultSetHandlerInterceptor
        Result result = (Result) pageResult.getResult().get(0);
        return new DefaultPaginationResultSet(pageResult.getTotal(),
                new DefaultPaginationResultSetMetadata(pageable, result.getColumns()), result.getRows());
    }
}
