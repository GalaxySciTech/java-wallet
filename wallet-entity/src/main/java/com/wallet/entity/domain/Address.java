package com.wallet.entity.domain;

import javax.persistence.Entity;
import javax.annotation.Generated;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.DynamicUpdate;
import com.querydsl.sql.Column;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Address is a Querydsl bean type
 */
@Entity
@DynamicInsert
@DynamicUpdate
public class Address implements Serializable {

    @Column("address")
    private String address;

    @Column("auto_collect")
    private Integer autoCollect;

    @Column("chain_type")
    private String chainType;

    @Column("created_at")
    private java.util.Date createdAt;

    @Column("id")
    @Id
    @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column("memo")
    private String memo;

    @Column("updated_at")
    private java.util.Date updatedAt;

    @Column("wallet_code")
    private String walletCode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAutoCollect() {
        return autoCollect;
    }

    public void setAutoCollect(Integer autoCollect) {
        this.autoCollect = autoCollect;
    }

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public void setWalletCode(String walletCode) {
        this.walletCode = walletCode;
    }

}

