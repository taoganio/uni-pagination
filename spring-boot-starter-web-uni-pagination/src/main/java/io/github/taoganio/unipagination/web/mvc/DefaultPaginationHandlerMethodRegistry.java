package io.github.taoganio.unipagination.web.mvc;

import io.github.taoganio.unipagination.statement.PaginationStatement;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultPaginationHandlerMethodRegistry
        implements PaginationHandlerMethodRegistry, ApplicationContextAware, InitializingBean {

    private final Map<String, HandlerMethod> registry = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(PaginationController.class);
        for (Object bean : beans.values()) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Map<Method, RequestMapping> methods = MethodIntrospector.selectMethods(
                    targetClass,
                    (MethodIntrospector.MetadataLookup<RequestMapping>) method ->
                            AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class)
            );
            for (Map.Entry<Method, RequestMapping> entry : methods.entrySet()) {
                Method method = entry.getKey();
                if (!PaginationStatement.class.isAssignableFrom(method.getReturnType())) {
                    throw new IllegalArgumentException("Method " + method.getName()
                            + " in " + targetClass.getName() + " must return PaginationStatement");
                }
                RequestMapping annotation = entry.getValue();
                HandlerMethod handlerMethod = new HandlerMethod(bean, method);
                for (String key : annotation.value()) {
                    register(key, handlerMethod);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.applicationContext = context;
    }

    @Override
    public Set<String> getAllPaginationKey() {
        return new HashSet<>(registry.keySet());
    }

    @Override
    public HandlerMethod getHandler(String paginationKey) {
        return registry.get(paginationKey);
    }

    public void register(String key, HandlerMethod handlerMethod) {
        if (registry.containsKey(key)) {
            HandlerMethod existing = registry.get(key);
            throw new IllegalStateException(String.format(
                    "PaginationStatement HandlerMethod Duplicate PaginationKey detected: key='%s' is already defined by %s#%s. " +
                            "Cannot be redefined by %s#%s.",
                    key,
                    existing.getBeanType().getName(),
                    existing.getMethod().getName(),
                    handlerMethod.getBeanType().getName(),
                    handlerMethod.getMethod().getName()
            ));
        }
        registry.put(key, handlerMethod);
    }
}
