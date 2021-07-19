/*
 Navicat Premium Data Transfer

 Source Server         : gamepay节点
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : xxl_job

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 19/04/2021 10:43:04
*/
create database `xxl_job` default character set utf8mb4 collate utf8mb4_general_ci;
use xxl_job;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xxl_job_group
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_group`;
CREATE TABLE `xxl_job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` varchar(512) DEFAULT NULL COMMENT '执行器地址列表，多地址逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_group
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_group` VALUES (2, 'cl-task', '节点执行器', 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_info
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_info`;
CREATE TABLE `xxl_job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_cron` varchar(128) NOT NULL COMMENT '任务执行CRON',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_info
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_info` VALUES (1, 2, '0 0/5 * * * ?', '比特币扫块', '2020-09-06 02:33:36', '2021-04-19 10:42:50', 'admin', '', 'FIRST', 'synBTC', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:33:36', '', 1, 0, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (2, 2, '0 0/5 * * * ?', '比特币归集', '2020-09-06 02:51:29', '2021-04-19 10:42:47', 'admin', '', 'FIRST', 'collectBTC', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:51:29', '', 1, 0, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (3, 2, '0 0/5 * * * ?', 'OMNI代币扫块', '2020-09-06 02:34:14', '2021-04-19 10:42:42', 'admin', '', 'FIRST', 'synOMNI', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:34:14', '', 1, 0, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (4, 2, '0 0/5 * * * ?', 'OMNI代币归集', '2020-09-06 02:50:41', '2021-04-19 10:42:38', 'admin', '', 'FIRST', 'collectOMNI', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:50:41', '', 1, 0, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (5, 2, '0 0/5 * * * ?', '以太坊扫块', '2020-06-29 02:03:25', '2020-09-17 17:22:06', 'admin', '', 'FIRST', 'synETH', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-06-29 02:03:25', '', 1, 1618800000000, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (6, 2, '0 0/5 * * * ?', '以太坊归集', '2020-09-06 02:50:58', '2020-12-09 15:57:16', 'admin', '', 'FIRST', 'collectETH', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:50:58', '', 1, 1618800000000, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (7, 2, '0 0/5 * * * ?', '发送ERC20代币归集手续费', '2020-09-06 03:02:28', '2020-12-09 15:57:13', 'admin', '', 'FIRST', 'sendFeeETH', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 03:02:28', '', 1, 1618800000000, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (8, 2, '0 0/5 * * * ?', 'ERC20代币归集', '2020-09-06 02:51:12', '2020-12-09 15:57:10', 'admin', '', 'FIRST', 'collectERC', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:51:12', '', 1, 1618800000000, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (9, 2, '0 0/5 * * * ?', 'DASH扫块', '2020-09-06 02:34:57', '2020-12-09 15:56:49', 'admin', '', 'FIRST', 'synDASH', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:34:57', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (10, 2, '0 0/5 * * * ?', 'DASH归集', '2020-09-06 02:51:42', '2020-09-17 17:11:27', 'admin', '', 'FIRST', 'collectDASH', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:51:42', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (11, 2, '0 0/5 * * * ?', '莱特币扫块', '2020-09-06 02:38:54', '2020-12-09 15:56:52', 'admin', '', 'FIRST', 'synLTC', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:38:54', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (12, 2, '0 0/5 * * * ?', '莱特币归集', '2020-09-06 12:13:45', '2020-09-17 17:11:13', 'admin', '', 'FIRST', 'collectLTC', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 12:13:45', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (13, 2, '0 0/5 * * * ?', 'BSV扫块', '2020-09-06 02:39:42', '2020-12-09 15:56:43', 'admin', '', 'FIRST', 'synBSV', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:39:42', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (14, 2, '0 0/5 * * * ?', 'BSV归集', '2020-09-06 12:14:11', '2020-09-17 17:10:55', 'admin', '', 'FIRST', 'collectBSV', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 12:14:11', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (15, 2, '0 0/5 * * * ?', 'BCH扫块', '2020-09-06 02:35:45', '2020-12-09 15:56:40', 'admin', '', 'FIRST', 'synBCH', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:35:45', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (16, 2, '0 0/5 * * * ?', 'BCH归集', '2020-09-06 12:14:41', '2020-09-06 12:14:41', 'admin', '', 'FIRST', 'collectBCH', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 12:14:41', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (17, 2, '0 0/5 * * * ?', 'DOGE扫快', '2020-09-17 17:08:26', '2020-12-09 15:56:36', 'admin', '', 'FIRST', 'synDOGE', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-17 17:08:26', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (18, 2, '0 0/5 * * * ?', 'DOGE归集', '2020-09-17 17:08:45', '2020-09-17 17:08:45', 'admin', '', 'FIRST', 'collectDOGE', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-17 17:08:45', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (98, 2, '0 0/1 * * * ?', '同步远程币价【暂不开启】', '2020-09-06 04:34:25', '2020-09-17 17:10:28', 'admin', '', 'FIRST', '', '', 'SERIAL_EXECUTION', 0, 0, 'GLUE_PHP', '<?php\n\n    echo \"xxl-job: 同步远程币价  \\n\";\n	echo file_get_contents(\"https://ubsbit.com/AutoRun/GetHuobi/ChainPrice.php\");\n    echo \"脚本位置：$argv[0]  \\n\";\n    echo \"任务参数：$argv[1]  \\n\";\n    echo \"分片序号 = $argv[2]  \\n\";\n    echo \"分片总数 = $argv[3]  \\n\";\n\n    echo \"完成  \\n\";\n    exit(0);\n\n?>\n', '0000', '2020-09-06 04:37:27', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (99, 2, '0/5 * * * * ?', '充值同步', '2020-09-06 02:42:52', '2020-09-21 07:47:58', 'admin', '', 'FIRST', 'synDeposit', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-06 02:42:52', '', 1, 1618800215000, 1618800220000);
INSERT INTO `xxl_job_info` VALUES (100, 2, '0 0/1 * * * ?', '导入地址同步', '2020-09-17 17:07:07', '2020-09-18 03:21:15', 'admin', '', 'FIRST', 'synImportAddress', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-17 17:07:07', '', 1, 1618800180000, 1618800240000);
INSERT INTO `xxl_job_info` VALUES (101, 2, '0 0/5 * * * ?', 'TRX扫块', '2021-04-19 10:39:20', '2021-04-19 10:42:23', 'pie', '', 'FIRST', 'synTRX', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-04-19 10:39:20', '', 1, 0, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (102, 2, '0 0/5 * * * ?', 'TRX归集', '2021-04-19 10:39:29', '2021-04-19 10:42:18', 'pie', '', 'FIRST', 'collectTRX', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-04-19 10:39:29', '', 1, 0, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (103, 2, '0 0/5 * * * ?', 'TRX转手续费', '2021-04-19 10:39:40', '2021-04-19 10:42:13', 'pie', '', 'FIRST', 'sendFeeTRX', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-04-19 10:39:40', '', 1, 0, 1618800300000);
INSERT INTO `xxl_job_info` VALUES (104, 2, '0 0/5 * * * ?', 'TRC20归集', '2021-04-19 10:39:58', '2021-04-19 10:42:09', 'pie', '', 'FIRST', 'collectTRC', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-04-19 10:39:58', '', 1, 0, 1618800300000);
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_lock
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_lock`;
CREATE TABLE `xxl_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_lock
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_lock` VALUES ('schedule_lock');
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_log
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log`;
CREATE TABLE `xxl_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB AUTO_INCREMENT=413 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_log
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_log` VALUES (317, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:36:20', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (318, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:36:25', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (319, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:36:30', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (320, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:36:35', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (321, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:36:40', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (322, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:36:45', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (323, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:36:50', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (324, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:15', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (325, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:20', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (326, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:25', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (327, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:30', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (328, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:35', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (329, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:40', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (330, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:45', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (331, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:50', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (332, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:37:55', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (333, 2, 100, NULL, 'synImportAddress', '', NULL, 0, '2021-04-19 10:38:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (334, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (335, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:05', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (336, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:10', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (337, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:15', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (338, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:20', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (339, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:25', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (340, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:30', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (341, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:35', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (342, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:40', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (343, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:45', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (344, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:50', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (345, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:38:55', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (346, 2, 100, NULL, 'synImportAddress', '', NULL, 0, '2021-04-19 10:39:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (347, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (348, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:05', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (349, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:10', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (350, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:15', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (351, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:20', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (352, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:25', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (353, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:30', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (354, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:35', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (355, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:40', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (356, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:45', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (357, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:50', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (358, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:39:55', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (359, 2, 7, NULL, 'sendFeeETH', '', NULL, 0, '2021-04-19 10:40:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (360, 2, 6, NULL, 'collectETH', '', NULL, 0, '2021-04-19 10:40:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (361, 2, 5, NULL, 'synETH', '', NULL, 0, '2021-04-19 10:40:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (362, 2, 100, NULL, 'synImportAddress', '', NULL, 0, '2021-04-19 10:40:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (363, 2, 8, NULL, 'collectERC', '', NULL, 0, '2021-04-19 10:40:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (364, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (365, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:05', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (366, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:10', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (367, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:15', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (368, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:20', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (369, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:25', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (370, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:30', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (371, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:35', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (372, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:40', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (373, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:45', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (374, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:50', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (375, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:40:55', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (376, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (377, 2, 100, NULL, 'synImportAddress', '', NULL, 0, '2021-04-19 10:41:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (378, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:05', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (379, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:10', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (380, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:15', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (381, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:20', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (382, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:25', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (383, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:30', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (384, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:35', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (385, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:40', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (386, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:45', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (387, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:50', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (388, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:41:55', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (389, 2, 100, NULL, 'synImportAddress', '', NULL, 0, '2021-04-19 10:42:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (390, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (391, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:05', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (392, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:10', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (393, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:15', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (394, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:20', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (395, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:25', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (396, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:30', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (397, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:35', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (398, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:40', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (399, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:45', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (400, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:50', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (401, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:42:55', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (402, 2, 100, NULL, 'synImportAddress', '', NULL, 0, '2021-04-19 10:43:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (403, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:43:00', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (404, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:43:05', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (405, 2, 99, NULL, 'synDeposit', '', NULL, 0, '2021-04-19 10:43:10', 500, '任务触发类型：Cron触发<br>调度机器：103.100.143.16<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_log_report
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log_report`;
CREATE TABLE `xxl_job_log_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',
  `running_count` int(11) NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
  `suc_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
  `fail_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_log_report
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_log_report` VALUES (1, '2020-09-20 12:00:00', 0, 4022, 485);
INSERT INTO `xxl_job_log_report` VALUES (2, '2020-09-19 12:00:00', 0, 0, 0);
INSERT INTO `xxl_job_log_report` VALUES (3, '2020-09-18 12:00:00', 0, 0, 0);
INSERT INTO `xxl_job_log_report` VALUES (4, '2020-09-21 12:00:00', 0, 21887, 577);
INSERT INTO `xxl_job_log_report` VALUES (5, '2020-09-22 12:00:00', 0, 21887, 577);
INSERT INTO `xxl_job_log_report` VALUES (6, '2020-09-23 12:00:00', 0, 21887, 577);
INSERT INTO `xxl_job_log_report` VALUES (7, '2020-09-24 12:00:00', 0, 17396, 5068);
INSERT INTO `xxl_job_log_report` VALUES (8, '2020-09-25 12:00:00', 1, 940, 3612);
INSERT INTO `xxl_job_log_report` VALUES (9, '2020-12-09 00:00:00', 0, 243, 55);
INSERT INTO `xxl_job_log_report` VALUES (10, '2020-12-08 00:00:00', 0, 0, 0);
INSERT INTO `xxl_job_log_report` VALUES (11, '2020-12-07 00:00:00', 0, 0, 0);
INSERT INTO `xxl_job_log_report` VALUES (12, '2021-04-19 00:00:00', 0, 0, 88);
INSERT INTO `xxl_job_log_report` VALUES (13, '2021-04-18 00:00:00', 0, 0, 0);
INSERT INTO `xxl_job_log_report` VALUES (14, '2021-04-17 00:00:00', 0, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_logglue
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_logglue`;
CREATE TABLE `xxl_job_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_logglue
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_logglue` VALUES (2, 16, 'GLUE_PHP', '<?php\n\n    echo \"xxl-job: 同步远程币价  \\n\";\n	echo file_get_contents(\"https://ubsbit.com/AutoRun/GetHuobi/ChainPrice.php\");\n    echo \"脚本位置：$argv[0]  \\n\";\n    echo \"任务参数：$argv[1]  \\n\";\n    echo \"分片序号 = $argv[2]  \\n\";\n    echo \"分片总数 = $argv[3]  \\n\";\n\n    echo \"完成  \\n\";\n    exit(0);\n\n?>\n', '0000', '2020-09-06 04:37:27', '2020-09-06 04:37:27');
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_registry
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_registry`;
CREATE TABLE `xxl_job_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for xxl_job_user
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_user`;
CREATE TABLE `xxl_job_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_user
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
