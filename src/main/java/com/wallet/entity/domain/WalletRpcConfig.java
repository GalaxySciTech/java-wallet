package com.wallet.entity.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wallet_rpc_config")
public class WalletRpcConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String chain;
    private String rpcUrl;
    private String rpcUsername;
    private String rpcPassword;
    private String rpcApiKey;
    private Integer rpcTimeoutMs;
    private Date updatedAt = new Date();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChain() { return chain; }
    public void setChain(String chain) { this.chain = chain; }
    public String getRpcUrl() { return rpcUrl; }
    public void setRpcUrl(String rpcUrl) { this.rpcUrl = rpcUrl; }
    public String getRpcUsername() { return rpcUsername; }
    public void setRpcUsername(String rpcUsername) { this.rpcUsername = rpcUsername; }
    public String getRpcPassword() { return rpcPassword; }
    public void setRpcPassword(String rpcPassword) { this.rpcPassword = rpcPassword; }
    public String getRpcApiKey() { return rpcApiKey; }
    public void setRpcApiKey(String rpcApiKey) { this.rpcApiKey = rpcApiKey; }
    public Integer getRpcTimeoutMs() { return rpcTimeoutMs; }
    public void setRpcTimeoutMs(Integer rpcTimeoutMs) { this.rpcTimeoutMs = rpcTimeoutMs; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
