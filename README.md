# Java Wallet (Monolith)

This project is now converging to a **single Spring Boot wallet service**.

## What changed
- Single startup service (`wallet-webapi` as app runtime)
- No RabbitMQ required
- No xxl-job required
- HSM capabilities are invoked in-process
- tokencore dependency unified to `com.github.galaxyscitech:tokencore:2.0.0`

## Quick Start
### 1) Start with Docker Compose
```bash
docker compose up -d --build
```

Services:
- `app` on `http://localhost:8080`
- `mysql` on `localhost:3306`

### 2) Start manually
```bash
./gradlew :wallet-webapi:bootRun
```

## API
- Wallet API: `/wallet/v1`
- Blockchain API: `/block_chain/v1`
- Admin API: `/admin`
- Swagger: `/swagger-ui/index.html`

## Runtime config
Core runtime behavior is expected to be DB-config driven. Current scheduler keys already supported in `sys_config`:
- `SCHEDULER_DEPOSIT_SCAN_ENABLED`
- `SCHEDULER_DEPOSIT_SCAN_MS`
- `SCHEDULER_CHAIN_SYNC_ENABLED`
- `SCHEDULER_CHAIN_SYNC_MS`
- `SCHEDULER_SWEEP_ENABLED`
- `SCHEDULER_SWEEP_MS`
- `SCHEDULER_FEE_SUPPLY_ENABLED`
- `SCHEDULER_FEE_SUPPLY_MS`

## Security notes
- Do not use default passwords in production.
- Never expose keystore password, mnemonic, or private key in logs.
- Keep RPC credentials masked in management APIs.

## Database updates
- New audit table: `wallet_admin_audit_log` (admin config change history).
