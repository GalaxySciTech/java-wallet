package com.wallet.entity.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wallet_scheduler_config")
public class WalletSchedulerConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String chain;
    private Boolean depositScanEnabled;
    private Long depositScanIntervalMs;
    private Boolean sweepEnabled;
    private Long sweepIntervalMs;
    private Boolean feeSupplyEnabled;
    private Long feeSupplyIntervalMs;
    private Date updatedAt = new Date();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChain() { return chain; }
    public void setChain(String chain) { this.chain = chain; }
    public Boolean getDepositScanEnabled() { return depositScanEnabled; }
    public void setDepositScanEnabled(Boolean depositScanEnabled) { this.depositScanEnabled = depositScanEnabled; }
    public Long getDepositScanIntervalMs() { return depositScanIntervalMs; }
    public void setDepositScanIntervalMs(Long depositScanIntervalMs) { this.depositScanIntervalMs = depositScanIntervalMs; }
    public Boolean getSweepEnabled() { return sweepEnabled; }
    public void setSweepEnabled(Boolean sweepEnabled) { this.sweepEnabled = sweepEnabled; }
    public Long getSweepIntervalMs() { return sweepIntervalMs; }
    public void setSweepIntervalMs(Long sweepIntervalMs) { this.sweepIntervalMs = sweepIntervalMs; }
    public Boolean getFeeSupplyEnabled() { return feeSupplyEnabled; }
    public void setFeeSupplyEnabled(Boolean feeSupplyEnabled) { this.feeSupplyEnabled = feeSupplyEnabled; }
    public Long getFeeSupplyIntervalMs() { return feeSupplyIntervalMs; }
    public void setFeeSupplyIntervalMs(Long feeSupplyIntervalMs) { this.feeSupplyIntervalMs = feeSupplyIntervalMs; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
