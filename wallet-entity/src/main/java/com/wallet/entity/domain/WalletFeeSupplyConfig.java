package com.wallet.entity.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet_fee_supply_config")
public class WalletFeeSupplyConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String chain;
    private Boolean feeSupplyEnabled;
    private String feeSupplyFromAddress;
    private String minGasBalance;
    private String targetGasBalance;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChain() { return chain; }
    public void setChain(String chain) { this.chain = chain; }
    public Boolean getFeeSupplyEnabled() { return feeSupplyEnabled; }
    public void setFeeSupplyEnabled(Boolean feeSupplyEnabled) { this.feeSupplyEnabled = feeSupplyEnabled; }
    public String getFeeSupplyFromAddress() { return feeSupplyFromAddress; }
    public void setFeeSupplyFromAddress(String feeSupplyFromAddress) { this.feeSupplyFromAddress = feeSupplyFromAddress; }
    public String getMinGasBalance() { return minGasBalance; }
    public void setMinGasBalance(String minGasBalance) { this.minGasBalance = minGasBalance; }
    public String getTargetGasBalance() { return targetGasBalance; }
    public void setTargetGasBalance(String targetGasBalance) { this.targetGasBalance = targetGasBalance; }
}
