package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QWhite is a Querydsl query type for White
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QWhite extends com.querydsl.sql.RelationalPathBase<White> {

    private static final long serialVersionUID = 1712170402;

    public static final QWhite white = new QWhite("white");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ip = createString("ip");

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final com.querydsl.sql.PrimaryKey<White> primary = createPrimaryKey(id);

    public QWhite(String variable) {
        super(White.class, forVariable(variable), "null", "white");
        addMetadata();
    }

    public QWhite(String variable, String schema, String table) {
        super(White.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QWhite(String variable, String schema) {
        super(White.class, forVariable(variable), schema, "white");
        addMetadata();
    }

    public QWhite(Path<? extends White> path) {
        super(path.getType(), path.getMetadata(), "null", "white");
        addMetadata();
    }

    public QWhite(PathMetadata metadata) {
        super(White.class, metadata, "null", "white");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(ip, ColumnMetadata.named("ip").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(4).ofType(Types.TIMESTAMP).withSize(19));
    }

}

