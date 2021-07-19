package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QDeposit is a Querydsl query type for Deposit
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QDeposit extends com.querydsl.sql.RelationalPathBase<Deposit> {

    private static final long serialVersionUID = 661026263;

    public static final QDeposit deposit = new QDeposit("deposit");

    public final StringPath address = createString("address");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final StringPath chainType = createString("chainType");

    public final NumberPath<Long> confirmations = createNumber("confirmations", Long.class);

    public final DateTimePath<java.util.Date> confirmTime = createDateTime("confirmTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final StringPath hash = createString("hash");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isUpload = createNumber("isUpload", Integer.class);

    public final StringPath tokenSymbol = createString("tokenSymbol");

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final com.querydsl.sql.PrimaryKey<Deposit> primary = createPrimaryKey(id);

    public QDeposit(String variable) {
        super(Deposit.class, forVariable(variable), "null", "deposit");
        addMetadata();
    }

    public QDeposit(String variable, String schema, String table) {
        super(Deposit.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QDeposit(String variable, String schema) {
        super(Deposit.class, forVariable(variable), schema, "deposit");
        addMetadata();
    }

    public QDeposit(Path<? extends Deposit> path) {
        super(path.getType(), path.getMetadata(), "null", "deposit");
        addMetadata();
    }

    public QDeposit(PathMetadata metadata) {
        super(Deposit.class, metadata, "null", "deposit");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(5).ofType(Types.DECIMAL).withSize(64).withDigits(18));
        addMetadata(chainType, ColumnMetadata.named("chain_type").withIndex(3).ofType(Types.VARCHAR).withSize(32));
        addMetadata(confirmations, ColumnMetadata.named("confirmations").withIndex(6).ofType(Types.BIGINT).withSize(19));
        addMetadata(confirmTime, ColumnMetadata.named("confirm_time").withIndex(9).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(10).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(2).ofType(Types.VARCHAR).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(isUpload, ColumnMetadata.named("is_upload").withIndex(7).ofType(Types.INTEGER).withSize(10));
        addMetadata(tokenSymbol, ColumnMetadata.named("token_symbol").withIndex(8).ofType(Types.VARCHAR).withSize(255));
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(11).ofType(Types.TIMESTAMP).withSize(19));
    }

}

