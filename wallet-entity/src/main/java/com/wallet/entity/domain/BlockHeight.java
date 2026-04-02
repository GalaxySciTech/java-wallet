package com.wallet.entity.domain;

import jakarta.persistence.Entity;
import jakarta.annotation.Generated;
import jakarta.persistence.GeneratedValue;
import org.hibernate.annotations.DynamicUpdate;
import com.querydsl.sql.Column;
import org.hibernate.annotations.DynamicInsert;
import jakarta.persistence.Id;
import java.io.Serializable;

/**
 * BlockHeight is a Querydsl bean type
 */
@Entity
@DynamicInsert
@DynamicUpdate
public class BlockHeight implements Serializable {

    @Column("chain_type")
    private String chainType;

    @Column("created_at")
    private java.util.Date createdAt;

    @Column("height")
    private Long height;

    @Column("id")
    @Id
    @GeneratedValue(strategy=jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

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

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}

