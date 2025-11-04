package io.github.taoganio.unipagination.domain;

import io.github.taoganio.unipagination.util.StringUtils;
import io.github.taoganio.unipagination.util.Assert;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sort使用的是 {@link org.springframework.data.domain.Sort}
 */
public class Sort implements Iterable<Sort.Order> {
    private static final Sort UNSORTED = Sort.by(new Order[0]);
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;
    private final List<Order> orders;

    protected Sort(List<Order> orders) {
        this.orders = orders;
    }

    private Sort(Direction direction, List<String> properties) {
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by");
        }
        this.orders = properties.stream() //
                .map(it -> new Order(direction, it)) //
                .collect(Collectors.toList());
    }


    public static Sort by(String... properties) {
        Assert.notNull(properties, "Properties must not be null");
        return properties.length == 0 //
                ? Sort.unsorted() //
                : new Sort(DEFAULT_DIRECTION, Arrays.asList(properties));
    }

    public static Sort by(List<Order> orders) {
        Assert.notNull(orders, "Orders must not be null");
        return orders.isEmpty() ? Sort.unsorted() : new Sort(orders);
    }

    public static Sort by(Order... orders) {
        Assert.notNull(orders, "Orders must not be null");
        return new Sort(Arrays.asList(orders));
    }

    public static Sort by(Direction direction, String... properties) {
        Assert.notNull(direction, "Direction must not be null");
        Assert.notNull(properties, "Properties must not be null");
        Assert.isTrue(properties.length > 0, "At least one property must be given");
        return Sort.by(Arrays.stream(properties)//
                .map(it -> new Order(direction, it))//
                .collect(Collectors.toList()));
    }

    public static Sort unsorted() {
        return UNSORTED;
    }

    public Sort descending() {
        return withDirection(Direction.DESC);
    }

    public Sort ascending() {
        return withDirection(Direction.ASC);
    }

    public boolean isSorted() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public boolean isUnsorted() {
        return !isSorted();
    }

    @Override
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Sort)) {
            return false;
        }
        Sort that = (Sort) obj;
        return new ArrayList<>(orders).equals(new ArrayList<>(that.orders));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + orders.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return isEmpty() ? "UNSORTED" : orders.stream().map(Order::toString).collect(Collectors.joining(","));
    }

    private Sort withDirection(Direction direction) {
        return Sort.by(orders.stream().map(it -> it.with(direction)).collect(Collectors.toList()));
    }

    public enum Direction {

        ASC, DESC;

        public boolean isAscending() {
            return this.equals(ASC);
        }

        public boolean isDescending() {
            return this.equals(DESC);
        }

        public static Direction fromString(String value) {

            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value '%s' for orders given; Has to be either 'desc' or 'asc' (case insensitive)", value), e);
            }
        }

        public static Optional<Direction> fromOptionalString(String value) {

            try {
                return Optional.of(fromString(value));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
    }

    public static class Order implements Serializable {

        private static final long serialVersionUID = 1522511010900108987L;
        private static final boolean DEFAULT_IGNORE_CASE = false;
        private final Direction direction;
        private final String property;
        private final boolean ignoreCase;

        public Order(@Nullable Direction direction, String property) {
            this(direction, property, DEFAULT_IGNORE_CASE);
        }

        public static Order by(String property) {
            return new Order(DEFAULT_DIRECTION, property);
        }

        public static Order asc(String property) {
            return new Order(Direction.ASC, property);
        }

        public static Order desc(String property) {
            return new Order(Direction.DESC, property);
        }

        private Order(@Nullable Direction direction, String property, boolean ignoreCase) {

            if (StringUtils.isEmpty(property)) {
                throw new IllegalArgumentException("Property must not be null or empty");
            }
            this.direction = direction == null ? DEFAULT_DIRECTION : direction;
            this.property = property;
            this.ignoreCase = ignoreCase;
        }

        public Direction getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }

        public boolean isAscending() {
            return this.direction.isAscending();
        }

        public boolean isDescending() {
            return this.direction.isDescending();
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public Order with(Direction direction) {
            return new Order(direction, this.property, this.ignoreCase);
        }

        public Order withProperty(String property) {
            return new Order(this.direction, property, this.ignoreCase);
        }

        public Sort withProperties(String... properties) {
            return Sort.by(this.direction, properties);
        }

        public Order ignoreCase() {
            return new Order(direction, property, true);
        }

        @Override
        public int hashCode() {

            int result = 17;

            result = 31 * result + direction.hashCode();
            result = 31 * result + property.hashCode();
            result = 31 * result + (ignoreCase ? 1 : 0);

            return result;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Order)) {
                return false;
            }

            Order that = (Order) obj;

            return this.direction.equals(that.direction) && this.property.equals(that.property)
                    && this.ignoreCase == that.ignoreCase;
        }

        @Override
        public String toString() {
            String result = String.format("%s: %s", property, direction);
            if (ignoreCase) {
                result += ", ignoring case";
            }
            return result;
        }
    }

}