package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QWaitCollect is a Querydsl query type for WaitCollect
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QWaitCollect extends com.querydsl.sql.RelationalPathBase<WaitCollect> {

    private static final long serialVersionUID = -718805842;

    public static final QWaitCollect waitCollect = new QWaitCollect("wait_collect");

    public final StringPath address = createString("address");

    public final StringPath chainType = createString("chainType");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> sendFee = createNumber("sendFee", Integer.class);

    public final NumberPath<Integer> sendFeeGasPrice = createNumber("sendFeeGasPrice", Integer.class);

    public final StringPath tokenSymbol = createString("tokenSymbol");

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final com.querydsl.sql.PrimaryKey<WaitCollect> primary = createPrimaryKey(id);

    public QWaitCollect(String variable) {
        super(WaitCollect.class, forVariable(variable), "null", "wait_collect");
        addMetadata();
    }

    public QWaitCollect(String variable, String schema, String table) {
        super(WaitCollect.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QWaitCollect(String variable, String schema) {
        super(WaitCollect.class, forVariable(variable), schema, "wait_collect");
        addMetadata();
    }

    public QWaitCollect(Path<? extends WaitCollect> path) {
        super(path.getType(), path.getMetadata(), "null", "wait_collect");
        addMetadata();
    }

    public QWaitCollect(PathMetadata metadata) {
        super(WaitCollect.class, metadata, "null", "wait_collect");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").withIndex(2).ofType(Types.VARCHAR).withSize(255));
        addMetadata(chainType, ColumnMetadata.named("chain_type").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(5).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(sendFee, ColumnMetadata.named("send_fee").withIndex(7).ofType(Types.INTEGER).withSize(10));
        addMetadata(sendFeeGasPrice, ColumnMetadata.named("send_fee_gas_price").withIndex(8).ofType(Types.INTEGER).withSize(10));
        addMetadata(tokenSymbol, ColumnMetadata.named("token_symbol").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(6).ofType(Types.TIMESTAMP).withSize(19));
    }

}

