package io.github.taoganio.web.test.jdbc;

import io.github.taoganio.unipagination.domain.Sort;

import static io.github.taoganio.unipagination.jdbc.JdbcPaginationStatement.of;

import io.github.taoganio.unipagination.statement.PaginationStatement;
import io.github.taoganio.unipagination.web.component.ComponentStatement;
import io.github.taoganio.unipagination.web.mvc.PaginationController;
import io.github.taoganio.web.test.mybatis.OrganizationDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PaginationController
public class OrganizationPagination {

    @RequestMapping("organization")
    public PaginationStatement organization(@RequestParam int page, @RequestParam int size,
                                            @RequestParam(required = false) String name) {
        return ComponentStatement.of(b -> b
                .statement(
                        of(pb -> pb
                                .sql("select * from o_stock.organization where name like :name order by id desc")
                                .paramMap("name", "%" + name + "%")
                                .pageable(page - 1, size, Sort.by("id"))
                        )
                )
                .rowMapper((m, r, i) -> {
                    OrganizationDTO dto = new OrganizationDTO();
                    dto.setId(r.getString("id"));
                    dto.setContactInfo(r.getString("contact_name") + " " + r.getString("contact_email"));
                    return dto;
                })
        );
    }

}
