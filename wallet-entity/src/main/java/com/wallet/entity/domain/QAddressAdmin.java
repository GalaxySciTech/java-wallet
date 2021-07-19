package com.wallet.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QAddressAdmin is a Querydsl query type for AddressAdmin
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QAddressAdmin extends com.querydsl.sql.RelationalPathBase<AddressAdmin> {

    private static final long serialVersionUID = -1604645854;

    public static final QAddressAdmin addressAdmin = new QAddressAdmin("address_admin");

    public final StringPath address = createString("address");

    public final NumberPath<Integer> addressType = createNumber("addressType", Integer.class);

    public final StringPath chainType = createString("chainType");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final StringPath walletCode = createString("walletCode");

    public final com.querydsl.sql.PrimaryKey<AddressAdmin> primary = createPrimaryKey(id);

    public QAddressAdmin(String variable) {
        super(AddressAdmin.class, forVariable(variable), "null", "address_admin");
        addMetadata();
    }

    public QAddressAdmin(String variable, String schema, String table) {
        super(AddressAdmin.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QAddressAdmin(String variable, String schema) {
        super(AddressAdmin.class, forVariable(variable), schema, "address_admin");
        addMetadata();
    }

    public QAddressAdmin(Path<? extends AddressAdmin> path) {
        super(path.getType(), path.getMetadata(), "null", "address_admin");
        addMetadata();
    }

    public QAddressAdmin(PathMetadata metadata) {
        super(AddressAdmin.class, metadata, "null", "address_admin");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").withIndex(2).ofType(Types.VARCHAR).withSize(255));
        addMetadata(addressType, ColumnMetadata.named("address_type").withIndex(3).ofType(Types.INTEGER).withSize(10));
        addMetadata(chainType, ColumnMetadata.named("chain_type").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(6).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(7).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(walletCode, ColumnMetadata.named("wallet_code").withIndex(5).ofType(Types.VARCHAR).withSize(255));
    }

}

