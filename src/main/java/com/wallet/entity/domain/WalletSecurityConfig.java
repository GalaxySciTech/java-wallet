package com.wallet.entity.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet_security_config")
public class WalletSecurityConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean allowExportPrivateKey;
    private Boolean exportPrivateKeyRequire2fa;
    private Boolean allowUpdateRpcByAdmin;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Boolean getAllowExportPrivateKey() { return allowExportPrivateKey; }
    public void setAllowExportPrivateKey(Boolean allowExportPrivateKey) { this.allowExportPrivateKey = allowExportPrivateKey; }
    public Boolean getExportPrivateKeyRequire2fa() { return exportPrivateKeyRequire2fa; }
    public void setExportPrivateKeyRequire2fa(Boolean exportPrivateKeyRequire2fa) { this.exportPrivateKeyRequire2fa = exportPrivateKeyRequire2fa; }
    public Boolean getAllowUpdateRpcByAdmin() { return allowUpdateRpcByAdmin; }
    public void setAllowUpdateRpcByAdmin(Boolean allowUpdateRpcByAdmin) { this.allowUpdateRpcByAdmin = allowUpdateRpcByAdmin; }
}
