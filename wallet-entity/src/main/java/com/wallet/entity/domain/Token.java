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
 * Token is a Querydsl bean type
 */
@Entity
@DynamicInsert
@DynamicUpdate
public class Token implements Serializable {

    @Column("chain_type")
    private String chainType;

    @Column("created_at")
    private java.util.Date createdAt;

    @Column("gas_limit")
    private Long gasLimit;

    @Column("id")
    @Id
    @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column("min_collect")
    private java.math.BigDecimal minCollect;

    @Column("token_address")
    private String tokenAddress;

    @Column("token_symbol")
    private String tokenSymbol;

    @Column("updated_at")
    private java.util.Date updatedAt;

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

    public Long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.math.BigDecimal getMinCollect() {
        return minCollect;
    }

    public void setMinCollect(java.math.BigDecimal minCollect) {
        this.minCollect = minCollect;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
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

