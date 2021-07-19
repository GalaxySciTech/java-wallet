package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QToken is a Querydsl query type for Token
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QToken extends com.querydsl.sql.RelationalPathBase<Token> {

    private static final long serialVersionUID = 1709609842;

    public static final QToken token = new QToken("token");

    public final StringPath chainType = createString("chainType");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final NumberPath<Long> gasLimit = createNumber("gasLimit", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> minCollect = createNumber("minCollect", java.math.BigDecimal.class);

    public final StringPath tokenAddress = createString("tokenAddress");

    public final StringPath tokenSymbol = createString("tokenSymbol");

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final com.querydsl.sql.PrimaryKey<Token> primary = createPrimaryKey(id);

    public QToken(String variable) {
        super(Token.class, forVariable(variable), "null", "token");
        addMetadata();
    }

    public QToken(String variable, String schema, String table) {
        super(Token.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QToken(String variable, String schema) {
        super(Token.class, forVariable(variable), schema, "token");
        addMetadata();
    }

    public QToken(Path<? extends Token> path) {
        super(path.getType(), path.getMetadata(), "null", "token");
        addMetadata();
    }

    public QToken(PathMetadata metadata) {
        super(Token.class, metadata, "null", "token");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(chainType, ColumnMetadata.named("chain_type").withIndex(2).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(5).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(gasLimit, ColumnMetadata.named("gas_limit").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(minCollect, ColumnMetadata.named("min_collect").withIndex(8).ofType(Types.DECIMAL).withSize(64).withDigits(18));
        addMetadata(tokenAddress, ColumnMetadata.named("token_address").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(tokenSymbol, ColumnMetadata.named("token_symbol").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(6).ofType(Types.TIMESTAMP).withSize(19));
    }

}

