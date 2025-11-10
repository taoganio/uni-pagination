package io.github.taoganio.unipagination.mongodb;

import com.mongodb.client.MongoDatabase;

import java.util.function.Supplier;

public interface MongoDatabaseSupplier extends Supplier<MongoDatabase> {
}
