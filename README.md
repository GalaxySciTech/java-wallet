<h1 align="center">Java-Wallet</h1>

<p align="center">
  Multi-chain cryptocurrency wallet backend system
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue" alt="Java 17">
  <img src="https://img.shields.io/badge/Kotlin-1.9.10-purple" alt="Kotlin">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.1.3-green" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Gradle-8.5-blue" alt="Gradle">
</p>

## Introduction

Java-Wallet is a production-grade multi-chain cryptocurrency wallet backend system. It provides blockchain address management, deposit detection, withdrawal processing, and automated collection (sweeping) across multiple blockchain networks.

### Supported Chains

| Chain | Native | Tokens |
|-------|--------|--------|
| Bitcoin | BTC | OMNI (USDT) |
| Ethereum | ETH | ERC-20 |
| Tron | TRX | TRC-20 |
| Bitcoin Cash | BCH | - |
| Bitcoin SV | BSV | - |
| Litecoin | LTC | - |
| Dogecoin | DOGE | - |
| Dash | DASH | - |

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│ wallet-webapi│     │  wallet-task  │     │  wallet-hsm  │
│  (REST API)  │     │  (Scheduler)  │     │ (Key Mgmt)   │
│  Port: 10001 │     │  Port: 10033  │     │  Port: 10888  │
└──────┬───────┘     └──────┬───────┘     └──────┬───────┘
       │                    │                     │
       └────────────┬───────┘                     │
                    │                             │
            ┌───────▼───────┐            ┌───────▼───────┐
            │ wallet-common  │            │ wallet-entity  │
            │  (Services)    │────────────│   (JPA/Data)   │
            └───────┬───────┘            └───────┬───────┘
                    │                            │
         ┌──────────┼──────────┐                 │
         │          │          │           ┌─────▼─────┐
    ┌────▼──┐  ┌────▼──┐  ┌───▼───┐      │   MySQL    │
    │ RPC   │  │RabbitMQ│  │xxl-job│      └───────────┘
    │Nodes  │  │       │  │       │
    └───────┘  └───────┘  └───────┘
```

### Module Overview

| Module | Purpose |
|--------|---------|
| **wallet-webapi** | REST API for address generation, send/receive, admin dashboard |
| **wallet-task** | Scheduled tasks: block sync, deposit detection, collection, fee sending |
| **wallet-hsm** | Hardware Security Module service for key derivation, signing, import/export |
| **wallet-common** | Shared business logic, RPC clients, utilities, caching |
| **wallet-entity** | JPA entities, repositories, QueryDSL integration |

## Tech Stack

- **Language**: Kotlin 1.9.10 + Java 17
- **Framework**: Spring Boot 3.1.3
- **Build**: Gradle 8.5
- **Database**: MySQL 8.0 + Spring Data JPA + HikariCP
- **Cache**: Caffeine
- **Messaging**: RabbitMQ (Spring AMQP)
- **Scheduling**: xxl-job 2.4.0
- **Blockchain**: web3j 4.10.3, bitcoin-rpc-client 1.2.4, tokencore
- **API Docs**: SpringDoc OpenAPI (Swagger UI at `/swagger-ui.html`)
- **Container**: Docker with multi-stage builds

## Quick Start

### Prerequisites

- JDK 17+
- MySQL 8.0+
- RabbitMQ 3.x (for wallet-task)

### Using Docker Compose

```bash
# Clone the repository
git clone https://github.com/GalaxySciTech/java-wallet.git
cd java-wallet

# Copy and configure environment variables
cp .env.example .env
# Edit .env with your settings

# Start all services
docker-compose up -d
```

### Manual Setup

1. **Configure environment variables** (see `.env.example` for all options):

```bash
export DB_URL="jdbc:mysql://localhost:3306/wallet_db?useSSL=false&characterEncoding=UTF-8"
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

2. **Build the project**:

```bash
chmod +x gradlew
./gradlew build -x test
```

3. **Run services**:

```bash
# Start the API server
./gradlew :wallet-webapi:bootRun

# Start the task scheduler (separate terminal)
./gradlew :wallet-task:bootRun

# Start the HSM service (separate terminal)
./gradlew :wallet-hsm:bootRun
```

### API Documentation

Once the webapi service is running, access:
- **Swagger UI**: http://localhost:10001/swagger-ui.html
- **OpenAPI spec**: http://localhost:10001/v3/api-docs

## Configuration

All sensitive configuration is externalized via environment variables. See `.env.example` for a complete list.

| Variable | Service | Description |
|----------|---------|-------------|
| `DB_URL` | webapi, task | MySQL JDBC connection URL |
| `DB_USERNAME` | webapi, task | Database username |
| `DB_PASSWORD` | webapi, task | Database password |
| `RABBITMQ_HOST` | task | RabbitMQ server host |
| `KEYSTORE_DIR` | hsm | Keystore file directory |
| `KEYSTORE_PASSWORD` | hsm | Keystore encryption password |

## API Endpoints

### Wallet API (`/wallet/v1`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/get_address` | Generate new blockchain addresses |
| POST | `/send` | Send cryptocurrency (withdraw) |
| GET | `/get_hot_address` | Query hot wallet addresses |
| POST | `/create_hot_address` | Generate hot wallet address |
| GET | `/check_address` | Verify address private key exists |
| GET | `/get_transaction` | Get transaction records |
| POST | `/export_wallet` | Export private key / mnemonic |
| POST | `/import_wallet` | Import private key / mnemonic |

### Blockchain API (`/block_chain/v1`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/get_transaction` | Query on-chain transaction |
| GET | `/get_address_balance` | Query on-chain balance |
| GET | `/get_recommend_fee` | Get recommended fee levels |
| GET | `/get_support_token` | List supported tokens |

## Disclaimer

Any commercial activities using this source code are the sole responsibility of the user. The authors hold no liability for any losses.

## License

This project is provided as-is. See the repository for license details.
