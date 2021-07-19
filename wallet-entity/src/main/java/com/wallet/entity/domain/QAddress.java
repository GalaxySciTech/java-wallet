package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QAddress extends com.querydsl.sql.RelationalPathBase<Address> {

    private static final long serialVersionUID = -2041119955;

    public static final QAddress address1 = new QAddress("address");

    public final StringPath address = createString("address");

    public final NumberPath<Integer> autoCollect = createNumber("autoCollect", Integer.class);

    public final StringPath chainType = createString("chainType");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final StringPath walletCode = createString("walletCode");

    public final com.querydsl.sql.PrimaryKey<Address> primary = createPrimaryKey(id);

    public QAddress(String variable) {
        super(Address.class, forVariable(variable), "null", "address");
        addMetadata();
    }

    public QAddress(String variable, String schema, String table) {
        super(Address.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QAddress(String variable, String schema) {
        super(Address.class, forVariable(variable), schema, "address");
        addMetadata();
    }

    public QAddress(Path<? extends Address> path) {
        super(path.getType(), path.getMetadata(), "null", "address");
        addMetadata();
    }

    public QAddress(PathMetadata metadata) {
        super(Address.class, metadata, "null", "address");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").withIndex(2).ofType(Types.VARCHAR).withSize(255));
        addMetadata(autoCollect, ColumnMetadata.named("auto_collect").withIndex(8).ofType(Types.INTEGER).withSize(10));
        addMetadata(chainType, ColumnMetadata.named("chain_type").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(6).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(memo, ColumnMetadata.named("memo").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(7).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(walletCode, ColumnMetadata.named("wallet_code").withIndex(5).ofType(Types.VARCHAR).withSize(255));
    }

}

