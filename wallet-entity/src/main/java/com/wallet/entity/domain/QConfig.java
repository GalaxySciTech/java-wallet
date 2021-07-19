package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QConfig is a Querydsl query type for Config
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QConfig extends com.querydsl.sql.RelationalPathBase<Config> {

    private static final long serialVersionUID = 971692265;

    public static final QConfig config = new QConfig("config");

    public final StringPath configGroup = createString("configGroup");

    public final StringPath configKey = createString("configKey");

    public final StringPath configValue = createString("configValue");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final com.querydsl.sql.PrimaryKey<Config> primary = createPrimaryKey(id);

    public QConfig(String variable) {
        super(Config.class, forVariable(variable), "null", "config");
        addMetadata();
    }

    public QConfig(String variable, String schema, String table) {
        super(Config.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QConfig(String variable, String schema) {
        super(Config.class, forVariable(variable), schema, "config");
        addMetadata();
    }

    public QConfig(Path<? extends Config> path) {
        super(path.getType(), path.getMetadata(), "null", "config");
        addMetadata();
    }

    public QConfig(PathMetadata metadata) {
        super(Config.class, metadata, "null", "config");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(configGroup, ColumnMetadata.named("config_group").withIndex(7).ofType(Types.VARCHAR).withSize(255));
        addMetadata(configKey, ColumnMetadata.named("config_key").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(configValue, ColumnMetadata.named("config_value").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(description, ColumnMetadata.named("description").withIndex(6).ofType(Types.VARCHAR).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
    }

}

