package com.wallet.entity.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wallet_chain_config")
public class WalletChainConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String chain;
    private Boolean enabled;
    private Boolean depositScanEnabled;
    private Boolean withdrawEnabled;
    private Integer confirmations;
    private Long startBlock;
    private Long currentBlock;
    private Integer scanBatchSize;
    private Long scanIntervalMs;
    private Date updatedAt = new Date();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChain() { return chain; }
    public void setChain(String chain) { this.chain = chain; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public Boolean getDepositScanEnabled() { return depositScanEnabled; }
    public void setDepositScanEnabled(Boolean depositScanEnabled) { this.depositScanEnabled = depositScanEnabled; }
    public Boolean getWithdrawEnabled() { return withdrawEnabled; }
    public void setWithdrawEnabled(Boolean withdrawEnabled) { this.withdrawEnabled = withdrawEnabled; }
    public Integer getConfirmations() { return confirmations; }
    public void setConfirmations(Integer confirmations) { this.confirmations = confirmations; }
    public Long getStartBlock() { return startBlock; }
    public void setStartBlock(Long startBlock) { this.startBlock = startBlock; }
    public Long getCurrentBlock() { return currentBlock; }
    public void setCurrentBlock(Long currentBlock) { this.currentBlock = currentBlock; }
    public Integer getScanBatchSize() { return scanBatchSize; }
    public void setScanBatchSize(Integer scanBatchSize) { this.scanBatchSize = scanBatchSize; }
    public Long getScanIntervalMs() { return scanIntervalMs; }
    public void setScanIntervalMs(Long scanIntervalMs) { this.scanIntervalMs = scanIntervalMs; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
