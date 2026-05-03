# Java Wallet（单体化改造中）

项目正在收敛为**单体 Spring Boot 钱包服务**。

## 当前变化
- **单模块**：源码仅在仓库根目录 [`src/`](src/)，Gradle 在根目录 [`build.gradle`](build.gradle)
- 不再依赖 RabbitMQ / xxl-job
- 签名与地址：**tokencore** `com.github.galaxyscitech:tokencore:2.0.1`（进程内）
- 可选 **外部 HTTP 签名**：表 `wallet_chain_config.signing_backend = EXTERNAL`，配置 `wallet.external-signer.base-url`

## 快速开始
### 1）Docker Compose 启动
```bash
docker compose up -d --build
```

服务：
- `app`：`http://localhost:8080`
- `mysql`：`localhost:3306`

### 2）手动启动
```bash
./gradlew bootRun
```

数据库初始化见 [`db/wallet_db.sql`](db/wallet_db.sql)。若已有库，需增加 `signing_backend` 列时执行 [`db/002_wallet_chain_signing_backend.sql`](db/002_wallet_chain_signing_backend.sql)。

环境变量示例：[`.env.example`](.env.example)。测试网链上冒烟：配置 Sepolia 等 `ETH_RPC_URL`，设置 `ETH_SIGN_CHAIN_ID=11155111`，准备测试资金后走 Swagger 提现/归集相关接口。

## 接口
- 钱包接口：`/wallet/v1`
- 链接口：`/block_chain/v1`
- 管理接口：`/admin`
- Swagger：`/swagger-ui/index.html`

## 运行配置
当前已支持通过 `sys_config` 配置动态调度键：
- `SCHEDULER_DEPOSIT_SCAN_ENABLED`
- `SCHEDULER_DEPOSIT_SCAN_MS`
- `SCHEDULER_CHAIN_SYNC_ENABLED`
- `SCHEDULER_CHAIN_SYNC_MS`
- `SCHEDULER_SWEEP_ENABLED`
- `SCHEDULER_SWEEP_MS`
- `SCHEDULER_FEE_SUPPLY_ENABLED`
- `SCHEDULER_FEE_SUPPLY_MS`

## 安全提示
- 生产环境务必修改默认密码。
- 禁止在日志输出私钥、助记词、keystore 密码。
- 管理接口返回敏感字段必须脱敏。

## 数据库更新
- 新增审计表：`wallet_admin_audit_log`（后台配置变更历史）。
