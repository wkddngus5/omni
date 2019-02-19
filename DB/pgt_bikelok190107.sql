/*
Navicat MySQL Data Transfer

Source Server         : 
Source Server Version : 50641
Source Host           : -new.cvupgnbyv2sa.us-east-2.rds.amazonaws.com:6612
Source Database       : 

Target Server Type    : MYSQL
Target Server Version : 50641
File Encoding         : 65001

Date: 2018-12-13 15:23:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `industryId` int(11) NOT NULL DEFAULT '1' COMMENT '管理员所属产业',
  `register_date` datetime DEFAULT NULL COMMENT '添加日期',
  `login_date` datetime DEFAULT NULL COMMENT '登陆日期',
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市ID',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `is_admin` int(1) NOT NULL DEFAULT '0' COMMENT '超级管理员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_admin_function
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_function`;
CREATE TABLE `t_admin_function` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `parent_id` int(5) DEFAULT '0' COMMENT '上级功能ID',
  `name` varchar(255) DEFAULT NULL COMMENT '功能名称',
  `findex` int(4) DEFAULT NULL COMMENT '排序索引',
  `href` varchar(200) DEFAULT NULL COMMENT '功能链接',
  `ref` varchar(50) DEFAULT NULL COMMENT '功能标记',
  `idparams` int(1) DEFAULT '0' COMMENT '是否有ID参数',
  `external` int(1) DEFAULT '0',
  `action_class` varchar(20) DEFAULT NULL COMMENT '功能类型',
  `action_target` varchar(20) DEFAULT NULL,
  `action_title` varchar(100) DEFAULT NULL,
  `others_tag` varchar(255) DEFAULT NULL COMMENT '其他标签',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_admin_group
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_group`;
CREATE TABLE `t_admin_group` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL COMMENT '管理员组备注',
  `date` datetime DEFAULT NULL,
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_admin_group_function
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_group_function`;
CREATE TABLE `t_admin_group_function` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL COMMENT '管理员组ID',
  `function_id` int(11) DEFAULT NULL COMMENT '权限ID',
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=569 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_admin_group_mapping
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_group_mapping`;
CREATE TABLE `t_admin_group_mapping` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `user_id` int(5) DEFAULT NULL,
  `group_id` int(5) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_admin_log
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_log`;
CREATE TABLE `t_admin_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `admin_id` int(5) DEFAULT NULL,
  `func_id` int(5) DEFAULT NULL COMMENT '操作权限ID',
  `data_id` varchar(200) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=739 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` bigint(15) DEFAULT NULL COMMENT '用户手机号（登录账号）',
  `password` varchar(100) NOT NULL DEFAULT 'E10ADC3949BA59ABBE56E057F20F883E' COMMENT '用户密码',
  `nickname` varchar(100) NOT NULL DEFAULT '' COMMENT '昵称',
  `industry_id` int(11) NOT NULL COMMENT '关联产业ID',
  `head_url` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `money` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '用户余额(单位：分)',
  `gift_money` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '赠送余额',
  `register_date` datetime DEFAULT NULL COMMENT '注册时间',
  `login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
  `auth_status` int(1) NOT NULL DEFAULT '1' COMMENT '认证进度 0：未认证 1：手机 2：实名 3：押金 4：银行卡  5:申请押金退还中（不能使用单车）6:已退押金（不能使用单车) 7:关闭用户使用单车功能 8：停用账户',
  `invite_code` varchar(6) DEFAULT NULL COMMENT '邀请码',
  `country_code` varchar(20) DEFAULT NULL COMMENT '国家编码',
  `credit_score` int(10) NOT NULL DEFAULT '100' COMMENT '信用积分',
  `city_id` int(11) NOT NULL DEFAULT '1' COMMENT '所在城市ID',
  `country_id` int(11) DEFAULT '49' COMMENT '国家ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`,`industry_id`) USING HASH,
  KEY `industry_id` (`industry_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_app_version
-- ----------------------------
DROP TABLE IF EXISTS `t_app_version`;
CREATE TABLE `t_app_version` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `version_name` varchar(10) DEFAULT NULL,
  `version_code` int(5) DEFAULT NULL,
  `type` int(1) DEFAULT '1' COMMENT '1:安卓 2：苹果（预留）',
  `url` varchar(255) DEFAULT NULL COMMENT '版本路径',
  `content` varchar(255) DEFAULT NULL COMMENT '更新内容',
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_area
-- ----------------------------
DROP TABLE IF EXISTS `t_area`;
CREATE TABLE `t_area` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `note` varchar(50) NOT NULL COMMENT '区间备注',
  `name` varchar(50) NOT NULL COMMENT '区间名',
  `detail` varchar(255) NOT NULL COMMENT '区间详情，点集合',
  `lat` double DEFAULT NULL COMMENT '中心点经纬度',
  `lng` double DEFAULT NULL COMMENT '中心点经度',
  `type` int(1) NOT NULL DEFAULT '1' COMMENT '区域类型 1：停车区域 2：禁停区域 3:强制停车区域',
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市Id',
  `date` datetime DEFAULT NULL,
  `ride_start_count` int(9) DEFAULT '0',
  `ride_end_count` int(9) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike
-- ----------------------------
DROP TABLE IF EXISTS `t_bike`;
CREATE TABLE `t_bike` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` bigint(11) NOT NULL COMMENT '单车编号',
  `imei` varchar(20) NOT NULL COMMENT '单车通讯IMEI号',
  `type_id` int(11) NOT NULL COMMENT '单车类型ID',
  `g_time` bigint(20) DEFAULT NULL COMMENT 'GPS定位坐标插入时间',
  `g_lat` double DEFAULT NULL COMMENT 'GPS定位坐标纬度',
  `g_lng` double DEFAULT NULL COMMENT 'GPS定位坐标经度',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '设备状态：0-关 1-开',
  `use_status` int(11) NOT NULL DEFAULT '0' COMMENT '使用状态，0-自由，1-使用，2-预约，3-正在解锁',
  `power` smallint(4) DEFAULT NULL COMMENT '电量',
  `gsm` tinyint(4) DEFAULT NULL COMMENT 'GPS信号值',
  `gps_number` tinyint(4) DEFAULT NULL COMMENT 'GPS卫星数',
  `city_id` int(11) NOT NULL COMMENT '投放城市ID',
  `heart_time` bigint(20) DEFAULT NULL COMMENT '上一次心跳检查时间',
  `version` varchar(20) DEFAULT NULL COMMENT '当前版本号',
  `version_time` varchar(20) DEFAULT NULL COMMENT '当前版本生成时间',
  `readpack` int(1) DEFAULT '0' COMMENT '红包单车： 1：是 0：否',
  `add_date` datetime DEFAULT NULL COMMENT '设备添加时间',
  `error_status` int(1) NOT NULL DEFAULT '0' COMMENT '故障状态  0：无故障 1：手动标记故障 2：自动标记故障 3:已报废 4:待激活',
  `server_ip` varchar(50) DEFAULT '' COMMENT '服务器地址',
  `bike_status` int(1) NOT NULL DEFAULT '1' COMMENT '车状态 1：正常 2：倒地',
  `sponsors_id` int(9) DEFAULT NULL COMMENT '赞助商',
  `bike_no` varchar(20) DEFAULT NULL COMMENT '单车编号',
  `bike_type` int(1) DEFAULT '1',
  `extend_info` varchar(1000) DEFAULT NULL COMMENT '扩展信息，json',
  `area_id` int(8) DEFAULT NULL COMMENT '车辆所在区域',
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`number`) USING BTREE,
  KEY `type_id` (`type_id`),
  KEY `city_id` (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_ble
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_ble`;
CREATE TABLE `t_bike_ble` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bid` int(11) NOT NULL,
  `mac` varchar(50) NOT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_card_phone
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_card_phone`;
CREATE TABLE `t_bike_card_phone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `iccid` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_error
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_error`;
CREATE TABLE `t_bike_error` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bnumber` bigint(11) NOT NULL COMMENT '单车编号',
  `uid` int(11) NOT NULL COMMENT '报告人ID',
  `type` int(1) NOT NULL COMMENT '报告类型 1：不能开锁 2：故障 3：违章 4:关锁后未结费 5.忘记关锁 6:被偷的车 7:被破坏的车 8：无人认领的车 9：其他问题',
  `error_type` varchar(50) DEFAULT '0' COMMENT '故障类型 ',
  `content` varchar(255) DEFAULT NULL COMMENT '报告描述',
  `image_ids` varchar(1000) DEFAULT NULL COMMENT '故障图片',
  `date` datetime DEFAULT NULL COMMENT '报告日期',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '处理状态 0：待审核 1：正在审核 2：通过审核 3：未通过审核 4:已自动审核(适用于关锁未结费或不能开锁故障）',
  `bike_useid` int(11) DEFAULT NULL COMMENT '骑行记录ID',
  `review_note` varchar(255) DEFAULT NULL,
  `review_date` datetime DEFAULT NULL COMMENT '审核时间',
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bnumber` (`bnumber`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_hold
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_hold`;
CREATE TABLE `t_bike_hold` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prev_use_id` int(11) NOT NULL,
  `next_use_id` int(11) DEFAULT NULL,
  `prev_bike_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `start_time` datetime NOT NULL,
  `expiration_time` datetime NOT NULL,
  `group_id` varchar(255) NOT NULL DEFAULT '',
  `trade_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_long_lease
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_long_lease`;
CREATE TABLE `t_bike_long_lease` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `start_time` bigint(20) NOT NULL,
  `end_time` bigint(20) NOT NULL,
  `ispay` int(1) NOT NULL DEFAULT '0' COMMENT '是否已支付 0：未支付 1：已支付 2:免费',
  PRIMARY KEY (`id`),
  KEY `fk_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_maintain
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_maintain`;
CREATE TABLE `t_bike_maintain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bid` int(11) NOT NULL,
  `type` int(2) NOT NULL COMMENT '维护类型 1：移动 2：维修',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态 0：待操作 1：已完成',
  `date` datetime DEFAULT NULL COMMENT '添加时间',
  `admin_id` int(11) NOT NULL COMMENT '处理管理员ID',
  `deal_date` datetime DEFAULT NULL COMMENT '处理时间',
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_order
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_order`;
CREATE TABLE `t_bike_order` (
  `id` int(11) NOT NULL,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_order_record
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_order_record`;
CREATE TABLE `t_bike_order_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imei` bigint(20) NOT NULL,
  `order_id` int(11) NOT NULL,
  `time` bigint(20) NOT NULL,
  `content` varchar(250) NOT NULL,
  `from` int(1) NOT NULL DEFAULT '0' COMMENT 'record from 0:gprs 1:ble',
  PRIMARY KEY (`id`),
  KEY `imei` (`imei`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=48700 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_reserve
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_reserve`;
CREATE TABLE `t_bike_reserve` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '单车预约记录ID',
  `bid` int(11) NOT NULL COMMENT '单车ID',
  `uid` int(11) NOT NULL COMMENT '用户ID',
  `date` bigint(20) NOT NULL COMMENT '预约时间',
  `out_date` bigint(20) NOT NULL COMMENT '过期时间',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '预约状态 0：待解锁 1：已解锁 2：已解其他锁 3：已取消 4：已超时',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `bid` (`bid`)
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_sim
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_sim`;
CREATE TABLE `t_bike_sim` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bid` int(11) DEFAULT NULL COMMENT '设备ID',
  `cid` int(11) DEFAULT NULL COMMENT '电话卡ID',
  `imei` bigint(20) NOT NULL,
  `iccid` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_type
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_type`;
CREATE TABLE `t_bike_type` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `price` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '单车单价(单位：元)',
  `unit_type` int(1) NOT NULL DEFAULT '1' COMMENT '单位 1：分钟 2：小时 3：月份',
  `count` int(10) NOT NULL DEFAULT '0' COMMENT '数量',
  `industry_id` int(11) NOT NULL COMMENT '产业ID',
  `city_id` int(11) NOT NULL DEFAULT '1' COMMENT '所在城市ID',
  `lock_type` int(1) NOT NULL DEFAULT '3' COMMENT '锁类型 1：GPRS(默认) 2：BLE 3：GPRS+BLE 4：GPRS+BLE+SMS',
  `note` varchar(255) DEFAULT NULL,
  `type` int(1) NOT NULL DEFAULT '1' COMMENT '押金类型',
  `hold_price` decimal(5,2) DEFAULT '0.00',
  `hold_unit_type` int(1) DEFAULT '1',
  `hold_count` int(10) DEFAULT '0',
  `hold_max_count` int(10) DEFAULT '0',
  `normal_pause_time` int(5) NOT NULL DEFAULT '120' COMMENT '正常停车时间',
  `outarea_pause_time` int(5) NOT NULL DEFAULT '60' COMMENT '越界暂停时间',
  `unlock_price` decimal(10,2) DEFAULT '0.00' COMMENT 'unlock price',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_type_price
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_type_price`;
CREATE TABLE `t_bike_type_price` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `type_id` int(5) DEFAULT NULL,
  `price` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '单车单价(单位：元)',
  `min_count` int(10) NOT NULL DEFAULT '0' COMMENT '最小数量',
  `max_count` int(10) NOT NULL DEFAULT '0' COMMENT '最大数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_use
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_use`;
CREATE TABLE `t_bike_use` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '用户使用记录ID',
  `uid` int(11) NOT NULL COMMENT '用户ID',
  `bid` int(11) NOT NULL COMMENT '单车ID',
  `start_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '开始时间',
  `end_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '结束时间',
  `duration` bigint(20) DEFAULT '0' COMMENT '运动时长',
  `distance` float NOT NULL DEFAULT '0' COMMENT '骑行距离',
  `orbit` text COMMENT '骑行轨迹',
  `start_lat` double NOT NULL DEFAULT '0' COMMENT '开始纬度（APP用户）',
  `start_lng` double NOT NULL DEFAULT '0' COMMENT '开始经度（APP用户）',
  `end_lat` double NOT NULL DEFAULT '0' COMMENT '结束纬度（锁设备）',
  `end_lng` double NOT NULL DEFAULT '0' COMMENT '结束经度（锁设备）',
  `ispay` int(11) NOT NULL DEFAULT '0' COMMENT '是否已支付 0：未支付 1：已支付 2:免费',
  `date` bigint(20) DEFAULT NULL COMMENT '请求时间',
  `lock_location` int(1) NOT NULL DEFAULT '0' COMMENT '锁位置是否上报（上锁）',
  `out_area` int(1) NOT NULL DEFAULT '0' COMMENT '骑行越界',
  `open_way` int(1) NOT NULL DEFAULT '0' COMMENT 'open lock way 0:gprs 1:ble(app) 2:small program',
  `ride_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '骑行金额',
  `ride_status` int(1) NOT NULL DEFAULT '0' COMMENT '状态:-2:重新开锁中 -1：待授权 0：开锁中 1：使用中 2：已结束 3：已支付',
  `old_date` varchar(255) DEFAULT '' COMMENT '旧时间戳',
  `close_way` int(1) NOT NULL DEFAULT '0' COMMENT 'close lock way 0:gprs 1:ble(app) 2:admin 3:auto',
  `old_duration` int(11) DEFAULT '0' COMMENT '旧骑行时长，如果管理员手动结费',
  `admin_id` int(11) DEFAULT '0' COMMENT '操作管理员',
  `update_time` bigint(13) DEFAULT '0' COMMENT 'record update time',
  `lock_time` bigint(13) DEFAULT NULL,
  `host_id` bigint(11) NOT NULL DEFAULT '0',
  `ride_user` varchar(255) DEFAULT '',
  `group_ride` int(1) DEFAULT '0',
  `start_area` int(9) DEFAULT '0',
  `end_area` int(9) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_uid` (`uid`),
  KEY `fk_bid` (`bid`),
  CONSTRAINT `t_bike_use_ibfk_1` FOREIGN KEY (`bid`) REFERENCES `t_bike` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=465 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_bike_warn
-- ----------------------------
DROP TABLE IF EXISTS `t_bike_warn`;
CREATE TABLE `t_bike_warn` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imei` bigint(20) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_cash_record
-- ----------------------------
DROP TABLE IF EXISTS `t_cash_record`;
CREATE TABLE `t_cash_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `amount_type` int(1) DEFAULT NULL COMMENT '1:红包 2：余额',
  `type` int(1) NOT NULL COMMENT '1:微信 2:支付宝',
  `order_id` varchar(100) DEFAULT NULL COMMENT '第三方提现订单ID',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '0:待处理,1:已退款,2:已退款[自动],3:退款失败,4:退款失败[自动]',
  `date` datetime DEFAULT NULL,
  `refund_amount` decimal(10,2) DEFAULT '0.00' COMMENT '实际退款金额',
  `refund_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_city
-- ----------------------------
DROP TABLE IF EXISTS `t_city`;
CREATE TABLE `t_city` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '城市名',
  `note` varchar(50) DEFAULT NULL,
  `code` int(11) DEFAULT NULL COMMENT '城市邮编',
  `area_detail` varchar(255) DEFAULT NULL COMMENT '城市区域边界详情',
  `area_lat` double DEFAULT NULL COMMENT '城市区域边界中心点',
  `area_lng` double DEFAULT NULL COMMENT '城市区域边界中心点',
  `currency` varchar(50) DEFAULT '' COMMENT '消费币种',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_country
-- ----------------------------
DROP TABLE IF EXISTS `t_country`;
CREATE TABLE `t_country` (
  `country_code` varchar(20) DEFAULT NULL COMMENT '国家代码',
  `english_name` varchar(50) DEFAULT NULL COMMENT '国名英文',
  `chinese_name` varchar(50) DEFAULT NULL COMMENT '国名中文',
  `local_name` varchar(50) DEFAULT NULL COMMENT '国名当地',
  `location` varchar(20) DEFAULT NULL COMMENT '所在洲',
  `local_language` varchar(20) DEFAULT NULL COMMENT '官方语言',
  `local_dialect` varchar(50) DEFAULT NULL COMMENT '本地方言',
  `phone_code` varchar(20) NOT NULL DEFAULT '' COMMENT '电话区号',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=247 DEFAULT CHARSET=utf8 COMMENT='国家';

-- ----------------------------
-- Table structure for t_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon`;
CREATE TABLE `t_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '优惠券名称',
  `value` decimal(10,2) DEFAULT NULL COMMENT '优惠比例(单位：元)',
  `day` int(5) DEFAULT '0' COMMENT '优惠天数',
  `type` int(1) NOT NULL COMMENT '优惠类型 1：折扣 2：抵扣 3:注册充值免费骑 4:限时免费',
  `isrepeat` int(1) DEFAULT '0' COMMENT '是否重复使用',
  `start_time` bigint(20) DEFAULT NULL COMMENT '优惠开始时间',
  `end_time` bigint(20) DEFAULT NULL COMMENT '结束时间',
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_deposit_conf
-- ----------------------------
DROP TABLE IF EXISTS `t_deposit_conf`;
CREATE TABLE `t_deposit_conf` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL COMMENT '押金金额',
  `return_min_day` int(2) NOT NULL DEFAULT '5' COMMENT '押金退款最短时间',
  `return_max_day` int(2) NOT NULL DEFAULT '7' COMMENT '押金退款最长时间',
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市ID',
  `date` datetime NOT NULL COMMENT '编辑时间',
  `automatic_refund` int(1) NOT NULL DEFAULT '1' COMMENT '是否自动退款',
  `type_id` int(5) NOT NULL DEFAULT '1' COMMENT '押金类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_deposit_return
-- ----------------------------
DROP TABLE IF EXISTS `t_deposit_return`;
CREATE TABLE `t_deposit_return` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL COMMENT '押金退还申请者ID',
  `trade_id` int(11) DEFAULT '0' COMMENT '当前订单ID',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '处理状态 0:待处理 1：已退还押金 2：已退还押金(自动) 3:退款失败 4:退款失败（自动）',
  `date` datetime DEFAULT NULL COMMENT '申请时间',
  `out_refund_no` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '外部退款号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_email
-- ----------------------------
DROP TABLE IF EXISTS `t_email`;
CREATE TABLE `t_email` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(50) NOT NULL,
  `receiver` varchar(50) NOT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `content` text,
  `status` int(1) DEFAULT '0' COMMENT '0:send fail 1:send success 2:used',
  `date` bigint(20) DEFAULT NULL,
  `code` int(6) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_image
-- ----------------------------
DROP TABLE IF EXISTS `t_image`;
CREATE TABLE `t_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) NOT NULL COMMENT '图片路径',
  `width` double DEFAULT NULL COMMENT '图片宽度',
  `height` double DEFAULT NULL COMMENT '图片高度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_industry
-- ----------------------------
DROP TABLE IF EXISTS `t_industry`;
CREATE TABLE `t_industry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产业ID',
  `industry_name` varchar(50) DEFAULT NULL COMMENT '产业名称',
  `consume_type` int(1) DEFAULT '1' COMMENT '消费类型 1：余额 2：第三方 3：余额加第三方',
  `currency` varchar(50) NOT NULL DEFAULT '' COMMENT '消费币种',
  `deposit` decimal(10,2) DEFAULT '0.00' COMMENT '押金金额',
  `country_code` varchar(20) NOT NULL COMMENT '国家代码',
  `area_code` varchar(50) DEFAULT NULL COMMENT '国家区号',
  `register_auth_num` int(1) NOT NULL DEFAULT '3' COMMENT '注册认证步骤数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_membership_plan
-- ----------------------------
DROP TABLE IF EXISTS `t_membership_plan`;
CREATE TABLE `t_membership_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_id` int(11) NOT NULL DEFAULT '1',
  `interval` int(10) NOT NULL,
  `interval_count` int(11) NOT NULL,
  `plan_price` decimal(5,2) NOT NULL DEFAULT '0.00',
  `ride_unit` int(11) NOT NULL DEFAULT '0' COMMENT 'MINUTE = 0; HOUR = 1; WEEK = 2; MONTH = 3; YEAR = 4;',
  `ride_free_unit_count` int(10) NOT NULL DEFAULT '0',
  `title` text NOT NULL,
  `description` text NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `stripe_id` varchar(100) DEFAULT NULL,
  `is_renewable` int(1) NOT NULL DEFAULT '1',
  `is_education` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_message_box
-- ----------------------------
DROP TABLE IF EXISTS `t_message_box`;
CREATE TABLE `t_message_box` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_type` varchar(10) DEFAULT NULL COMMENT '消息来源: admin后台 user 用户',
  `admin_id` int(11) NOT NULL DEFAULT '0' COMMENT '管理员ID,0为系统消息',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `message_type` varchar(20) DEFAULT 'text' COMMENT '消息类型',
  `title` varchar(50) DEFAULT NULL COMMENT '消息标题',
  `content` varchar(200) DEFAULT NULL COMMENT '消息内容',
  `date` datetime DEFAULT NULL COMMENT '消息生成时间',
  `isread` int(1) NOT NULL DEFAULT '0' COMMENT '是否已读',
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市ID',
  `reply` int(1) NOT NULL DEFAULT '0' COMMENT '是否已回复',
  `update_date` datetime DEFAULT NULL COMMENT '消息更新时间',
  `status` int(1) DEFAULT '0' COMMENT '消息状态 -1：已删除 0：正常 1：收藏',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_message_box_reply
-- ----------------------------
DROP TABLE IF EXISTS `t_message_box_reply`;
CREATE TABLE `t_message_box_reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msgbox_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '消息主题ID',
  `from_type` varchar(10) DEFAULT NULL COMMENT '消息来源: admin后台 user 用户',
  `admin_id` int(11) DEFAULT '0' COMMENT '管理员ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `message_type` varchar(20) NOT NULL DEFAULT 'text' COMMENT '消息类型',
  `content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  `date` datetime DEFAULT NULL,
  `isread` int(1) NOT NULL DEFAULT '0' COMMENT '是否已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_money_record
-- ----------------------------
DROP TABLE IF EXISTS `t_money_record`;
CREATE TABLE `t_money_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `type` int(2) NOT NULL COMMENT '余额：1:消费 2:充值；红包： 3：红包 4：提现；',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `record_id` int(11) DEFAULT NULL COMMENT '关联业务记录ID',
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_news
-- ----------------------------
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL COMMENT '资讯标题',
  `thumb` varchar(255) DEFAULT NULL COMMENT '封面',
  `content` text COMMENT '资讯内容',
  `date` datetime DEFAULT NULL,
  `start_time` int(20) DEFAULT NULL COMMENT '开始时间',
  `end_time` int(20) DEFAULT NULL COMMENT '结束时间',
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市ID',
  `type` int(1) DEFAULT '0' COMMENT '0:content 1:link',
  `link` varchar(100) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_notification_config
-- ----------------------------
DROP TABLE IF EXISTS `t_notification_config`;
CREATE TABLE `t_notification_config` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `type` int(2) DEFAULT NULL COMMENT '通知类型  1：故障反馈 2：报警  3：心跳异常 4：定位异常 5：移动速度异常 6:低电量 7：退押金 8:退余额 9:异常骑行',
  `template` varchar(255) DEFAULT NULL COMMENT '通知模板内容',
  `nf_sms` int(1) NOT NULL DEFAULT '0' COMMENT '短信通知',
  `nf_email` int(1) NOT NULL DEFAULT '1' COMMENT '邮件通知',
  `other_config` varchar(255) DEFAULT NULL COMMENT 'Others Configuration（JSON）',
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_notification_mapping
-- ----------------------------
DROP TABLE IF EXISTS `t_notification_mapping`;
CREATE TABLE `t_notification_mapping` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `group_id` int(5) DEFAULT NULL,
  `notify_config_type` int(3) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_recharge_amount
-- ----------------------------
DROP TABLE IF EXISTS `t_recharge_amount`;
CREATE TABLE `t_recharge_amount` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL COMMENT '充值金额',
  `gift` decimal(10,2) DEFAULT '0.00' COMMENT '赠送金额',
  `date` datetime DEFAULT NULL,
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市ID',
  `gift_type` int(1) NOT NULL DEFAULT '1' COMMENT '赠送类型 1：余额 2：优惠券',
  `gift_id` int(11) NOT NULL DEFAULT '0' COMMENT '赠送关联ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_red_package_bike
-- ----------------------------
DROP TABLE IF EXISTS `t_red_package_bike`;
CREATE TABLE `t_red_package_bike` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bid` int(11) NOT NULL COMMENT '红包被绑定单车ID',
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '红包领取用户ID',
  `rule_id` int(11) NOT NULL COMMENT '规则ID',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '红包金额',
  `start_time` int(20) NOT NULL COMMENT '红包开始时间',
  `end_time` int(20) NOT NULL COMMENT '红包结束时间',
  `date` datetime DEFAULT NULL COMMENT '红包生成时间',
  `user_date` datetime DEFAULT NULL COMMENT '红包领取时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_red_package_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_red_package_rule`;
CREATE TABLE `t_red_package_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `free_use_time` int(20) DEFAULT NULL COMMENT '骑行免费时间',
  `least_use_time` int(20) DEFAULT NULL COMMENT '最低骑行时长',
  `max_amount` decimal(10,2) DEFAULT NULL COMMENT '最高金额',
  `start_time` int(20) DEFAULT NULL COMMENT '过期时长',
  `end_time` int(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `type` int(1) NOT NULL DEFAULT '1' COMMENT '红包规则类型：1：现金 2：优惠券',
  `coupon_id` int(11) DEFAULT NULL COMMENT '优惠券ID',
  `coupon_num` int(2) DEFAULT '0' COMMENT '优惠券数',
  `area_ids` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '区域ID组合',
  `must_in_area` int(1) DEFAULT '0' COMMENT '是否必须骑行至目标区域',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_region
-- ----------------------------
DROP TABLE IF EXISTS `t_region`;
CREATE TABLE `t_region` (
  `code` varchar(20) NOT NULL COMMENT '区位码',
  `country_code` varchar(20) DEFAULT NULL COMMENT '国家代码',
  `region_name_e` varchar(80) DEFAULT NULL COMMENT '地区名称英文',
  `region_name_c` varchar(50) DEFAULT NULL COMMENT '地区名称中文',
  `level` varchar(20) DEFAULT NULL COMMENT '级别',
  `upper_region` varchar(20) DEFAULT NULL COMMENT '上级地区',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地区';

-- ----------------------------
-- Table structure for t_request_warn
-- ----------------------------
DROP TABLE IF EXISTS `t_request_warn`;
CREATE TABLE `t_request_warn` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) NOT NULL,
  `uid` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_rfid_card
-- ----------------------------
DROP TABLE IF EXISTS `t_rfid_card`;
CREATE TABLE `t_rfid_card` (
  `id` varchar(50) NOT NULL,
  `card_id` varchar(50) NOT NULL,
  `card_no` varchar(50) NOT NULL,
  `uid` int(9) DEFAULT '0',
  `status` int(1) DEFAULT '0' COMMENT '0:未激活，1:激活，2:冻结',
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_card_id` (`card_id`) USING HASH,
  UNIQUE KEY `uk_card_no` (`card_no`) USING HASH,
  UNIQUE KEY `uk_id` (`id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_ride_rating
-- ----------------------------
DROP TABLE IF EXISTS `t_ride_rating`;
CREATE TABLE `t_ride_rating` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `ride_id` bigint(11) DEFAULT NULL,
  `star` int(1) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `t_ride_rating_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sms
-- ----------------------------
DROP TABLE IF EXISTS `t_sms`;
CREATE TABLE `t_sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` bigint(15) NOT NULL,
  `code` varchar(10) DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `type` int(1) NOT NULL COMMENT '短信类型 1:验证类 2：服务通知类 3:接收短信',
  `date` bigint(20) DEFAULT NULL COMMENT '发送时间',
  `used` int(1) NOT NULL DEFAULT '0' COMMENT '是否已使用 0：未使用 1：已使用',
  `area_code` varchar(20) DEFAULT NULL COMMENT '手机号区域代码',
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_template`;
CREATE TABLE `t_sms_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `industry_id` int(11) NOT NULL,
  `type` int(2) DEFAULT NULL COMMENT '短信类型 1:验证类 2：服务通知类',
  `template` text NOT NULL COMMENT '模板',
  `date` datetime DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sponsors
-- ----------------------------
DROP TABLE IF EXISTS `t_sponsors`;
CREATE TABLE `t_sponsors` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `bike_type` varchar(50) DEFAULT NULL,
  `city_id` int(5) NOT NULL DEFAULT '1' COMMENT '城市ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_system_config
-- ----------------------------
DROP TABLE IF EXISTS `t_system_config`;
CREATE TABLE `t_system_config` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '配置名称',
  `config` varchar(255) NOT NULL COMMENT '配置内容 json data',
  `date` datetime DEFAULT NULL,
  `cf_key` varchar(50) NOT NULL DEFAULT '' COMMENT '配置键',
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_third_user
-- ----------------------------
DROP TABLE IF EXISTS `t_third_user`;
CREATE TABLE `t_third_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL COMMENT '用户表ID',
  `uuid` varchar(200) NOT NULL COMMENT '第三方平台用户ID',
  `type` int(1) DEFAULT NULL COMMENT '1:微信 2:QQ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_trade
-- ----------------------------
DROP TABLE IF EXISTS `t_trade`;
CREATE TABLE `t_trade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `record_id` varchar(50) DEFAULT NULL COMMENT '消费ID，外键使用ID',
  `status` int(1) DEFAULT '0' COMMENT '支付状态 0：待支付 1：支付成功 2:交易关闭 3;已部分退款 4:已全额退款',
  `way` int(1) DEFAULT NULL COMMENT '支付方式 1:账户余额 2：微信 3：支付宝 4:PayPay 5:VISA 6:Strip 7:长租免费 8:优惠券 9:红包支付 10:Anet 11:AcquiroPay 12:PayU 13:微信公众号支付',
  `date` datetime DEFAULT NULL COMMENT '订单日期',
  `amount` decimal(20,2) DEFAULT NULL COMMENT '金额',
  `type` int(1) DEFAULT NULL COMMENT '支付类型 1：消费 2：充值 3:押金 4：长租 5:提现',
  `uid` int(11) NOT NULL COMMENT '用户ID',
  `out_pay_id` int(11) DEFAULT NULL COMMENT '关联支付ID（优惠券，长租）',
  `notify` int(1) NOT NULL DEFAULT '0' COMMENT '是否已通知： 0：未通知 1：已通知',
  `out_trade_no` varchar(100) DEFAULT NULL COMMENT '第三方支付订单号',
  `balance` decimal(20,2) DEFAULT NULL COMMENT '余额',
  `city_id` int(11) NOT NULL DEFAULT '1' COMMENT '所在城市ID',
  `account_pay_amount` decimal(10,2) DEFAULT '0.00',
  `gift_pay_amount` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=406 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_trade_receipt
-- ----------------------------
DROP TABLE IF EXISTS `t_trade_receipt`;
CREATE TABLE `t_trade_receipt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `trade_ids` varchar(255) DEFAULT NULL COMMENT '订单ID，‘，’分割',
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态 0：待处理 1：已处理 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for t_user_address
-- ----------------------------
DROP TABLE IF EXISTS `t_user_address`;
CREATE TABLE `t_user_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `title` varchar(255) NOT NULL COMMENT '地址标题',
  `detail` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `lan` double DEFAULT NULL COMMENT '地址经纬度',
  `lng` double DEFAULT NULL COMMENT '地址经纬度',
  `is_default` int(1) NOT NULL DEFAULT '0' COMMENT '是否默认',
  PRIMARY KEY (`id`),
  KEY `fk_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_user_auth`;
CREATE TABLE `t_user_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `status` int(1) DEFAULT '0' COMMENT '认证进度 0：未认证 1：手机 2：实名 3：押金 4：银行卡',
  PRIMARY KEY (`id`),
  KEY `fk_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_card
-- ----------------------------
DROP TABLE IF EXISTS `t_user_card`;
CREATE TABLE `t_user_card` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `card_number` varchar(100) NOT NULL COMMENT '银行卡号',
  `exp_date` varchar(50) NOT NULL COMMENT '过期日期',
  `cvv` varchar(10) NOT NULL COMMENT '信用卡校验码',
  `name_on_card` varchar(255) NOT NULL COMMENT '卡主姓名',
  PRIMARY KEY (`id`),
  KEY `fk` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_user_coupon`;
CREATE TABLE `t_user_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `cid` int(11) NOT NULL COMMENT '优惠券ID',
  `code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '优惠券代码',
  `date` datetime DEFAULT NULL COMMENT '生成时间',
  `active_date` datetime DEFAULT NULL COMMENT '激活时间',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠券余额',
  `used` int(1) NOT NULL DEFAULT '0' COMMENT '是否已使用',
  `start_time` bigint(20) DEFAULT NULL COMMENT '优惠开始时间',
  `end_time` bigint(20) DEFAULT NULL COMMENT '优惠结束时间',
  `gift_from` int(1) NOT NULL DEFAULT '0' COMMENT '奖励类型 1：注册 2：邀请好友 3：故障上报 4：红包车',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_credit_score
-- ----------------------------
DROP TABLE IF EXISTS `t_user_credit_score`;
CREATE TABLE `t_user_credit_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL COMMENT '用户ID',
  `rule_type` int(2) DEFAULT NULL COMMENT '1:加分 2:减分 3:设为0',
  `data_type` int(2) DEFAULT NULL COMMENT '数据类型',
  `count` int(11) DEFAULT NULL COMMENT '积分数量',
  `date` datetime DEFAULT NULL COMMENT '获取时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=387 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_user_deposit
-- ----------------------------
DROP TABLE IF EXISTS `t_user_deposit`;
CREATE TABLE `t_user_deposit` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `deposit_id` int(5) NOT NULL COMMENT '押金ID',
  `count` int(3) NOT NULL COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `status` int(1) DEFAULT '0' COMMENT '状态 0:1:2/待支付:支付成功:已退押金',
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_user_detail`;
CREATE TABLE `t_user_detail` (
  `uid` int(11) NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `redpack` decimal(10,2) DEFAULT '0.00' COMMENT '未提现红包总额',
  `idcard` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `birthday` varchar(100) DEFAULT NULL COMMENT '生日',
  `gender` int(1) DEFAULT '0' COMMENT '性别 0：未知 1:男 2：女 3:其他',
  `third_info` varchar(255) DEFAULT NULL COMMENT '关联第三方信息（JSON）',
  `email_auth` int(1) NOT NULL DEFAULT '0' COMMENT 'email auth 0:no 1:usual 2:education',
  KEY `fk_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_device
-- ----------------------------
DROP TABLE IF EXISTS `t_user_device`;
CREATE TABLE `t_user_device` (
  `uid` int(11) NOT NULL,
  `type` int(1) DEFAULT NULL COMMENT '设备类型 1：Android 2：IOS',
  `token` varchar(255) DEFAULT NULL COMMENT '设备推送证书',
  `uuid` varchar(255) DEFAULT NULL COMMENT 'APP在设备中的唯一标记',
  `request_token` varchar(500) DEFAULT NULL COMMENT '请求列表',
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_invaite
-- ----------------------------
DROP TABLE IF EXISTS `t_user_invaite`;
CREATE TABLE `t_user_invaite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL COMMENT '被邀请人',
  `i_uid` int(11) NOT NULL COMMENT '邀请人',
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_uid` (`uid`),
  KEY `fk_iuid` (`i_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_membership
-- ----------------------------
DROP TABLE IF EXISTS `t_user_membership`;
CREATE TABLE `t_user_membership` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `membership_plan_id` int(11) NOT NULL,
  `interval` int(11) NOT NULL,
  `interval_count` int(11) NOT NULL,
  `canceled` int(1) NOT NULL DEFAULT '0',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `canceled_time` datetime DEFAULT NULL,
  `through_time` datetime DEFAULT NULL,
  `stripe_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_pause_parking_history
-- ----------------------------
DROP TABLE IF EXISTS `t_user_pause_parking_history`;
CREATE TABLE `t_user_pause_parking_history` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `ride_id` bigint(19) NOT NULL,
  `start_time` bigint(15) NOT NULL DEFAULT '0' COMMENT '开始时间',
  `end_time` bigint(15) NOT NULL DEFAULT '0' COMMENT '结束时间',
  `duration` int(9) DEFAULT NULL,
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '1:正常暂停 2：越界暂停',
  `date` datetime DEFAULT NULL,
  `pause_amount` decimal(10,2) DEFAULT '0.00' COMMENT '暂停费用',
  `status` int(1) DEFAULT '0' COMMENT '暂停状态 0：暂停中 1：已结束 2：重新开锁',
  `notify_time` bigint(15) NOT NULL DEFAULT '0' COMMENT '通知时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_pause_parking_time
-- ----------------------------
DROP TABLE IF EXISTS `t_user_pause_parking_time`;
CREATE TABLE `t_user_pause_parking_time` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `pause_time` int(9) DEFAULT NULL,
  `outarea_pause_time` int(9) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_read_package
-- ----------------------------
DROP TABLE IF EXISTS `t_user_read_package`;
CREATE TABLE `t_user_read_package` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `pk_id` int(11) NOT NULL COMMENT '红包ID',
  `amount` decimal(10,0) NOT NULL COMMENT '金额',
  `date` datetime DEFAULT NULL,
  `iscash` int(1) DEFAULT NULL COMMENT '是否提现 0：未体现 1：已提现',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_violation
-- ----------------------------
DROP TABLE IF EXISTS `t_violation`;
CREATE TABLE `t_violation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `useid` int(11) DEFAULT NULL,
  `type` int(1) DEFAULT NULL COMMENT '违规类型 1:违停一次 2;忘记关锁 3:加装私锁 4:车辆丢失',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_web_content
-- ----------------------------
DROP TABLE IF EXISTS `t_web_content`;
CREATE TABLE `t_web_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `industry_id` int(11) DEFAULT '0',
  `type` int(1) DEFAULT '0' COMMENT '1 用户协议 2：充值协议 3:找不到车 4：举报违停 5：押金说明 ,6红包攻略,7积分规则,8隐私条款 9 how to sigin 10使用条款 11 会员协议 12:邮箱激活',
  `content` longtext NOT NULL COMMENT '协议类型',
  `date` datetime DEFAULT NULL COMMENT '协议更新时间',
  `title` varchar(255) DEFAULT '' COMMENT '自定义标题，适用于扩展内容',
  `web_key` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


-- ----------------------------
-- View structure for view_admin_group_function
-- ----------------------------
DROP VIEW IF EXISTS `view_admin_group_function`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_admin_group_function` AS select `t_admin_group_function`.`group_id` AS `group_id`,`t_admin_function`.`parent_id` AS `parent_id`,`t_admin_group_function`.`function_id` AS `function_id`,`t_admin_function`.`name` AS `name`,`t_admin_function`.`findex` AS `findex`,`t_admin_function`.`href` AS `href`,`t_admin_function`.`ref` AS `ref`,`t_admin_function`.`idparams` AS `idparams`,`t_admin_function`.`action_class` AS `action_class`,`t_admin_function`.`action_target` AS `action_target`,`t_admin_function`.`action_title` AS `action_title`,`t_admin_function`.`id` AS `id`,`t_admin_function`.`external` AS `external`,`t_admin_function`.`others_tag` AS `others_tag` from (`t_admin_group_function` join `t_admin_function` on((`t_admin_function`.`id` = `t_admin_group_function`.`function_id`))) ;

-- ----------------------------
-- View structure for view_admin_log
-- ----------------------------
DROP VIEW IF EXISTS `view_admin_log`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_admin_log` AS select `t_admin`.`nickname` AS `nickname`,`t_admin_log`.`date` AS `date`,`t_admin_function`.`name` AS `name`,`t_admin_log`.`data_id` AS `data_id`,`t_admin`.`user_name` AS `user_name`,`t_admin_log`.`id` AS `id`,`t_admin_function`.`parent_id` AS `parent_id`,`t_admin_log`.`func_id` AS `func_id`,`t_admin`.`city_id` AS `city_id` from ((`t_admin_log` join `t_admin_function` on((`t_admin_function`.`id` = `t_admin_log`.`func_id`))) join `t_admin` on((`t_admin`.`id` = `t_admin_log`.`admin_id`))) ;

-- ----------------------------
-- View structure for view_admin_log_detail
-- ----------------------------
DROP VIEW IF EXISTS `view_admin_log_detail`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_admin_log_detail` AS select `view_admin_log`.`nickname` AS `nickname`,`view_admin_log`.`date` AS `date`,`view_admin_log`.`name` AS `name`,`view_admin_log`.`data_id` AS `data_id`,`view_admin_log`.`user_name` AS `user_name`,`view_admin_log`.`id` AS `id`,`view_admin_log`.`parent_id` AS `parent_id`,`t_admin_function`.`name` AS `parent_name`,`view_admin_log`.`func_id` AS `func_id`,`view_admin_log`.`city_id` AS `city_id` from (`view_admin_log` left join `t_admin_function` on((`t_admin_function`.`id` = `view_admin_log`.`parent_id`))) order by `view_admin_log`.`id` desc ;

-- ----------------------------
-- View structure for view_admin_notification
-- ----------------------------
DROP VIEW IF EXISTS `view_admin_notification`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_admin_notification` AS select `t_notification_config`.`template` AS `template`,`t_admin_group_mapping`.`user_id` AS `user_id`,`t_notification_mapping`.`notify_config_type` AS `type`,`t_admin_group_mapping`.`group_id` AS `group_id`,`t_admin`.`phone` AS `phone`,`t_admin`.`email` AS `email`,`t_admin`.`nickname` AS `nickname`,`t_notification_config`.`nf_sms` AS `nf_sms`,`t_notification_config`.`nf_email` AS `nf_email` from (((`t_notification_mapping` left join `t_admin_group_mapping` on((`t_notification_mapping`.`group_id` = `t_admin_group_mapping`.`group_id`))) left join `t_notification_config` on((`t_notification_config`.`type` = `t_notification_mapping`.`notify_config_type`))) left join `t_admin` on((`t_admin`.`id` = `t_admin_group_mapping`.`user_id`))) where (`t_admin_group_mapping`.`user_id` is not null) group by `t_notification_mapping`.`notify_config_type`,`t_admin_group_mapping`.`user_id` ;

-- ----------------------------
-- View structure for view_bike_ble
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_ble`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_ble` AS select `t_bike_ble`.`mac` AS `mac`,`t_bike`.`number` AS `number`,`t_bike`.`imei` AS `imei`,`t_bike_ble`.`id` AS `id`,`t_bike_ble`.`bid` AS `bid` from (`t_bike` join `t_bike_ble` on((`t_bike_ble`.`bid` = `t_bike`.`id`))) ;

-- ----------------------------
-- View structure for view_bike_maintain
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_maintain`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_maintain` AS select `t_bike`.`city_id` AS `city_id`,`t_bike_maintain`.`id` AS `id`,`t_bike_maintain`.`type` AS `type`,`t_bike_maintain`.`status` AS `status`,`t_bike_maintain`.`date` AS `date`,`t_bike_maintain`.`deal_date` AS `deal_date`,`t_admin`.`nickname` AS `nickname`,`t_bike`.`number` AS `number`,`t_bike_maintain`.`admin_id` AS `admin_id`,`t_bike_maintain`.`bid` AS `bid`,`t_bike_maintain`.`note` AS `note` from ((`t_bike_maintain` join `t_bike` on((`t_bike_maintain`.`bid` = `t_bike`.`id`))) join `t_admin` on((`t_admin`.`id` = `t_bike_maintain`.`admin_id`))) order by `t_bike_maintain`.`id` desc ;

-- ----------------------------
-- View structure for view_bike_order_record
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_order_record`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_order_record` AS select date_format(from_unixtime(`t_bike_order_record`.`time`),'%Y-%m-%d %H:%i:%S') AS `date_format(from_unixtime(time),'%Y-%m-%d %H:%i:%S')`,`t_bike`.`number` AS `number` from (`t_bike_order_record` join `t_bike` on((`t_bike`.`imei` = `t_bike_order_record`.`imei`))) ;

-- ----------------------------
-- View structure for view_bike_phone
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_phone`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_phone` AS select `t_bike`.`number` AS `number`,`t_bike`.`imei` AS `imei`,`t_bike_card_phone`.`phone` AS `phone`,`t_bike_sim`.`iccid` AS `iccid` from ((`t_bike` join `t_bike_sim` on((`t_bike_sim`.`imei` = `t_bike`.`imei`))) left join `t_bike_card_phone` on((`t_bike_card_phone`.`id` = `t_bike_sim`.`cid`))) ;

-- ----------------------------
-- View structure for view_bike_use
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_use`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_use` AS select `t_bike_use`.`start_time` AS `start_time`,`t_bike_use`.`end_time` AS `end_time`,`t_bike`.`status` AS `status`,`t_bike`.`use_status` AS `use_status`,`t_bike_use`.`id` AS `id`,`t_bike`.`id` AS `bid`,`t_user`.`phone` AS `phone`,`t_bike_use`.`end_lng` AS `end_lng`,`t_bike_use`.`end_lat` AS `end_lat`,`t_bike_use`.`ride_status` AS `ride_status`,`t_bike_use`.`ride_amount` AS `ride_amount`,`t_bike_use`.`uid` AS `uid`,`t_bike_type`.`price` AS `price`,`t_bike_type`.`count` AS `count`,`t_bike_ble`.`mac` AS `mac`,`t_bike`.`number` AS `number`,`t_bike_use`.`start_lat` AS `start_lat`,`t_bike_use`.`start_lng` AS `start_lng`,`t_bike_use`.`host_id` AS `host_id`,`t_bike_use`.`ride_user` AS `ride_user`,`t_bike_use`.`out_area` AS `out_area`,`t_bike`.`city_id` AS `city_id`,`t_bike_use`.`duration` AS `duration`,`t_bike_use`.`lock_location` AS `lock_location`,`t_bike`.`imei` AS `imei`,`t_bike`.`bike_type` AS `bike_type`,`t_bike_use`.`date` AS `date`,`t_bike_use`.`orbit` AS `orbit`,`t_bike`.`power` AS `power`,`t_bike_use`.`close_way` AS `close_way` from ((((`t_bike_use` join `t_bike` on((`t_bike_use`.`bid` = `t_bike`.`id`))) join `t_user` on((`t_bike_use`.`uid` = `t_user`.`id`))) join `t_bike_type` on((`t_bike`.`type_id` = `t_bike_type`.`id`))) left join `t_bike_ble` on((`t_bike_ble`.`bid` = `t_bike`.`id`))) order by `t_bike_use`.`id` desc ;

-- ----------------------------
-- View structure for view_bike_use_detail
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_use_detail`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_use_detail` AS select `t_bike_use`.`start_time` AS `start_time`,`t_bike_use`.`end_time` AS `end_time`,`t_bike`.`status` AS `status`,`t_bike`.`use_status` AS `use_status`,`t_bike_use`.`id` AS `id`,`t_bike`.`id` AS `bid`,`t_user`.`phone` AS `phone`,`t_bike`.`number` AS `number`,`t_bike_use`.`end_lng` AS `end_lng`,`t_bike_use`.`end_lat` AS `end_lat`,`t_bike_use`.`start_lng` AS `start_lng`,`t_bike_use`.`start_lat` AS `start_lat`,`t_bike_use`.`orbit` AS `orbit`,`t_bike_use`.`distance` AS `distance`,`t_bike_use`.`duration` AS `duration`,`t_bike_use`.`uid` AS `uid`,`t_violation`.`type` AS `violationType`,`t_bike`.`city_id` AS `city_id`,`t_bike`.`imei` AS `imei`,`t_bike_use`.`lock_location` AS `lock_location`,`t_bike_use`.`date` AS `date`,`t_trade`.`amount` AS `amount`,`t_trade`.`id` AS `trade_id`,`t_bike_use`.`out_area` AS `out_area`,`t_bike`.`type_id` AS `type_id`,`t_bike_use`.`ride_amount` AS `ride_amount`,`t_bike_use`.`ride_status` AS `ride_status`,`t_bike_use`.`open_way` AS `open_way`,`t_bike_use`.`close_way` AS `close_way`,`t_bike_use`.`admin_id` AS `admin_id`,`t_bike_use`.`old_duration` AS `old_duration`,`t_admin`.`user_name` AS `admin_name`,`t_bike_use`.`host_id` AS `host_id`,`t_bike_use`.`group_ride` AS `group_ride`,`t_bike`.`bike_type` AS `bike_type`,`t_user_detail`.`email` AS `email` from ((((((`t_bike_use` join `t_bike` on((`t_bike_use`.`bid` = `t_bike`.`id`))) join `t_user` on((`t_bike_use`.`uid` = `t_user`.`id`))) left join `t_violation` on((`t_violation`.`useid` = `t_bike_use`.`id`))) left join `t_trade` on((`t_bike_use`.`id` = `t_trade`.`record_id`))) left join `t_admin` on((`t_bike_use`.`admin_id` = `t_admin`.`id`))) join `t_user_detail` on((`t_user`.`id` = `t_user_detail`.`uid`))) group by `t_bike_use`.`id` order by `t_violation`.`id` desc ;

-- ----------------------------
-- View structure for view_bike_use_pay
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_use_pay`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_use_pay` AS select `t_bike_use`.`uid` AS `uid`,date_format(from_unixtime(`t_bike_use`.`start_time`),'%Y-%m-%d %H:%i:%S') AS `start`,date_format(from_unixtime(`t_bike_use`.`end_time`),'%Y-%m-%d %H:%i:%S') AS `end`,`t_trade`.`record_id` AS `record_id`,`t_trade`.`amount` AS `amount`,`t_trade`.`date` AS `date`,`t_bike_use`.`id` AS `id` from (`t_bike_use` left join `t_trade` on((`t_bike_use`.`id` = `t_trade`.`record_id`))) order by `t_bike_use`.`id` desc ;

-- ----------------------------
-- View structure for view_bike_used_today
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_used_today`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_used_today` AS select `t_bike_use`.`distance` AS `distance`,`t_bike_use`.`duration` AS `duration`,`t_trade`.`amount` AS `amount`,`t_trade`.`uid` AS `uid` from (`t_bike_use` join `t_trade` on((`t_bike_use`.`id` = `t_trade`.`record_id`))) where ((`t_trade`.`status` = 1) and (date_format(curdate(),'%Y-%m-%d') = date_format(`t_trade`.`date`,'%Y-%m-%d'))) ;

-- ----------------------------
-- View structure for view_bike_used_total
-- ----------------------------
DROP VIEW IF EXISTS `view_bike_used_total`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_bike_used_total` AS select `t_bike_use`.`distance` AS `distance`,`t_bike_use`.`duration` AS `duration`,`t_trade`.`amount` AS `amount`,`t_trade`.`uid` AS `uid`,`t_trade`.`id` AS `id`,date_format(from_unixtime(`t_bike_use`.`date`),'%Y-%m-%d') AS `date`,`t_trade`.`type` AS `type` from (`t_bike_use` join `t_trade` on((`t_bike_use`.`id` = `t_trade`.`record_id`))) where (`t_trade`.`status` = 1) ;

-- ----------------------------
-- View structure for view_cash_record
-- ----------------------------
DROP VIEW IF EXISTS `view_cash_record`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_cash_record` AS select `t_user`.`phone` AS `phone`,`t_cash_record`.`uid` AS `uid`,`t_cash_record`.`id` AS `id`,`t_cash_record`.`amount` AS `amount`,`t_cash_record`.`amount_type` AS `amount_type`,`t_cash_record`.`type` AS `type`,`t_cash_record`.`order_id` AS `order_id`,`t_cash_record`.`status` AS `status`,`t_cash_record`.`date` AS `date`,`t_user`.`city_id` AS `city_id`,`t_cash_record`.`refund_amount` AS `refund_amount`,`t_cash_record`.`refund_date` AS `refund_date` from (`t_cash_record` join `t_user` on((`t_user`.`id` = `t_cash_record`.`uid`))) ;

-- ----------------------------
-- View structure for view_deposit_return
-- ----------------------------
DROP VIEW IF EXISTS `view_deposit_return`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_deposit_return` AS select `t_deposit_return`.`id` AS `id`,`t_deposit_return`.`status` AS `status`,`t_deposit_return`.`date` AS `date`,`t_trade`.`way` AS `way`,`t_trade`.`date` AS `trade_date`,`t_trade`.`out_trade_no` AS `out_trade_no`,`t_user`.`phone` AS `phone`,`t_deposit_return`.`trade_id` AS `trade_id`,`t_deposit_return`.`uid` AS `uid`,`t_user`.`city_id` AS `city_id` from ((`t_deposit_return` left join `t_trade` on((`t_trade`.`id` = `t_deposit_return`.`trade_id`))) join `t_user` on((`t_user`.`id` = `t_deposit_return`.`uid`))) ;

-- ----------------------------
-- View structure for view_list_bike
-- ----------------------------
DROP VIEW IF EXISTS `view_list_bike`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_list_bike` AS select `t_bike`.`id` AS `id`,`t_bike`.`number` AS `number`,`t_bike`.`imei` AS `imei`,`t_bike`.`type_id` AS `type_id`,`t_bike`.`g_time` AS `g_time`,`t_bike`.`g_lat` AS `g_lat`,`t_bike`.`g_lng` AS `g_lng`,`t_bike`.`status` AS `status`,`t_bike`.`use_status` AS `use_status`,`t_bike`.`power` AS `power`,`t_bike`.`gsm` AS `gsm`,`t_bike`.`gps_number` AS `gps_number`,`t_bike`.`city_id` AS `city_id`,`t_bike`.`heart_time` AS `heart_time`,`t_bike`.`version` AS `version`,`t_bike`.`version_time` AS `version_time`,`t_bike`.`readpack` AS `readpack`,`t_bike`.`add_date` AS `add_date`,`t_bike`.`error_status` AS `error_status`,`t_bike`.`server_ip` AS `server_ip`,count(`t_bike_use`.`id`) AS `ride_count`,from_unixtime(max(`t_bike_use`.`start_time`)) AS `last_ride_date`,`t_bike`.`bike_status` AS `bike_status`,`t_sponsors`.`name` AS `name`,`t_bike`.`bike_no` AS `bike_no`,`t_bike`.`bike_type` AS `bike_type`,`t_bike_ble`.`mac` AS `mac` from (((`t_bike` left join `t_bike_use` on((`t_bike_use`.`bid` = `t_bike`.`id`))) left join `t_sponsors` on((`t_bike`.`sponsors_id` = `t_sponsors`.`id`))) left join `t_bike_ble` on((`t_bike`.`id` = `t_bike_ble`.`bid`))) group by `t_bike`.`id` ;

-- ----------------------------
-- View structure for view_peding_report
-- ----------------------------
DROP VIEW IF EXISTS `view_peding_report`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_peding_report` AS select `t_bike_error`.`id` AS `id`,`t_bike_error`.`bnumber` AS `bnumber`,`t_bike_error`.`uid` AS `uid`,`t_bike_error`.`type` AS `type`,`t_bike_error`.`error_type` AS `error_type`,`t_bike_error`.`content` AS `content`,`t_bike_error`.`image_ids` AS `image_ids`,`t_bike_error`.`date` AS `date`,`t_bike_error`.`status` AS `status`,`t_bike_error`.`bike_useid` AS `bike_useid`,`t_bike_error`.`review_note` AS `review_note`,`t_bike_error`.`review_date` AS `review_date`,`t_bike_error`.`lat` AS `lat`,`t_bike_error`.`lng` AS `lng` from `t_bike_error` where (`t_bike_error`.`status` = 0) ;


-- ----------------------------
-- View structure for view_map_bike
-- ----------------------------
DROP VIEW IF EXISTS `view_map_bike`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_map_bike` AS select distinct `t_bike`.`id` AS `id`,`t_bike`.`number` AS `number`,`t_bike`.`imei` AS `imei`,`t_bike`.`type_id` AS `type_id`,`t_bike`.`g_time` AS `g_time`,`t_bike`.`g_lat` AS `g_lat`,`t_bike`.`g_lng` AS `g_lng`,`t_bike`.`status` AS `status`,`t_bike`.`use_status` AS `use_status`,`t_bike`.`power` AS `power`,`t_bike`.`gsm` AS `gsm`,`t_bike`.`gps_number` AS `gps_number`,`t_bike`.`city_id` AS `city_id`,`t_bike`.`heart_time` AS `heart_time`,`t_bike`.`version` AS `version`,`t_bike`.`version_time` AS `version_time`,`t_bike`.`readpack` AS `readpack`,`t_bike`.`add_date` AS `add_date`,`t_bike`.`error_status` AS `error_status`,`t_bike`.`server_ip` AS `server_ip`,count(`view_peding_report`.`id`) AS `report_count`,`t_bike`.`sponsors_id` AS `sponsors_id`,`t_bike`.`bike_no` AS `bike_no`,`t_bike`.`bike_type` AS `bike_type`,`t_bike`.`extend_info` AS `extend_info` from (`t_bike` left join `view_peding_report` on((`view_peding_report`.`bnumber` = `t_bike`.`number`))) group by `t_bike`.`id` ;

-- ----------------------------
-- View structure for view_message_box
-- ----------------------------
DROP VIEW IF EXISTS `view_message_box`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_message_box` AS select `t_message_box`.`id` AS `id`,`t_message_box`.`title` AS `title`,`t_message_box`.`content` AS `content`,`t_message_box`.`date` AS `date`,`t_user`.`phone` AS `user_phone`,`t_admin`.`nickname` AS `admin_name`,`t_message_box`.`user_id` AS `user_id`,`t_message_box`.`admin_id` AS `admin_id`,`t_message_box`.`city_id` AS `city_id`,`t_message_box`.`reply` AS `reply`,`t_message_box`.`update_date` AS `update_date`,`t_message_box`.`from_type` AS `from_type`,`t_message_box`.`status` AS `status` from ((`t_message_box` left join `t_admin` on((`t_message_box`.`admin_id` = `t_admin`.`id`))) join `t_user` on((`t_message_box`.`user_id` = `t_user`.`id`))) ;

-- ----------------------------
-- View structure for view_message_box_reply
-- ----------------------------
DROP VIEW IF EXISTS `view_message_box_reply`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_message_box_reply` AS select `view_message_box`.`user_phone` AS `user_phone`,`view_message_box`.`city_id` AS `city_id`,`view_message_box`.`title` AS `title`,`t_message_box_reply`.`id` AS `id`,`t_message_box_reply`.`content` AS `reply_content`,`t_message_box_reply`.`date` AS `date`,`t_admin`.`nickname` AS `admin_name`,`view_message_box`.`id` AS `msgbox_id`,`view_message_box`.`content` AS `content`,`t_message_box_reply`.`from_type` AS `from_type`,`t_message_box_reply`.`user_id` AS `user_id` from ((`t_message_box_reply` join `view_message_box` on((`t_message_box_reply`.`msgbox_id` = `view_message_box`.`id`))) left join `t_admin` on((`t_message_box_reply`.`admin_id` = `t_admin`.`id`))) ;

-- ----------------------------
-- View structure for view_redpack_bike_rule
-- ----------------------------
DROP VIEW IF EXISTS `view_redpack_bike_rule`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_redpack_bike_rule` AS select `t_red_package_rule`.`least_use_time` AS `least_use_time`,`t_red_package_rule`.`free_use_time` AS `free_use_time`,`t_red_package_rule`.`max_amount` AS `max_amount`,`t_red_package_bike`.`bid` AS `bid`,`t_red_package_rule`.`type` AS `type`,`t_red_package_rule`.`coupon_num` AS `coupon_num`,`t_red_package_rule`.`id` AS `rule_id`,`t_red_package_rule`.`must_in_area` AS `must_in_area`,`t_red_package_rule`.`area_ids` AS `area_ids`,`t_bike`.`city_id` AS `city_id`,`t_red_package_rule`.`name` AS `name` from ((`t_red_package_bike` join `t_red_package_rule` on((`t_red_package_rule`.`id` = `t_red_package_bike`.`rule_id`))) join `t_bike` on((`t_red_package_bike`.`bid` = `t_bike`.`id`))) where (`t_red_package_bike`.`uid` = 0) ;


-- ----------------------------
-- View structure for view_near_bike
-- ----------------------------
DROP VIEW IF EXISTS `view_near_bike`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_near_bike` AS select `t_bike`.`power` AS `power`,`t_bike`.`id` AS `id`,`t_bike`.`number` AS `number`,`t_bike`.`imei` AS `imei`,`t_bike`.`g_lat` AS `g_lat`,`t_bike`.`g_lng` AS `g_lng`,`t_bike_type`.`price` AS `price`,`t_bike`.`type_id` AS `type_id`,`t_bike`.`readpack` AS `readpack`,`view_redpack_bike_rule`.`max_amount` AS `max_amount`,`view_redpack_bike_rule`.`free_use_time` AS `free_use_time`,`view_redpack_bike_rule`.`least_use_time` AS `least_use_time`,`t_bike_type`.`count` AS `count`,`t_bike`.`city_id` AS `city_id`,`view_redpack_bike_rule`.`type` AS `type`,`view_redpack_bike_rule`.`coupon_num` AS `coupon_num`,`view_redpack_bike_rule`.`rule_id` AS `rule_id`,`view_redpack_bike_rule`.`must_in_area` AS `must_in_area`,`view_redpack_bike_rule`.`area_ids` AS `area_ids`,`t_bike`.`error_status` AS `error_status`,`t_bike`.`bike_type` AS `bike_type`,`t_bike`.`area_id` AS `area_id` from ((`t_bike` join `t_bike_type` on((`t_bike`.`type_id` = `t_bike_type`.`id`))) left join `view_redpack_bike_rule` on((`t_bike`.`id` = `view_redpack_bike_rule`.`bid`))) where ((`t_bike`.`status` = 0) and (`t_bike`.`use_status` = 0) and (`t_bike`.`g_lat` is not null) and (`t_bike`.`g_lng` is not null) and (`t_bike`.`error_status` = 0)) group by `t_bike`.`number` ;


-- ----------------------------
-- View structure for view_redpack_bike
-- ----------------------------
DROP VIEW IF EXISTS `view_redpack_bike`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_redpack_bike` AS select `t_red_package_bike`.`amount` AS `amount`,`t_red_package_bike`.`start_time` AS `start_time`,`t_red_package_bike`.`end_time` AS `end_time`,`t_red_package_bike`.`date` AS `date`,`t_user`.`phone` AS `phone`,`t_bike`.`number` AS `number`,`t_red_package_bike`.`rule_id` AS `rule_id`,`t_red_package_bike`.`user_date` AS `user_date`,`t_red_package_bike`.`uid` AS `uid`,`t_red_package_bike`.`id` AS `id`,`t_red_package_bike`.`bid` AS `bid`,`t_bike`.`city_id` AS `city_id` from ((`t_red_package_bike` join `t_bike` on((`t_red_package_bike`.`bid` = `t_bike`.`id`))) left join `t_user` on((`t_red_package_bike`.`uid` = `t_user`.`id`))) order by `t_red_package_bike`.`id` ;


-- ----------------------------
-- View structure for view_redpack_rule
-- ----------------------------
DROP VIEW IF EXISTS `view_redpack_rule`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_redpack_rule` AS select `t_red_package_rule`.`id` AS `id`,`t_red_package_rule`.`free_use_time` AS `free_use_time`,`t_red_package_rule`.`least_use_time` AS `least_use_time`,`t_red_package_rule`.`max_amount` AS `max_amount`,`t_red_package_rule`.`start_time` AS `start_time`,`t_red_package_rule`.`end_time` AS `end_time`,`t_red_package_rule`.`date` AS `date`,`t_red_package_rule`.`type` AS `type`,`t_red_package_rule`.`coupon_id` AS `coupon_id`,`t_red_package_rule`.`coupon_num` AS `coupon_num`,`t_red_package_rule`.`area_ids` AS `area_ids`,`t_red_package_rule`.`must_in_area` AS `must_in_area`,`t_coupon`.`name` AS `coupon_name`,`t_red_package_rule`.`name` AS `name`,`t_coupon`.`city_id` AS `city_id` from (`t_red_package_rule` left join `t_coupon` on((`t_red_package_rule`.`coupon_id` = `t_coupon`.`id`))) ;

-- ----------------------------
-- View structure for view_rfid_card
-- ----------------------------
DROP VIEW IF EXISTS `view_rfid_card`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_rfid_card` AS select `t_rfid_card`.`id` AS `id`,`t_rfid_card`.`card_id` AS `card_id`,`t_rfid_card`.`card_no` AS `card_no`,`t_rfid_card`.`uid` AS `uid`,`t_rfid_card`.`status` AS `status`,`t_rfid_card`.`date` AS `date`,`t_user`.`phone` AS `phone` from (`t_rfid_card` join `t_user` on((`t_rfid_card`.`uid` = `t_user`.`id`))) ;

-- ----------------------------
-- View structure for view_ride_history
-- ----------------------------
DROP VIEW IF EXISTS `view_ride_history`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_ride_history` AS select `t`.`status` AS `status`,`t`.`amount` AS `amount`,`b`.`number` AS `number`,`bu`.`end_lng` AS `end_lng`,`bu`.`end_lat` AS `end_lat`,`bu`.`start_lng` AS `start_lng`,`bu`.`start_lat` AS `start_lat`,`bu`.`orbit` AS `orbit`,`bu`.`distance` AS `distance`,`bu`.`end_time` AS `end_time`,`bu`.`start_time` AS `start_time`,`t`.`id` AS `id`,`t`.`uid` AS `uid`,`t`.`out_pay_id` AS `out_pay_id`,`t`.`way` AS `way`,`t`.`date` AS `date`,`t`.`notify` AS `notify`,`b`.`use_status` AS `use_status`,`bu`.`date` AS `use_date`,`t`.`type` AS `type` from ((`t_bike` `b` join `t_bike_use` `bu`) join `t_trade` `t`) where ((`b`.`id` = `bu`.`bid`) and (`bu`.`id` = `t`.`record_id`) and (`t`.`status` <> 2) and (`t`.`type` = 1)) ;

-- ----------------------------
-- View structure for view_ride_rating
-- ----------------------------
DROP VIEW IF EXISTS `view_ride_rating`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_ride_rating` AS select `t_ride_rating`.`id` AS `id`,`t_ride_rating`.`uid` AS `uid`,`t_ride_rating`.`ride_id` AS `ride_id`,`t_ride_rating`.`star` AS `star`,`t_ride_rating`.`content` AS `content`,`t_ride_rating`.`date` AS `date`,`t_user`.`phone` AS `phone`,`t_bike_use`.`duration` AS `duration`,`t_bike_use`.`distance` AS `distance`,`t_bike`.`number` AS `number`,`t_bike_use`.`bid` AS `bid` from (((`t_ride_rating` join `t_user` on((`t_ride_rating`.`uid` = `t_user`.`id`))) join `t_bike_use` on((`t_ride_rating`.`ride_id` = `t_bike_use`.`id`))) join `t_bike` on((`t_bike_use`.`bid` = `t_bike`.`id`))) ;

-- ----------------------------
-- View structure for view_trade_bike
-- ----------------------------
DROP VIEW IF EXISTS `view_trade_bike`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_trade_bike` AS select `t_trade`.`status` AS `status`,`t_trade`.`amount` AS `amount`,`t_bike`.`number` AS `number`,`t_bike_use`.`end_lng` AS `end_lng`,`t_bike_use`.`end_lat` AS `end_lat`,`t_bike_use`.`start_lng` AS `start_lng`,`t_bike_use`.`start_lat` AS `start_lat`,`t_bike_use`.`orbit` AS `orbit`,`t_bike_use`.`distance` AS `distance`,`t_bike_use`.`end_time` AS `end_time`,`t_bike_use`.`start_time` AS `start_time`,`t_trade`.`id` AS `id`,`t_trade`.`uid` AS `uid`,`t_trade`.`out_pay_id` AS `out_pay_id`,`t_trade`.`way` AS `way`,`t_trade`.`date` AS `date`,`t_trade`.`notify` AS `notify`,`t_bike`.`use_status` AS `use_status`,`t_bike_use`.`date` AS `use_date`,`t_trade`.`record_id` AS `record_id`,`t_bike_use`.`ride_status` AS `ride_status`,`t_bike_use`.`admin_id` AS `admin_id`,`t_bike`.`imei` AS `imei` from ((`t_bike` join `t_bike_use` on((`t_bike_use`.`bid` = `t_bike`.`id`))) join `t_trade` on((`t_trade`.`record_id` = `t_bike_use`.`id`))) where ((`t_trade`.`status` <> 2) and (`t_trade`.`type` = 1)) ;

-- ----------------------------
-- View structure for view_useing_bike
-- ----------------------------
DROP VIEW IF EXISTS `view_useing_bike`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_useing_bike` AS select `t_bike_use`.`start_time` AS `start_time`,`t_bike_use`.`uid` AS `uid`,`t_bike`.`status` AS `status`,`t_bike`.`use_status` AS `use_status`,`t_bike_use`.`id` AS `id`,`t_bike_use`.`orbit` AS `orbit`,`t_bike_use`.`distance` AS `distance`,`t_bike`.`type_id` AS `type_id`,`t_bike_type`.`price` AS `price`,`t_bike`.`imei` AS `imei`,`t_bike`.`number` AS `number`,`t_bike_use`.`start_lat` AS `start_lat`,`t_bike_use`.`start_lng` AS `start_lng`,`t_bike_use`.`end_lat` AS `end_lat`,`t_bike_use`.`end_lng` AS `end_lng`,`t_bike`.`readpack` AS `readpack`,`t_bike_use`.`bid` AS `bid`,`t_bike_type`.`count` AS `count`,`t_bike`.`city_id` AS `city_id`,`t_bike_use`.`out_area` AS `out_area`,`t_bike_use`.`date` AS `date`,`t_bike_use`.`open_way` AS `open_way`,`t_bike_use`.`ride_status` AS `ride_status`,`t_bike_use`.`old_date` AS `old_date`,`t_bike_type`.`outarea_pause_time` AS `outarea_pause_time`,`t_bike_type`.`normal_pause_time` AS `normal_pause_time`,`t_bike_ble`.`mac` AS `mac`,`t_bike_use`.`update_time` AS `update_time`,`t_bike_use`.`lock_time` AS `lock_time`,`t_bike_use`.`host_id` AS `host_id`,`t_bike_use`.`ride_user` AS `ride_user`,`t_bike_use`.`group_ride` AS `group_ride`,`t_bike`.`bike_type` AS `bike_type`,`t_bike_type`.`unlock_price` AS `unlock_price`,`t_bike`.`power` AS `power` from (((`t_bike` join `t_bike_use` on((`t_bike`.`id` = `t_bike_use`.`bid`))) join `t_bike_type` on((`t_bike`.`type_id` = `t_bike_type`.`id`))) left join `t_bike_ble` on((`t_bike`.`id` = `t_bike_ble`.`bid`))) where (`t_bike_use`.`end_time` = 0) ;

-- ----------------------------
-- View structure for view_useing_bike_deposit
-- ----------------------------
DROP VIEW IF EXISTS `view_useing_bike_deposit`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_useing_bike_deposit` AS select `t_deposit_conf`.`amount` AS `amount`,`t_bike`.`number` AS `number`,`t_bike_use`.`ride_status` AS `ride_status`,`t_bike_use`.`uid` AS `uid`,`t_bike_use`.`bid` AS `bid` from (((`t_bike_use` join `t_bike_type`) join `t_deposit_conf`) join `t_bike`) where ((`t_deposit_conf`.`type_id` = `t_bike_type`.`id`) and (`t_bike`.`type_id` = `t_bike_type`.`id`) and (`t_bike_use`.`bid` = `t_bike`.`id`) and (`t_bike_use`.`ride_status` <> 3)) ;

-- ----------------------------
-- View structure for view_user_coupon
-- ----------------------------
DROP VIEW IF EXISTS `view_user_coupon`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_user_coupon` AS select `t_user_coupon`.`uid` AS `uid`,`t_coupon`.`type` AS `type`,`t_coupon`.`name` AS `name`,`t_coupon`.`id` AS `id`,`t_coupon`.`value` AS `value`,`t_user_coupon`.`id` AS `uc_id`,`t_user`.`phone` AS `phone`,`t_user_coupon`.`cid` AS `cid`,`t_user_coupon`.`code` AS `code`,`t_user_coupon`.`date` AS `date`,`t_user_coupon`.`active_date` AS `active_date`,`t_user_coupon`.`used` AS `used`,`t_user_coupon`.`end_time` AS `end_time`,`t_user_coupon`.`start_time` AS `start_time`,`t_coupon`.`day` AS `day`,`t_user_coupon`.`amount` AS `amount`,`t_coupon`.`isrepeat` AS `isrepeat`,`t_coupon`.`city_id` AS `city_id` from ((`t_user_coupon` join `t_coupon` on((`t_user_coupon`.`cid` = `t_coupon`.`id`))) left join `t_user` on((`t_user_coupon`.`uid` = `t_user`.`id`))) where (`t_user_coupon`.`end_time` > unix_timestamp()) order by `t_user_coupon`.`id` desc ;

-- ----------------------------
-- View structure for view_user_coupon_all
-- ----------------------------
DROP VIEW IF EXISTS `view_user_coupon_all`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_user_coupon_all` AS select `t_user_coupon`.`uid` AS `uid`,`t_coupon`.`type` AS `type`,`t_coupon`.`name` AS `name`,`t_coupon`.`id` AS `id`,`t_coupon`.`value` AS `value`,`t_user_coupon`.`id` AS `uc_id`,`t_user`.`phone` AS `phone`,`t_user_coupon`.`cid` AS `cid`,`t_user_coupon`.`code` AS `code`,`t_user_coupon`.`date` AS `date`,`t_user_coupon`.`active_date` AS `active_date`,`t_user_coupon`.`used` AS `used`,`t_user_coupon`.`end_time` AS `end_time`,`t_user_coupon`.`start_time` AS `start_time`,`t_coupon`.`day` AS `day`,`t_user_coupon`.`amount` AS `amount`,`t_coupon`.`isrepeat` AS `isrepeat`,`t_coupon`.`city_id` AS `city_id`,`t_user_coupon`.`gift_from` AS `gift_from` from ((`t_user_coupon` join `t_coupon` on((`t_user_coupon`.`cid` = `t_coupon`.`id`))) left join `t_user` on((`t_user_coupon`.`uid` = `t_user`.`id`))) order by `t_user_coupon`.`id` desc ;

-- ----------------------------
-- View structure for view_user_deposit
-- ----------------------------
DROP VIEW IF EXISTS `view_user_deposit`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_user_deposit` AS select `t_user_deposit`.`id` AS `id`,`t_user_deposit`.`uid` AS `uid`,`t_user_deposit`.`deposit_id` AS `deposit_id`,`t_user_deposit`.`count` AS `count`,`t_user_deposit`.`status` AS `status`,`t_user_deposit`.`date` AS `date`,`t_bike_type`.`note` AS `note`,`t_bike_type`.`type` AS `type`,`t_user_deposit`.`amount` AS `amount` from ((`t_deposit_conf` join `t_bike_type` on((`t_deposit_conf`.`type_id` = `t_bike_type`.`id`))) join `t_user_deposit` on((`t_user_deposit`.`deposit_id` = `t_deposit_conf`.`id`))) order by `t_bike_type`.`type` ;

-- ----------------------------
-- View structure for view_user_detail
-- ----------------------------
DROP VIEW IF EXISTS `view_user_detail`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_user_detail` AS select `d`.`uid` AS `uid`,`d`.`firstname` AS `firstname`,`d`.`lastname` AS `lastname`,`d`.`email` AS `email`,`d`.`address` AS `address`,`d`.`zip_code` AS `zip_code`,`d`.`country` AS `country`,`d`.`redpack` AS `redpack`,`d`.`idcard` AS `idcard`,`d`.`birthday` AS `birthday`,`d`.`gender` AS `gender`,`d`.`third_info` AS `third_info`,`u`.`phone` AS `phone`,`u`.`city_id` AS `city_id`,`c`.`name` AS `city_name`,`u`.`invite_code` AS `invite_code`,`u`.`head_url` AS `head_url`,`t_country`.`phone_code` AS `phone_code`,`u`.`nickname` AS `nickname`,`u`.`auth_status` AS `auth_status`,`d`.`email_auth` AS `email_auth`,`u`.`money` AS `money`,`u`.`id` AS `id`,`u`.`password` AS `password`,`u`.`industry_id` AS `industry_id`,`u`.`gift_money` AS `gift_money`,`u`.`register_date` AS `register_date`,`u`.`login_date` AS `login_date`,`u`.`country_code` AS `country_code`,`u`.`credit_score` AS `credit_score`,`u`.`country_id` AS `country_id` from (((`t_user` `u` left join `t_user_detail` `d` on((`u`.`id` = `d`.`uid`))) left join `t_city` `c` on((`u`.`city_id` = `c`.`id`))) left join `t_country` on((`u`.`country_id` = `t_country`.`id`))) ;

-- ----------------------------
-- View structure for view_user_invaite
-- ----------------------------
DROP VIEW IF EXISTS `view_user_invaite`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_user_invaite` AS select `t_user_invaite`.`id` AS `id`,`t_user_invaite`.`i_uid` AS `i_uid`,`t_user_invaite`.`date` AS `date`,`t_user_invaite`.`uid` AS `uid`,`user`.`phone` AS `phone`,`iuser`.`phone` AS `i_phone` from ((`t_user_invaite` join `t_user` `user` on((`t_user_invaite`.`uid` = `user`.`id`))) join `t_user` `iuser` on((`t_user_invaite`.`i_uid` = `iuser`.`id`))) ;

-- ----------------------------
-- View structure for view_user_list
-- ----------------------------
DROP VIEW IF EXISTS `view_user_list`;
CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `view_user_list` AS select `t_user`.`phone` AS `phone`,`t_user`.`nickname` AS `nickname`,`t_user_detail`.`firstname` AS `firstname`,`t_user_detail`.`lastname` AS `lastname`,`t_user_detail`.`email` AS `email`,`t_user`.`money` AS `money`,`t_user`.`auth_status` AS `auth_status`,`t_user`.`register_date` AS `register_date`,`t_user`.`login_date` AS `login_date`,`t_user_detail`.`uid` AS `uid`,`t_city`.`name` AS `city_name`,`t_user`.`city_id` AS `city_id`,`t_country`.`phone_code` AS `phone_code`,count(`t_bike_use`.`id`) AS `ride_count`,`t_user`.`id` AS `id`,`t_user`.`gift_money` AS `gift_money` from ((((`t_user` left join `t_user_detail` on((`t_user`.`id` = `t_user_detail`.`uid`))) join `t_city` on((`t_user`.`city_id` = `t_city`.`id`))) left join `t_country` on((`t_user`.`country_id` = `t_country`.`id`))) left join `t_bike_use` on((`t_user`.`id` = `t_bike_use`.`uid`))) group by `t_user`.`id` ;

-- ----------------------------
-- Event structure for event_bike_open_fail
-- ----------------------------
DROP EVENT IF EXISTS `event_bike_open_fail`;
DELIMITER ;;
CREATE  EVENT `event_bike_open_fail` ON SCHEDULE EVERY 10 SECOND STARTS '2017-04-21 03:17:18' ON COMPLETION NOT PRESERVE ENABLE DO #删除未开锁成功的记录，超时2分钟
delete from t_bike_use where start_time = 0 and end_time = 0 and date < UNIX_TIMESTAMP() - 120
;;
DELIMITER ;

-- ----------------------------
-- Event structure for event_delete_bike_order_record_5ago
-- ----------------------------
DROP EVENT IF EXISTS `event_delete_bike_order_record_5ago`;
DELIMITER ;;
CREATE  EVENT `event_delete_bike_order_record_5ago` ON SCHEDULE EVERY 1 DAY STARTS '2017-09-01 16:34:07' ON COMPLETION NOT PRESERVE ENABLE DO /**清空5天前的日志**/
DELETE  from t_bike_order_record where time < UNIX_TIMESTAMP()-60*60*24*5
;;
DELIMITER ;

-- ----------------------------
-- Event structure for event_redpack_bike_end
-- ----------------------------
DROP EVENT IF EXISTS `event_redpack_bike_end`;
DELIMITER ;;
CREATE  EVENT `event_redpack_bike_end` ON SCHEDULE EVERY 1 MINUTE STARTS '2017-04-27 19:24:55' ON COMPLETION NOT PRESERVE ENABLE DO #红包过期检查
delete from t_red_package_bike where end_time <= UNIX_TIMESTAMP()
;;



DELIMITER ;
DROP TRIGGER IF EXISTS `tg_reserve_add`;
DELIMITER ;;
CREATE TRIGGER `tg_reserve_add` AFTER INSERT ON `t_bike_reserve` FOR EACH ROW /**当新增一条设备的预约记录时，修改设备状态为预约中**/
update t_bike set use_status = 2 where id = new.bid
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `tg_reserve_update`;
DELIMITER ;;
CREATE TRIGGER `tg_reserve_update` AFTER UPDATE ON `t_bike_reserve` FOR EACH ROW /**当一条设备的预约记录被删除时，修改设备状态为正常状态（自由）**/
update t_bike set use_status = 0 where id = old.bid  and use_status = 2
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `tg_bike_use_start`;
DELIMITER ;;
CREATE TRIGGER `tg_bike_use_start` AFTER INSERT ON `t_bike_use` FOR EACH ROW /**当开始一个设备的使用时，修改设备状态为正在解锁**/
update t_bike set use_status = 3 where id = new.bid
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `tg_bike_end_use`;
DELIMITER ;;
CREATE TRIGGER `tg_bike_end_use` BEFORE UPDATE ON `t_bike_use` FOR EACH ROW /**当完成一次骑行时**/
if(new.end_time > 0)
then 
/*计算骑行时间**/
set new.duration = new.end_time-new.start_time;
end if
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `tg_bike_use_update`;
DELIMITER ;;
CREATE TRIGGER `tg_bike_use_update` AFTER UPDATE ON `t_bike_use` FOR EACH ROW /**当完成一次骑行时**/
if(new.end_time > 0)
then 
/*修改设备状态为正常状态（自由）;**/
update t_bike set use_status = 0,status = 0 where id = old.bid;
/*update t_bike set use_status = 0 where id = old.bid;*/
/**当成功开始的使用时**/
elseif(new.start_time != 0 && new.end_time = 0 &&  new.ride_status != 4 && new.ride_status != -2 && old.ride_status != 1)
then
/**修改设备状态为开，使用状态为使用中**/
update t_bike set use_status = 1,status = 1 where id = old.bid;
/**更新预约记录为正常解锁**/
update t_bike_reserve set status = 1 where uid = old.uid and status = 0;
/**
elseif(new.start_time != 0 && new.end_time = 0 &&  new.ride_status = 4)
then
/**暂停骑行时，修改锁为关状态
update t_bike set status = 0 where id = old.bid;**/

end if
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `tb_bike_use_delete`;
DELIMITER ;;
CREATE TRIGGER `tb_bike_use_delete` AFTER DELETE ON `t_bike_use` FOR EACH ROW /**删除开锁失败记录时，设置对应单车为自由状态**/
if(old.start_time = 0)
then
update t_bike set use_status = 0 where id = old.bid;
end if
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `retrun_success`;
DELIMITER ;;
CREATE TRIGGER `retrun_success` AFTER UPDATE ON `t_deposit_return` FOR EACH ROW /**设置用户认证状态为：已退押金（不能使用单车)**/
if(new.status = 1 || new.status = 2)
then
update t_user set auth_status = 6 where id = new.uid;
end if
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `redbike_set`;
DELIMITER ;;
CREATE TRIGGER `redbike_set` AFTER INSERT ON `t_red_package_bike` FOR EACH ROW /**设置单车为红包单车**/
update t_bike set readpack =1 where id = new.bid
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `redbike_use`;
DELIMITER ;;
CREATE TRIGGER `redbike_use` AFTER UPDATE ON `t_red_package_bike` FOR EACH ROW /***当红包单车被使用时，标记单车为普通单车***/
if(new.uid != 0)
then
update t_bike set readpack =0 where id = new.bid;
/**增加用户未提现红包总额**/
update t_user_detail set redpack = redpack + new.amount where uid = new.uid;
end if
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `redbike_delete`;
DELIMITER ;;
CREATE TRIGGER `redbike_delete` AFTER DELETE ON `t_red_package_bike` FOR EACH ROW /**设置单车为普通单车**/
update t_bike set readpack =0 where id = old.bid
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `add_detail`;
DELIMITER ;;
CREATE TRIGGER `add_detail` AFTER INSERT ON `t_user` FOR EACH ROW /**关联新增用户详情记录**/
insert into t_user_detail (uid) values (new.id)
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `tg_update_user_score`;
DELIMITER ;;
CREATE TRIGGER `tg_update_user_score` AFTER INSERT ON `t_user_credit_score` FOR EACH ROW /***update user score**/
if(new.rule_type = 1)
then

update t_user set credit_score = credit_score + new.count where id = new.uid;

elseif(new.rule_type = 2)
then

update t_user set credit_score = credit_score - new.count where id = new.uid;

elseif(new.rule_type = 3)
then

update t_user set credit_score = 0 where id = new.uid;

end if
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `tg_pause_end`;
DELIMITER ;;
CREATE TRIGGER `tg_pause_end` AFTER UPDATE ON `t_user_pause_parking_history` FOR EACH ROW if(new.status = 2)
then 
/**当结束暂停时，设置骑行为正常状态**/
update t_bike_use set ride_status = 1 where id = new.ride_id;
end if
;;
DELIMITER ;
