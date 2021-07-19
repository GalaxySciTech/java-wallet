package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QWithdraw is a Querydsl query type for Withdraw
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QWithdraw extends com.querydsl.sql.RelationalPathBase<Withdraw> {

    private static final long serialVersionUID = 1428263665;

    public static final QWithdraw withdraw = new QWithdraw("withdraw");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final StringPath chainType = createString("chainType");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final StringPath fromAddress = createString("fromAddress");

    public final StringPath hash = createString("hash");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath toAddress = createString("toAddress");

    public final StringPath tokenSymbol = createString("tokenSymbol");

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final NumberPath<Integer> withdrawType = createNumber("withdrawType", Integer.class);

    public final com.querydsl.sql.PrimaryKey<Withdraw> primary = createPrimaryKey(id);

    public QWithdraw(String variable) {
        super(Withdraw.class, forVariable(variable), "null", "withdraw");
        addMetadata();
    }

    public QWithdraw(String variable, String schema, String table) {
        super(Withdraw.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QWithdraw(String variable, String schema) {
        super(Withdraw.class, forVariable(variable), schema, "withdraw");
        addMetadata();
    }

    public QWithdraw(Path<? extends Withdraw> path) {
        super(path.getType(), path.getMetadata(), "null", "withdraw");
        addMetadata();
    }

    public QWithdraw(PathMetadata metadata) {
        super(Withdraw.class, metadata, "null", "withdraw");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(3).ofType(Types.DECIMAL).withSize(64).withDigits(18));
        addMetadata(chainType, ColumnMetadata.named("chain_type").withIndex(6).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(9).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(fromAddress, ColumnMetadata.named("from_address").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(2).ofType(Types.VARCHAR).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(toAddress, ColumnMetadata.named("to_address").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(tokenSymbol, ColumnMetadata.named("token_symbol").withIndex(7).ofType(Types.VARCHAR).withSize(255));
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(10).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(withdrawType, ColumnMetadata.named("withdraw_type").withIndex(8).ofType(Types.INTEGER).withSize(10));
    }

}

