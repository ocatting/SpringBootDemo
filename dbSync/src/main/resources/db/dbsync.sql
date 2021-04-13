/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : dbsync

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 12/04/2021 18:27:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_sync_db
-- ----------------------------
DROP TABLE IF EXISTS `t_sync_db`;
CREATE TABLE `t_sync_db`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `db_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `jdbc_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'jdbc地址连接',
  `driver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '驱动',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `open_remote` tinyint(1) NULL DEFAULT 0 COMMENT '开启远程模式：0-false,1-true',
  `remote_address` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '远程调用地址',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库连接配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sync_db
-- ----------------------------
INSERT INTO `t_sync_db` VALUES (1, '3d可视化生产', 'mysql', 'jdbc:mysql://39.104.236.112:3306/g20?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai', 'com.mysql.cj.jdbc.Driver', 'g20', 'xx3FcdktJWGCcEwD', 0, NULL, NULL, NULL);
INSERT INTO `t_sync_db` VALUES (2, 'g20_test', 'mysql', 'jdbc:mysql://localhost:3306/g20?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai', 'com.mysql.cj.jdbc.Driver', 'root', '123456', 0, NULL, NULL, NULL);
INSERT INTO `t_sync_db` VALUES (3, 'bmTest', 'mysql', 'jdbc:mysql://119.3.1.135:3306/bm202012?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai', 'com.mysql.cj.jdbc.Driver', 'root', 'root', 0, NULL, '2021-03-31 17:59:29', NULL);
INSERT INTO `t_sync_db` VALUES (4, 'g20_test_2', 'mysql', 'jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai', 'com.mysql.cj.jdbc.Driver', 'root', '123456', 0, NULL, NULL, NULL);
INSERT INTO `t_sync_db` VALUES (5, '3d可视化测试', 'mysql', 'jdbc:mysql://119.3.1.135:3306/g20?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai', 'com.mysql.cj.jdbc.Driver', 'root', 'root', 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_sync_group
-- ----------------------------
DROP TABLE IF EXISTS `t_sync_group`;
CREATE TABLE `t_sync_group`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组名称',
  `create_time` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sync_group
-- ----------------------------
INSERT INTO `t_sync_group` VALUES (1, 'ITSS数据同步到G20数据库', '2021-04-12 14:19:53');

-- ----------------------------
-- Table structure for t_sync_read_config
-- ----------------------------
DROP TABLE IF EXISTS `t_sync_read_config`;
CREATE TABLE `t_sync_read_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `table` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名称',
  `query_sql` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义SQL',
  `limit_memory_bytes` int NULL DEFAULT 256 COMMENT '限制内存大小',
  `mandatory_encoding` varchar(9) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '强制转码',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sync_read_config
-- ----------------------------
INSERT INTO `t_sync_read_config` VALUES (1, 't_judge_report', NULL, 256, 'utf-8', '2021-04-09 11:16:57');
INSERT INTO `t_sync_read_config` VALUES (6, 'daily_check_term_info', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (7, 'daily_check_point_info', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (8, 'device_hydraulic_record', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (9, 'device_together_set_detail', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (10, 'daily_check_line_user_rel', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (11, 'daily_check_user_rel', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (12, 'device_type_info', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (13, 'device_manhole_cover_record', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (14, 'daily_check_term_point_rel', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (16, 'daily_check_info', NULL, 256, 'utf-8', '2021-04-12 14:19:53');
INSERT INTO `t_sync_read_config` VALUES (17, 'daily_check_line_point_rel', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (18, 'device_together_set', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (19, 'device_warning_log', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (20, 'device_hydrant_record', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (21, 'device_video', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (22, 'device_info', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (23, 'daily_check_line_info', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (24, 'device_warning_deal_log', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (25, 'daily_check_detail', NULL, 256, 'utf-8', '2021-04-12 14:19:54');
INSERT INTO `t_sync_read_config` VALUES (26, 'device_electricity_record', NULL, 256, 'utf-8', '2021-04-12 14:19:54');

-- ----------------------------
-- Table structure for t_sync_task_info
-- ----------------------------
DROP TABLE IF EXISTS `t_sync_task_info`;
CREATE TABLE `t_sync_task_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` int NULL DEFAULT NULL COMMENT '任务组ID',
  `task_cron` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务执行CRON',
  `task_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `read_db_id` int NULL DEFAULT NULL COMMENT '读数据库配置',
  `read_conf_id` int NULL DEFAULT NULL COMMENT '读配置',
  `write_db_id` int NULL DEFAULT NULL COMMENT '写数据库配置',
  `write_conf_id` int NULL DEFAULT NULL COMMENT '写配置',
  `model` int NULL DEFAULT 0 COMMENT '模式：0-local，1-http',
  `trigger_status` int NULL DEFAULT NULL COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` datetime(0) NULL DEFAULT '0000-00-00 00:00:00' COMMENT '上次调度时间',
  `trigger_next_time` datetime(0) NULL DEFAULT '0000-00-00 00:00:00' COMMENT '下次调度时间',
  `increment_col` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '增量字段(注意该字段确保存在索引)',
  `increment_val` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '增量值',
  `increment_type` int NULL DEFAULT 2 COMMENT '增量类型，1 自增id,2 time',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库同步任务信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sync_task_info
-- ----------------------------
INSERT INTO `t_sync_task_info` VALUES (1, 0, '0/5 * * * * ? *', '测试', 4, 1, 2, 1, 0, 0, '2021-03-31 17:57:35', '2021-03-31 17:59:30', 'create_time', NULL, 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (2, 1, '0/30 * * * * ? *', 'daily_check_term_info', 3, 6, 5, 2, 0, 1, '2021-04-12 18:13:00', '2021-04-12 18:13:30', 'create_time', '2021-04-12 18:13:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (3, 1, '0/30 * * * * ? *', 'daily_check_point_info', 3, 7, 5, 3, 0, 1, '2021-04-12 18:13:00', '2021-04-12 18:13:30', 'create_time', '2021-04-12 18:13:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (4, 1, '0/30 * * * * ? *', 'device_hydraulic_record', 3, 8, 5, 4, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (5, 1, '0/30 * * * * ? *', 'device_together_set_detail', 3, 9, 5, 5, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (6, 1, '0/30 * * * * ? *', 'daily_check_line_user_rel', 3, 10, 5, 6, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (7, 1, '0/30 * * * * ? *', 'daily_check_user_rel', 3, 11, 5, 7, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (8, 1, '0/30 * * * * ? *', 'device_type_info', 3, 12, 5, 8, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (9, 1, '0/30 * * * * ? *', 'device_manhole_cover_record', 3, 13, 5, 9, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (10, 1, '0/30 * * * * ? *', 'daily_check_term_point_rel', 3, 14, 5, 10, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (12, 1, '0/30 * * * * ? *', 'daily_check_info', 3, 16, 5, 12, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:00', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (13, 1, '0/30 * * * * ? *', 'daily_check_line_point_rel', 3, 17, 5, 13, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (14, 1, '0/30 * * * * ? *', 'device_together_set', 3, 18, 5, 14, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (15, 1, '0/30 * * * * ? *', 'device_warning_log', 3, 19, 5, 15, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (16, 1, '0/30 * * * * ? *', 'device_hydrant_record', 3, 20, 5, 16, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (17, 1, '0/30 * * * * ? *', 'device_video', 3, 21, 5, 17, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'id', '', 1, NULL);
INSERT INTO `t_sync_task_info` VALUES (18, 1, '0/30 * * * * ? *', 'device_info', 3, 22, 5, 18, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (19, 1, '0/30 * * * * ? *', 'daily_check_line_info', 3, 23, 5, 19, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (20, 1, '0/30 * * * * ? *', 'device_warning_deal_log', 3, 24, 5, 20, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (21, 1, '0/30 * * * * ? *', 'daily_check_detail', 3, 25, 5, 21, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);
INSERT INTO `t_sync_task_info` VALUES (22, 1, '0/30 * * * * ? *', 'device_electricity_record', 3, 26, 5, 22, 0, 0, '2021-04-12 17:41:00', '2021-04-12 17:41:30', 'create_time', '2021-04-12 17:41:01', 2, NULL);

-- ----------------------------
-- Table structure for t_sync_task_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sync_task_log`;
CREATE TABLE `t_sync_task_log`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `task_id` int NOT NULL,
  `trigger_time` datetime(0) NULL DEFAULT NULL,
  `handle_time` int NULL DEFAULT NULL COMMENT '单位毫秒(ms)',
  `handle_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理状态 succsss/fail',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sync_task_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_sync_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sync_user`;
CREATE TABLE `t_sync_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `role` tinyint(1) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `i_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sync_user
-- ----------------------------
INSERT INTO `t_sync_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);

-- ----------------------------
-- Table structure for t_sync_write_config
-- ----------------------------
DROP TABLE IF EXISTS `t_sync_write_config`;
CREATE TABLE `t_sync_write_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `table` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名称',
  `write_mode` varchar(9) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'insert' COMMENT '写入模式',
  `before_sql` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '前置执行SQL',
  `exec_sql` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '执行SQL',
  `post_sql` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '后置执行SQL',
  `mapping_json` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '字段映射JSON',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sync_write_config
-- ----------------------------
INSERT INTO `t_sync_write_config` VALUES (1, 't_judge_report', 'insert', NULL, NULL, NULL, NULL, '2021-04-09 11:17:23');
INSERT INTO `t_sync_write_config` VALUES (2, 'daily_check_term_info', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (3, 'daily_check_point_info', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (4, 'device_hydraulic_record', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (5, 'device_together_set_detail', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (6, 'daily_check_line_user_rel', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (7, 'daily_check_user_rel', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (8, 'device_type_info', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (9, 'device_manhole_cover_record', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (10, 'daily_check_term_point_rel', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (12, 'daily_check_info', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (13, 'daily_check_line_point_rel', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:53');
INSERT INTO `t_sync_write_config` VALUES (14, 'device_together_set', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');
INSERT INTO `t_sync_write_config` VALUES (15, 'device_warning_log', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');
INSERT INTO `t_sync_write_config` VALUES (16, 'device_hydrant_record', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');
INSERT INTO `t_sync_write_config` VALUES (17, 'device_video', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');
INSERT INTO `t_sync_write_config` VALUES (18, 'device_info', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');
INSERT INTO `t_sync_write_config` VALUES (19, 'daily_check_line_info', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');
INSERT INTO `t_sync_write_config` VALUES (20, 'device_warning_deal_log', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');
INSERT INTO `t_sync_write_config` VALUES (21, 'daily_check_detail', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');
INSERT INTO `t_sync_write_config` VALUES (22, 'device_electricity_record', 'insert', NULL, NULL, NULL, NULL, '2021-04-12 14:19:54');

SET FOREIGN_KEY_CHECKS = 1;
