package io.github.taoganio.web.test.jdbc;

import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;

@RequestMapping
@RestController
public class HelloController {

    @RequestMapping(value = "/hello")
    public String hello(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return "Hello World!";
    }

    @RequestMapping(value = "/validate", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public UserRoleDTO hello(@RequestBody @Valid UserRoleDTO dto) {
        return dto;
    }

    @RequestMapping(value = "/collection", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public Collection<?> collection(@RequestBody @Valid UserRoleDTO dto) {
        return Collections.emptyList();
    }
}
