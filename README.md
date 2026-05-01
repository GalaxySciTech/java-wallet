# Java Wallet (Monolith)

Java Wallet is a **production-oriented Spring Boot wallet platform** for centralized exchanges, payment systems, and custodial services.  
It provides unified wallet management, blockchain synchronization, deposit detection, sweep automation, and admin tooling in a single deployable service.

---

## Why this project

This repository is converging to a **single-service architecture** to make deployment and operations simpler:

- One startup service (`wallet-webapi` runtime)
- No RabbitMQ dependency
- No xxl-job dependency
- HSM capabilities invoked in-process
- Unified tokencore dependency: `com.github.galaxyscitech:tokencore:2.0.0`

This means fewer moving parts, easier troubleshooting, and faster onboarding for developers and DevOps teams.

---

## Product capabilities

- **Wallet lifecycle management**: create, query, and manage wallet addresses
- **Deposit scanning**: continuously detect inbound transfers
- **Chain synchronization**: sync on-chain data into internal services
- **Sweep automation**: consolidate balances based on strategy
- **Fee supply scheduling**: ensure fee accounts are topped up when needed
- **Admin operations**: operational controls and configuration through admin APIs

---

## Typical use cases

### 1) Centralized Exchange (CEX)
Use Java Wallet as the deposit/withdrawal backend for multi-chain assets:
- Generate deposit addresses per user
- Detect incoming deposits and credit user balances
- Sweep funds from hot collection addresses to treasury/cold paths

### 2) Payment or Merchant Platform
Integrate chain deposits as payment rails:
- Assign invoice addresses
- Confirm on-chain settlement
- Trigger internal payment workflows after confirmations

### 3) Custodial Asset Service
Operate managed wallets for institutional users:
- Centralize key and wallet operations
- Track chain states
- Keep operational history with admin audit logs

---

## Quick start

### Option A: Run with Docker Compose (recommended)

```bash
docker compose up -d --build
```

Services:
- `app`: `http://localhost:8080`
- `mysql`: `localhost:3306`

### Option B: Run locally with Gradle

```bash
./gradlew :wallet-webapi:bootRun
```

---

## First-time user path (10-minute onboarding)

1. **Start services** using Docker Compose.
2. **Open Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
3. **Check health/basic endpoints** from Swagger.
4. **Create or query wallet resources** via Wallet API.
5. **Observe scheduler-driven behaviors** (deposit scan, sync, sweep) via logs and DB state.

If you are new to this project, using Swagger first is the fastest way to understand request/response models.

---

## API entry points

- Wallet API: `/wallet/v1`
- Blockchain API: `/block_chain/v1`
- Admin API: `/admin`
- Swagger UI: `/swagger-ui/index.html`

---

## Runtime configuration (DB-driven)

Runtime scheduler behavior is controlled through `sys_config` keys:

- `SCHEDULER_DEPOSIT_SCAN_ENABLED`
- `SCHEDULER_DEPOSIT_SCAN_MS`
- `SCHEDULER_CHAIN_SYNC_ENABLED`
- `SCHEDULER_CHAIN_SYNC_MS`
- `SCHEDULER_SWEEP_ENABLED`
- `SCHEDULER_SWEEP_MS`
- `SCHEDULER_FEE_SUPPLY_ENABLED`
- `SCHEDULER_FEE_SUPPLY_MS`

> Suggestion: keep intervals conservative in production first, then tune based on chain throughput and DB load.

---

## Security recommendations

- Never use default passwords in production.
- Never expose keystore passwords, mnemonic phrases, or private keys in logs.
- Mask RPC credentials in admin/management outputs.
- Restrict admin endpoints with network controls and strong authentication.

---

## Database notes

- Audit table: `wallet_admin_audit_log` (admin configuration change history)

---

## README optimization ideas (for next iteration)

To make this project even easier for users, consider adding:

1. **Architecture diagram** (single-service + MySQL + chain nodes)
2. **End-to-end demo flow** (create address → deposit → detect → sweep)
3. **API cookbook** with copy-paste `curl` examples
4. **Environment matrix** (`dev` / `staging` / `prod`) with recommended defaults
5. **Troubleshooting guide** (common startup, DB, RPC, and sync issues)
6. **FAQ** for operations and security best practices

These additions reduce onboarding time and lower support burden.
