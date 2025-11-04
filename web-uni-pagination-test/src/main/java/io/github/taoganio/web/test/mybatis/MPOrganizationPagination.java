package io.github.taoganio.web.test.mybatis;

import io.github.taoganio.unipagination.annotation.PaginationKey;
import io.github.taoganio.unipagination.domain.Sort;
import io.github.taoganio.unipagination.mybatis.MybatisPaginationStatement;
import io.github.taoganio.unipagination.result.set.PaginationResultSetHandler;
import io.github.taoganio.unipagination.result.set.PaginationRowMapper;
import io.github.taoganio.unipagination.statement.PaginationStatement;
import io.github.taoganio.unipagination.web.component.PaginationComponent;
import io.github.taoganio.unipagination.web.mvc.PaginationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PaginationController
@PaginationKey("mp-organization")
public class MPOrganizationPagination implements PaginationComponent {

    private static final Logger log = LoggerFactory.getLogger(MPOrganizationPagination.class);

    @RequestMapping(value = "mp-organization", produces = "application/json")
    public PaginationStatement organization(@RequestParam int page, @RequestParam int size,
                                            @RequestParam(required = false) String name) {
        return MybatisPaginationStatement.of(OrganizationPaginationMapper.class,
                b -> b.reference(OrganizationPaginationMapper::getOrganizations, name)
                        .pageable(page - 1, size, Sort.by("name"))
        );
    }

    @Override
    public PaginationResultSetHandler<?> getDataResultSetHandler() {
        return null;
    }

    @Override
    public PaginationRowMapper<?> getDataRowMapper() {
        return (metadata, prs, rowNum) -> {
            OrganizationDTO dto = new OrganizationDTO();
            dto.setId(prs.getString("id"));
            dto.setContactInfo(prs.getString("contact_name") + " " + prs.getString("contact_email"));
            return dto;
        };
    }

}
