package io.github.taoganio.unipagination.mongodb;

import com.mongodb.CursorType;
import com.mongodb.client.model.Collation;
import jakarta.annotation.Nullable;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.conversions.Bson;

import java.util.concurrent.TimeUnit;

import static com.mongodb.assertions.Assertions.isTrueArgument;
import static com.mongodb.assertions.Assertions.notNull;

public class MongoFindOptions {
    private int batchSize;
    private long maxTimeMS;
    private long maxAwaitTimeMS;
    private CursorType cursorType = CursorType.NonTailable;
    private boolean noCursorTimeout;
    private boolean oplogReplay;
    private boolean partial;
    private Collation collation;
    private BsonValue comment;
    private Bson hint;
    private String hintString;
    private Bson variables;
    private Bson max;
    private Bson min;
    private boolean returnKey;
    private boolean showRecordId;
    private Boolean allowDiskUse;

    public MongoFindOptions() {
    }

    MongoFindOptions(final int batchSize, final long maxTimeMS, final long maxAwaitTimeMS,
                     final CursorType cursorType, final boolean noCursorTimeout, final boolean oplogReplay, final boolean partial,
                     final Collation collation, final BsonValue comment, final Bson hint, final String hintString, final Bson variables,
                     final Bson max, final Bson min, final boolean returnKey, final boolean showRecordId, final Boolean allowDiskUse) {
        this.batchSize = batchSize;
        this.maxTimeMS = maxTimeMS;
        this.maxAwaitTimeMS = maxAwaitTimeMS;
        this.cursorType = cursorType;
        this.noCursorTimeout = noCursorTimeout;
        this.oplogReplay = oplogReplay;
        this.partial = partial;
        this.collation = collation;
        this.comment = comment;
        this.hint = hint;
        this.hintString = hintString;
        this.variables = variables;
        this.max = max;
        this.min = min;
        this.returnKey = returnKey;
        this.showRecordId = showRecordId;
        this.allowDiskUse = allowDiskUse;
    }

    public static MongoFindOptions create() {
        return new MongoFindOptions();
    }

    public MongoFindOptions withBatchSize(final int batchSize) {
        return new MongoFindOptions(batchSize, maxTimeMS, maxAwaitTimeMS, cursorType, noCursorTimeout,
                oplogReplay, partial, collation, comment, hint, hintString, variables, max, min, returnKey, showRecordId, allowDiskUse);
    }

    public long getMaxTime(final TimeUnit timeUnit) {
        notNull("timeUnit", timeUnit);
        return timeUnit.convert(maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public MongoFindOptions maxTime(final long maxTime, final TimeUnit timeUnit) {
        notNull("timeUnit", timeUnit);
        isTrueArgument("maxTime > = 0", maxTime >= 0);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public long getMaxAwaitTime(final TimeUnit timeUnit) {
        notNull("timeUnit", timeUnit);
        return timeUnit.convert(maxAwaitTimeMS, TimeUnit.MILLISECONDS);
    }

    public MongoFindOptions maxAwaitTime(final long maxAwaitTime, final TimeUnit timeUnit) {
        notNull("timeUnit", timeUnit);
        isTrueArgument("maxAwaitTime > = 0", maxAwaitTime >= 0);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public MongoFindOptions batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public boolean isNoCursorTimeout() {
        return noCursorTimeout;
    }

    public MongoFindOptions noCursorTimeout(final boolean noCursorTimeout) {
        this.noCursorTimeout = noCursorTimeout;
        return this;
    }

    public boolean isOplogReplay() {
        return oplogReplay;
    }

    public MongoFindOptions oplogReplay(final boolean oplogReplay) {
        this.oplogReplay = oplogReplay;
        return this;
    }

    public boolean isPartial() {
        return partial;
    }

    public MongoFindOptions partial(final boolean partial) {
        this.partial = partial;
        return this;
    }

    public CursorType getCursorType() {
        return cursorType;
    }

    public MongoFindOptions cursorType(final CursorType cursorType) {
        this.cursorType = notNull("cursorType", cursorType);
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return collation;
    }

    public MongoFindOptions collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }

    @Nullable
    public BsonValue getComment() {
        return comment;
    }

    public MongoFindOptions comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }

    public MongoFindOptions comment(@Nullable final String comment) {
        this.comment = comment != null ? new BsonString(comment) : null;
        return this;
    }

    @Nullable
    public Bson getHint() {
        return hint;
    }

    public MongoFindOptions hint(@Nullable final Bson hint) {
        this.hint = hint;
        return this;
    }

    @Nullable
    public String getHintString() {
        return hintString;
    }

    public MongoFindOptions hintString(@Nullable final String hint) {
        this.hintString = hint;
        return this;
    }

    @Nullable
    public Bson getLet() {
        return variables;
    }

    public MongoFindOptions let(@Nullable final Bson variables) {
        this.variables = variables;
        return this;
    }

    @Nullable
    public Bson getMax() {
        return max;
    }

    public MongoFindOptions max(@Nullable final Bson max) {
        this.max = max;
        return this;
    }

    @Nullable
    public Bson getMin() {
        return min;
    }

    public MongoFindOptions min(@Nullable final Bson min) {
        this.min = min;
        return this;
    }

    public boolean isReturnKey() {
        return returnKey;
    }

    public MongoFindOptions returnKey(final boolean returnKey) {
        this.returnKey = returnKey;
        return this;
    }

    public boolean isShowRecordId() {
        return showRecordId;
    }

    public MongoFindOptions showRecordId(final boolean showRecordId) {
        this.showRecordId = showRecordId;
        return this;
    }

    public Boolean isAllowDiskUse() {
        return allowDiskUse;
    }

    public MongoFindOptions allowDiskUse(@Nullable final Boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }

    @Override
    public String toString() {
        return "MongoFindOptions{"
                + "batchSize=" + batchSize
                + ", maxTimeMS=" + maxTimeMS
                + ", maxAwaitTimeMS=" + maxAwaitTimeMS
                + ", cursorType=" + cursorType
                + ", noCursorTimeout=" + noCursorTimeout
                + ", oplogReplay=" + oplogReplay
                + ", partial=" + partial
                + ", collation=" + collation
                + ", comment='" + comment + "'"
                + ", hint=" + hint
                + ", let=" + variables
                + ", max=" + max
                + ", min=" + min
                + ", returnKey=" + returnKey
                + ", showRecordId=" + showRecordId
                + ", allowDiskUse=" + allowDiskUse
                + "}";
    }
}
