package com.wallet.entity.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet_sweep_config")
public class WalletSweepConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String chain;
    private Boolean sweepEnabled;
    private String sweepToAddress;
    private String minSweepAmount;
    private String reserveAmount;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChain() { return chain; }
    public void setChain(String chain) { this.chain = chain; }
    public Boolean getSweepEnabled() { return sweepEnabled; }
    public void setSweepEnabled(Boolean sweepEnabled) { this.sweepEnabled = sweepEnabled; }
    public String getSweepToAddress() { return sweepToAddress; }
    public void setSweepToAddress(String sweepToAddress) { this.sweepToAddress = sweepToAddress; }
    public String getMinSweepAmount() { return minSweepAmount; }
    public void setMinSweepAmount(String minSweepAmount) { this.minSweepAmount = minSweepAmount; }
    public String getReserveAmount() { return reserveAmount; }
    public void setReserveAmount(String reserveAmount) { this.reserveAmount = reserveAmount; }
}
