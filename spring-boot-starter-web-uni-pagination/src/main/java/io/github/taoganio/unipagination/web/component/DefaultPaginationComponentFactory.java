package io.github.taoganio.unipagination.web.component;

import io.github.taoganio.unipagination.annotation.PaginationKey;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPaginationComponentFactory
        implements PaginationComponentFactory, ApplicationContextAware, InitializingBean {

    private ApplicationContext context;
    private final Map<String, PaginationComponent> registry = new ConcurrentHashMap<>();

    @Override
    public PaginationComponent getPaginationComponent(String paginationKey) {
        return registry.get(paginationKey);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> components = context.getBeansWithAnnotation(PaginationKey.class);
        for (Object component : components.values()) {
            if (component instanceof PaginationComponent) {
                PaginationComponent paginationComponent = (PaginationComponent) component;
                PaginationKey annotation = component.getClass().getAnnotation(PaginationKey.class);
                for (String v : annotation.value()) {
                    register(v, paginationComponent);
                }
            }
        }
    }

    public void register(String key, PaginationComponent component) {
        Assert.hasText(key, "Key must not be empty");
        Assert.notNull(component, "Component must not be null");
        PaginationComponent existing = registry.get(key);
        if (existing != null) {
            throw new IllegalStateException("PaginationComponent Duplicate PaginationKey detected: " +
                    "key='" + key + "' is already defined by " + existing.getClass().getName() + "." +
                    " Cannot be redefined by " + component.getClass().getName() + ".");
        }

        registry.put(key, component);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
