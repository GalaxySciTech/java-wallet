package com.wallet.entity.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet_withdraw_config")
public class WalletWithdrawConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String chain;
    private Boolean withdrawEnabled;
    private Boolean manualReviewEnabled;
    private String maxAutoWithdrawAmount;
    private String dailyWithdrawLimit;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChain() { return chain; }
    public void setChain(String chain) { this.chain = chain; }
    public Boolean getWithdrawEnabled() { return withdrawEnabled; }
    public void setWithdrawEnabled(Boolean withdrawEnabled) { this.withdrawEnabled = withdrawEnabled; }
    public Boolean getManualReviewEnabled() { return manualReviewEnabled; }
    public void setManualReviewEnabled(Boolean manualReviewEnabled) { this.manualReviewEnabled = manualReviewEnabled; }
    public String getMaxAutoWithdrawAmount() { return maxAutoWithdrawAmount; }
    public void setMaxAutoWithdrawAmount(String maxAutoWithdrawAmount) { this.maxAutoWithdrawAmount = maxAutoWithdrawAmount; }
    public String getDailyWithdrawLimit() { return dailyWithdrawLimit; }
    public void setDailyWithdrawLimit(String dailyWithdrawLimit) { this.dailyWithdrawLimit = dailyWithdrawLimit; }
}
