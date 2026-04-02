<h1 align="center">Java-Wallet</h1>

<p align="center">
  <strong>Production-grade multi-chain cryptocurrency wallet backend</strong>
</p>

<p align="center">
  <a href="https://github.com/GalaxySciTech/java-wallet/actions">
    <img src="https://github.com/GalaxySciTech/java-wallet/actions/workflows/ci.yml/badge.svg" alt="Build Status">
  </a>
  <img src="https://img.shields.io/badge/Java-17-blue" alt="Java 17">
  <img src="https://img.shields.io/badge/Kotlin-1.9-purple" alt="Kotlin">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.1-green" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Gradle-8.5-02303A" alt="Gradle">
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
</p>

<p align="center">
  <a href="README_CN.md">中文文档</a> ·
  <a href="https://t.me/GalaxySciTech">Telegram</a> ·
  <a href="https://github.com/GalaxySciTech/java-wallet/issues">Issues</a> ·
  <a href="#quick-start">Quick Start</a>
</p>

---

## Introduction

Java-Wallet is a battle-tested multi-chain cryptocurrency wallet backend that has safely managed hundreds of millions of dollars in transactions in production. It provides blockchain address generation, deposit detection, withdrawal processing, and automated fund sweeping across all major public chains.

The core signing component [tokencore](https://github.com/GalaxySciTech/tokencore) performs all cryptographic operations locally — private keys never leave the process.

### Supported Chains

| Chain | Native | Token |
|:------|:------:|:-----:|
| Bitcoin | BTC | OMNI (USDT) |
| Ethereum | ETH | ERC-20 |
| Tron | TRX | TRC-20 |
| Bitcoin Cash | BCH | — |
| Bitcoin SV | BSV | — |
| Litecoin | LTC | — |
| Dogecoin | DOGE | — |
| Dash | DASH | — |

## Architecture

```
                         ┌──────────────────────────────────────────────┐
                         │               Client / Admin UI              │
                         └──────────────────┬───────────────────────────┘
                                            │  HTTP
                ┌───────────────────────────┼───────────────────────────┐
                │                           │                           │
        ┌───────▼────────┐         ┌────────▼────────┐         ┌───────▼────────┐
        │  wallet-webapi  │         │   wallet-task    │         │   wallet-hsm    │
        │   REST API      │         │  Scheduled Jobs  │         │  Key Management │
        │   :10001        │         │  :10033          │         │  :10888         │
        └───────┬─────────┘         └────────┬─────────┘         └───────┬─────────┘
                │                            │                           │
                └────────────┬───────────────┘                           │
                             │                                           │
                     ┌───────▼────────┐                          ┌───────▼────────┐
                     │  wallet-common  │                          │  wallet-entity  │
                     │  Business Logic │◄─────────────────────────│  JPA / Data     │
                     └──┬─────┬─────┬─┘                          └───────┬─────────┘
                        │     │     │                                     │
                   ┌────▼┐ ┌──▼──┐ ┌▼──────┐                     ┌───────▼────────┐
                   │ RPC │ │ MQ  │ │xxl-job│                     │    MySQL 8     │
                   │Nodes│ │     │ │       │                     └────────────────┘
                   └─────┘ └─────┘ └───────┘
```

| Module | Description |
|:-------|:------------|
| **wallet-webapi** | REST API — address generation, deposits & withdrawals, admin dashboard, Swagger docs |
| **wallet-task** | Scheduled jobs — block syncing, deposit detection, auto-sweeping, fee top-up |
| **wallet-hsm** | Key management — HD wallet derivation, offline signing, key import/export |
| **wallet-common** | Shared layer — business logic, RPC clients, caching, utilities |
| **wallet-entity** | Data layer — JPA entities, repositories, QueryDSL |

## Tech Stack

| Category | Technology |
|:---------|:-----------|
| Language | Kotlin 1.9 + Java 17 |
| Framework | Spring Boot 3.1.3 |
| Build | Gradle 8.5 |
| Database | MySQL 8.0 · Spring Data JPA · HikariCP |
| Cache | Caffeine |
| Message Queue | RabbitMQ (Spring AMQP) |
| Task Scheduling | xxl-job 2.4.0 |
| Blockchain | [tokencore 1.3.0](https://github.com/GalaxySciTech/tokencore) · web3j 4.10.3 · bitcoin-rpc-client 1.2.4 |
| API Docs | SpringDoc OpenAPI 2.3 (Swagger UI) |
| Container | Docker multi-stage · docker-compose |
| CI/CD | GitHub Actions · GitLab CI |

## Quick Start

### Prerequisites

- JDK 17+
- MySQL 8.0+
- RabbitMQ 3.x (required by wallet-task)

### Docker Compose (Recommended)

```bash
git clone https://github.com/GalaxySciTech/java-wallet.git
cd java-wallet

cp .env.example .env   # edit with your settings
docker-compose up -d
```

Once running:
- **API**: http://localhost:10001
- **Swagger UI**: http://localhost:10001/swagger-ui.html
- **RabbitMQ Console**: http://localhost:15672

### Manual Setup

```bash
# 1. Set environment variables (see .env.example for full list)
export DB_URL="jdbc:mysql://localhost:3306/wallet_db?useSSL=false&characterEncoding=UTF-8"
export DB_USERNAME=root
export DB_PASSWORD=your_password

# 2. Build
chmod +x gradlew
./gradlew build -x test

# 3. Run each service in a separate terminal
./gradlew :wallet-webapi:bootRun
./gradlew :wallet-task:bootRun
./gradlew :wallet-hsm:bootRun
```

## Configuration

All sensitive values are injected via environment variables — nothing is hardcoded. See [`.env.example`](.env.example) for the full list.

| Variable | Service | Description |
|:---------|:--------|:------------|
| `DB_URL` | webapi, task | MySQL JDBC connection URL |
| `DB_USERNAME` / `DB_PASSWORD` | webapi, task | Database credentials |
| `RABBITMQ_HOST` / `RABBITMQ_PASSWORD` | task | RabbitMQ connection |
| `KEYSTORE_DIR` / `KEYSTORE_PASSWORD` | hsm | Keystore path and password |
| `XXL_JOB_ADMIN_ADDRESSES` | task | xxl-job scheduler address |
| `WALLET_CRYPTO_PUSH_KEY` | webapi | AES key for deposit push notifications |

## API Reference

### Wallet API `/wallet/v1`

| Method | Endpoint | Description |
|:------:|:---------|:------------|
| POST | `/get_address` | Generate new blockchain addresses in batch |
| POST | `/send` | Withdraw (specify `from` or auto-select from hot wallet) |
| POST | `/create_hot_address` | Create a hot wallet or gas wallet |
| POST | `/export_wallet` | Export private key or mnemonic |
| POST | `/import_wallet` | Import private key or mnemonic |
| GET | `/get_hot_address` | Query hot wallet addresses |
| GET | `/check_address` | Check if address private key exists |
| GET | `/get_transaction` | Query deposit / withdrawal / sweep records |
| GET | `/get_new_deposit` | Fetch new deposits (marked as read after fetch) |
| GET | `/check_tx_status` | Check on-chain transaction status |

### Blockchain API `/block_chain/v1`

| Method | Endpoint | Description |
|:------:|:---------|:------------|
| GET | `/get_transaction` | Query on-chain transaction details |
| GET | `/get_address_balance` | Query on-chain address balance |
| GET | `/get_recommend_fee` | Get recommended fees (slow / medium / fast) |
| GET | `/calculation_fee` | Calculate transaction fee |
| GET | `/get_support_token` | List all supported tokens |

### Admin API `/admin`

| Method | Endpoint | Description |
|:------:|:---------|:------------|
| POST | `/login` | Admin login |
| GET | `/get_dashboard` | Dashboard overview |
| GET | `/get_addr_list` | Address list |
| GET | `/get_token_list` | Token list |
| POST | `/edit_token` | Edit token configuration |
| POST | `/edit_config` | Edit system configuration |
| POST | `/edit_white` | Edit IP whitelist |

> Full interactive API docs are available at Swagger UI after starting the service.

## Reliability

Java-Wallet has been running in production and has safely processed hundreds of millions of dollars in transactions. It is built for high reliability, scalability, and real-world utility.

## Contact

For technical support, business inquiries, or deployment consulting:

**[@GalaxySciTech](https://t.me/GalaxySciTech)** on Telegram

## Disclaimer

Any commercial use of this source code is at your own risk. The authors assume no liability for any losses incurred by yourself or others.

## License

This project is provided as-is. See the repository for license details.
