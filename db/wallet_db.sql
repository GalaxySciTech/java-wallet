/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : wallet_db

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 14/12/2020 23:50:01
*/
create database `wallet_db` default character set utf8mb4 collate utf8mb4_general_ci;

use wallet_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `chain_type` varchar(255) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `wallet_code` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `auto_collect` int(11) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for address_admin
-- ----------------------------
DROP TABLE IF EXISTS `address_admin`;
CREATE TABLE `address_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `address_type` int(11) DEFAULT NULL COMMENT '100发送200归集',
  `chain_type` varchar(255) DEFAULT NULL,
  `wallet_code` varchar(255) DEFAULT NULL COMMENT '发送地址必须有',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for block_height
-- ----------------------------
DROP TABLE IF EXISTS `block_height`;
CREATE TABLE `block_height` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `chain_type` varchar(255) DEFAULT NULL,
  `height` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of block_height
-- ----------------------------
BEGIN;
INSERT INTO `block_height` VALUES (1, '2020-12-14 02:15:15', '2020-12-14 02:15:13', 'BITCOIN', 12);
INSERT INTO `block_height` VALUES (2, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'ETHEREUM', 0);
INSERT INTO `block_height` VALUES (3, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'DASH', 0);
INSERT INTO `block_height` VALUES (4, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'LITECOIN', 0);
INSERT INTO `block_height` VALUES (5, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'BITCOINCASH', 0);
INSERT INTO `block_height` VALUES (6, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'BITCOINSV', 0);
INSERT INTO `block_height` VALUES (7, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'DOGECOIN', 0);
INSERT INTO `block_height` VALUES (8, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'TRON', 0);
INSERT INTO `block_height` VALUES (9, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'OMNI', 0);
COMMIT;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `config_key` varchar(255) DEFAULT NULL,
  `config_value` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `config_group` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of config
-- ----------------------------
BEGIN;
INSERT INTO `config` VALUES (1, '2020-11-01 08:51:33', '2020-12-13 18:16:16', 'HSM_URL', 'http://127.0.0.1:10888', 'hsm 请求地址', '全局');
INSERT INTO `config` VALUES (2, '2020-11-01 08:51:33', '2020-12-14 14:41:48', 'ETH_RPC_URL', 'https://mainnet.infura.io/v3/7820361508e54d148b1941ef1029ebba', 'eth RPC地址', '以太坊');
INSERT INTO `config` VALUES (3, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'OMNI_RPC_URL', 'http://127.0.0.1:8332', 'omni RPC地址', '比特币');
INSERT INTO `config` VALUES (4, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'DASH_RPC_URL', 'http://127.0.0.1:8332', 'dash RPC地址', '达世');
INSERT INTO `config` VALUES (5, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'LTC_RPC_URL', 'http://127.0.0.1:8332', 'ltc RPC地址', '莱特币');
INSERT INTO `config` VALUES (6, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'BCH_RPC_URL', 'http://127.0.0.1:8332', 'bch RPC地址', '比特现金');
INSERT INTO `config` VALUES (7, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'BSV_RPC_URL', 'http://127.0.0.1:8332', 'bsv RPC地址', '比特币SV');
INSERT INTO `config` VALUES (8, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'DOGE_RPC_URL', 'http://127.0.0.1:8332', 'doge RPC地址', '狗狗币');
INSERT INTO `config` VALUES (9, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'EOS_RPC_URL', 'https://api.eosflare.io', 'eos RPC地址', '柚子');
INSERT INTO `config` VALUES (10, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'TRX_API_URL', 'https://api.trongrid.io', 'trx HTTP API地址', '波场');
INSERT INTO `config` VALUES (11, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'ETH_SCAN_BACK', '12', '以太坊回扫高度数', '以太坊');
INSERT INTO `config` VALUES (12, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'ETH_GAS_LEVEL', 'fast', '以太坊手续费等级 fast  average safeLow', '以太坊');
INSERT INTO `config` VALUES (13, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'BTC_SCAN_BACK', '6', '比特币回扫高度数', '比特币');
INSERT INTO `config` VALUES (14, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'BTC_GAS_LEVEL', 'fastestFee', '比特币手续费等级 fastestFee halfHourFee hourFee', '比特币');
INSERT INTO `config` VALUES (15, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'GAS_PROP', '1', 'gas使用比例，范围0-1', '全局');
INSERT INTO `config` VALUES (16, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'LOG_LEVEL', '1', '0-9 log等级', '全局');
INSERT INTO `config` VALUES (17, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'ERROR_LEVEL', '0', '0 展示错误信息 |1 展示堆栈', '全局');
INSERT INTO `config` VALUES (18, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'TRX_SCAN_BACK', '12', '波场回扫高度数', '波场');
INSERT INTO `config` VALUES (19, '2020-11-01 08:51:33', '2020-12-13 18:26:44', 'DEPOSIT_POST_NOTIFY_SALT', '1', '充值同步post数据盐值', '充值通知');
INSERT INTO `config` VALUES (20, '2020-11-01 08:51:33', '2020-11-01 08:51:33', 'DEPOSIT_POST_NOTIFY_URL', 'http://192.168.31.222', '充值同步post数据地址 逗号分割可以推送多个服务端 例如 http://192.168.31.222,http://192.168.31.223', '充值通知');
INSERT INTO `config` VALUES (21, '2020-11-01 08:51:33', '2020-12-13 18:26:31', 'DEPOSIT_NOTIFY_MODE', 'post', '充值同步模式 post rabbitmq 逗号分割可以同时推送多个 例如 post,rabbitmq', '充值通知');
COMMIT;

-- ----------------------------
-- Table structure for deposit
-- ----------------------------
DROP TABLE IF EXISTS `deposit`;
CREATE TABLE `deposit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash` varchar(255) DEFAULT NULL,
  `chain_type` varchar(32) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `amount` decimal(64,18) DEFAULT NULL,
  `confirmations` bigint(11) DEFAULT NULL,
  `is_upload` int(11) DEFAULT '0' COMMENT '1已上传 0未上传',
  `token_symbol` varchar(255) DEFAULT NULL,
  `confirm_time` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chain_type` varchar(255) DEFAULT NULL,
  `token_symbol` varchar(255) DEFAULT NULL,
  `token_address` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `gas_limit` bigint(11) DEFAULT NULL,
  `min_collect` decimal(64,18) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of token
-- ----------------------------
BEGIN;
INSERT INTO `token` VALUES (1, 'BITCOIN', 'BITCOIN', '', '2020-12-13 17:37:39', '2020-12-13 18:00:38', NULL, 0.011000000000000000);
INSERT INTO `token` VALUES (2, 'BITCOIN', 'USDT', '31', '2020-12-13 17:37:39', '2020-12-13 17:37:39', NULL, 0.000000000000000000);
INSERT INTO `token` VALUES (3, 'ETHEREUM', 'USDT', '0xdac17f958d2ee523a2206206994597c13d831ec7', '2020-12-13 17:37:39', '2020-12-13 17:37:39', 60000, 0.000000000000000000);
INSERT INTO `token` VALUES (4, 'ETHEREUM', 'ETHEREUM', NULL, '2020-12-13 17:37:39', '2020-12-13 17:37:39', NULL, 0.000000000000000000);
INSERT INTO `token` VALUES (5, 'TRON', 'USDT', 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t', '2020-12-13 17:37:39', '2020-12-13 17:37:39', 1000000, 0.000000000000000000);
INSERT INTO `token` VALUES (6, 'TRON', 'TRON', NULL, '2020-12-13 17:37:39', '2020-12-13 17:37:39', NULL, 0.000000000000000000);
INSERT INTO `token` VALUES (7, 'DASH', 'DASH', NULL, '2020-12-13 17:37:39', '2020-12-13 17:37:39', NULL, 0.000000000000000000);
INSERT INTO `token` VALUES (8, 'DOGECOIN', 'DOGECOIN', NULL, '2020-12-13 17:37:39', '2020-12-13 17:37:39', NULL, 0.000000000000000000);
INSERT INTO `token` VALUES (9, 'LITECOIN', 'LITECOIN', NULL, '2020-12-13 17:37:39', '2020-12-13 17:37:39', NULL, 0.000000000000000000);
INSERT INTO `token` VALUES (10, 'BITCOINSV', 'BITCOINSV', NULL, '2020-12-13 17:37:39', '2020-12-13 17:37:39', NULL, 0.000000000000000000);
INSERT INTO `token` VALUES (11, 'BITCOINCASH', 'BITCOINCASH', NULL, '2020-12-13 17:37:39', '2020-12-13 17:37:39', NULL, 0.000000000000000000);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `salt` varchar(255) DEFAULT NULL,
  `access_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'aa', '32143$kKpkSD2HOPDQ1bMlK.YRP0', '2020-12-07 10:07:49', '2020-12-07 17:19:54', '32143', 'token');
COMMIT;

-- ----------------------------
-- Table structure for wait_collect
-- ----------------------------
DROP TABLE IF EXISTS `wait_collect`;
CREATE TABLE `wait_collect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `token_symbol` varchar(255) DEFAULT NULL,
  `chain_type` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `send_fee` int(11) DEFAULT '0',
  `send_fee_gas_price` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wait_import
-- ----------------------------
DROP TABLE IF EXISTS `wait_import`;
CREATE TABLE `wait_import` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `chain_type` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for white
-- ----------------------------
DROP TABLE IF EXISTS `white`;
CREATE TABLE `white` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `ip` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of white
-- ----------------------------
BEGIN;
INSERT INTO `white` VALUES (1, '2020-11-01 08:52:25', '0.0.0.0', '2020-11-01 08:52:25');
COMMIT;

-- ----------------------------
-- Table structure for withdraw
-- ----------------------------
DROP TABLE IF EXISTS `withdraw`;
CREATE TABLE `withdraw` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash` varchar(255) DEFAULT NULL,
  `amount` decimal(64,18) DEFAULT NULL,
  `from_address` varchar(255) DEFAULT NULL,
  `to_address` varchar(255) DEFAULT NULL,
  `chain_type` varchar(255) DEFAULT NULL,
  `token_symbol` varchar(255) DEFAULT NULL,
  `withdraw_type` int(11) DEFAULT NULL COMMENT '100提现200归集',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of withdraw
-- ----------------------------
BEGIN;
INSERT INTO `withdraw` VALUES (1, 'dsadsa', 11.000000000000000000, 'cadsa', 'dsads', 'BITCOIN', NULL, 300, '2020-12-07 14:02:35', '2020-12-07 14:02:35');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for wallet_admin_audit_log
-- ----------------------------
DROP TABLE IF EXISTS `wallet_admin_audit_log`;
CREATE TABLE `wallet_admin_audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator` varchar(128) DEFAULT NULL,
  `action` varchar(128) DEFAULT NULL,
  `target_key` varchar(255) DEFAULT NULL,
  `after_value` varchar(1024) DEFAULT NULL,
  `request_ip` varchar(128) DEFAULT NULL,
  `user_agent` varchar(512) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wallet_rpc_config
-- ----------------------------
DROP TABLE IF EXISTS `wallet_rpc_config`;
CREATE TABLE `wallet_rpc_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chain` varchar(64) NOT NULL,
  `rpc_url` varchar(512) DEFAULT NULL,
  `rpc_username` varchar(255) DEFAULT NULL,
  `rpc_password` varchar(255) DEFAULT NULL,
  `rpc_api_key` varchar(255) DEFAULT NULL,
  `rpc_timeout_ms` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wallet_rpc_chain` (`chain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wallet_scheduler_config
-- ----------------------------
DROP TABLE IF EXISTS `wallet_scheduler_config`;
CREATE TABLE `wallet_scheduler_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chain` varchar(64) NOT NULL,
  `deposit_scan_enabled` tinyint(1) DEFAULT 1,
  `deposit_scan_interval_ms` bigint(20) DEFAULT 15000,
  `sweep_enabled` tinyint(1) DEFAULT 0,
  `sweep_interval_ms` bigint(20) DEFAULT 30000,
  `fee_supply_enabled` tinyint(1) DEFAULT 0,
  `fee_supply_interval_ms` bigint(20) DEFAULT 30000,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wallet_scheduler_chain` (`chain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wallet_chain_config
-- ----------------------------
DROP TABLE IF EXISTS `wallet_chain_config`;
CREATE TABLE `wallet_chain_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chain` varchar(64) NOT NULL,
  `enabled` tinyint(1) DEFAULT 1,
  `deposit_scan_enabled` tinyint(1) DEFAULT 1,
  `withdraw_enabled` tinyint(1) DEFAULT 0,
  `confirmations` int(11) DEFAULT 12,
  `start_block` bigint(20) DEFAULT 0,
  `current_block` bigint(20) DEFAULT 0,
  `scan_batch_size` int(11) DEFAULT 100,
  `scan_interval_ms` bigint(20) DEFAULT 15000,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wallet_chain_chain` (`chain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wallet_sweep_config`;
CREATE TABLE `wallet_sweep_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chain` varchar(64) NOT NULL,
  `sweep_enabled` tinyint(1) DEFAULT 0,
  `sweep_to_address` varchar(255) DEFAULT NULL,
  `min_sweep_amount` varchar(64) DEFAULT '0',
  `reserve_amount` varchar(64) DEFAULT '0',
  PRIMARY KEY (`id`), UNIQUE KEY `uk_wallet_sweep_chain` (`chain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wallet_withdraw_config`;
CREATE TABLE `wallet_withdraw_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chain` varchar(64) NOT NULL,
  `withdraw_enabled` tinyint(1) DEFAULT 0,
  `manual_review_enabled` tinyint(1) DEFAULT 1,
  `max_auto_withdraw_amount` varchar(64) DEFAULT '0',
  `daily_withdraw_limit` varchar(64) DEFAULT '0',
  PRIMARY KEY (`id`), UNIQUE KEY `uk_wallet_withdraw_chain` (`chain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wallet_fee_supply_config`;
CREATE TABLE `wallet_fee_supply_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chain` varchar(64) NOT NULL,
  `fee_supply_enabled` tinyint(1) DEFAULT 0,
  `fee_supply_from_address` varchar(255) DEFAULT NULL,
  `min_gas_balance` varchar(64) DEFAULT '0',
  `target_gas_balance` varchar(64) DEFAULT '0',
  PRIMARY KEY (`id`), UNIQUE KEY `uk_wallet_fee_supply_chain` (`chain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wallet_security_config`;
CREATE TABLE `wallet_security_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `allow_export_private_key` tinyint(1) DEFAULT 0,
  `export_private_key_require2fa` tinyint(1) DEFAULT 1,
  `allow_update_rpc_by_admin` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
