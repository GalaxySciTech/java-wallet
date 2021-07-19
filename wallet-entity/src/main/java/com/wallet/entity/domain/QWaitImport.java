package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QWaitImport is a Querydsl query type for WaitImport
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QWaitImport extends com.querydsl.sql.RelationalPathBase<WaitImport> {

    private static final long serialVersionUID = -961515615;

    public static final QWaitImport waitImport = new QWaitImport("wait_import");

    public final StringPath address = createString("address");

    public final StringPath chainType = createString("chainType");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final com.querydsl.sql.PrimaryKey<WaitImport> primary = createPrimaryKey(id);

    public QWaitImport(String variable) {
        super(WaitImport.class, forVariable(variable), "null", "wait_import");
        addMetadata();
    }

    public QWaitImport(String variable, String schema, String table) {
        super(WaitImport.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QWaitImport(String variable, String schema) {
        super(WaitImport.class, forVariable(variable), schema, "wait_import");
        addMetadata();
    }

    public QWaitImport(Path<? extends WaitImport> path) {
        super(path.getType(), path.getMetadata(), "null", "wait_import");
        addMetadata();
    }

    public QWaitImport(PathMetadata metadata) {
        super(WaitImport.class, metadata, "null", "wait_import");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").withIndex(2).ofType(Types.VARCHAR).withSize(255));
        addMetadata(chainType, ColumnMetadata.named("chain_type").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(4).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(5).ofType(Types.TIMESTAMP).withSize(19));
    }

}

