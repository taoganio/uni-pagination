package io.github.taoganio.unipagination.mongodb;

import com.mongodb.lang.Nullable;
import io.github.taoganio.unipagination.result.set.PaginationRow;
import org.bson.Document;
import org.bson.codecs.Encoder;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import java.util.List;

public interface MongoPaginationRow extends PaginationRow, Bson {

    @Nullable
    <T> T getEmbedded(List<?> keys, Class<T> clazz);

    @Nullable
    <T> T getEmbedded(List<?> keys, T defaultValue);

    @Nullable
    <T> List<T> getList(Object key, Class<T> clazz);

    @Nullable
    <T> List<T> getList(Object key, Class<T> clazz, List<T> defaultValue);

    String toJson();

    String toJson(JsonWriterSettings writerSettings);

    String toJson(Encoder<Document> encoder);

    String toJson(JsonWriterSettings writerSettings, Encoder<Document> encoder);

}