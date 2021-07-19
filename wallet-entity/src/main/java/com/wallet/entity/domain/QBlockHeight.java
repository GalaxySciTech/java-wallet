package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QBlockHeight is a Querydsl query type for BlockHeight
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QBlockHeight extends com.querydsl.sql.RelationalPathBase<BlockHeight> {

    private static final long serialVersionUID = -1304690995;

    public static final QBlockHeight blockHeight = new QBlockHeight("block_height");

    public final StringPath chainType = createString("chainType");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final NumberPath<Long> height = createNumber("height", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final com.querydsl.sql.PrimaryKey<BlockHeight> primary = createPrimaryKey(id);

    public QBlockHeight(String variable) {
        super(BlockHeight.class, forVariable(variable), "null", "block_height");
        addMetadata();
    }

    public QBlockHeight(String variable, String schema, String table) {
        super(BlockHeight.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QBlockHeight(String variable, String schema) {
        super(BlockHeight.class, forVariable(variable), schema, "block_height");
        addMetadata();
    }

    public QBlockHeight(Path<? extends BlockHeight> path) {
        super(path.getType(), path.getMetadata(), "null", "block_height");
        addMetadata();
    }

    public QBlockHeight(PathMetadata metadata) {
        super(BlockHeight.class, metadata, "null", "block_height");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(chainType, ColumnMetadata.named("chain_type").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(height, ColumnMetadata.named("height").withIndex(5).ofType(Types.BIGINT).withSize(19));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
    }

}

