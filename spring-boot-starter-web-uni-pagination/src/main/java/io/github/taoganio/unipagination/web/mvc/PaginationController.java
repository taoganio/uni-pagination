package io.github.taoganio.unipagination.web.mvc;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseBody
@Component
public @interface PaginationController {
}
