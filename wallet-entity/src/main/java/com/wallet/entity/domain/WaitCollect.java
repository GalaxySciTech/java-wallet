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
 * WaitCollect is a Querydsl bean type
 */
@Entity
@DynamicInsert
@DynamicUpdate
public class WaitCollect implements Serializable {

    @Column("address")
    private String address;

    @Column("chain_type")
    private String chainType;

    @Column("created_at")
    private java.util.Date createdAt;

    @Column("id")
    @Id
    @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column("send_fee")
    private Integer sendFee;

    @Column("send_fee_gas_price")
    private Integer sendFeeGasPrice;

    @Column("token_symbol")
    private String tokenSymbol;

    @Column("updated_at")
    private java.util.Date updatedAt;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getSendFee() {
        return sendFee;
    }

    public void setSendFee(Integer sendFee) {
        this.sendFee = sendFee;
    }

    public Integer getSendFeeGasPrice() {
        return sendFeeGasPrice;
    }

    public void setSendFeeGasPrice(Integer sendFeeGasPrice) {
        this.sendFeeGasPrice = sendFeeGasPrice;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}

