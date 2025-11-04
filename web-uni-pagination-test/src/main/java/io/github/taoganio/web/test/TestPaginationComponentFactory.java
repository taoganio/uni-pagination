package io.github.taoganio.web.test;

import cn.idev.excel.FastExcel;
import io.github.taoganio.unipagination.exception.PaginationException;
import io.github.taoganio.unipagination.result.set.PaginationResultSet;
import io.github.taoganio.unipagination.result.set.PaginationResultSetMetadata;
import io.github.taoganio.unipagination.result.set.PaginationRow;
import io.github.taoganio.unipagination.web.component.DefaultPaginationComponent;
import io.github.taoganio.unipagination.web.component.DefaultPaginationComponentFactory;
import io.github.taoganio.unipagination.web.component.PaginationComponent;
import io.github.taoganio.unipagination.web.component.PaginationExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TestPaginationComponentFactory extends DefaultPaginationComponentFactory {

    private static final Logger log = LoggerFactory.getLogger(TestPaginationComponentFactory.class);
    private final DefaultPaginationComponent defaultPaginationComponent = new DefaultPaginationComponent();

    public TestPaginationComponentFactory() {
        defaultPaginationComponent.setExporter(new DefaultExporter());
    }

    @Override
    public PaginationComponent getDefault() {
        return defaultPaginationComponent;
    }

    private static class DefaultExporter implements PaginationExporter {

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
}
