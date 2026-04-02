<h1 align="center">Java-Wallet</h1>

<p align="center">
  <strong>生产级多链加密货币钱包后台系统</strong>
</p>

<p align="center">
  <a href="https://github.com/GalaxySciTech/java-wallet/actions">
    <img src="https://github.com/GalaxySciTech/java-wallet/actions/workflows/ci.yml/badge.svg" alt="构建状态">
  </a>
  <img src="https://img.shields.io/badge/Java-17-blue" alt="Java 17">
  <img src="https://img.shields.io/badge/Kotlin-1.9-purple" alt="Kotlin">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.1-green" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Gradle-8.5-02303A" alt="Gradle">
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
</p>

<p align="center">
  <a href="README.md">English</a> ·
  <a href="https://t.me/GalaxySciTech">Telegram</a> ·
  <a href="https://github.com/GalaxySciTech/java-wallet/issues">Issues</a> ·
  <a href="#快速开始">快速开始</a>
</p>

---

## 简介

Java-Wallet 是一套经过生产环境验证的多链加密货币钱包后台系统，曾稳定管理过数亿美元级别的交易。系统提供区块链地址生成、充值检测、提现处理和自动归集等核心功能，支持主流公链和代币。

核心签名组件 [tokencore](https://github.com/GalaxySciTech/tokencore) 支持全链离线签名，私钥全程本地运算，从不外泄。

### 支持的公链

| 公链 | 原生币 | 代币 |
|:-----|:------:|:----:|
| Bitcoin | BTC | OMNI (USDT) |
| Ethereum | ETH | ERC-20 |
| Tron | TRX | TRC-20 |
| Bitcoin Cash | BCH | — |
| Bitcoin SV | BSV | — |
| Litecoin | LTC | — |
| Dogecoin | DOGE | — |
| Dash | DASH | — |

## 系统架构

```
                         ┌──────────────────────────────────────────────┐
                         │             客户端 / 管理后台 UI              │
                         └──────────────────┬───────────────────────────┘
                                            │  HTTP
                ┌───────────────────────────┼───────────────────────────┐
                │                           │                           │
        ┌───────▼────────┐         ┌────────▼────────┐         ┌───────▼────────┐
        │  wallet-webapi  │         │   wallet-task    │         │   wallet-hsm    │
        │   REST 接口     │         │    定时任务       │         │    密钥管理      │
        │   :10001        │         │  :10033          │         │  :10888         │
        └───────┬─────────┘         └────────┬─────────┘         └───────┬─────────┘
                │                            │                           │
                └────────────┬───────────────┘                           │
                             │                                           │
                     ┌───────▼────────┐                          ┌───────▼────────┐
                     │  wallet-common  │                          │  wallet-entity  │
                     │    业务逻辑     │◄─────────────────────────│   数据访问层     │
                     └──┬─────┬─────┬─┘                          └───────┬─────────┘
                        │     │     │                                     │
                   ┌────▼┐ ┌──▼──┐ ┌▼──────┐                     ┌───────▼────────┐
                   │ RPC │ │ MQ  │ │xxl-job│                     │    MySQL 8     │
                   │ 节点 │ │     │ │       │                     └────────────────┘
                   └─────┘ └─────┘ └───────┘
```

| 模块 | 说明 |
|:-----|:-----|
| **wallet-webapi** | REST API — 地址生成、充提币、管理后台、Swagger 文档 |
| **wallet-task** | 定时任务 — 区块同步、充值检测、自动归集、手续费补发 |
| **wallet-hsm** | 密钥管理 — HD 钱包派生、离线签名、私钥导入导出 |
| **wallet-common** | 公共层 — 业务逻辑、RPC 客户端、缓存、工具类 |
| **wallet-entity** | 数据层 — JPA 实体、Repository、QueryDSL |

## 技术栈

| 类别 | 技术 |
|:-----|:-----|
| 语言 | Kotlin 1.9 + Java 17 |
| 框架 | Spring Boot 3.1.3 |
| 构建 | Gradle 8.5 |
| 数据库 | MySQL 8.0 · Spring Data JPA · HikariCP |
| 缓存 | Caffeine |
| 消息队列 | RabbitMQ (Spring AMQP) |
| 任务调度 | xxl-job 2.4.0 |
| 区块链 | [tokencore 1.3.0](https://github.com/GalaxySciTech/tokencore) · web3j 4.10.3 · bitcoin-rpc-client 1.2.4 |
| 接口文档 | SpringDoc OpenAPI 2.3 (Swagger UI) |
| 容器化 | Docker 多阶段构建 · docker-compose |
| CI/CD | GitHub Actions · GitLab CI |

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- RabbitMQ 3.x（wallet-task 需要）

### Docker Compose（推荐）

```bash
git clone https://github.com/GalaxySciTech/java-wallet.git
cd java-wallet

cp .env.example .env   # 编辑配置
docker-compose up -d
```

服务启动后：
- **API 接口**: http://localhost:10001
- **Swagger 文档**: http://localhost:10001/swagger-ui.html
- **RabbitMQ 管理**: http://localhost:15672

### 手动部署

```bash
# 1. 配置环境变量（完整列表见 .env.example）
export DB_URL="jdbc:mysql://localhost:3306/wallet_db?useSSL=false&characterEncoding=UTF-8"
export DB_USERNAME=root
export DB_PASSWORD=your_password

# 2. 构建项目
chmod +x gradlew
./gradlew build -x test

# 3. 启动服务（分别在不同终端运行）
./gradlew :wallet-webapi:bootRun
./gradlew :wallet-task:bootRun
./gradlew :wallet-hsm:bootRun
```

## 配置说明

所有敏感配置均通过环境变量注入，绝不硬编码在代码中。完整列表见 [`.env.example`](.env.example)。

| 变量 | 服务 | 说明 |
|:-----|:-----|:-----|
| `DB_URL` | webapi, task | MySQL JDBC 连接地址 |
| `DB_USERNAME` / `DB_PASSWORD` | webapi, task | 数据库用户名和密码 |
| `RABBITMQ_HOST` / `RABBITMQ_PASSWORD` | task | RabbitMQ 连接信息 |
| `KEYSTORE_DIR` / `KEYSTORE_PASSWORD` | hsm | 密钥库存储路径与密码 |
| `XXL_JOB_ADMIN_ADDRESSES` | task | xxl-job 调度中心地址 |
| `WALLET_CRYPTO_PUSH_KEY` | webapi | 充值推送通知 AES 加密密钥 |

## 接口文档

### 钱包接口 `/wallet/v1`

| 方法 | 路径 | 说明 |
|:----:|:-----|:-----|
| POST | `/get_address` | 批量生成区块链地址 |
| POST | `/send` | 提现（可指定 from 地址，不指定则自动从热钱包转出） |
| POST | `/create_hot_address` | 创建热钱包 / Gas 钱包地址 |
| POST | `/export_wallet` | 导出私钥或助记词 |
| POST | `/import_wallet` | 导入私钥或助记词 |
| GET | `/get_hot_address` | 查询热钱包地址 |
| GET | `/check_address` | 验证地址私钥是否存在 |
| GET | `/get_transaction` | 查询充值/提现/归集记录 |
| GET | `/get_new_deposit` | 获取新充值记录（拉取后自动标记已读） |
| GET | `/check_tx_status` | 查询链上交易状态 |

### 区块链接口 `/block_chain/v1`

| 方法 | 路径 | 说明 |
|:----:|:-----|:-----|
| GET | `/get_transaction` | 链上查询交易详情 |
| GET | `/get_address_balance` | 链上查询地址余额 |
| GET | `/get_recommend_fee` | 获取推荐手续费（低速/中速/快速） |
| GET | `/calculation_fee` | 计算交易手续费 |
| GET | `/get_support_token` | 获取支持的币种列表 |

### 管理后台接口 `/admin`

| 方法 | 路径 | 说明 |
|:----:|:-----|:-----|
| POST | `/login` | 管理员登录 |
| GET | `/get_dashboard` | 获取仪表盘概览数据 |
| GET | `/get_addr_list` | 获取地址列表 |
| GET | `/get_token_list` | 获取代币列表 |
| POST | `/edit_token` | 编辑代币配置 |
| POST | `/edit_config` | 编辑系统配置 |
| POST | `/edit_white` | 编辑 IP 白名单 |

> 完整的交互式接口文档请在启动服务后访问 Swagger UI。

## 可靠性

Java-Wallet 已在生产环境中长期稳定运行，安全管理过数亿美元的交易量。系统具备高可靠性、可扩展性和实用性，值得信赖。

## 联系我们

如需技术支持、商业合作或部署咨询，请通过 Telegram 联系：

**[@GalaxySciTech](https://t.me/GalaxySciTech)**

## 免责声明

使用本源代码进行的任何商业活动，由此产生的损失（无论是对自己还是对他人）均由使用者自行承担，作者不对此承担任何责任。

## 许可证

本项目按原样提供，详见仓库中的许可证文件。
