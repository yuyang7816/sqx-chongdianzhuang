/*
 MySQL dump for 充电桩开源演示库
 Source Schema         : chongdianzhuang
 Source Server Version : 5.7.44-log
 Date: 06/11/2026 10:09:08

 说明：全量表结构；大体量/隐私流水表仅结构不导数据；
       系统配置、菜单、门店样例等保留，便于本地导入后启动。
       演示后台账号：admin / 123456
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '地址id',
  `consignee` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收货人',
  `create_at` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `detail` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '详细地址',
  `mobile` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电话',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `is_default` int(11) DEFAULT NULL COMMENT '是否默认地址',
  `provinces` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '省市区',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of address
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for adverti
-- ----------------------------
DROP TABLE IF EXISTS `adverti`;
CREATE TABLE `adverti` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '广告id',
  `create_at` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `img_url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片路径',
  `link_url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '页面路由',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of adverti
-- ----------------------------
INSERT INTO `adverti` (`id`, `create_at`, `img_url`, `link_url`) VALUES (1, '2023-10-25 13:32:46', '', '/pages/index/mian');

-- ----------------------------
-- Table structure for agency
-- ----------------------------
DROP TABLE IF EXISTS `agency`;
CREATE TABLE `agency` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申请id',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `img` varchar(500) DEFAULT NULL COMMENT '头像',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '时间',
  `status` int(11) DEFAULT NULL COMMENT '状态0待审核 1同意 2拒绝',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of agency
-- ----------------------------

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'app升级配置',
  `android_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '安卓升级地址',
  `create_at` varchar(600) DEFAULT NULL COMMENT '创建时间',
  `des` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `ios_version` varchar(255) DEFAULT NULL COMMENT '苹果升级版本号',
  `ios_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '苹果升级地址',
  `method` varchar(600) DEFAULT NULL COMMENT '是否强制升级',
  `version` varchar(600) DEFAULT NULL COMMENT '安卓升级版本号',
  `wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '通用下载地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` (`id`, `android_wgt_url`, `create_at`, `des`, `ios_version`, `ios_wgt_url`, `method`, `version`, `wgt_url`) VALUES (5, 'https://www.pgyer.com/hb2v', '2020-07-20 18:35:00', '升级', NULL, '', 'false', '1.0', 'https://www.pgyer.com/hb2v');

-- ----------------------------
-- Table structure for apply
-- ----------------------------
DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply` (
  `apply_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申请id',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '商户名称',
  `apply_name` varchar(255) DEFAULT NULL COMMENT '负责人姓名',
  `apply_phone` varchar(255) DEFAULT NULL COMMENT '负责人电话',
  `shop_address` varchar(255) DEFAULT NULL COMMENT '商铺地址',
  `shop_address_details` varchar(255) DEFAULT NULL COMMENT '商铺详细地址',
  `card_type` int(11) DEFAULT NULL COMMENT '证件类型',
  `card_no` varchar(255) DEFAULT NULL COMMENT '证件号码',
  `card_front_img` varchar(255) DEFAULT NULL COMMENT '证件正面',
  `card_reverse_img` varchar(255) DEFAULT NULL COMMENT '证件背面',
  `shop_img` varchar(255) DEFAULT NULL COMMENT '商铺图片',
  `business_license_img` varchar(255) DEFAULT NULL COMMENT '商铺营业执照',
  `apply_content` varchar(255) DEFAULT NULL COMMENT '内容',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `status` int(11) DEFAULT NULL COMMENT '状态 1待审核 2通过 3拒绝',
  `audit_content` varchar(500) DEFAULT NULL COMMENT '审核内容',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `longitude` varchar(255) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`apply_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of apply
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'bannerid',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `state` int(2) DEFAULT NULL COMMENT '状态1正常2隐藏',
  `classify` int(2) DEFAULT NULL COMMENT '分类1banner图2金刚区分类',
  `url` varchar(255) DEFAULT NULL COMMENT '跳转地址 ',
  `sort` int(4) DEFAULT NULL COMMENT '顺序',
  `describes` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (19, '2025-04-08 09:48:00', '邀请好友背景图', 'https://tu.mianyazuju.com/20260914/51caf279bce54a729ce87da8b672a399.png', 1, 5, '-', NULL, '邀请好友一起绿色出行');
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (40, '2025-02-24 17:56:16', '充电桩轮播图', 'https://tu.mianyazuju.com/20260914/51caf279bce54a729ce87da8b672a399.png', 1, 1, '-', NULL, '绿色充电 随充随走');

-- ----------------------------
-- Table structure for blacklist
-- ----------------------------
DROP TABLE IF EXISTS `blacklist`;
CREATE TABLE `blacklist` (
  `blacklist_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '拉黑id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `by_user_id` int(11) DEFAULT NULL COMMENT '拉黑用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`blacklist_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of blacklist
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for car
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car` (
  `car_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '车辆id',
  `car_type` varchar(255) DEFAULT NULL COMMENT '车辆型号',
  `car_classify` varchar(255) DEFAULT NULL COMMENT '车辆类型',
  `car_no` varchar(64) DEFAULT NULL COMMENT '车辆号码',
  `car_color` varchar(255) DEFAULT NULL COMMENT '车辆颜色',
  `car_name` varchar(255) DEFAULT NULL COMMENT '车主名称',
  `car_phone` varchar(255) DEFAULT NULL COMMENT '车主电话',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `car_logo` varchar(500) DEFAULT NULL COMMENT '车辆logo',
  PRIMARY KEY (`car_id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of car
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for cash_out
-- ----------------------------
DROP TABLE IF EXISTS `cash_out`;
CREATE TABLE `cash_out` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请提现id',
  `create_at` varchar(255) DEFAULT NULL COMMENT '申请时间',
  `is_out` int(2) DEFAULT NULL COMMENT '是否转账',
  `money` decimal(10,2) DEFAULT NULL COMMENT '提现金额',
  `out_at` varchar(255) DEFAULT NULL COMMENT '转账时间',
  `relation_id` varchar(255) DEFAULT NULL COMMENT '会员编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `zhifubao` varchar(255) DEFAULT NULL COMMENT '支付宝账号',
  `zhifubao_name` varchar(64) DEFAULT NULL COMMENT '支付宝姓名',
  `order_number` varchar(255) DEFAULT NULL COMMENT '订单编号',
  `state` int(11) DEFAULT NULL COMMENT '状态 0待转账 1成功 -1退款',
  `refund` varchar(1000) DEFAULT NULL COMMENT '退款原因',
  `classify` int(11) DEFAULT NULL COMMENT '提现方式 1支付宝 2微信小程序  3微信公众号',
  `wx_img` varchar(600) DEFAULT NULL COMMENT '微信图片',
  `rate` varchar(10) DEFAULT NULL COMMENT '利息',
  `user_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of cash_out
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `chat_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '聊天记录id',
  `user_key` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'chatGpt key',
  `model` varchar(40) CHARACTER SET utf8 DEFAULT NULL COMMENT '模型id',
  `prompt` text CHARACTER SET utf8 COMMENT '问题',
  `answer` text COMMENT '答案',
  `create_time` varchar(32) DEFAULT NULL COMMENT '创建时间',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `message_type` int(11) DEFAULT NULL COMMENT '类型 1文字 2图片',
  `word_url` varchar(255) DEFAULT NULL COMMENT 'word文档地址',
  `pdf_url` varchar(255) DEFAULT NULL COMMENT 'pdf地址',
  `music_url` varchar(255) DEFAULT NULL COMMENT '音频地址',
  `good` varchar(255) DEFAULT NULL COMMENT '是否是精选 1是',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `audio_content` varchar(255) DEFAULT NULL COMMENT '百度识别 音频转文字',
  `user_message_type` varchar(255) DEFAULT NULL COMMENT '用户消息 1文字 2语音',
  `is_end` varchar(255) DEFAULT NULL,
  `jpg_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `is_delete` int(11) DEFAULT NULL COMMENT '用户端 是否删除  1是',
  `text` text COMMENT '语音转文字储存',
  `sec` int(11) DEFAULT NULL COMMENT '语音秒数',
  `free` int(11) DEFAULT NULL COMMENT '本条消息是否是会员免费  1是会员免费',
  PRIMARY KEY (`chat_id`) USING BTREE,
  UNIQUE KEY `chatid_index` (`chat_id`) USING BTREE,
  UNIQUE KEY `create_date_index` (`create_time`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16207 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天记录表';

-- ----------------------------
-- Records of chat
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for chats
-- ----------------------------
DROP TABLE IF EXISTS `chats`;
CREATE TABLE `chats` (
  `chat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话id',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `user_count` int(20) DEFAULT '0' COMMENT '用户未读条数',
  `user_count2` int(20) DEFAULT '0' COMMENT '好友未读条数',
  `user_head` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `user_head2` varchar(255) DEFAULT NULL COMMENT '好友头像',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(20) DEFAULT NULL COMMENT '好友id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `user_name2` varchar(255) DEFAULT NULL COMMENT '好友昵称',
  `store_count` int(11) DEFAULT '0' COMMENT '后台未读条数',
  `store_head` varchar(255) DEFAULT NULL COMMENT '后台头像',
  `store_id` bigint(20) DEFAULT NULL COMMENT '总后台id(总后台传0',
  `store_name` varchar(255) DEFAULT NULL COMMENT '商户昵称',
  PRIMARY KEY (`chat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天会话';

-- ----------------------------
-- Records of chats
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for chats_content
-- ----------------------------
DROP TABLE IF EXISTS `chats_content`;
CREATE TABLE `chats_content` (
  `chat_content_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话内容id',
  `chat_id` bigint(20) DEFAULT NULL COMMENT '会话Id',
  `content` text COMMENT '聊天内容',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `send_type` bigint(20) DEFAULT NULL COMMENT '消息来源（1用户消息 2好友消息）',
  `status` int(11) DEFAULT '1' COMMENT '是否已读(1未读 2已读)',
  `type` int(1) DEFAULT NULL COMMENT '类型（1文字 2图片 3链接）',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(20) DEFAULT NULL COMMENT '好友id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '后台id（总后台传0）',
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天会话内容';

-- ----------------------------
-- Records of chats_content
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for chat_content
-- ----------------------------
DROP TABLE IF EXISTS `chat_content`;
CREATE TABLE `chat_content` (
  `chat_content_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天内容id',
  `chat_conversation_id` int(11) DEFAULT NULL COMMENT '聊天会话id',
  `content` varchar(2000) DEFAULT NULL COMMENT '内容',
  `message_type` int(11) DEFAULT NULL COMMENT '类型 1文字 2图片 3语音',
  `width` varchar(64) DEFAULT NULL COMMENT '图片宽度',
  `height` varchar(64) DEFAULT NULL COMMENT '图片高度',
  `user_id` int(11) DEFAULT NULL COMMENT '发送用户id',
  `status` int(11) DEFAULT NULL COMMENT '0未读 1已读',
  `create_time` varchar(64) DEFAULT NULL COMMENT '发送时间',
  `voice_length` varchar(64) DEFAULT NULL COMMENT '语音时长',
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of chat_content
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for chat_conversation
-- ----------------------------
DROP TABLE IF EXISTS `chat_conversation`;
CREATE TABLE `chat_conversation` (
  `chat_conversation_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天会话id',
  `user_id` int(11) DEFAULT NULL COMMENT '发送用户id',
  `focused_user_id` int(11) DEFAULT NULL COMMENT '接受用户id',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_send_msg` int(11) DEFAULT NULL COMMENT '是否推送未读短信',
  `is_wx_msg` int(11) DEFAULT NULL COMMENT '是否推送未读小程序消息',
  PRIMARY KEY (`chat_conversation_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of chat_conversation
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `city_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '城市id',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `status` int(11) DEFAULT NULL COMMENT '状态 1开启 2关闭',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`city_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of city
-- ----------------------------
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (2, '吴忠市', 1, '2025-03-30 00:32:37');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (69, '西安市', 1, '2025-03-30 00:32:52');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (71, '徐州市', 2, '2025-03-30 00:31:39');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (72, '福州市', 2, '2025-03-30 00:31:40');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (73, '厦门市', 2, '2025-03-30 00:31:42');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (74, '渭南市', 2, '2025-03-30 00:31:43');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (75, '沧州市', 2, '2025-03-30 00:31:44');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (76, '上饶市', 2, '2025-03-30 00:31:46');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (77, '广州市', 2, '2025-03-30 00:31:47');
INSERT INTO `city` (`city_id`, `city`, `status`, `create_time`) VALUES (78, '深圳市', 2, '2025-03-30 00:31:51');

-- ----------------------------
-- Table structure for common_info
-- ----------------------------
DROP TABLE IF EXISTS `common_info`;
CREATE TABLE `common_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置文件id',
  `create_at` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `max` varchar(255) DEFAULT NULL,
  `min` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '配置文件名称',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '值',
  `condition_from` varchar(255) DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1167 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of common_info
-- ----------------------------
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1, '2025-03-31 15:36:23', NULL, '客服二维码', 1, 'https://tu.mianyazuju.com/20250331/cd9c38de215b407bae24deb3e3fb9f7f.jpg', 'image');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (2, '2025-03-31 15:39:06', NULL, '公众号二维码', 2, 'https://tu.mianyazuju.com/20250331/91f1680f7a8b4d9fbd30347d111701d5.jpg', 'image');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (3, '2023-11-10 09:24:35', NULL, '邀请码是否必填', 3, '否', 'kaiguan');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (4, '2023-11-10 09:24:35', NULL, '美团，抖音优惠券', 4, '50', 'xitongs');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (6, '2026-01-22 13:33:12', NULL, '微信公众号APPID	', 5, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (7, '2025-04-15 23:57:35', NULL, '短信签名', 6, '', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (16, '2026-01-22 13:38:09', NULL, '微信公众号秘钥	', 21, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (17, '2020-02-25 20:43:59', NULL, '公众号Token', 16, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (18, '2020-02-25 20:44:15', NULL, '公众号EncodingAESKey	', 17, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (20, '2025-08-15 16:37:51', NULL, '后台管理平台域名配置	', 20, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (21, '2025-08-15 16:37:58', NULL, 'h5服务域名配置	', 19, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (22, '2020-07-27 15:17', NULL, '短信服务商（1 腾讯云  2阿里云  3短信宝）', 79, '3', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (23, '2026-06-17 20:21:08', NULL, '后台服务名称	', 12, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (27, '2025-03-30 00:06:19', NULL, '通用APP下载地址	', 25, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (33, '2021-03-15 21:19:27', NULL, '腾讯云短信clientId	', 31, '', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (34, '2020-03-28 00:47', NULL, '腾讯云短信clientSecret	', 32, '', 'duanxins');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (44, '2023-11-15 14:46:45', NULL, '客服微信号', 44, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (47, '2022-01-22 20:52:42', NULL, '微信小程序APPID', 45, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (48, '2022-01-22 20:52:48', NULL, '微信小程序秘钥', 46, '', 'weixins');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (51, '2025-03-30 00:06:22', NULL, '分享安卓下载地址', 49, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (52, '2025-03-30 00:06:25', NULL, '分享苹果下载地址', 50, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (58, '2026-01-22 13:55:56', NULL, '开启微信登录', 53, '否', 'kaiguan');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (69, '2020-06-04 16:34', NULL, 'APP消息推送PushAppKey', 60, '', 'push');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (70, '2020-06-04 16:34', NULL, 'APP消息推送PushAppId', 61, '', 'push');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (71, '2020-06-04 16:34', NULL, 'APP消息推送PushMasterSecret', 62, '', 'push');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (72, '2020-06-04 16:34', NULL, '企业支付宝APPID', 63, '', 'zhifubao');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (73, '2020-06-04 16:34', NULL, '企业支付宝公钥', 64, '', 'zhifubaos');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (74, '2020-06-04 16:34', NULL, '企业支付宝商户秘钥', 65, '', 'zhifubaos');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (75, '2020-06-04 16:34', NULL, '支付宝支付方式1证书 2秘钥', 66, '2', 'zhifubao');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (76, '2020-06-04 16:34', NULL, '支付宝证书地址', 67, '', 'zhifubao');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (77, '2021-03-15 21:22:30', NULL, '文件上传阿里云Endpoint', 68, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (78, '2025-03-19 19:35:32', NULL, '文件上传阿里云账号accessKeyId', 69, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (79, '2025-03-19 19:35:37', NULL, '文件上传阿里云账号accessKeySecret', 70, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (80, '2025-04-16 13:38:14', NULL, '文件上传阿里云Bucket名称', 71, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (81, '2025-04-16 13:39:00', NULL, '文件上传阿里云Bucket域名', 72, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (83, '2020-07-27 15:17', NULL, '微信APPappId', 74, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (84, '2020-07-27 15:17', NULL, '微信商户key', 75, '', 'weixins');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (85, '2026-01-22 15:01:44', NULL, '微信商户号mchId', 76, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (90, '2023-03-18 11:05:50', NULL, '百度云apiKey', 90, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (91, '2023-03-18 11:05:50', NULL, '百度云secretKey', 91, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (94, '2023-08-22 11:18:49', NULL, '违规提问提示语', 94, '网络不是法外之地，请注意您的言辞1.', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (96, '2020-07-27 15:17', NULL, '阿里云登陆或注册模板code（开启阿里云短信必须配置）', 80, '', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (97, '2020-07-27 15:17', NULL, '阿里云找回密码模板code（开启阿里云短信必须配置）', 81, '', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (98, '2020-07-27 15:17', NULL, '阿里云绑定手机号模板code（开启阿里云短信必须配置）', 82, '', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (99, '2020-07-27 15:17', NULL, '阿里云短信accessKeyId', 83, '', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (100, '2020-07-27 15:17', NULL, '阿里云短信accessSecret', 84, '', 'duanxins');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (101, '2022-07-06 20:53:43', NULL, '官方邀请码', 88, '343737', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (102, '2022-04-21 19:40:07', NULL, '转账方式 1支付宝证书  2支付宝秘钥 3手动', 98, '3', 'zhifubao');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (108, '2026-01-22 13:32:38', NULL, '公众号是否自动登录', 108, '是', 'kaiguan');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (118, '2025-02-21 23:03:53', NULL, '邀请赚钱推广内容', 101, '绿色充电 随充随走', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (120, '2020-07-27 15:17', NULL, '邀请赚钱微信分享内容', 103, '附近充电站一键导航，电瓶车汽车都能充，安全便捷更省心', 'xitongs');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (130, '2020-11-04 10:31:40', NULL, '提现最低额度', 112, '10', 'fuwufei');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (132, '2023-05-24 15:49:12', NULL, '文本审核内容 1微信小程序 2百度文本审核', 132, '1', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (134, '2025-02-21 22:31:33', NULL, '海报邀请语', 116, '绿色充电 随充随走', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (140, '2020-07-27 15:17', NULL, '腾讯地图key', 128, '', 'xitongs');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (146, '2023-05-19 09:56:49', NULL, '违规封禁触犯次数', 146, '200', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (153, '2023-05-16 19:28:12', NULL, '提问请求方式  5：阿里deepseek-r1(671B)', 147, '5', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (154, '2020-11-04 10:31:40', NULL, '是否开启APP微信分享', 136, '否', 'weixins');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (159, '2021-11-02 14:53:27', NULL, 'H5推广是否分享APP下载页面', 141, '否', 'xitongs');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (170, '2023-11-10 16:38:13', NULL, '提现手续费（用户提现时候的手续费，可以作为平台服务费使用，设置0.3就是百分之30，举例提现100服务费30，到账70）', 152, '0', 'fuwufei');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (171, '2021-09-06 16:19:51', NULL, '最高提现金额', 153, '1000', 'fuwufei');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (172, '2022-06-21 15:29:05', NULL, '提现金币比例', 154, '1', 'fuwufeis');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (173, '2022-01-25 14:33:48', NULL, '平台费率（0.3表示百分之30举例比如陪玩发布100用户看到价格上浮30价格就是130）', 155, '0', 'xitongs');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (174, '2022-08-19 13:25:59', NULL, '用户会员折扣费率（0.9表示百分之90）', 156, '1', 'fuwufeis');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (182, '2022-03-20 15:06:28', NULL, '短信宝用户名', 164, '', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (183, '2026-05-12 17:34:30', NULL, '短信宝密码', 165, '', 'duanxin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (200, '2021-07-19 11:32:12', NULL, '帮助中心', 175, '<p>本应用尊重并保护所有使用服务用户的个人隐私权。为了给您提供更准确、更有个性化的服务，本应用会按照本隐私权政策的规定使用和披露您的个人信息。但本应用将以高度的勤勉、审慎义务对待这些信息。除本隐私权政策另有规定外，在未征得您事先许可的情况下，本应用不会将这些信息对外披露或向第三方提供。本应用会不时更新本隐私权政策。 您在同意本应用服务使用协议之时，即视为您已经同意本隐私权政策全部内容。本隐私权政策属于本应用服务使用协议不可分割的一部分。</p><p>1. 适用范围</p><p>(a) 在您注册本应用帐号时，您根据本应用要求提供的个人注册信息；</p><p>(b) 在您使用本应用网络服务，或访问本应用平台网页时，本应用自动接收并记录的您的浏览器和计算机上的信息，包括但不限于您的IP地址、浏览器的类型、使用的语言、访问日期和时间、软硬件特征信息及您需求的网页记录等数据；</p><p>(c) 本应用通过合法途径从商业伙伴处取得的用户个人数据。</p><p>您了解并同意，以下信息不适用本隐私权政策：</p><p>(a) 您在使用本应用平台提供的搜索服务时输入的关键字信息；</p><p>(b) 本应用收集到的您在本应用发布的有关信息数据，包括但不限于参与活动、成交信息及评价详情；</p><p>(c) 违反法律规定或违反本应用规则行为及本应用已对您采取的措施。</p><p>2. 信息使用</p><p>(a)本应用不会向任何无关第三方提供、出售、出租、分享或交易您的个人信息，除非事先得到您的许可，或该第三方和本应用（含本应用关联公司）单独或共同为您提供服务，且在该服务结束后，其将被禁止访问包括其以前能够访问的所有这些资料。</p><p>(b) 本应用亦不允许任何第三方以任何手段收集、编辑、出售或者无偿传播您的个人信息。任何本应用平台用户如从事上述活动，一经发现，本应用有权立即终止与该用户的服务协议。</p><p>(c) 为服务用户的目的，本应用可能通过使用您的个人信息，向您提供您感兴趣的信息，包括但不限于向您发出产品和服务信息，或者与本应用合作伙伴共享信息以便他们向您发送有关其产品和服务的信息（后者需要您的事先同意）。</p><p>3. 信息披露</p><p>在如下情况下，本应用将依据您的个人意愿或法律的规定全部或部分的披露您的个人信息：</p><p>(a) 经您事先同意，向第三方披露；</p><p>(b)为提供您所要求的产品和服务，而必须和第三方分享您的个人信息；</p><p>(c) 根据法律的有关规定，或者行政或司法机构的要求，向第三方或者行政、司法机构披露；</p><p>(d) 如您出现违反中国有关法律、法规或者本应用服务协议或相关规则的情况，需要向第三方披露；</p><p>(e) 如您是适格的知识产权投诉人并已提起投诉，应被投诉人要求，向被投诉人披露，以便双方处理可能的权利纠纷；</p><p>(f) 在本应用平台上创建的某一交易中，如交易任何一方履行或部分履行了交易义务并提出信息披露请求的，本应用有权决定向该用户提供其交易对方的联络方式等必要信息，以促成交易的完成或纠纷的解决。</p><p>(g) 其它本应用根据法律、法规或者网站政策认为合适的披露。</p><p>4. 信息存储和交换</p><p>本应用收集的有关您的信息和资料将保存在本应用及（或）其关联公司的服务器上，这些信息和资料可能传送至您所在国家、地区或本应用收集信息和资料所在地的境外并在境外被访问、存储和展示。</p><p>5. Cookie的使用</p><p>(a) 在您未拒绝接受cookies的情况下，本应用会在您的计算机上设定或取用cookies ，以便您能登录或使用依赖于cookies的本应用平台服务或功能。本应用使用cookies可为您提供更加周到的个性化服务，包括推广服务。</p><p>(b) 您有权选择接受或拒绝接受cookies。您可以通过修改浏览器设置的方式拒绝接受cookies。但如果您选择拒绝接受cookies，则您可能无法登录或使用依赖于cookies的本应用网络服务或功能。</p><p>(c) 通过本应用所设cookies所取得的有关信息，将适用本政策。</p><p>6. 信息安全</p><p>(a) 本应用帐号均有安全保护功能，请妥善保管您的用户名及密码信息。本应用将通过对用户密码进行加密等安全措施确保您的信息不丢失，不被滥用和变造。尽管有前述安全措施，但同时也请您注意在信息网络上不存在“完善的安全措施”。</p><p>(b) 在使用本应用网络服务进行网上交易时，您不可避免的要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或者邮政地址。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。如您发现自己的个人信息泄密，尤其是本应用用户名及密码发生泄露，请您立即联络本应用客服，以便本应用采取相应措施。</p><p>7.本隐私政策的更改</p><p>(a)如果决定更改隐私政策，我们会在本政策中、本公司网站中以及我们认为适当的位置发布这些更改，以便您了解我们如何收集、使用您的个人信息，哪些人可以访问这些信息，以及在什么情况下我们会透露这些信息。</p><p>(b)本公司保留随时修改本政策的权利，因此请经常查看。如对本政策作出重大更改，本公司会通过网站通知的形式告知。</p>', 'xieyis');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (201, '2025-04-03 12:41:08', NULL, '隐私政策', 176, '<p class="ql-align-center"><strong>《西安省钱兄网络科技有限公司“充电桩”平台隐私政策》</strong></p><p><strong>提示条款：</strong></p><p><strong>西安省钱兄网络科技有限公司“充电桩”平台重视保护用户隐私，我们将按法律法规要求，采取相应安全保护措施，尽力保护用户个人信息的安全可控。鉴此，“充电桩”平台服务提供者（即西安省钱兄网络科技有限公司，以下简称“我们”或“充电桩”）制定本《“充电桩”平台隐私权保护政策》（下称“本政策/本隐私权政策”）并提醒您：</strong></p><p><strong>本政策适用于充电桩平台产品或服务。如我们关联公司的产品或服务中使用了充电桩提供的产品或服务（例如直接使用充电桩账户登录、会员真实姓名、身份证号码、电话号码等用于实名认证；获取会员地理位置的定位信息；用于拍摄照片、上传头像、分享图片及视频的开启相机/摄像头权限；提供语音聊天功能的麦克风权限、微信和支付宝的支付功能权限</strong>）但未设独立法律声明及隐私权政策的，则本政策同样适用于该部分产品或服务。</p><p>需要特别说明的是，<strong>本政策不适用于其他第三方向您提供的服务，也不适用于充电桩中已另行独立设置法律声明或隐私权保护政策的产品或服务。例如充电桩上的服务提供方依托充电桩向您提供服务时，您向服务提供方提供的个人信息不适用本政策。</strong></p><p><strong>在使用充电桩各项产品或服务前，请您务必仔细阅读并透彻理解本政策，在确认充分理解并“点击同意”后方可使用相关产品或服务。一旦您开始使用充电桩各项产品或服务，即表示您已充分理解并同意本政策。如对本政策内容有任何疑问、意见或建议，您可通过充电桩提供的各种联系方式与我们联系。</strong></p><p><strong>第一部分定义</strong></p><p><strong>充电桩：指充电桩（域名为：mianyazuju.com）网站、充电桩App客户端及充电桩微信小程序客户端。</strong></p><p><strong>充电桩服务提供者：指充电桩的网络及软件技术服务提供者西安省钱兄网络科技有限公司</strong></p><p><strong>公司地址：</strong>陕西省西安市高新区高新四路13号朗臣大厦1幢1单元11808室</p><p><strong>邮箱： </strong></p><p><strong>个人信息：指以电子或者其他方式记录的能够单独或者与其他信息结合识别特定自然人身份或者反映特定自然人活动情况的各种信息。本隐私政策中涉及的个人信息包括自然人的基本资料（包括个人姓名、生日、性别、住址、个人电话号码、电子邮箱地址）、个人身份信息（包括身份证件号码）、个人生物识别信息（包括指纹、面部特征）、网络身份标识信息（包括系统账号、IP地址、个人数字证书等）、个人财产信息（包括银行账号、口令、交易和消费记录、虚拟货币、虚拟交易、兑换码等虚拟财产信息）、通讯录信息、个人上网记录（包括网站浏览记录、软件使用记录、使用中的软件列表）、个人常用设备信息（包括硬件序列号、硬件型号、设备MAC地址、操作系统类型、软件列表、唯一设备识别码）、个人位置信息（包括大概地理位置、精准定位信息）。我们实际具体收集的个人信息种类以下文描述为准。</strong></p><p><strong>个人敏感信息：包括身份证件号码、个人生物识别信息、银行账号、财产信息、行踪轨迹、交易信息、14岁以下（含）儿童的个人信息等。</strong></p><p><strong>个人信息删除：指在实现日常业务功能所涉及的系统中去除个人信息的行为，使其保持不可被检索、访问的状态。唯一设备识别码（专属ID或UUID）：是指由设备制造商编入到设备中的一串字符，可用于以独有方式标识相应设备（如IMEI/Android ID/IDFA/OpenUDID/GUID/SIM卡IMSI信息等）。唯一设备识别码有多种用途，其中可在不能使用Cookie（例如在移动应用程序中）时用以提供广告。</strong></p><p><strong>第二部分法律声明权利归属</strong></p><p><strong>除非充电桩另行声明，充电桩内的所有产品、技术、软件、程序、数据及其他信息（包括文字、图标、图片、照片、音频、视频、图表、色彩组合、版面设计等）的所有权利（包括版权、商标权、专利权、商业秘密及其他相关权利）均归充电桩服务提供者及/或其关联公司所有。未经充电桩服务提供者及/或其关联公司许可，任何人不得以包括通过机器人、蜘蛛等程序或设备监视、复制、传播、展示、镜像、上载、下载等方式擅自使用充电桩内的任何内容。</strong></p><p><strong>充电桩的所有标识、徽记、产品和服务名称均为充电桩服务提供者及/或其关联公司在中国和其他国家的商标，如有宣传、展示等任何使用需要，您必须取得充电桩服务提供者及/或其关联公司事先书面授权。</strong></p><p><strong>责任限制</strong></p><p><strong>鉴于充电桩提供的信息发布服务属于电子公告牌性质，充电桩上的服务信息（包括但不限于服务提供者姓名/名称、联系人及联络信息、服务的描述和说明、相关图片、视频等）由用户自行提供并上传，由用户对其提供并上传的信息承担相应法律责任。充电桩服务提供者对此另有其他约定的，将在相关的协议或其他法律文本中与您进行明确。</strong></p><p><strong>充电桩转载的信息出于传递更多信息之目的，并不意味我们赞同其观点或已经证实其内容的真实性。</strong></p><p><strong>知识产权保护</strong></p><p><strong>我们尊重知识产权，反对并打击侵犯知识产权的行为。用户在充电桩平台发布的文字、图片和视频等相关信息的知识产权归属于平台用户和充电桩平台共同所有。知识产权权利人若认为充电桩内容（包括但不限于充电桩用户发布的商品信息、文字、图片及视频）侵犯其合法权益的，可以向充电桩平台提出投诉，我们将在收到知识产权权利人合格通知后依据相应的法律法规以及平台规则及时处理。</strong></p><p><strong>第三部分隐私权政策</strong></p><p><strong>本隐私权政策部分将帮助您了解以下内容：</strong></p><p><strong>1、我们如何收集和使用您的个人信息</strong></p><p><strong>2、我们如何使用Cookie 和同类技术</strong></p><p><strong>3、我们如何共享、转让、公开披露您的个人信息</strong></p><p><strong>4、我们如何保护您的个人信息</strong></p><p><strong>5、您如何管理您的个人信息</strong></p><p><strong>6、我们如何处理未成年人的个人信息</strong></p><p><strong>7、您的个人信息如何在全球范围转移</strong></p><p><strong>8、本隐私权政策如何更新</strong></p><p><strong>9、如何联系我们</strong></p><p><strong>一、我们如何收集和使用您的信息</strong></p><p><strong>我们会出于本政策所述的以下用途，收集和使用您的个人信息：</strong></p><p><strong>（一）账号注册</strong></p><p><strong>1、您注册并登录充电桩账号时，需要按照我们的指引完成一系列注册程序，在此过程中，您需要向我们提供以下信息：账号昵称、手机号码（个人敏感信息，用于接受验证码匹配个人身份）。您只有提供真实准确的上述信息，才能成功注册充电桩账号并使用产品和/或服务的核心功能。如果您选择不提供上述为实现充电桩核心产品功能的必备信息，或将导致我们无法为您提供该核心产品功能。</strong></p><p><strong>2、我们的产品支持您使用第三方平台的账号（微博账号/QQ账号/苹果账号）进行登录，如您使用第三方平台的账号登录的，我们将根据您的授权获取该第三方账号下的相关信息（包括：用户名、昵称、头像等）以及身份验证信息（个人敏感信息）。我们承诺，收集上述信息是用于为您提供账号登录服务以及保障您的账号安全，防范安全风险。如您拒绝授权此类信息，则您将无法使用第三方平台的账号登录我们平台，但不影响我们提供的其他产品和服务的正常使用。</strong></p><p><strong>3、同时，您需理解，手机号码和验证码匹配结果属于您的个人敏感信息。我们收集该类信息是基于法律法规的相关要求，如您拒绝提供可能导致您无法注册账号并使用相关产品功能，请您谨慎考虑后再选择是否提供。</strong></p><p><strong>（二）注册为活动发起人</strong></p><p><strong>用户在注册时需提供头像、相关必要的个人用户账号数据材料以及实名认证。我们向您承诺，我们会以最大努力保障您的个人信息安全，严格要求在线电子签约系统供应商对您的个人信息保密，只以实名认证之目的获悉和使用，不得对无关方泄露或做其他任何用途。若未实名认证则无法申请成为活动发起人，同时出于对未成年人的保护，不允许未成年人申请成为活动发起人进行平台交易。</strong></p><p><strong>（三）为您展示和推送商品或服务披露您的个人信息</strong></p><p><strong>为改善我们的产品或服务、向您提供个性化的信息搜索及交易服务，我们会根据您的浏览及搜索记录、设备信息、位置信息、订单信息，提取您的浏览、搜索偏好、行为习惯、位置信息、IMSI等特征，基于特征标签进行定向推送、精准营销，同时我们会以“推荐”、“个性化展示”等字样区分产品中的个性化推荐服务。</strong></p><p><strong>如您不希望接收到我们为您推送的个性化内容，您可以点击本协议中的“了解个性化推荐”进行管理，随时关闭或开启个性化服务，我的页面-设置中心-个性化推荐功能可手动关闭。</strong></p><p><strong>（四）搜索</strong></p><p><strong>当您在我们的产品中使用搜索功能时，我们需要收集您的一些信息，其中包括如下个人信息：您的设备信息、您的日志信息，包括您搜索的字或词、您的浏览记录和时间、您搜索的时间以及次数。我们收集这些信息是为了向您快速匹配您所需要的内容以及您可能感兴趣的内容，同时亦为了改进我们的产品和服务。</strong></p><p><strong>（五）动态</strong></p><p><strong>当您开启了您的设备定位功能并同时使用动态服务时，我们会向您展示您周边的动态信息、显示您与动态发布者发布位置的距离以及将您发布的动态信息推送至附近页面，为此我们可能会收集有关您的个人位置信息（其中精准定位信息为个人敏感信息）以及您设备的IP地址。</strong></p><p><strong>我们收集IP地址信息是为了向您提供展示附近动态的服务，如您拒绝提供上述权限将可能导致您无法使用我们的相关产品与服务。请您理解，单独的IP地址等是无法识别特定自然人身份的信息。如果我们将这类非个人信息与其他信息结合用于识别特定自然人身份，或者将其与个人信息结合使用，则在结合使用期间，这类非个人信息将被视为个人信息，除取得您授权或法律法规另有规定外，我们会将该类个人信息做匿名化、去标识化处理。</strong></p><p><strong>（六）录制视频、音频、实时语音</strong></p><p><strong>&nbsp;</strong></p><p><strong>当您使用录制视频、音频或实时语音服务时我们将获取访问您的设备录音（麦克风）相关权限，我们需要收集您的语音信息、语音交互信息（个人信息）。如您拒绝提供仅会使您无法使用该功能，但并不影响您正常使用产品与/或服务的其他功能。同时，您也可以随时通过您的设备系统功能设置页面开启/取消该权限。</strong></p><p><strong>当您使用发布视频动态或语音动态服务时我们将获取访问您的设备存储文件相关权限，我们需要读取您存储的视频文件或者音频文件（个人信息）。如您拒绝提供仅会使您无法使用该功能，但并不影响您正常使用产品与/或服务的其他功能。同时，您也可以随时通过您的设备系统或使用我们的产品与/或服务的相关功能设置页面开启/取消该权限。</strong></p><p><strong>（七）向您提供服务</strong></p><p><strong>1、您向我们提供的信息</strong></p><p><strong>为完成交易，您可能需要提供支付工具账号并选择付款方式，以便我们了解您的支付状态。在支付过程中，我们可能会收集您的第三方支付渠道的user ID（例如支付宝user ID、微信open ID、苹果apple ID）（个人敏感信息）。上述信息为我们向您提供您购买商品和/或服务所必须的信息，我们无法仅根据该信息获得您的个人财产信息，也不能识别特定自然人身份信息。</strong></p><p><strong>您可以通过充电桩为其他人订购服务，您需要提供该实际订购人的前述个人信息。在向充电桩提供该实际订购人的前述个人信息之前，您需确保您已经取得其授权同意。</strong></p><p><strong>2、我们在您使用服务过程中收集的信息</strong></p><p><strong>&nbsp;</strong></p><p><strong>为向您提供更契合您需求的页面展示和搜索结果、了解产品适配性、识别账号异常状态，我们会收集关于您使用的服务以及使用方式的信息并将这些信息进行关联，这些信息包括：设备信息：我们会根据您在软件安装及使用中授予的具体权限，接收并记录您所使用的设备相关信息（例如设备型号、设备MAC地址、操作系统版本、设备设置、唯一设备标识符等软硬件特征信息）、设备所在位置相关信息（例如IP 地址、GPS位置以及能够提供相关信息的WLAN接入点、蓝牙和基站等传感器信息）。</strong></p><p><strong>日志信息：当您使用我们的网站或客户端提供的产品或服务时，我们会自动收集您对我们服务的详细使用情况，作为有关网络日志保存。例如您的搜索查询内容、IP地址、浏览器的类型、电信运营商、使用的语言、访问日期和时间及您访问的网页记录等。</strong></p><p><strong>请注意，单独的设备信息、日志信息等是无法识别特定自然人身份的信息。如果我们将这类非个人信息与其他信息结合用于识别特定自然人身份，或者将其与个人信息结合使用，则在结合使用期间，这类非个人信息将被视为个人信息，除取得您授权或法律法规另有规定外，我们会将该类个人信息做匿名化、去标识化处理。</strong></p><p><strong>为展示您账户的订单信息，我们会收集您在使用我们服务过程中产生的订单信息用于向您展示及便于您对订单进行管理。</strong></p><p><strong>当您与我们联系时，我们可能会保存您的通信/通话记录和内容或您留下的联系方式等信息，以便与您联系或帮助您解决问题，或记录相关问题的处理方案及结果。</strong></p><p><strong>&nbsp;</strong></p><p><strong>3、我们通过您授权同意的方式获得收集到您的个人信息为确认交易状态及为您提供售后与争议解决服务，我们会通过您基于交易所选择的交易对象、支付机构等收集与交易进度相关的您的交易、支付、物流信息，或将您的交易信息共享给上述服务提供者。</strong></p><p><strong>如您拒绝提供上述信息或拒绝授权，可能无法使用我们关联公司的相应产品或服务，或者无法展示相关信息，但不影响使用充电桩浏览、搜索、交易等核心服务。</strong></p><p><strong>（八）为您提供安全保障</strong></p><p><strong>我们需要收集您的一些信息来保障您使用我们的产品与/或服务时的账号与系统安全，并协助提升我们的产品与/服务的安全性和可靠性，以防产生任何危害用户、充电桩平台、社会的行为，包括如下信息：账号登录地、个人常用设备信息、登录IP地址、产品版本号、浏览记录、服务故障信息。我们会根据上述信息来进行身份验证、客户服务、检测及防范安全事件、和备份用途，并依法采取必要的记录、审计、分析、处置措施，一旦我们检测出存在或疑似存在账号安全风险时，我们会使用相关信息进行安全验证与风险排除，确保我们向您提供的产品和服务的安全性，以用来保障您的权益不受侵害。同时，当发生账号或系统安全问题时，我们会收集这些信息来优化我们的产品和服务。</strong></p><p><strong>请注意，为确保会员身份真实性、向您提供更好的安全保障，您可以向我们提供身份证、护照、驾驶证、社保卡、居住证等身份信息、面部特征等生物识别信息完成实名认证。如您拒绝提供上述信息，可能无法使用账户管理、出售服务、继续可能存在风险的交易等服务，但不会影响您使用浏览、搜索等服务。</strong></p><p><strong>为提高您使用我们及我们关联公司、合作伙伴提供服务的安全性，保护您或其他用户或公众的人身财产安全免遭侵害，更好地预防钓鱼网站、欺诈、网络漏洞、计算机病毒、网络攻击、网络侵入等安全风险，更准确地识别违反法律法规或充电桩相关协议规则的情况，我们可能使用或整合您的会员信息、交易信息、设备信息、有关网络日志以及我们关联公司、合作伙伴取得您授权或依据法律共享的信息，来综合判断您账户及交易风险、进行身份验证、检测及防范安全事件，并依法采取必要的记录、审计、分析、处置措施。</strong></p><p><strong>（九）其他用途</strong></p><p><strong>如果我们将您的隐私信息用于本政策未载明的其他用途，或者将基于特定目的收集而来的信息用于其他目的时，会事先征求您的同意。</strong></p><p><strong>（十）征得授权同意的例外</strong></p><p><strong>根据相关法律法规规定，以下情形中收集您的个人信息无需征得您的授权同意：</strong></p><p><strong>1、与国家安全、国防安全有关的；</strong></p><p><strong>2、与公共安全、公共卫生、重大公共利益有关的；</strong></p><p><strong>3、与犯罪侦查、起诉、审判和判决执行等有关的；</strong></p><p><strong>4、出于维护个人信息主体或其他个人的生命、财产等重大合法权益但又很难得到您本人同意的；</strong></p><p><strong>5、所收集的个人信息是您自行向社会公众公开的；</strong></p><p><strong>6、从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道；</strong></p><p><strong>7、根据您的要求签订合同所必需的；</strong></p><p><strong>8、用于维护所提供的产品或服务的安全稳定运行所必需的，例如发现、处置服务的故障；</strong></p><p><strong>&nbsp;</strong></p><p><strong>9、为合法的新闻报道所必需的；</strong></p><p><strong>10、学术研究机构基于公共利益开展统计或学术研究所必要，且对外提供学术研究或描述的结果时，对结果中所包含的个人信息进行去标识化处理的；</strong></p><p><strong>11、法律法规规定的其他情形。</strong></p><p><strong>如我们停止运营充电桩产品或服务，我们将及时停止继续收集您个人信息的活动，并将停止运营的通知以逐一送达或公告的形式通知您，对所持有的个人信息进行删除或匿名化处理。</strong></p><p><strong>二、我们如何使用 Cookie 和同类技术</strong></p><p><strong>（一）Cookie</strong></p><p><strong>为确保网站正常运转、为您获得更轻松的访问体验、向您推荐您可能感兴趣的内容，我们会在您的计算机或移动设备上存储名为 Cookie 的小数据文件。Cookie 通常包含标识符、站点名称以及一些号码和字符。借助于Cookie，网站能够存储您的偏好或历史交易等数据。</strong></p><p><strong>您可根据自己的偏好管理或删除 Cookie。有关详情，请参见 AboutCookies.org。您可以清除计算机上保存的所有 Cookie，大部分网络浏览器都设有阻止 Cookie 的功能。但如果您这么做，则需要在每一次访问我们的网站时更改用户设置。如需详细了解如何更改浏览器设置，请访问您使用的浏览器的相关设置页面。</strong></p><p><strong>（二）网站信标和像素标签</strong></p><p><strong>除 Cookje 外，我们还会在网站上使用网站信标和像素标签等其他同类技术。例如，我们向您发送的电子邮件可能含有链接至我们网站内容的地址链接，如果您点击该链接，我们则会跟踪此次点击，帮助我们了解您的产品或服务偏好以便于我们主动改善客户服务体验。网站信标通常是一种嵌入到网站或电子邮件中的透明图像。借助于电子邮件中的像素标签，我们能够获知电子邮件是否被打开。如果您不希望自己的活动以这种方式被追踪，则可以随时从我们的寄信名单中退订。</strong></p><p><strong>三、我们如何共享、转让、公开披露您的个人信息</strong></p><p><strong>（一）共享</strong></p><p><strong>我们不会与充电桩服务提供者以外的公司、组织和个人共享您的个人信息，但以下情况除外：</strong></p><p><strong>1、在获取明确同意的情况下共享：获得您的明确同意后，我们会与其他方共享您的个人信息。</strong></p><p><strong>2、在法定情形下的共享：我们可能会根据法律法规规定、诉讼争议解决需要，或按行政、司法机关依法提出的要求，对外共享您的个人信息。</strong></p><p><strong>3、与关联公司间共享：为便于我们基于关联账号共同向您提供服务，推荐您可能感兴趣的信息或保护充电桩关联公司或其他用户或公众的人身财产安全免遭侵害，您的个人信息可能会与我们的关联公司共享。我们只会共享必要的个人信息（如为便于您使用充电桩账号使用我们关联公司产品或服务，我们会向关联公司共享您必要的账户信息），如果我们共享您的个人敏感信息或关联公司改变个人信息的使用及处理目的，将再次征求您的授权同意。</strong></p><p><strong>4、与授权合作伙伴共享：仅为实现本隐私权政策中声明的目的，我们的某些服务将由我们和授权合作伙伴共同提供。我们可能会与合作伙伴共享您的某些个人信息，以提供更好的客户服务和用户体验。例如，在您购买我们平台展示的服务时，我们可能会安排合作伙伴提供服务。我们仅会出于合法、正当、必要、特定、明确的目的共享您的个人信息，并且只会共享提供服务所必要的个人信息。我们的合作伙伴无权将共享的个人信息用于与产品或服务无关的其他用途。</strong></p><p><strong>目前，我们的授权合作伙伴包括以下类型：</strong></p><p><strong>（1）广告、分析服务类的授权合作伙伴。除非得到您的许可，否则我们不会将您的个人身份信息（指可以识别您身份的信息，例如姓名或电子邮箱，通过这些信息可以联系到您或识别您的身份）与提供广告、分析服务的合作伙伴共享。我们会向这些合作伙伴提供有关其广告覆盖面和有效性的信息，而不会提供您的个人身份信息，或者我们将这些信息进行汇总，以便它不会识别您个人。例如，只有在广告主同意遵守我们的广告发布准则后，我们才可能会告诉广告主他们广告的效果如何，或者有多少人看了他们广告或在看到广告后安装了应用，或者向这些合作伙伴提供不能识别个人身份的统计信息，帮助他们了解其受众或顾客。</strong></p><p><strong>（2）供应商、服务提供商和其他合作伙伴。我们将信息发送给支持我们业务的供应商、服务提供商和其他合作伙伴，这些支持包括提供技术基础设施服务、分析我们服务的使用方式、衡量广告和服务的有效性、提供客户服务、支付便利或进行学术研究和调查。</strong></p><p><strong>对我们与之共享个人信息的公司、组织和个人，我们会与其签署严格的数据保护协定，要求他们按照我们的说明、本隐私权政策以及其他任何相关的保密和安全措施来处理个人信息。我们的合作伙伴SDK或相关技术获取用户信息情况披露点，下方是我们所使用的第三方目录SDK目录清单，如您同意本政策，则视为您已同意我们按照第三方SDK目录处理您的个人信息。</strong></p><p><strong>关于我们接入的合作伙伴等第三方的SDK目录及说明请参见附件中的 《第三方的SDK目录及说明》。</strong></p><p><strong>&nbsp;</strong></p><p><strong>（二）转让</strong></p><p><strong>我们不会将您的个人信息转让给任何公司、组织和个人，但以下情况除外：</strong></p><p><strong>1、在获取明确同意的情况下转让：获得您的明确同意后，我们会向其他方转让您的个人信息；</strong></p><p><strong>2、在充电桩服务提供者发生合并、收购或破产清算情形，或其他涉及合并、收购或破产清算情形时，如涉及到个人信息转让，我们会要求新的持有您个人信息的公司、组织继续受本政策的约束，否则我们将要求该公司、组织和个人重新向您征求授权同意。</strong></p><p><strong>（三）公开披露</strong></p><p><strong>我们仅会在以下情况下，公开披露您的个人信息：</strong></p><p><strong>1、获得您明确同意或基于您的主动选择，我们可能会公开披露您的个人信息；</strong></p><p><strong>2、如果我们确定您出现违反法律法规或严重违反充电桩相关协议规则的情况，或为保护充电桩及其关联公司用户或公众的人身财产安全免遭侵害，我们可能依据法律法规或充电桩相关协议规则征得您同意的情况下披露关于您的个人信息，包括相关违规行为以及充电桩已对您采取的措施。例如，若您销售假货而严重违反充电桩规则，我们可能会公开披露您的店铺主体信息与处罚情况。</strong></p><p><strong>（四）共享、转让、公开披露个人信息时事先征得授权同意的例外</strong></p><p><strong>以下情形中，共享、转让、公开披露您的个人信息无需事先征得您的授权同意：</strong></p><p><strong>1、与国家安全、国防安全有关的；</strong></p><p><strong>2、与公共安全、公共卫生、重大公共利益有关的；</strong></p><p><strong>3、与犯罪侦查、起诉、审判和判决执行等有关的；</strong></p><p><strong>4、出于维护您或其他个人的生命、财产等重大合法权益但又很难得到本人同意的；</strong></p><p><strong>5、您自行向社会公众公开的个人信息；</strong></p><p><strong>6、从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道。</strong></p><p><strong>根据法律规定，共享、转让经去标识化处理的个人信息，且确保数据接收方无法复原并重新识别个人信息主体的，不属于个人信息的对外共享、转让及公开披露行为，对此类数据的保存及处理将无需另行向您通知并征得您的同意。</strong></p><p><strong>四、我们如何保护您的个人信息安全</strong></p><p><strong>（一）我们已采取符合业界标准、合理可行的安全防护措施保护您提供的个人信息安全，防止个人信息遭到未经授权访问、公开披露、使用、修改、损坏或丢失。例如，在您的浏览器与服务器之间交换数据（如信用卡信息）时受SSL （Secure Socket Layer）协议加密保护；我们同时对充电桩网站提供安全浏览方式我们会使用加密技术提高个人信息的安全性；我们会使用受信赖的保护机制防止个人信息遭到恶意攻击我们会部署访问控制机制，尽力确保只有授权人员才可访问个人信息以及我们会举办安全和隐私保护培训课程，加强员工对于保护个人信息重要性的认识。</strong></p><p><strong>（二）我们有行业先进的以数据为核心，围绕数据生命周期进行的数据安全管理体系，从组织建设、制度设计、人员管理、产品技术等方面多维度提升整个系统的安全性。</strong></p><p><strong>（三）我们只会在达成本政策所述用途所需的期限内或法律要求的期限内保留您的个人信息，除非延长保留期征得您的同意或受到法律的允许。</strong></p><p><strong>&nbsp;</strong></p><p><strong>（四）互联网并非绝对安全的环境，我们强烈建议您不要使用非充电桩推荐的通信方式发送个人信息。您可以通过我们的服务建立联系和相互分享。当您通过我们的服务创建交流、交易或分享时，您可以自主选择沟通、交易或分享的对象，作为能够看到您的交易内容、联络方式、交流信息或分享内容等相关信息的第三方。</strong></p><p><strong>在使用充电桩服务进行网上交易时，您不可避免地要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或联系地址。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。如您发现自己的个人信息尤其是您的账户或密码发生泄露，请您立即联络充电桩客服，以便我们根据您的申请采取相应措施。</strong></p><p><strong>请注意，您在使用我们服务时自愿共享甚至公开分享的信息，可能会涉及您或他人的个人信息甚至个人敏感信息，如您在评价时选择上传包含个人信息的图片。请您更加谨慎地考虑，是否在使用我们的服务时共享甚至公开分享相关信息。</strong></p><p><strong>请使用复杂密码，协助我们保证您的账号安全。我们将尽力保障您发送给我们的任何信息的安全性。如果我们的物理、技术或管理防护设施遭到破坏，导致信息被非授权访问、公开披露、篡改或毁坏，导致您的合法权益受损，我们将承担相应的法律责任。</strong></p><p><strong>（五）我们将不定期更新并公开安全风险、个人信息安全影响评估报告等有关内容，您可通过充电桩公告方式获得。</strong></p><p><strong>（六）在不幸发生个人信息安全事件后，我们将按照法律法规的要求向您告知安全事件的基本情况和可能的影响、我们已采取或将要采取的处置措施、您可自主防范和降低风险的建议、对您的补救措施等。事件相关情况我们将以邮件、信函、电话、推送通知等方式告知您，难以逐一告知个人信息主体时，我们会采取合理、有效的方式发布公告。</strong></p><p><strong>同时，我们还将按照监管部门要求，上报个人信息安全事件的处置情况。</strong></p><p><strong>五、您如何管理您的个人信息</strong></p><p><strong>您可以通过以下方式访问及管理您的个人信息：（一）访问您的个人信息</strong></p><p><strong>您有权访问您的个人信息，法律法规规定的例外情况除外。您可以通过以下方式自行访问您的个人信息：</strong></p><p><strong>账户信息——如果您希望访问或编辑您的账户中的个人基本资料信息和支付信息、更改您的密码、添加安全信息或关闭您的账户等，您可以通过登录账号通过“账号管理”执行此类操作。</strong></p><p><strong>搜索信息——您可以登录账号通过“我的足迹”访问或清除您的搜索历史记录。</strong></p><p><strong>订单信息您可以在充电桩中查阅或清除您的订单记录、交易记录等。</strong></p><p><strong>如果您无法通过上述路径访问该等个人信息，您可以随时通过充电桩客服与我们取得联系。</strong></p><p><strong>对于您在使用我们的产品或服务过程中产生的其他个人信息，我们将根据本条“（七）响应您的上述请求”中的相关安排向您提供。</strong></p><p><strong>（二）更正或补充您的个人信息</strong></p><p><strong>当您发现我们处理的关于您的个人信息有错误时，您有权要求我们做出更正或补充。您可以向我们提出更正或补充申请。</strong></p><p><strong>（三）删除您的个人信息</strong></p><p><strong>在以下情形中，您可以向我们提出删除个人信息的请求：</strong></p><p><strong>1、如果我们处理个人信息的行为违反法律法规；</strong></p><p><strong>2、如果我们收集、使用您的个人信息，却未征得您的明确同意；</strong></p><p><strong>3、如果我们处理个人信息的行为严重违反了与您的约定；</strong></p><p><strong>4、如果您不再使用我们的产品或服务，或您主动注销了账号；</strong></p><p><strong>5、如果我们永久不再为您提供产品或服务。</strong></p><p><strong>若我们决定响应您的删除请求，我们还将同时尽可能通知从我们处获得您的个人信息的主体，要求其及时删除，除非法律法规另有规定，或这些主体获得您的独立授权。当您从我们的服务中删除信息后，我们可能不会立即从备份系统中删除相应的信息，但会在备份更新时删除这些信息。</strong></p><p><strong>（四）改变您授权同意的范围</strong></p><p><strong>每个业务功能需要一些基本的个人信息才能得以完成。除此之外，对于额外个人信息的收集和使用，您可以与充电桩客服联系给予或收回您的授权同意。当您收回同意后，我们将不再处理相应的个人信息。但您收回同意的决定，不会影响此前基于您的授权而开展的个人信息处理。</strong></p><p><strong>（五）个人信息主体注销账户</strong></p><p><strong>我们向您提供账户注销的途径。在您符合国家相关法律法规规定及约定注销条件的情况下，可以通过本隐私政策预留的联系方式与充电桩客服联系注销您的账户或在线申请注销您的账户。示例如下：</strong></p><p><strong>在线申请注销：【我的】-【设置中心】-【注销账号】进行申请。</strong></p><p><strong>在您主动注销账户之后，我们将停止为您提供产品或服务，根据适用法律的要求删除您的个人信息，或使其匿名化处理。</strong></p><p><strong>（六）约束信息系统自动决策</strong></p><p><strong>&nbsp;</strong></p><p><strong>在某些业务功能中，我们可能仅依据信息系统、算法等在内的非人工自动决策机制做出决定。如果这些决定显著影响您的合法权益，您有权要求我们做出解释，我们也将在不侵害充电桩商业秘密或其他用户权益、社会公共利益的前提下提供申诉方法。</strong></p><p><strong>（七）响应您的上述请求</strong></p><p><strong>为保障安全，您可能需要提供书面请求，或以其他方式证明您的身份。我们可能会先要求您验证自己的身份，然后再处理您的请求。如您不满意，还可以通过充电桩客服发起投诉。对于您合理的请求，我们原则上不收取费用，但对多次重复、超出合理限度的请求，我们将视情收取一定成本费用。对于那些无端重复、需要过多技术手段（例如，需要开发新系统或从根本上改变现行惯例）、给他人合法权益带来风险或者非常不切实际的请求，我们可能会予以拒绝。</strong></p><p><strong>在以下情形中，按照法律法规要求，我们将无法响应您的请求：</strong></p><p><strong>1、与国家安全、国防安全有关的；</strong></p><p><strong>2、与公共安全、公共卫生、重大公共利益有关的；</strong></p><p><strong>3、与犯罪侦查、起诉、审判和执行判决等有关的；</strong></p><p><strong>4、有充分证据表明个人信息主体存在主观恶意或滥用权利的；</strong></p><p><strong>5、响应您的请求将导致您或其他个人、组织的合法权益受到严重损害的；</strong></p><p><strong>6、涉及商业秘密的；</strong></p><p><strong>六、我们如何处理未成年人的个人信息</strong></p><p><strong>如果没有父母或监护人的同意，未成年人不得创建自己的用户账户。如您为未成年人的，建议您请您的父母或监护人仔细阅读本隐私权政策，并在征得您的父母或监护人同意的前提下使用我们的服务或向我们提供信息。</strong></p><p><strong>&nbsp;</strong></p><p><strong>对于经父母或监护人同意使用我们的产品或服务而收集未成年人个人信息的情况，我们只会在法律法规允许、父母或监护人明确同意或者保护未成年人所必要的情况下使用、共享、转让或披露此信息。</strong></p><p><strong>七、您的个人信息如何在全球范围转移</strong></p><p><strong>我们在中华人民共和国境内运营中收集和产生的个人信息，存储在中国境内，以下情形除外</strong></p><p><strong>1、法律法规有明确规定；</strong></p><p><strong>2、获得您的明确授权；</strong></p><p><strong>3、您通过互联网进行跨境交易等个人主动行为。</strong></p><p><strong>针对以上情形，我们会确保依据本隐私权政策对您的个人信息提供足够的保护。</strong></p><p><strong>八、本隐私权政策如何更新</strong></p><p><strong>我们的隐私权政策可能变更。未经您明确同意，我们不会限制您按照本隐私权政策所应享有的权利。我们会在专门页面上发布对隐私权政策所做的任何变更。对于重大变更，我们还会提供更为显著的通知（包括我们会通过充电桩公示的方式进行通知甚至向您提供弹窗提示）。</strong></p><p><strong>本政策所指的重大变更包括但不限于：</strong></p><p><strong>1、我们的服务模式发生重大变化。如处理个人信息的目的、处理的个人信息类型、个人信息的使用方式等；</strong></p><p><strong>2、我们在控制权等方面发生重大变化。如并购重组等引起的所有者变更等；</strong></p><p><strong>3、个人信息共享、转让或公开披露的主要对象发生变化；</strong></p><p><strong>4、您参与个人信息处理方面的权利及其行使方式发生重大变化；</strong></p><p><strong>5、我们负责处理个人信息安全的责任部门、联络方式及投诉渠道发生变化时；</strong></p><p><strong>6、个人信息安全影响评估报告表明存在高风险时。我们还会将本隐私权政策的旧版本在充电桩专门页面存档，供您查阅。</strong></p><p><strong>九、如何联系我们</strong></p><p><strong>您可以通过以下方式与我们联系</strong></p><p><strong>1、如对本政策内容有任何疑问、意见或建议，您可通过充电桩客服与我们联系或通过充电桩邮箱( </strong><strong> )与我们联系；</strong></p><p><strong>2、如发现个人信息可能被泄露，您可以通过充电桩客服进行投诉举报或通过充电桩邮箱( </strong><strong> )投诉举报；</strong></p><p><strong>3、如果您对我们的回复不满意，特别是您认为我们的个人信息处理行为损害了您的合法权益，您还可以通过向被告住所地有管辖权的法院提起诉讼来寻求解决方案。</strong></p><p><strong>附录</strong></p><p><strong>附件一：《第三方的SDK目录及说明》</strong></p><p><strong>为了适配在不同手机设备上使用本产品以及第三方平台服务，实现您的手机设备身份认证、手机设备安全、接收信息推送、登录第三方平台账号、通过第三方平台分享信息以及支付安全，本产品接入了第三方SDK。本产品所接入的部分第三方SDK可能会调用您的设备权限、获取您的相关信息，以便您在不同的手机设备或第三方平台上正常使用相应功能，我们将这些具有个人信息收集功能的第三方SDK的名称、所提供的功能/服务、官网链接、隐私政策链接、申请调用的权限及收集个人信息类型，按照实现功能/服务的类别划分列明如下。</strong></p><p><strong>&nbsp;</strong></p><p><strong>微信 SDK</strong></p><p><strong>公司名称：深圳市腾讯计算机系统有限公司</strong></p><p><strong>使用目的：帮助用户使用微信账号登录，分享内容，同步信息</strong></p><p><strong>共享信息：设备标识信息（如IMEI/android ID/IDFA等）、需要分享的社交账户公开信息。</strong></p><p><strong>共享方式：SDK本机采集</strong></p><p><strong>使用场景：使用微信登录或将内容分享至微信，微信支付</strong></p><p><strong>官网地址：</strong><a href="https://open.weixin.qq.com/" rel="noopener noreferrer" target="_blank" style="color: rgb(112, 177, 231);"><strong>https://open.weixin.qq.com/</strong></a></p><p><strong>&nbsp;</strong></p><p><strong>支付宝 SDK</strong></p><p><strong>公司名称：支付宝（中国）网络技术有限公司</strong></p><p><strong>使用目的：支付、提现</strong></p><p><strong>共享信息：设备标识符、运营商信息、网络状态信息</strong></p><p><strong>共享方式：SDK本机采集</strong></p><p><strong>使用场景：使用支付宝支付或提现</strong></p><p><strong>官方说明或隐私政策：</strong><a href="https://opendocs.alipay.com/open/54" rel="noopener noreferrer" target="_blank" style="color: rgb(112, 177, 231);"><strong>https://opendocs.alipay.com/open/54</strong></a></p><p><strong>&nbsp;</strong></p><p><strong>腾讯地图 SDK</strong></p><p><strong>公司名称：深圳市腾讯计算机系统有限公司</strong></p><p><strong>使用目的：获取城市信息，定位</strong></p><p><strong>共享信息：IMEI、IMSI、设备MAC地址 ，地理位置信息。</strong></p><p><strong>共享方式：SDK本机采集</strong></p><p><strong>使用场景：推荐附近的人</strong></p><p><strong>官方说明或隐私政策：</strong><a href="https://map.qq.com/" rel="noopener noreferrer" target="_blank" style="color: rgb(112, 177, 231);"><u>https://map.qq.com/</u></a></p><p><strong>&nbsp;</strong></p><p><strong>Hbuilder </strong></p><p><strong>公司名称：数字天堂有限公司</strong></p><p><strong>使用目的：我们的产品基于DCloud uni-app(5+ App/Wap2App)开发</strong></p><p><strong>共享信息：应用运行期间需要收集您的设备唯一识别码（IMEI/android ID/DEVICE_ID/IDFA/SSID/BSSID、SIM 卡 IMSI 信息、OAID）以提供统计分析服务</strong></p><p>(1)设备信息、网络信息:必选信息:设备平台、设备厂商、设备品牌、设备型号及系统版本、OAID、AndroidlD、WIFI连接信息、运营商信息可选信息:IME1、MAC、GoogleAdvertising ID(GAID)、Serial Number、IMSI、<span style="color: rgb(102, 102, 102); background-color: rgb(246, 248, 250);">DHCP、</span>ICCID、/SSID/BSSID、IP地址、基站信息。用于生成唯一的推送目标ID(CID)和设备ID(GID)，以此保证消息推送的准确下发和消息设备的准确识别。(2)位置相关信息:应景推送、智能推送和应急推送扩展功能的必选信息，提供线下场景的精细化推送，可以为您推荐更符合您需要的推送内容，减少无用推送消息对您的打扰。应国家有关部门要求，，为了提升移动互联网技术服务社会公益的功能，发挥 APP消息推送在灾害预警方面的作用，当发生地震及气象等重大灾害时，通过开发者的APP 推送通道实时向受影响地区的用户下发相关的灾害通知信息(3)应用列表信息:智能推送、应急推送和链路调节器扩展功能的必选信息，推荐更符合您需要的推送内容，减少无用推送信息对您的打扰。应国家有关部门要求，为了提升移动互联网技术服务社会公益的功能，发挥 APP 消息推送在灾害预警方面的作用，当发生地震及气象等重大灾害时，通过开发者的APP 推送通道实时向受影响地区的用户下发相关的灾害通知信息。我们采用合并链路技术进行技术推送，当一个设备有多个APP的推送链路同时活跃时，我们会随机合并成一条链路，以达到为您省电省流量的目的，当一个设备有离线推过消息待下发的场景时，通过关联启动功能可以下发离线消息，您可以选择开启或关闭这项服务，当您关闭这项服务时，关联启动也同时会关闭，会导致电量和流量消耗加大的同时消息触达率也会降低</p><p><strong>共享方式：SDK本机采集</strong></p><p><strong>使用场景：通过应用启动数据及异常错误日志分析改进性能和用户体验，为用户提供更好的服务。</strong></p><p><br></p><h2><a href="https://uniapp.dcloud.io/tutorial/app-privacy-android" rel="noopener noreferrer" target="_blank" style="color: rgb(112, 177, 231);">https://uniapp.dcloud.io/tutorial/app-privacy-android</a></h2><p>&nbsp;</p><p><strong>&nbsp;</strong></p><p><strong>个推</strong></p><p><strong>公司名称：每日互动股份有限公司 </strong></p><p><strong>使用目的：推送消息</strong></p><p><strong>共享信息：我们可能会将您的设备平台、设备厂商、设备品牌、设备识别码等设备信息，应用列表信息、网络信息以及位置相关信息提供给每日互动股份有限公司，用于为您提供消息推送技术服务 </strong></p><p><strong>共享方式：SDK本机采集</strong></p><p><strong>使用场景：我们在向您推送消息时，我们可能会授权每日互动股份有限公司进行链路调节，相互促活被关闭的SDK推送进程，保障您可以及时接收到我们向您推送的消息。详细内容请访问《个推用户隐私政策》（需将《个推用户隐私政策》超链至：</strong><a href="http://docs.getui.com/privacy" rel="noopener noreferrer" target="_blank" style="color: rgb(112, 177, 231);"><strong>http://docs.getui.com/privacy</strong></a><strong>）</strong></p><p><strong>&nbsp;</strong></p><p><strong>高德开放平台</strong></p><p><strong>公司名称：高德软件有限公司</strong></p><p><strong>使用目的：获取用户位置推荐动态</strong></p><p><strong>共享信息：存储的个人文件、位置信息、读取手机状态和身份、网络信息、传感器信息</strong></p><p><strong>共享方式：SDK本机采集</strong></p><p><strong>使用场景：获取用户定位信息后推荐附近周边活动和动态</strong></p><p><a href="https://lbs.amap.com/agreement/compliance" rel="noopener noreferrer" target="_blank" style="color: rgb(112, 177, 231);"><strong>https://lbs.amap.com/agreement/compliance</strong></a></p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p><strong>&nbsp;</strong></p><p><strong>&nbsp;</strong></p><p><strong>&nbsp;</strong></p><p><strong>本隐私政策最后更新时间为：2025年3月1日。</strong></p><p><strong>生效日期：2025年3月1日</strong></p><p class="ql-align-justify">&nbsp;</p><p>&nbsp;</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (202, '2025-04-02 11:40:21', NULL, '注册协议', 177, '<p class="ql-align-center">《"充电桩"平台用户协议》</p><p>"充电桩"是西安省钱兄网络科技有限公司开发、运营的新能源充电服务平台，提供充电站查询、在线启动充电、订单管理与费用结算等服务。本协议是您（下称"用户"）与西安省钱兄网络科技有限公司（简称"充电桩平台运营方"）之间签订的协议。</p><p><strong>1、重要须知：</strong></p><p><strong>1.1用户欲下载安装充电桩，必须事先认真阅读（未成年人应当在监护人陪同下阅读）、充分理解本《"充电桩"用户协议》（下称"本协议"）中各条款，包括免除或者限制充电桩平台运营方责任的免责条款及对用户权利限制的条款。如用户不同意本协议，请不要使用并主动取消充电桩提供的服务。用户的使用行为将被视为用户对本协议全部的完全接受。</strong></p><p><strong>1.2 在用户接受本协议之后，此文本可能因国家政策、产品以及履行本协议的环境发生变化而进行修改，修改后的协议发布在充电桩官方平台上，有效代替原来的服务条款。若用户对修改后的协议有异议的，请立即停止访问、使用充电桩，用户继续访问或使用的行为，视为对修改后的协议予以认可。</strong></p><p>1.3 用户使用<strong>充电桩</strong>服务，须自行配备上网和使用电信增值业务所需的设备，自行负担个人上网或第三方（包括但不限于电信、移动通信服务提供商）收取的通讯费、信息费等有关费用。</p><p>1.4充电桩向用户提供丰富的互联网服务，如果某一特定服务有单独的服务条款、规则、指引，用户须遵守该单独的服务条款、规则和指引，用户在接受单独的服务条款、规则、指引的同时，仍应遵守本协议。</p><p><strong>2、关于充电桩：</strong></p><p>2.1充电桩是一个同城组局、扫码启动充电、商城购物、交友平台，为用户提供交友服务、活动发布、线上交易功能、交易评价系统等服务，用户可通过充电桩平台接收到其他用户发送的动态信息、自主选择交易对象并达成在线交易。前述的技能交易包括但不限于活动组队、教程分享、心得交流等辅助服务。</p><p>2.2用户使用本服务需要下载充电桩客户端软件，对于这些软件，充电桩平台运营方给予用户一项个人的、不可转让及非排他性的许可。用户仅可为非商业目的而访问或使用这些软件及服务。</p><p>2.3充电桩客户端软件提供iOS、Android等应用版本，用户必须选择与所安装手机相匹配的软件版本。</p><p>2.4用户可以直接从充电桩平台运营方的网站上获取本软件，也可以从得到充电桩平台运营方授权的第三方获取。如果用户从未经充电桩平台运营方授权的第三方获取本软件或与本软件名称相同的安装程序，充电桩平台运营方无法保证该软件能够正常使用，并对因此给用户造成的损失不予负责。</p><p>2.5用户使用充电桩服务，即表示用户愿意接受充电桩服务可能发出的相关邮件或系统消息。因此而有可能给用户带来的不便，用户可及时向充电桩提出意见或建议，充电桩平台运营方不负担任何责任。</p><p>2.6充电桩提供的服务中可能包含广告或者其他国际互联网网站、资源的链接，用户同意在使用过程中显示前述信息。但对于前述广告、网站或资源是否可以利用以及利用的后果，充电桩平台运营方不予担保。用户因使用或依赖上述网站或资源所产生的损失或损害，充电桩平台运营方也不承担任何责任，由用户自行承担。</p><p>2.7为了向用户提供更便捷、优质的服务，充电桩经用户许可后会将用户的手机通讯录上传到服务器中，并根据通讯录内容向用户推送好友推荐信息。用户知悉并授权充电桩进行前述的上传、推送行为。</p><p><strong>2.8 因业务发展需要，充电桩平台运营方保留单方面对本服务的全部或部分服务内容在任何时候不经任何通知的情况下变更、暂停、限制、终止或撤销充电桩服务的权利，用户需承担此风险。</strong></p><p>2.9本协议其他条款未明示授权的其他一切权利仍由<strong>充电桩</strong>公司保留，用户在行使这些权利时须另外取得<strong>充电桩</strong>公司的书面许可。<strong>充电桩</strong>公司如果未行使前述任何权利，不构成对该权利的放弃。</p><p><strong>3、用户使用条款：</strong></p><p>3.1用户在使用本服务前需要注册一个<strong>充电桩</strong>账号。充电桩平台运营方有权根据用户需求或产品需要对账号注册、登录方式进行变更。</p><p>3.2<strong>充电桩</strong>账号的所有权归<strong>充电桩</strong>公司所有，用户完成申请注册手续后，仅获得<strong>充电桩</strong>账号的使用权，且该使用权仅属于初始申请注册人。同时，初始申请注册人不得赠与、借用、租用、转让或售卖充电桩账号或者以其他方式许可非初始申请注册人使用充电桩账号。非初始申请注册人不得通过受赠、继承、承租、受让或者其他任何方式使用充电桩账号。</p><p>3.3用户充分了解并同意，须对在充电桩上填写的注册信息、身份资料信息的真实性、合法性、有效性承担全部责任，用户不得冒充他人、不得利用他人的名义传播任何信息;否则充电桩平台运营方有权立即停止提供服务，收回充电桩账号并由用户独自承担由此而产生的一切法律责任。</p><p>3.4 用户有责任妥善保管注册帐户信息及帐户密码的安全，用户需要对注册帐户以及密码下的行为承担法律责任。用户同意在任何情况下不向他人透露帐户及密码信息。在用户怀疑他人使用自己的账号时，应立即通知充电桩平台运营方，充电桩平台运营方将进行核查，未及时通知所造成的损失由用户自行承担。</p><p>3.5 用户注册充电桩账号后如果超过【365】日不登录充电桩平台运营方注册账号，充电桩平台运营方有权回收该账号，由此带来的任何损失均由用户自行承担。</p><p>3.6用户不得利用本服务制作、复制、发布、传播、存储含有下列内容的信息：</p><p>1）反对宪法所确定的基本原则的;</p><p>2）危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的;</p><p>3）损害国家荣誉和利益的;</p><p>4）煽动民族仇恨、民族歧视，破坏民族团结的;</p><p>5）破坏国家宗教政策，宣扬邪教和封建迷信的;</p><p>6）散布谣言，扰乱社会秩序，破坏社会稳定的;</p><p>7）散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的;</p><p>8）侮辱或者诽谤他人，侵害他人合法权益的;</p><p>9）含有法律、行政法规禁止的其他内容的。</p><p>3.7 用户未经充电桩平台运营方事先书面同意，不得利用充电桩服务进行任何诸如发布广告、销售商品、进行调查、绕开充电桩平台与其他用户达成线下交易等的商业行为，或者进行任何非法侵害充电桩平台运营方利益的行为，否则将承担违约责任。</p><p>3.8 用户不得利用本软件发布、传播违法信息、虚假信息，损害任何第三方的名誉权、隐私权、肖像权、知识产权等合法权益的信息，或者发布垃圾信息，与本服务无关的广告信息骚扰信息。</p><p>3.9用户在充电桩服务中或通过充电桩服务所传送的任何内容并不反映充电桩平台运营方的观点或政策，用户须对在充电桩上传、分享信息的真实性、合法性、无害性、有效性等承担全部责任。与用户所传播信息相关的任何法律责任由用户自行承担，与充电桩平台运营方无关，若因此给充电桩平台运营方造成损害的，还应承担赔偿责任。</p><p>3.10用户应对其他用户发布的内容自行加以判断，并承担因使用该内容而引起的所有风险，包括因对内容的真实性、正确性、完整性、实用性及适商性的依赖而产生的风险。充电桩平台运营方无法且不会对因用户行为而导致的任何损失或损害承担责任。</p><p>3.11用户不得对充电桩或者该软件运行过程中释放到任何终端中的数据及该软件运行过程中客户端与服务器端的交互数据进行复制、更改、修改、挂接运行或创作任何衍生作品，形式包括但不限于通过非充电桩平台运营方开发、授权或认可的三方兼容软件、插件、外挂，登录或使用充电桩及服务。</p><p>3.12除非充电桩平台运营方同意，用户不可对本服务任何部分的任何信息进行复制、拷贝、出售。用户不得使用任何手段删除、修改本软件展示的信息，不得对充电桩进行反向工程、反向汇编、反向编译等。</p><p>3.13 用户不得通过修改或伪造充电桩软件作品运行中的指令、数据、数据包，增加、删减、变动软件的功能或运行效果，不得将用于上述用途的软件通过信息网络向公众传播或者运营，否则将承担违约责任。</p><p>3.14用户不得进行任何危害计算机网络安全的行为，包括但不限于：&nbsp;使用未经许可的数据或进入未经许可的服务器/账号;未经允许进入公众计算机网络或者他人计算机系统并删除、修改、增加存储信息;未经许可，企图探查、扫描、测试本"软件"系统或网络的弱点或其它实施破坏网络安全的行为;企图干涉、破坏充电桩软件系统或网站的正常运行，故意传播恶意程序或病毒以及其他破坏干扰正常网络信息服务的行为;伪造TCP/IP数据包名称或部分名称。</p><p>3.15用户未经充电桩平台运营方许可，不得将充电桩安装在未经明示许可的其他终端设备上。包括但不限于电视盒、机顶盒、手持设备、电话、无线上网机、活动机、电视机、DVD机等。</p><p>3.16用户不得以任何不合法的方式、为任何不合法的目的、或以任何与本协议不一致的方式使用本软件和提供的其他服务。</p><p>3.17用户应确保用户使用本软件和提供的有偿服务中使用的交易资产是用户依法取得的合法财产，并接受与配合充电桩平台运营方及政府有关部门的资金合法来源、反洗钱、廉洁等监督检查工作。否则，用户应当按照法律法规承担相应责任，由此给充电桩平台运营方及相关方造成损失的，用户亦需承担相应的责任。如充电桩平台运营方发现用户行为涉嫌或可能涉嫌洗钱活动的，充电桩平台运营方依法有权向反洗钱行政部门或公安机关举报。</p><p>3.18用户应遵守本协议的各项条款，正确、适当地使用本服务，如因用户违反本协议中的任何条款，充电桩平台运营方有权终止对违约用户提供服务。同时，充电桩平台运营方保留在任何时候收回充电桩账号的权利。</p><p><strong>4、充电桩声明：</strong></p><p>4.1充电桩平台运营方有权对用户使用充电桩的情况进行监督，如经由通知、举报等途径发现用户有在使用充电桩提供的网络服务时违反任何本协议规定的嫌疑，充电桩平台运营方可依合理判断，对违反有关法律法规或本协议约定，或侵犯、妨害、威胁任何人权利或安全的内容，或者假冒他人的行为，充电桩平台运营方有权依法停止传输任何前述内容，并有权依自行判断对违反本条款的任何人士采取适当的法律行动，包括但不限于，从充电桩服务中过滤、屏蔽、删除具有违法性、侵权性、不当性等内容，终止违反者的成员资格，阻止其使用充电桩全部或部分服务，并且依据法律法规保存有关信息并向有关部门报告等。</p><p>4.2 用户使用充电桩服务必须遵守国家有关法律和政策，维护国家利益，保护国家安全，并遵守本条款，对于用户违法或违反本协议的使用而引起的一切责任，由用户承担全部责任。</p><p>4.3充电桩服务同大多数互联网产品一样，易受到各种安全问题的困扰，包括但不限于：</p><p>1）用户透露详细个人资料，被不法分子利用，造成现实生活中的骚扰。</p><p>2）被哄骗、破译密码。</p><p>3）下载安装的其它软件中含有木马或病毒，威胁到个人终端机器上的信息和数据安全，继而威胁对本服务的使用。</p><p>对于发生上述情况的，用户应当自行承担责任。</p><p>4.4 用户理解，互联网技术的不稳定性，可能导致政府政策管制、病毒入侵、黑客攻击、服务器系统崩溃或者其他现今技术无法解决的风险发生，可能导致充电桩平台运营方服务中断或账号财产（含虚拟财产）损失，用户对此非人为因素引起的损失，充电桩平台运营方不承担责任。</p><p>4.5 对于系统发生故障影响到充电桩服务的正常运行，充电桩平台运营方承诺一经发现便及时处理、进行修复。但用户因此而产生的经济和精神损失，充电桩平台运营方不承担责任。</p><p><strong>5、知识产权：</strong></p><p>5.1充电桩平台运营方是本软件的知识产权权利人。本软件的著作权、商标权、专利权、商业秘密等知识产权，以及与本软件相关的信息内容（包括但不限于文字、图片、音频、视频，图表、界面设计、版面框架、有关数据或电子文档等）均受中华人民共和国法律法规和相应的国际条约保护，充电桩平台运营方享有上述知识产权，但其他相关权利人依照法律规定应享有的权利除外。</p><p>5.2本软件可能会使用第三方软件或技术，这种使用已经获得合法授权。前述的授权文件或其他文件，均是本协议不可分割的组成部分，与本协议具有同等的法律效力，用户应当遵守这些要求。如果用户没有遵守这些要求，该第三方或者国家机关可能会对用户提起诉讼、罚款或采取其他制裁措施，并要求充电桩平台运营方给予协助，用户应当自行承担法律责任。</p><p>5.3 如因本软件使用的第三方软件或技术引发的任何纠纷，应由该第三方负责解决，充电桩平台运营方不承担任何责任。充电桩平台运营方不对第三方软件或技术提供客服支持，若用户需要获取支持，请与第三方联系。</p><p>5.4用户使用充电桩服务，只能在本协议以及相应的授权许可协议范围使用充电桩平台运营方的知识产权，未经授权超越范围使用的将构成对充电桩平台运营方的侵权。</p><p>5.5用户在使用充电桩服务时发布上传的文字、图片等用户原创的信息，及提供的技能服务（包括但不限于活动组队、教程分享、心得交流等活动辅助服务） 所衍生的第三方活动账号信息、虚拟角色属性、视频、文字、语音等互联网信息，此部分信息的知识产权归用户所有，但用户的发表、上传行为是对充电桩平台运营方的授权，用户确认其发表、上传的信息非独占性、永久性的授权充电桩平台运营方使用，且该项授权为可转授权，充电桩平台运营方及其旗下的服务平台可使用、再次编辑后使用前述信息，也可以授权给合作方使用。</p><p>5.6 若充电桩发布的信息以及其他用户上传、分享的信息有侵犯用户或第三人的知识产权或其他权利的，用户可通过投诉通道反馈意见。</p><p>5.7 若用户认为自己的著作权/信息网络传播权被侵犯，请向充电桩发出权利通知书。充电桩将根据中国法律法规和政府规范性文件采取措施删除被控作品内容或断开与被控作品的链接。</p><p>5.8充电桩根据前述原因删除相关内容的，被删除内容的提供者可以向充电桩发出关于被删除内容不侵犯知识产权/合法权益的反通知。反通知发出后，充电桩可以恢复被删除的内容或链接，且依法对该恢复行为不承担法律责任。</p><p><strong>6、隐私政策：</strong></p><p>6.1保护用户个人信息是充电桩平台运营方的一项基本原则。充电桩将采取合理的措施保护用户的个人信息，包括但不限于采用专业加密存储与传输方式，保障用户个人信息的安全。除法律法规规定的情形外，充电桩未经用户许可不会向第三方公开、透露用户的个人信息。</p><p>6.2 用户在注册账号或使用本服务的过程中，可能需要提供一些必要的信息，例如：为向用户提供账号注册服务或进行用户身份识别，需要用户填写手机号码或邮箱账号等信息;手机通讯录匹配功能需要用户授权访问手机通讯录; 根据国家法律法规或政策规定，用户需要提供真实的身份信息等。若用户提供的信息不完整，则无法使用本服务或在使用过程中受到限制。</p><p>6.3充电桩提醒用户注意，切勿在充电桩服务中透露自己的各类财产账户、银行卡、信用卡、第三方支付账户及对应密码等重要资料，否则由此带来的任何损失由用户自行承担。</p><p>6.4用户在使用充电桩服务时不可将自认为隐私的信息发表、上传至充电桩及网站，也不可将该等信息通过充电桩的服务传播给其他人，若用户的行为引起的隐私泄漏，由用户承担责任。</p><p>6.5就下列相关事宜的发生，充电桩不承担任何法律责任：</p><p>6.5.1充电桩平台运营方根据法律规定或相关政府、司法机关的要求提供用户的个人信息;</p><p>6.5.2由于用户将用户密码告知他人或与他人共享注册账户，由此导致的任何个人信息的泄漏，或其他非因充电桩平台运营方原因导致的个人信息的泄漏;</p><p>6.5.3任何由于黑客攻击、电脑病毒侵入造成的信息泄漏;</p><p>6.5.4因不可抗力导致的信息泄漏。</p><p>7、其他条款：</p><p>7.1本协议条款的签订、解释以及争议的解决均适用中华人民共和国法律。</p><p>7.2本协议的签署地点为西安市高新区，若用户与充电桩平台运营方发生法律争议的.双方同意将争议提交西安市高新区人民法院诉讼解决。</p><p>7.3违反本协议约定条款均视为违约，违约方需向守约方承担赔偿责任，且守约方有权根据实际情况采取如下措施：</p><p>（1）提出口头或书面警告，要求违约方限期内纠正违约行为，若在限期内，违约方仍未纠正履约的，则守约方有权解除合同并要求违约方赔偿损失；</p><p>（2）要求对方继续履行合同，对因违约行为所致的损失，要求违约方承担赔偿责任；</p><p>（3）解除合同终止合作并要求违约方赔偿损失。</p><p>损失无法计算的按【 100000 】元计。</p><p>7.4如产生违约金、赔偿金、退款等款项支付义务的，充电桩均有权在您账户余额中直接扣除。</p><p>7.5本协议所述损失包括但不限于直接经济损失，向第三方支付的违约金、赔偿金，行政处罚款，因维权产生的律师费、诉讼费、保全费、调查费、差旅费等。</p><p>7.6本协议在安装登陆充电桩软件的页面或充电桩平台运营方网页上展示，对用户和充电桩平台运营方具有法律约束力，用户在安装登陆界面或网页上接受本协议，对用户具有法律约束力。</p><p>&nbsp;</p><p>&nbsp;</p><p>《活动组织发布协议》</p><p>为了保障本程序的合法性和安全性。发布者需要提交程序的相关信息和材料，系统会进行审核，确保发布内容符合相关规定和要求，同时保障用户的信息安全和隐私权。如果发布者存在违规行为，如包含、恶俗、色情、赌博等内容，系统有权不予审核，并通知相关部门处理。发布者需要承担相应的法律责任。协议书是保障程序合法性和安全性的重要措施。发布者需要遵守相关规定和要求，确保程序的质量和用户体验，同时保障用户的信息安全和隐私权.</p><p>&nbsp;</p><p>&nbsp;</p><p>一、声明:</p><p>1、该协议的目的是为了活动发起人、组织者或领队(以下协议中简称:发起者)和同行成员明确知晓户外活动所存在的风险，提高参加人员的抗风险和自律能力，免除活动的发起人和同行成员在活动中出现的相关赔偿及法律连带等责任，让户外活动更加安全、健康、快乐、有意义。</p><p>2、活动中，对于违反国家相关法规、恶意侵犯他人或其它涉及犯罪行为的事件，则不在此协议范围内，必须由个人承担相应的法律责任。</p><p>3、凡报名参加活动的人员，均视为具有完全民事行为能力的人，均视为你已经仔细阅读和完全理解并同意接受《免责协议书》的全部条款，并要在注册成为会员时签字和在本协议上签字。</p><p>4、此协议为相关责任的豁免、权利的放弃、风险的承担和免赔的协议。你如果在此协议上签字，潜在代表你就已经完全知晓、理解和同意接受《免责协议书》全部条款，你就放弃了向活动发起者和其他成员提起诉讼的权利;永远免除活动的发起者和同行成员的赔偿及法律连带责任。</p><p>二、风险的承担:</p><p>我参加户外活动是在完全自愿的基础上，依签署此有关豁免、权利放弃和风险自担的协议为前提条件的。我知晓活动的发起者只是活动的联系人，并非职业的领队、向导或有许可证的急救人员。我知晓领队、向导和急救人员及同行的成员可能并没有参加过户外生存课程或急救(包括野外)的培训和经验。我知晓他们并不能对我安全负责。</p><p>三、关于豁免责任，放弃权利和赔偿的协议内容:</p><p>1、本次活动非商业活动，非旅行社组团旅游，也非赢利性质自助游活动;是属于个人设计的出行日期及路线，活动性质仅限共同爱好者自愿参加、共同参与、风险自担、责任自负;全程遵循“自助，互助，低碳环保”的原则;发起者和其它参加活动人员一样，同样也是自愿参与者，对其他参加者没有绝对的管理和支配权力;对于活动中突发的一切足以影响到大家切身安全和利益的不可抗因素等重大事项尚需参加者共同商定;活动中发起者仅有义务对全队活动进行组织协调，参加者也有义务服从活动的整体安排。</p><p>2、凡报名参加本次活动的人员均视为你已经仔细阅读、完全理解责任豁免、权利的放弃、风险的承担和赔偿的协议的全部条款内容。代他人报名需告知被代报人《户外活动免责协议书》的全部条款，参加活动的被代报人也视同接受了本协议全部条款。同时，我知晓报名前必须事先与自己的家属沟通，取得家属的理解和支持，知晓并同意接受本协议全部条款后，报名后视同其家属也已知情并同意。</p><p>3、户外运动不同于常规的旅游活动，采取的线路也不是一般的常规旅游线路，有时气象条件也非常规，而是具有一定的探险性质;该项运动本身最大的特点在于其具有一定的风险性;活动安排的时间是估算时间，实际活动时间可能出现较大的差异;报名前，我已阅读活动内容和《户外活动免责协议书》的全部条款，并经过合理判断，同意接受本活动计划和《户外活动免责协议书》全部条款，同意自行承担本次活动的所有的风险和后果;包括交通工具及其他第三方设施带来的风险;如果由于我的行为和我的参加而导致了第三方的财产损失或个人伤害，我同意免除所有其他人的赔偿连带责任。</p><p>4、参加本次活动，包括登山、徒步、郊游、住宿(露营)、就餐以及活动和装备的使用等都存在有潜在危险，可能会受到身体损伤、瘫痪、甚至死亡的风险，我也知道健身和休闲等活动也存在突发病、心脏病等甚至死亡的风险;我自愿参加这些活动和使用活动的装备，如果因为在参加本活动中受到伤害，我同意自行承担和接受任何和所有伤害的风险，甚至死亡的风险，并且，放弃追究和要求赔偿的</p><p>权利，同时我永远免除此次活动发起者和同行成员的赔偿及法律连带责任。</p><p>5、本次活动可能对身体和精神方面都有一定考验，可能遇到在恶劣天气中进行活动的可能，也可能远离救助和医疗服务地点，短时间(或24小时)内无法得到救援;我清楚的知晓活动发起者无法全面预见该活动中所有的风险和不利，我知道以上所列的风险并不完全包含本次活动中的所有可能的风险;当我万一遇到上述等伤害后，所有救援和医疗等全部费用我同意全部承担，活动的发者和其它参加者不承担所发生的一切费用和法律连带责任;同时我永远免除此次活动的发起者和同行成员的赔偿和法律责任，并不仅限于以上所列的风险中。</p><p>6、我清楚知道此次活动发起者不是保险公司，没有义务为我购买意外保险等险种，我知道自己购买保险的意义，以防万一;我知道发起者已在活动内容中提醒我本人，在活动开始前自己了解保险类别和购买保险;对于活动中可能出现的意外事故，可由投保的保险公司承担相应的责任。</p><p>8在此我谨宣布我自己身体健康，适合参加户外骑行活动，没有那些会影响我参加户外骑行活动或使用装备的不适、损伤、病痛、心脏病、突发病历或其它疾病;我理解我能够通过以下措施来减少我的风险:注意环境，注意我的身体和精神状况，注意所有与安全相关的衣物和装备的适用状况，只参加那些自己精神和身体能力之内的活动;我清楚知道，我的安全是我个人的责任和取决于我的警惕和良好的判断;我同意和保证，如果任何时候我认为是不安全的，那么我会立刻中断活动的进行。如我在活动中发生各种疾病或伤害，甚至死亡，我同意放弃追究和要求赔偿的权利，同时我永远免除活动发起者和同行成员的赔偿及法律连带责任。</p><p>9当由于出现各种意外事故、突发气候变化和急性疾病等不可预测因素造成身体损害时，发起者和同行成员有义务尽力救助，但如果造成了不可逆转的损害，我同意放弃追究和要求赔偿的权利，同时我永远免除此次活动发起者和同行成员的赔偿及法律连带责任;我完全理解，在同伴需要协助的时候，我将配合协助发起者及同行成员尽力协助我的同伴，但这只是在以下情况:在我的判断中，这是自己力所能及的范围内，并且不会给自己带来不合理的危险;我进一步理解到，我没有法律上的责任去协助别人，同时，发起者以及我的同伴或者其他成员也没有任何法律上的责任来协助我。</p><p>10、我在此特意和有意的声明:我已经年满18岁!我已阅读和理解以上声明和全部条款内容;我理解参加户外活动相关的风险;我同意承担由于选择和参加以后的户外活动而随之来的全部责任;对于参加户外活动而引发或与之连带的任何和所有的法律要求(包括第三方的法律要求)和无论出现任何形式和性质的害伤，无论这些法律要求是基于某方的疏忽或其它原因引起的，我永远免除活动发起者和同行成员、任何第三方的赔偿及法律责任</p><p>11、我知晓报名前必须事先与自己的家属沟通，取得家属的理解和支持，知道并同意接受本协议全部条款后，才能报名参加本次活动，报名后视同其家属也已知情并同意;我知晓，一旦签署，此协议将生效，也知晓本协议同样有效于我的继承人、近亲、执行人、管理人、个人代表和转让人。代为签名者视为已沟通并被授权，否则由代签人承担后果。</p><p>12、协议范围:凡是报名参加并注册成为会员者均视为已经阅读、理解，并完全接受该协议条款;该协议自注册成为会员后开始生效，至退出成员时终止。(至集合地点前和活动结束解散后的路途，已不属于活动范围内)。(协议中的本次活动指同城服务活动每次举行的活动)。所产生的效力直至本人退出活动为止。</p><p>&nbsp;</p><p>&nbsp;</p><p>《“充电桩”平台用户行为守则》</p><p>为维护平台正常秩序，西安省钱兄网络科技有限公司“充电桩”平台特颁布本《“充电桩”平台用户行为守则》，同时对入驻我方平台的“达人和活动发起人”和使用我方平台寻找达人和活动发起人的“用户”违规行为制定相应处罚机制。</p><p>“充电桩”平台“用户”和“达人和活动发起人”若违反以下行为，一经发现将视情节轻重给予处罚：</p><p>违反法律法规及涉及道德底线</p><p>发布内容或用户行为涉嫌反动、反社会道德、反政治类或政治相关，宣扬恐怖、邪教、封建迷信、损害国家荣誉和利益、破坏民族团结、危害国家安全、泄露国家秘密等。</p><p>处罚规则：永久封停账户。</p><p>“用户”和“达人和活动发起人”传播色情淫秽、低俗擦边信息，发布涉及淫秽色情、低俗信息、描绘性行为或性暗示的图片或文字</p><p>具体情形包括且不限于：</p><p>1、使用暴露、性暗示、色情、低俗头像；&nbsp;</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次永久封停账户。</p><p>2、 昵称、个性签名、介绍、资质说明、资质图等个人资料使用色情擦边、淫秽、性暗示词汇；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次永久封停账户。</p><p>3、在聊天中发布以色情为目的色情语音、色情视频、色情图片、色情内容等；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次永久封停账户。</p><p>4、在消息、动态等公共环境中散布色情擦边、淫秽、性暗示类内容；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次永久封停账户。</p><p>盗版侵权</p><p>“用户”和“达人和活动发起人”未经知识产权所有人本人同意、在平台内以任何形式宣传或使用他人肖像、姓名、作品。</p><p>具体情形包括且不限于：</p><p>1、“达人和活动发起人”未经本人同意，盗用他人、网络红人、明星照片等私人信息盈利接单；</p><p>2、昵称涉嫌侵占和假冒名人明星；</p><p>3、通过账号信息恶意模仿“充电桩”平台其他特定用户，并伪装成模仿的对象与他人进行互动。</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户7天、三次封停账户30天；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次封停账户30天。</p><p>诈骗</p><p>“用户”和“达人和活动发起人”以非法占有为目的，用虚构事实或者隐瞒真相的方法骗取数额较大的平台和用户的公私财物。</p><p>具体情形包括且不限于：</p><p>1、资金源涉及违法或以各种渠道转移非法资金；</p><p>处罚规定：永久封停账户</p><p>2、以任何形式诱导用户下订单/送礼后不提供服务，造成用户财产损失；</p><p>处罚规定：视情节严重程度对账户处以警告至封停账户1天-15天，二次封停账户3 天-30天、三次封停账户30天-90天；对提供违规服务的达人和活动发起人进行一次警告+封停账户1天-15天，二次封停账户3 天-30天、三次封停账户30天-90天；</p><p>3、通过非正当方式获得平台收益的行为；</p><p>处罚规定：永久封停账户</p><p>脱离平台交易</p><p>“用户”和“达人和活动发起人”诱导或通过其他途径进行非平台订单交易，微信、支付宝、现金等一切非平台支付方式属于脱离平台交易。</p><p>具体情形包括且不限于：</p><p>诱导用户和达人和活动发起人以任何形式越过平台使用第三方支付方式支付订单费用。</p><p>处罚规定：永久封停账户。</p><p>广告宣传</p><p>“用户”和“达人和活动发起人”宣传、鼓励使用其他竞争平台，以任何形式传播任何非充电桩平台授权的第三方个人/平台/广告信息/工具（如外挂/脚本/辅助）。</p><p>具体情形包括且不限于：</p><p>1、以任何形式宣传第三方平台广告信息、其他应用、竞争平台、工具（如外挂/脚本/辅助）；</p><p>处罚规定：永久封停账户 。</p><p>2、以任何形式怂恿他人加入竞争平台；</p><p>处罚规定：永久封停账户。</p><p>损害平台利益、扰乱平台秩序</p><p>“用户”和“达人和活动发起人”以任何形式干扰平台管理、严重扰乱平台秩序、损害其他用户或平台的合法权益的行为。</p><p>具体情形包括且不限于：&nbsp;</p><p>1、以任何形式冒充官方工作人员，包括但不限于昵称、头像、言语；</p><p>2、泄露平台信息、数据、用户/达人和活动发起人隐私及个人信息；</p><p>3、在平台或服务过程中宣传买卖官方虚拟货币、官方账号损害平台利益；</p><p>4、恶意诋毁平台/用户，散布虚假言论，对平台/平台用户造成损失；</p><p>5、在平台或服务过程中宣传、买卖资质图、资质标签、代考信息，扰乱平台秩序；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+7天封停账户，二次封停账户15天、三次永久封停账户。</p><p>6、在平台恶意攻击、骚扰、辱骂他人，诽谤平台/用户/达人和活动发起人，严重扰乱平台秩序；</p><p>处罚规定：视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+7天封停账户，二次封停账户15天、三次永久封停账户。</p><p>7、利用作弊工具扰乱平台秩序，如外挂/模拟器；</p><p>处罚规定：视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户</p><p>8、用平台规则漏洞扰乱平台秩序、获得不正当利益；</p><p>处罚规定：视情节严重程度对账户处以7天至永久封停账户。</p><p>9、在任何场景下，宣传或使用充电桩面币做平台服务以外的交易；</p><p>处罚规定：视情节严重程度对账户处以警告至封停账户3天，二次封停账户7天、三次封停账户30天。</p><p>10、公会恶意广告宣传行为或拉人行为；</p><p>处罚规定：视情节严重程度对账户处以7天至永久封停账户。</p><p>11、以任何形式套现，包括但不限于聊天、语音、欢迎语引导、动态留言、推广套现相关收售礼物与线下交易等内容；</p><p>处罚规定：视情节严重程度进行7天至永久封停账户。</p><p>损害平台利益、扰乱活动秩序</p><p>“用户”和“达人和活动发起人”以任何形式干扰平台管理、严重扰乱平台活动秩序、损害其他用户或平台的合法权益的行为。</p><p>具体情形包括且不限于：</p><p>1、以任何形式冒充官方工作人员，包括但不限于昵称、头像、言语；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户。</p><p>2、在活动过程中谩骂、侮辱、诋毁、嘲讽他人；&nbsp;</p><p>处罚规定：视情节严重程度对账户处以3天至永久封停账户；对提供服务的达人和活动发起人进行3 天至永久封停账户。</p><p>3、发布信息，如：空降、见面、线下、代练代打、租借、买卖账号、违禁歌曲、喝酒、收徒、随缘、卖自己、渡劫及非平台不提供的服务且不明确内容；</p><p>处罚规定：视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+7天封停账户，二次封停账户15天、三次永久封停账户。</p><p>4、发布信息，如：代充面币、非法买卖面币、微商代购广告、竞争平台拉人广告等相关内容。&nbsp;</p><p>处罚规定：视情节严重程度进行封停账户7天至永久封停账户。</p><p>5、利用他人身份资料或使用他人充电桩账号进行达人和活动发起人服务行为；&nbsp;</p><p>处罚规定：视情节严重程度进行封停账户7天至永久封停账户。</p><p>6、进行账户租借或买卖行为；&nbsp;</p><p>处罚规定：视情节严重程度进行封停账户7天至永久封停账户。</p><p>备注：以上所有处罚行为，平台会根据实际投诉情况、情节轻重、影响范围等各方面因素，给予处罚“加重”和“减轻”等量刑情节的判断。</p><p>&nbsp;</p><p>《免责协议》</p><p>一、声明:</p><p>1、该协议的目的是为了活动发起人、组织者或领队(以下协议中简称:发起者)和同行成员明确知晓户外活动所存在的风险，提高参加人员的抗风险和自律能力，免除活动的发起人和同行成员在活动中出现的相关赔偿及法律连带等责任，让户外活动更加安全、健康、快乐、有意义。</p><p>2、活动中，对于违反国家相关法规、恶意侵犯他人或其它涉及犯罪行为的事件，则不在此协议范围内，必须由个人承担相应的法律责任。</p><p>3、凡报名参加活动的人员，均视为具有完全民事行为能力的人，均视为你已经仔细阅读和完全理解并同意接受《同城服务躲猫猫免责协议书》的全部条款，并要在注册成为会员时签字和在本协议上签字。</p><p>4、此协议为相关责任的豁免、权利的放弃、风险的承担和免赔的协议。你如果在此协议上签字，潜在代表你就已经完全知晓、理解和同意接受《同城服务躲猫猫免责协议书》全部条款，你就放弃了向活动发起者和其他成员提起诉讼的权利;永远免除活动的发起者和同行成员的赔偿及法律连带责任。</p><p>二、风险的承担:</p><p>我参加户外活动是在完全自愿的基础上，依签署此有关豁免、权利放弃和风险自担的协议为前提条件的。我知晓活动的发起者只是活动的联系人，并非职业的领队、向导或有许可证的急救人员。我知晓领队、向导和急救人员及同行的成员可能并没有参加过户外生存课程或急救(包括野外)的培训和经验。我知晓他们并不能对我安全负责。</p><p>三、关于豁免责任，放弃权利和赔偿的协议内容:</p><p>1、本次活动非商业活动，非旅行社组团旅游，也非赢利性质自助游活动;是属于个人设计的出行日期及路线，活动性质仅限共同爱好者自愿参加、共同参与、风险自担、责任自负;全程遵循“自助，互助，低碳环保”的原则;发起者和其它参加活动人员一样，同样也是自愿参与者，对其他参加者没有绝对的管理和支配权力;对于活动中突发的一切足以影响到大家切身安全和利益的不可抗因素等重大事项尚需参加者共同商定;活动中发起者仅有义务对全队活动进行组织协调，参加者也有义务服从活动的整体安排。</p><p>2、凡报名参加本次活动的人员均视为你已经仔细阅读、完全理解责任豁免、权利的放弃、风险的承担和赔偿的协议的全部条款内容。代他人报名需告知被代报人《户外活动免责协议书》的全部条款，参加活动的被代报人也视同接受了本协议全部条款。同时，我知晓报名前必须事先与自己的家属沟通，取得家属的理解和支持，知晓并同意接受本协议全部条款后，报名后视同其家属也已知情并同意。</p><p>3、户外运动不同于常规的旅游活动，采取的线路也不是一般的常规旅游线路，有时气象条件也非常规，而是具有一定的探险性质;该项运动本身最大的特点在于其具有一定的风险性;活动安排的时间是估算时间，实际活动时间可能出现较大的差异;报名前，我已阅读活动内容和《户外活动免责协议书》的全部条款，并经过合理判断，同意接受本活动计划和《户外活动免责协议书》全部条款，同意自行承担本次活动的所有的风险和后果;包括交通工具及其他第三方设施带来的风险;如果由于我的行为和我的参加而导致了第三方的财产损失或个人伤害，我同意免除所有其他人的赔偿连带责任。</p><p>4、参加本次活动，包括登山、徒步、郊游、住宿(露营)、就餐以及活动和装备的使用等都存在有潜在危险，可能会受到身体损伤、瘫痪、甚至死亡的风险，我也知道健身和休闲等活动也存在突发病、心脏病等甚至死亡的风险;我自愿参加这些活动和使用活动的装备，如果因为在参加本活动中受到伤害，我同意自行承担和接受任何和所有伤害的风险，甚至死亡的风险，并且，放弃追究和要求赔偿的</p><p>权利，同时我永远免除此次活动发起者和同行成员的赔偿及法律连带责任。</p><p>5、本次活动可能对身体和精神方面都有一定考验，可能遇到在恶劣天气中进行活动的可能，也可能远离救助和医疗服务地点，短时间(或24小时)内无法得到救援;我清楚的知晓活动发起者无法全面预见该活动中所有的风险和不利，我知道以上所列的风险并不完全包含本次活动中的所有可能的风险;当我万一遇到上述等伤害后，所有救援和医疗等全部费用我同意全部承担，活动的发者和其它参加者不承担所发生的一切费用和法律连带责任;同时我永远免除此次活动的发起者和同行成员的赔偿和法律责任，并不仅限于以上所列的风险中。</p><p>6、我清楚知道此次活动发起者不是保险公司，没有义务为我购买意外保险等险种，我知道自己购买保险的意义，以防万一;我知道发起者已在活动内容中提醒我本人，在活动开始前自己了解保险类别和购买保险;对于活动中可能出现的意外事故，可由投保的保险公司承担相应的责任。</p><p>8在此我谨宣布我自己身体健康，适合参加户外骑行活动，没有那些会影响我参加户外骑行活动或使用装备的不适、损伤、病痛、心脏病、突发病历或其它疾病;我理解我能够通过以下措施来减少我的风险:注意环境，注意我的身体和精神状况，注意所有与安全相关的衣物和装备的适用状况，只参加那些自己精神和身体能力之内的活动;我清楚知道，我的安全是我个人的责任和取决于我的警惕和良好的判断;我同意和保证，如果任何时候我认为是不安全的，那么我会立刻中断活动的进行。如我在活动中发生各种疾病或伤害，甚至死亡，我同意放弃追究和要求赔偿的权利，同时我永远免除活动发起者和同行成员的赔偿及法律连带责任。</p><p>9当由于出现各种意外事故、突发气候变化和急性疾病等不可预测因素造成身体损害时，发起者和同行成员有义务尽力救助，但如果造成了不可逆转的损害，我同意放弃追究和要求赔偿的权利，同时我永远免除此次活动发起者和同行成员的赔偿及法律连带责任;我完全理解，在同伴需要协助的时候，我将配合协助发起者及同行成员尽力协助我的同伴，但这只是在以下情况:在我的判断中，这是自己力所能及的范围内，并且不会给自己带来不合理的危险;我进一步理解到，我没有法律上的责任去协助别人，同时，发起者以及我的同伴或者其他成员也没有任何法律上的责任来协助我。</p><p>10、我在此特意和有意的声明:我已经年满18岁!我已阅读和理解以上声明和全部条款内容;我理解参加户外活动相关的风险;我同意承担由于选择和参加以后的户外活动而随之来的全部责任;对于参加户外活动而引发或与之连带的任何和所有的法律要求(包括第三方的法律要求)和无论出现任何形式和性质的害伤，无论这些法律要求是基于某方的疏忽或其它原因引起的，我永远免除活动发起者和同行成员、任何第三方的赔偿及法律责任</p><p>11、我知晓报名前必须事先与自己的家属沟通，取得家属的理解和支持，知道并同意接受本协议全部条款后，才能报名参加本次活动，报名后视同其家属也已知情并同意;我知晓，一旦签署，此协议将生效，也知晓本协议同样有效于我的继承人、近亲、执行人、管理人、个人代表和转让人。代为签名者视为已沟通并被授权，否则由代签人承担后果。</p><p>12、协议范围:凡是报名参加并注册成为会员者均视为已经阅读、理解，并完全接受该协议条款;该协议自注册成为会员后开始生效，至退出成员时终止。(至集合地点前和活动结束解散后的路途，已不属于活动范围内)。(协议中的本次活动指同城服务躲猫猫每次举行的活动)。所产生的效力直至本人退出活动为止。</p><p><br></p><p>《“充电桩”平台用户行为守则》</p><p>为维护平台正常秩序，西安省钱兄网络科技有限公司“充电桩”平台特颁布本《“充电桩”平台用户行为守则》，同时对入驻我方平台的“达人和活动发起人”和使用我方平台寻找达人和活动发起人的“用户”违规行为制定相应处罚机制。</p><p>“充电桩”平台“用户”和“达人和活动发起人”若违反以下行为，一经发现将视情节轻重给予处罚：</p><p>违反法律法规及涉及道德底线</p><p>发布内容或用户行为涉嫌反动、反社会道德、反政治类或政治相关，宣扬恐怖、邪教、封建迷信、损害国家荣誉和利益、破坏民族团结、危害国家安全、泄露国家秘密等。</p><p>处罚规则：永久封停账户。</p><p>“用户”和“达人和活动发起人”传播色情淫秽、低俗擦边信息，发布涉及淫秽色情、低俗信息、描绘性行为或性暗示的图片或文字</p><p>具体情形包括且不限于：</p><p>1、使用暴露、性暗示、色情、低俗头像；&nbsp;</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次永久封停账户。</p><p>2、 昵称、个性签名、介绍、资质说明、资质图等个人资料使用色情擦边、淫秽、性暗示词汇；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次永久封停账户。</p><p>3、在聊天中发布以色情为目的色情语音、色情视频、色情图片、色情内容等；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次永久封停账户。</p><p>4、在消息、动态等公共环境中散布色情擦边、淫秽、性暗示类内容；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次永久封停账户。</p><p>盗版侵权</p><p>“用户”和“达人和活动发起人”未经知识产权所有人本人同意、在平台内以任何形式宣传或使用他人肖像、姓名、作品。</p><p>具体情形包括且不限于：</p><p>1、“达人和活动发起人”未经本人同意，盗用他人、网络红人、明星照片等私人信息盈利接单；</p><p>2、昵称涉嫌侵占和假冒名人明星；</p><p>3、通过账号信息恶意模仿“充电桩”平台其他特定用户，并伪装成模仿的对象与他人进行互动。</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至3天封停账户，二次封停账户7天、三次封停账户30天；对提供服务的达人和活动发起人进行一次警告+封停账户3天，二次封停账户15天、三次封停账户30天。</p><p>诈骗</p><p>“用户”和“达人和活动发起人”以非法占有为目的，用虚构事实或者隐瞒真相的方法骗取数额较大的平台和用户的公私财物。</p><p>具体情形包括且不限于：</p><p>1、资金源涉及违法或以各种渠道转移非法资金；</p><p>处罚规定：永久封停账户</p><p>2、以任何形式诱导用户下订单/送礼后不提供服务，造成用户财产损失；</p><p>处罚规定：视情节严重程度对账户处以警告至封停账户1天-15天，二次封停账户3 天-30天、三次封停账户30天-90天；对提供违规服务的达人和活动发起人进行一次警告+封停账户1天-15天，二次封停账户3 天-30天、三次封停账户30天-90天；</p><p>3、通过非正当方式获得平台收益的行为；</p><p>处罚规定：永久封停账户</p><p>脱离平台交易</p><p>“用户”和“达人和活动发起人”诱导或通过其他途径进行非平台订单交易，微信、支付宝、现金等一切非平台支付方式属于脱离平台交易。</p><p>具体情形包括且不限于：</p><p>诱导用户和达人和活动发起人以任何形式越过平台使用第三方支付方式支付订单费用。</p><p>处罚规定：永久封停账户。</p><p>广告宣传</p><p>“用户”和“达人和活动发起人”宣传、鼓励使用其他竞争平台，以任何形式传播任何非充电桩平台授权的第三方个人/平台/广告信息/工具（如外挂/脚本/辅助）。</p><p>具体情形包括且不限于：</p><p>1、以任何形式宣传第三方平台广告信息、其他应用、竞争平台、工具（如外挂/脚本/辅助）；</p><p>处罚规定：永久封停账户 。</p><p>2、以任何形式怂恿他人加入竞争平台；</p><p>处罚规定：永久封停账户。</p><p>损害平台利益、扰乱平台秩序</p><p>“用户”和“达人和活动发起人”以任何形式干扰平台管理、严重扰乱平台秩序、损害其他用户或平台的合法权益的行为。</p><p>具体情形包括且不限于：&nbsp;</p><p>1、以任何形式冒充官方工作人员，包括但不限于昵称、头像、言语；</p><p>2、泄露平台信息、数据、用户/达人和活动发起人隐私及个人信息；</p><p>3、在平台或服务过程中宣传买卖官方虚拟货币、官方账号损害平台利益；</p><p>4、恶意诋毁平台/用户，散布虚假言论，对平台/平台用户造成损失；</p><p>5、在平台或服务过程中宣传、买卖资质图、资质标签、代考信息，扰乱平台秩序；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+7天封停账户，二次封停账户15天、三次永久封停账户。</p><p>6、在平台恶意攻击、骚扰、辱骂他人，诽谤平台/用户/达人和活动发起人，严重扰乱平台秩序；</p><p>处罚规定：视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+7天封停账户，二次封停账户15天、三次永久封停账户。</p><p>7、利用作弊工具扰乱平台秩序，如外挂/模拟器；</p><p>处罚规定：视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户</p><p>8、用平台规则漏洞扰乱平台秩序、获得不正当利益；</p><p>处罚规定：视情节严重程度对账户处以7天至永久封停账户。</p><p>9、在任何场景下，宣传或使用充电桩面币做平台服务以外的交易；</p><p>处罚规定：视情节严重程度对账户处以警告至封停账户3天，二次封停账户7天、三次封停账户30天。</p><p>10、公会恶意广告宣传行为或拉人行为；</p><p>处罚规定：视情节严重程度对账户处以7天至永久封停账户。</p><p>11、以任何形式套现，包括但不限于聊天、语音、欢迎语引导、动态留言、推广套现相关收售礼物与线下交易等内容；</p><p>处罚规定：视情节严重程度进行7天至永久封停账户。</p><p>损害平台利益、扰乱活动秩序</p><p>“用户”和“达人和活动发起人”以任何形式干扰平台管理、严重扰乱平台活动秩序、损害其他用户或平台的合法权益的行为。</p><p>具体情形包括且不限于：</p><p>1、以任何形式冒充官方工作人员，包括但不限于昵称、头像、言语；</p><p>处罚规定：针对部分违规信息清空；视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户。</p><p>2、在活动过程中谩骂、侮辱、诋毁、嘲讽他人；&nbsp;</p><p>处罚规定：视情节严重程度对账户处以3天至永久封停账户；对提供服务的达人和活动发起人进行3 天至永久封停账户。</p><p>3、发布信息，如：空降、见面、线下、代练代打、租借、买卖账号、违禁歌曲、喝酒、收徒、随缘、卖自己、渡劫及非平台不提供的服务且不明确内容；</p><p>处罚规定：视情节严重程度对账户处以警告至7天封停账户，二次封停账户15天、三次永久封停账户；对提供服务的达人和活动发起人进行一次警告+7天封停账户，二次封停账户15天、三次永久封停账户。</p><p>4、发布信息，如：代充面币、非法买卖面币、微商代购广告、竞争平台拉人广告等相关内容。&nbsp;</p><p>处罚规定：视情节严重程度进行封停账户7天至永久封停账户。</p><p>5、利用他人身份资料或使用他人充电桩账号进行达人和活动发起人服务行为；&nbsp;</p><p>处罚规定：视情节严重程度进行封停账户7天至永久封停账户。</p><p>6、进行账户租借或买卖行为；&nbsp;</p><p>处罚规定：视情节严重程度进行封停账户7天至永久封停账户。</p><p>备注：以上所有处罚行为，平台会根据实际投诉情况、情节轻重、影响范围等各方面因素，给予处罚“加重”和“减轻”等量刑情节的判断。</p><p>&nbsp;</p><p class="ql-align-center">本守则2025年3月1日起生效</p><p><br></p><p>&nbsp;</p><p>最后更新时间：2025年3月1日</p><p>&nbsp;</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (203, '2025-03-30 00:19:02', NULL, '关于我们', 187, '<p><strong>充电桩</strong>是面向电瓶车与新能源汽车用户的智慧充电服务平台，提供附近站点查询、在线启动充电、订单管理与费用结算等服务。</p>\n<p>- 支持电瓶车按小时计费、汽车按电量计费</p>\n<p>- 支持地图找桩、空闲插座实时查看</p>\n<p>- 7×24小时自助充电，安全便捷</p>\n<p>&nbsp;</p>\n<p>小程序备案号：陕ICP备19023892号-21X</p>\n<p>联系我们：</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (204, '2021-08-14 19:09:54', NULL, '登录是否获取手机号', 188, '是', 'kaiguan');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (205, '2021-12-28 16:14:17', NULL, '邀请好友奖励金额', 189, '0', 'fuwufeis');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (217, '2025-04-15 15:36:21', NULL, '腾讯地图key', 217, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (234, '2025-03-28 14:02:06', NULL, '上传方式 1阿里云oss  2本地  3腾讯云', 234, '1', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (238, '2023-03-30 13:30:25', NULL, '是否开启我的余额', 301, '是', 'kaiguan');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (239, '2025-08-07 11:15:15', NULL, '小程序是否发布（小程序上架规则处理）', 302, '是', 'kaiguan');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (243, '2025-03-19 13:49:46', NULL, '是否开启微信提现', 243, '否', 'kaiguan');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (244, '2021-12-22 10:11:36', NULL, '微信提现方式 1自动 2手动', 244, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (245, '2020-11-04 10:31:40', NULL, '是否开启支付宝提现', 245, '是', 'zhifubao');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (246, '2020-10-28 16:57:07', NULL, '微信证书地址', 201, '', 'weixin');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (251, '2022-01-24 17:23:10', NULL, '用户佣金', 206, '0.4', 'yongjins');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (252, '2023-11-13 09:31:31', NULL, '一级佣金', 207, '0.2', 'yongjins');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (253, '2023-11-13 09:31:38', NULL, '二级佣金', 208, '0.1', 'yongjins');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (414, '2023-07-26 16:27:15', NULL, '地图方式 1腾讯 2天地图', 414, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (415, '2023-07-26 16:27:15', NULL, '天地图key', 415, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (615, '2022-05-16 15:40:56', NULL, '投诉配置', 615, '去投诉', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (616, '2022-11-07 13:49:11', NULL, '客服链接', 616, 'https://work.weixin.qq.com/kfid/kfc756a665af7283fdd', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (617, '2022-11-07 13:48:59', NULL, '客服AppId', 617, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (618, '2025-03-30 00:05:40', NULL, '客服联系方式 1手机号 2企业客服 3微信客服 4二维码客服', 618, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (619, '2025-03-30 00:05:50', NULL, '客服手机号', 619, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (701, '2022-11-01 11:18:00', NULL, '风险地区', 701, '云南', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (800, '2022-11-01 11:18:00', NULL, '腾讯云oss AccessKey', 800, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (801, '2022-11-01 11:18:00', NULL, '腾讯云oss SecretKey', 801, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (802, '2022-11-01 11:18:00', NULL, '腾讯云oss bucket', 802, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (803, '2022-11-01 11:18:00', NULL, '腾讯云oss bucketName', 803, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (804, '2022-11-01 11:18:00', NULL, '腾讯云oss Bucket域名', 804, '', 'oss');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (806, '2022-11-01 11:18:00', NULL, '是否开启天网中心', 806, '否', 'kaiguans');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (807, '2022-11-01 11:18:00', NULL, '设备网址', 807, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (808, '2022-11-01 11:18:00', NULL, '设备账号', 808, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (809, '2022-11-01 11:18:00', NULL, '设备密码', 809, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (811, '2025-04-02 13:45:33', NULL, '用户注销协议', 811, '<p>码上台球账号注销协议</p><p>重要提示：注销账号是不可恢复的操作，请确认已完成订单结算、解绑第三方服务等前置操作，并备份重要数据。</p><p>一、注销后果</p><p>1. 数据清除</p><p>&nbsp;立即删除账号内所有台球赛事记录、积分数据、优惠券等虚拟资产​</p><p>&nbsp;清空与账号关联的球友聊天记录、约球动态及个人资料​</p><p>2. 功能限制</p><p>&nbsp;无法再使用本账号登录小程序或参与线上线下赛事活动</p><p>&nbsp;已绑定的球房会员卡、支付功能等第三方服务自动解绑​</p><p>3. 资源释放</p><p>&nbsp;账号昵称进入2天保护期，仅允许同主体账号优先复用​</p><p>&nbsp;释放手机号/邮箱等注册资源，30天后可重新注册新账号</p><p>二、注销流程</p><p>1. 前置条件</p><p>&nbsp;账户内无未完成的赛事押金或球房消费账单​</p><p>&nbsp;已终止所有进行中的赛事报名或约球订单</p><p>2. 操作步骤 ① 登录小程序 → 设置 → 账号安全 → 申请注销 ② 阅读本协议 → ③ 进入7天冻结期（可随时撤销注销） → 管理员微信确认即生效​</p><p>三、特别条款</p><p>1. 虚拟资产处理 未消耗的台球金币、赛事积分等视为自动放弃，不支持折现或转移​</p><p>2. 法律责任 若因注销导致第三方权益受损（如未结算的球房分成），用户需自行承担赔偿责任​</p><p>3. 隐私保护 注销后将匿名化处理历史数据，但赛事成绩等公开信息仍保留平台数据库​</p><p>四、协议更新</p><p>本协议可能根据《微信小程序平台运营规范》调整，最新版本以小程序内公示为准。继续操作即视为接受条款约束。</p><p> 生效日期：2025年4月2日</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (812, '2022-11-01 11:18:00', NULL, '每周基础积分', 812, '10', 'xitongs');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (813, '2022-11-01 11:18:00', NULL, '连续签到叠加积分', 813, '2', 'xitongs');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (817, '2021-07-19 11:32:12', NULL, '兑换规则', 817, '<p>尊敬的用户您好：在您使用本服务之前，请您认真阅读本用户协议，更好的了解我们所提供的服务以及您享有的权利义务。您开始使用时，即表示您已经了解并确认接受了本文件中的全部条款，包括我们对本服务条款随时做的任何修改。</p><p>一、协议的效力</p><p>本协议内容包括协议正文及所有已经发布或将来可能发布的各类规则。所有规则为本协议不可分割的组成部分，与协议正文具有同等法律效力。您承诺接受并遵守本协议的约定。如果您不同意本协议的约定，您应立即停止使用本平台服务。</p><p>二、用户行为规范</p><p>用户同意将不会利用本服务进行任何违法或不正当的活动，包括但不限于下列行为∶</p><p>发布或以其它方式传送含有下列内容之一的信息：</p><p>反对宪法所确定的基本原则的；</p><p>危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；</p><p>损害国家荣誉和利益的；</p><p>煽动民族仇恨、民族歧视、破坏民族团结的；</p><p>破坏国家宗教政策，宣扬邪教和封建迷信的；</p><p>散布谣言，扰乱社会秩序，破坏社会稳定的；</p><p>散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；</p><p>侮辱或者诽谤他人，侵害他人合法权利的；</p><p>含有虚假、诈骗、有害、胁迫、侵害他人隐私、骚扰、侵害、中伤、粗俗、猥亵、或其它道德上令人反感的内容；</p><p>含有当地法律、法规、规章、条例以及任何具有法律效力之规范所限制或禁止的其它内容的；</p><p>含有不适合在本平台展示的内容；</p><p>以任何方式危害他人的合法权益；</p><p>冒充其他任何人或机构，或以虚伪不实的方式陈述或谎称与任何人或机构有关；</p><p>将依据任何法律或合约或法定关系（例如由于雇佣关系和依据保密合约所得知或揭露之内部资料、专属及机密资料）知悉但无权传送之任何内容加以发布、发送电子邮件或以其它方式传送；</p><p>将侵害他人著作权、专利权、商标权、商业秘密、或其它专属权利（以下简称“专属权利”）之内容加以发布或以其它方式传送；</p><p>将任何广告信函、促销资料、“垃圾邮件”、““滥发信件”、“连锁信件”、“直销”或其它任何形式的劝诱资料加以发布、发送或以其它方式传送；</p><p>将设计目的在于干扰、破坏或限制任何计算机软件、硬件或通讯设备功能之计算机病毒（包括但不限于木马程序（trojan horses）、蠕虫（worms）、定时炸弹、删除蝇（cancelbots）（以下简称“病毒”）或其它计算机代码、档案和程序之任何资料，加以发布、发送或以其它方式传送；</p><p>干扰或破坏本服务或与本服务相连线之服务器和网络，或违反任何关于本服务连线网络之规定、程序、政策或规范；</p><p>跟踪、人肉搜索或以其它方式骚扰他人；</p><p>故意或非故意地违反任何适用的当地、国家法律，以及任何具有法律效力的规则；</p><p>未经合法授权而截获、篡改、收集、储存或删除他人个人信息、站内邮件或其它数据资料，或将获知的此类资料用于任何非法或不正当目的。</p><p>三、知识产权</p><p>本平台所有设计图样以及其他图样、产品及服务名称。任何人不得使用、复制或用作其他用途。未经我们许可，任何单位和个人不得私自复制、传播、展示、镜像、上载、下载、使用，或者从事任何其他侵犯我们知识产权的行为。否则，我们将追究相关法律责任。</p><p>我们鼓励用户充分利用平台自由地张贴和共享自己的信息，但这些内容必须位于公共领域内，或者用户拥有这些内容的使用权。同时，用户对于其创作并在本平台上发布的合法内容依法享有著作权及其相关权利。</p><p>四、免责声明</p><p>互联网是一个开放平台，用户将照片等个人资料上传到互联网上，有可能会被其他组织或个人复制、转载、擅改或做其它非法用途，用户必须充分意识此类风险的存在。用户明确同意其使用本服务所存在的风险将完全由其自己承担；因其使用本服务而产生的一切后果也由其自己承担，我们对用户不承担任何责任。</p><p>对于用户上传的照片、资料、证件等，已采用相关措施并已尽合理努力进行审核，但不保证其内容的正确性、合法性或可靠性，相关责任由上传上述内容的会员负责。</p><p>尽管已采取相应的技术保障措施 ，但用户仍有可能收到各类的广告信或其他不以招聘/应聘为目的邮件或其它方式传送的任何内容，本平台不承担责任。</p><p>对于各种广告信息、链接、资讯等，不保证其内容的正确性、合法性或可靠性，相关责任由广告商承担；用户通过本服务与广告商进行任何形式的通讯或商业往来，或参与促销活动，包含相关商品或服务之付款及交付，以及达成的其它任何相关条款、条件、保证或声明，完全为用户与广告商之间之行为，与本平台无关。用户因前述任何交易或前述广告商而遭受的任何性质的损失或损害，本平台不承担任何责任。</p><p>本平台不保证其提供的服务一定能满足用户的要求和期望，也不保证服务不会中断，对服务的及时性、安全性、准确性也都不作保证。对于因不可抗力或无法控制的原因造成的网络服务中断或其他缺陷，不承担任何责任。我们不对用户所发布信息的删除或储存失败承担责任。我们有权判断用户的行为是否符合本网站使用协议条款之规定，如果我们认为用户违背了协议条款的规定，我们有终止向其提供服务的权利。</p><p>本平台保留变更、中断或终止部分网络服务的权利。保留根据实际情况随时调整平台提供的服务种类、形式的权利。本平台不承担因业务调整给用户造成的损失。本平台仅提供相关服务，除此之外与本服务有关的设备（如电脑、调制解调器及其他与接入互联网有关的装置）及所需的费用（如为接入互联网而支付的电话费及上网费）均应由用户自行负担。</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (823, '2025-04-02 11:35:26', NULL, '充值协议', 823, '<p class="ql-align-center">《“码上台球”用户充值协议》</p><p><strong>尊敬的用户，在同意本协议前，请您认真阅读并充分知悉、理解码上台球平台的各项规则及要求，以及国家关于该类互联网信息服务的法律法规等。</strong></p><p><strong>如果您对本协议的任何条款表示异议，您可以选择不使用本服务;如使用则意味着您将同意遵守本协议下全部规定，以及我们后续对使用协议随时所做的任何修改，并完全服从于我们的统一管理。</strong>本协议已对与您的权益有或可能具有重大关系的条款，及对码上台球平台具有或可能具有免责或限制责任的条款用粗体字予以标注，请您注意。</p><p><strong>请确认您具有完全民事行为能力，如您已年满18 周岁，或您已年满16周岁且依靠自己的劳动收入作为主要生活来源，否则请您停止注册或使用本协议项下的服务。</strong></p><p class="ql-align-center">第一章总则</p><p>第1条 码上台球平台是西安省钱兄网络科技有限公司（下称"本公司"）向用户提供的同城活动发布平台。码上台球平台将为用户提供交易信息展示、交易咨询、购买虚拟货币、账户充值等功能和服务。</p><p>第2条 码上台球平台的所有权、经营权、管理权均属于本公司。</p><p class="ql-align-center">第二章 虚拟货币购买规则</p><p>第3条"码上台球会员卡"是本公司向您提供的用于在码上台球平台上进行相关消费的虚拟货币您可以用"会员卡"自由购买虚拟礼物等码上台球平台上的其他产品或服务。您可将购买的虚拟礼物赠与。<strong>但是，""不能兑换为人民币或其他任何货币，您应根据自己的实际需求合理购买相应数量的"会员卡"。"会员卡"与人民币的兑换比例以充值页面显示的为准。</strong></p><p>第4条 您可以通过<strong>码上台球</strong>手机APP、码上台球微信小程序、码上台球PC官网（<a href="https://www.dingjianpp.com/" rel="noopener noreferrer" target="_blank" style="color: rgb(112, 177, 231);">www.</a>xiansqx.com）、微信公众号（<a href="https://www.dingjianpp.com/" rel="noopener noreferrer" target="_blank" style="color: rgb(112, 177, 231);">码上台球</a>）等渠道购买码上台球虚拟货币，在安卓/IOS系统、码上台球PC 官网中您可以通过支付宝、微信等第三方支付平台购买码上台球虚拟货币。<strong>使用第三方支付平台购买虚拟货币可能产生由第三方收取的手续费等费用，请您务必注意该等渠道充值操作页面提示，并自行了解收费规则，相关费用须由您自行承担。</strong></p><p><strong>第5条 码上台球平台保留根据相关法律规定、主管部门要求、业务开展情况等因素对使用人民币购买虚拟货币的规则进行单方面变更、调整、中止或终止的权利。您可以选择即时终止购买行为，否则视为同意无条件接受对上述购买规则的变更、调整、中止或终止，码上台球平台开展前述行动时将以于码上台球平台公布的方式通知，并自公布之日起自动生效，而无需另行单独通知您，也不就该等行动给您造成的任何损失承担任何责任。</strong></p><p class="ql-align-center">第三章 关于充值</p><p>第6条 <strong>码上台球</strong>平台涉及在线支付服务，为使您更便捷地使用该服务，码上台球平台免费为你提供电子钱包功能。您可根据实际需求，对您的电子钱包余额进行充值，用于购买由码上台球官方或第三方提供的服务。<strong>您在充值时，请务必仔细确认自己的账号并仔细选择相关操作选项。若因为您自身输入账号错误、操作不当或不了解充值计费方式等因素造成充错账号、错选充值种类等情形而损害自身权益的，码上台球平台将不会作出任何补偿或赔偿。</strong></p><p><strong>第7条 若您以非法的方式，或使用非码上台球平台所指定的充值方式进行充值，码上台球平台不保证该充值顺利或者正确完成。若因此造成您权益受损，码上台球平台不会作出任何补偿或赔偿，码上台球平台同时保留随时终止您码上台球账号资格及使用各项充值服务的权利，若您的非法行为给码上台球平台造成损失的，您仍需承担赔偿责任。</strong></p><p><strong>第8条 充值成功后，电子钱包内所增加的"余额"可由您在码上台球平台上自由使用，但码上台球平台不会提供任何退还或逆向兑换服务。</strong></p><p><strong>第9条 如果码上台球平台发现因系统故障或其他任何原因导致的处理错误，无论有利于码上台球平台还是有利于您，码上台球平台都有权在以电子邮件、微信或其他合理方式通知您后纠正该错误。如果该措施导致您实际收到的新增"余额"数量少于您应获得的新增"余额"，则码上台球平台在确认该处理错误后会尽快将差额补足至您的码上台球账户中。如果该错误导致您实际收到的新增"余额"数量多于您应获得的新增"余额"，则无论错误的性质和原因如何，码上台球平台有权从您的码上台球账户中直接扣除差额。</strong></p><p class="ql-align-center">第四章 权利声明</p><p>第10条 您可随时在手机APP或手机微信小程序上查看您的<strong>码上台球</strong>虚拟货币和电子钱包"余额"使用情况。如您对该记录有异议，应立即向<strong>码上台球</strong>平台提出，码上台球平台核对后确定有错误的，将予以更正;否则您同意将码上台球平台上的交易记录作为您的码上台球虚拟货币和电子钱包"余额"交易数据的唯一有效依据。</p><p>第11条 <strong>码上台球</strong>平台有权基于交易安全等方面的考虑不时设定涉及交易（含购买虚拟货币、充值余额，下同）的相关事项，包括但不限于交易限额、交易次数等。您了解，码上台球平台的前述设定可能对您的交易造成一定不便，您对此没有异议。</p><p>第12条 在任何情况下，对于您使用码上台球虚拟货币和电子钱包"余额"时涉及由第三方提供相关服务的责任由该第三方承担，码上台球平台不承担该等责任。</p><p>第13条 进行充值时，您应确保您是相应支付平台账户（如支付宝、银行卡、微信支付等，下同）的持有人，可合法、有效使用该支付工具账户且未侵犯任何第三方合法权益，否则因此造成支付工具账户实际所有人损失的，您应单独负责解决由此产生的纠纷并承担全部法律责任，由此给码上台球平台造成损失的，您还需承担赔偿责任。</p><p>第14条 <strong>因您自身的原因导致码上台球平台无法提供虚拟货币购买和电子钱包"余额"充值服务而产生的任何损失或责任，由您自行负责，码上台球平台不承担责任，包括但不限于:</strong></p><p><strong>（1）您未按照本协议或码上台球平台不定时公布的任何规则进行操作;</strong></p><p><strong>（2）因您的码上台球账号失效、丢失、被封停;</strong></p><p><strong>（3）因您绑定的第三方支付工具账户的原因导致的损失或责任，包括您使用未经认证的第三方支付工具账户或使用非您本人的第三方支付工具账户，您的第三方支付工具账户被冻结、查封等;</strong></p><p><strong>（4）您将密码告知他人导致的财产损失;</strong></p><p><strong>（5）因您个人的故意或重大过失所造成的财产损失。</strong></p><p>第15条 <strong>码上台球</strong>平台系统因下列状况无法正常运作，使您无法使用各项服务或任何虚拟财产丢失时，本公司不承担损害赔偿责任，该状况包括但不限于：</p><p>（1）在<strong>码上台球</strong>平台公告之系统停机维护、升级、调整期间;</p><p>（2）电信通讯或设备出现故障不能进行数据传输的;</p><p>（3）因台风、地震、海啸、洪水、停电、战争、恐怖袭击、政府管制等不可抗力之因素造成码上台球平台系统障碍不能执行业务的;</p><p>（4）由于黑客攻击、电信部门技术调整或故障、网站升级、相关第三方的问题等原因而造成的服务中断或者延迟。</p><p class="ql-align-center">第五章 处罚规则</p><p>第16条 如发生下列任何一种情形，码上台球平台有权随时中断或终止向您提供本协议项下的网络服务而无需通知您：</p><p>（1）您提供的个人资料不真实;</p><p>（2）您违反本协议中规定的购买、充值规则;</p><p>（3）利用充值进行任何违法犯罪活动，例如利用充值漏洞牟利等。除前款所述情形外，码上台球平台同时保留在不事先通知您的情况下随时中断或终止部分或全部网络充值服务的权利，情节严重的，依法移交司法机关。对于充值服务的中断或终止而造成的任何损失码上台球平台无需对您或任何第三方承担任何责任。</p><p>第17条 <strong>如果用户违规使用非本人手机应用商店代充值，或者通过其他非码上台球平台认可的渠道非法购买码上台球虚拟货币，则码上台球平台有权冻结该账户，并进行相应惩罚，严重者可以进行封号处理。</strong></p><p><strong>第18条 用户在使用码上台球平台时，如出现违反国家法律法规、本协议约定或其他码上台球平台对用户的管理规定的情形，码上台球平台有权暂时或永久封禁您的账号。账号封禁后至解禁（如有）前，您的码上台球虚拟货币和电子钱包"余额"将被暂时冻结或全部扣除，不可继续用于购买平台上的虚拟产品或服务、不能进行交易，同时不予返还您购买码上台球虚拟货币和充值电子钱包时的现金价值。情节严重的，依法移交司法机关处理。</strong></p><p class="ql-align-center">第六章 附则</p><p>第19条 <strong>码上台球</strong>平台保留修改或增补本协议内容的权利。本协议的修改文本将公告于<strong>码上台球</strong>平台或以其他<strong>码上台球</strong>平台认为可行的方式公告。修改文本一旦公布则立即生效，且对生效前的用户同样适用。若您在本协议内容发生修订后，继续使用本服务的，则视为您同意最新修订的协议内容;否则您须立即停止使用本服务。</p><p>第20条 因本协议引起的或与本协议有关的争议，均适用中华人民共和国法律。</p><p>第21条 <strong>因本协议引起的或与本协议有关的争议，码上台球平台与您将协商解决。协商不成的，任何一方均有权向码上台球平台的运营公司工商注册地有管辖权的法院提起诉讼。</strong></p><p>第22条 本协议部分内容被有管辖权的法院认定为违法或无效的，不因此影响其他内容的效力。</p><p>第23条 本协议未涉及的问题参见国家有关法律法规，当本协议与国家法律法规冲突时，以国家法律法规为准。</p><p>第24条 本协议自您阅读并点击“同意”之日起生效。</p><p>最后更新时间：2025年3月1日</p><p>&nbsp;</p><p><strong>《儿童个人信息及隐私保护政策》</strong></p><p>&nbsp;</p><p>欢迎您使用西安省钱兄网络科技有限公司（以下简称“我们”）提供的产品或服务，我们深知儿童个人信息及隐私保护的重要性，特制订《儿童个人信息及隐私保护政策》（以下简称“本政策”），帮助儿童、儿童的父母或其他监护人（以下简称“监护人”）进一步了解我们收集、使用、存储和共享儿童个人信息的情况，以及儿童及监护人所享有的相关权利。请儿童、监护人务必仔细阅读并充分理解本政策，尤其是以加粗等方式重点标识的条款。</p><p>&nbsp;</p><p><strong>儿童特别提示：</strong></p><p><strong>&nbsp;</strong></p><p><strong>若您是未满十四周岁的未成年人，在使用产品或服务前，需要在监护人的陪同下阅读本政策，特别是以加粗等方式重点标识的内容，并在征得监护人的同意后使用相关产品或服务。</strong></p><p><strong>&nbsp;</strong></p><p><strong>监护人特别提示：</strong></p><p><strong>&nbsp;</strong></p><p><strong>若您是未满十四周岁未成年人的监护人，在使用产品或服务前，请确保您与您监护的儿童共同阅读并充分理解本政策，且儿童需在您的同意和指导下使用我们的产品或服务、提交个人信息。如果您不同意本政策，您应要求您监护的儿童立即停止访问或使用相关产品或服务，若您监护的儿童使用或继续使用我们提供的产品或服务，即表示您和您监护的儿童同意本政策的内容。</strong></p><p><strong>&nbsp;</strong></p><p><strong>本政策适用于我们在中华人民共和国境内通过网络从事收集、使用、存储、共享、转让和披露儿童个人信息等活动。</strong></p><p><strong>&nbsp;</strong></p><p><strong>一、 我们收集的儿童个人信息</strong></p><p><strong>&nbsp;</strong></p><p><strong>我们严格遵守合法、正当、必要和诚信原则，积极履行法律法规规定的儿童个人信息保护义务和责任，在征得监护人同意后收集儿童的个人信息。</strong></p><p><strong>&nbsp;</strong></p><p><strong>1. 在使用产品或服务过程中，我们可能会根据用户填写的身份信息（如身份证号码、生日信息等），识别该用户是否为儿童，并按照法律法规的规定，在征得监护人的同意后为儿童提供相关产品或服务。</strong></p><p><strong>&nbsp;</strong></p><p><strong>2. 当我们识别出用户为儿童时，我们将收集监护人的联系方式（如手机号码、电子邮箱等），并与监护人取得联系以验证其与儿童的监护关系。为更好地保护儿童的相关权益，我们还将进一步收集监护人信息（如姓名、身份证号码、户口本或其他监护关系证明），以便更好的验证监护人与儿童的监护关系。</strong></p><p><strong>&nbsp;</strong></p><p><strong>3. 在儿童使用相关产品或服务过程中，我们可能还会收集儿童其他个人信息，具体详情，请阅读《西安省钱兄网络科技有限公司“码上台球”平台隐私政策》“我们如何收集和使用您的信息”</strong>章节。如果我们超出前述范围收集儿童个人信息的，我们将再次征得监护人的同意。</p><p>&nbsp;</p><p>4. 征得授权同意的例外</p><p>&nbsp;</p><p><strong>根据相关法律法规的规定，以下情形中收集儿童个人信息无需征得儿童和/或监护人的授权同意：</strong></p><p><strong>&nbsp;</strong></p><p><strong>（1） 与国家安全、国防安全、公共安全、公共卫生、重大公共利益直接相关的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（2） 与刑事侦查、起诉、审判和判决执行等直接相关的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（3） 出于维护信息主体或其他个人的生命、财产等重大合法权益但又很难得到监护人本人同意的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（4） 所收集的个人信息是儿童或监护人自行向社会公众公开的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（5） 从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（6） 根据监护人的要求签订合同所必需的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（7） 用于维护产品或服务的安全稳定运行所必需的，例如发现、处置产品或服务的故障；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（8） 通过计算机信息系统自动留存处理信息且无法识别所留存处理的信息属于儿童个人信息的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（9） 出于公共利益开展统计或学术研究所必要，且对外提供学术研究或描述的结果时，对结果中所包含的个人信息进行去标识化处理的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（10） 法律法规规定的其他情形。</strong></p><p><strong>&nbsp;</strong></p><p><strong>二、 我们如何使用儿童个人信息</strong></p><p><strong>&nbsp;</strong></p><p><strong>我们严格遵守法律法规的规定以及与用户的约定，按照本政策及《“码上台球”平台用户协议》</strong>、<strong>《西安省钱兄网络科技有限公司“码上台球”平台隐私政策》</strong>所述使用我们收集的个人信息。</p><p>&nbsp;</p><p>有关我们使用儿童个人信息的方式详情参见<strong>《西安省钱兄网络科技有限公司“码上台球”平台隐私政策》</strong>，若我们使用儿童的个人信息，超出了与收集时所声称的目的及具有直接或合理关联的范围，我们将在使用儿童的个人信息前，再次向监护人告知并征得监护人的同意。</p><p>&nbsp;</p><p><strong>三、 儿童个人信息的存储</strong></p><p><strong>&nbsp;</strong></p><p><strong>1. 我们按照法律法规的规定，将在中华人民共和国境内收集到的儿童的个人信息存储于中华人民共和国境内，并依法对这些信息进行严格保密。目前我们不会跨境传输或存储儿童的个人信息，如我们需要向境外传输儿童的个人信息，我们会严格按照法律法规的规定执行，并保证儿童的个人信息安全。</strong></p><p><strong>&nbsp;</strong></p><p><strong>2. 一般情况下，我们只会在为实现服务目的所必需的时间或法律法规规定的时限内存储儿童的个人信息，并将在超出保留期限后删除或匿名化处理儿童的个人信息。</strong></p><p><strong>&nbsp;</strong></p><p><strong>四、 儿童个人信息的安全</strong></p><p><strong>&nbsp;</strong></p><p><strong>1. 为了保障儿童的个人信息安全，我们会在现有技术水平下采取合理必要的措施来保护儿童的个人信息，采取物理保护、安全技术、管理制度等措施来降低丢失、误用、非法授权访问、披露和更改的风险，包括但不限于数据加密传输、防火墙和加密存储、物理访问控制以及信息访问授权控制。</strong></p><p><strong>&nbsp;</strong></p><p><strong>2. 为了保障儿童的个人信息安全，我们建立了专门的管理制度、流程和组织保障信息的安全，例如，我们严格限制访问信息的人员范围，要求他们遵守保密义务，并进行审计。</strong></p><p><strong>&nbsp;</strong></p><p><strong>3. 若发生儿童个人信息泄露等安全事件，我们会启动应急预案，阻止安全事件扩大。安全事件发生后，我们会及时以推送通知、邮件等形式告知监护人及儿童安全事件的基本情况、我们即将或已经采取的处置措施和补救措施，以及我们对监护人及儿童的应对建议。如果难以实现逐一告知，我们将通过公告等方式发布警示。同时，我们还将按照监管部门要求，上报个人信息安全事件的处置情况。</strong></p><p><strong>&nbsp;</strong></p><p><strong>4. 请监护人及儿童理解，由于技术的限制以及可能存在的各种恶意手段，在互联网行业，即便竭尽所能加强安全措施，也不可能始终保证信息百分之百的安全。监护人需要了解，儿童接入我们的服务所用的系统和通讯网络，有可能因我们可控范围外的因素而出现问题。</strong></p><p><strong>&nbsp;</strong></p><p><strong>五、 我们如何共享、转让、披露儿童的个人信息</strong></p><p><strong>&nbsp;</strong></p><p><strong>1. 共享</strong></p><p><strong>&nbsp;</strong></p><p><strong>我们将严格遵守法律法规的规定，并按照本政策及《西安省钱兄网络科技有限公司“码上台球”平台隐私政策》</strong>的约定，对儿童个人信息的共享进行严格限制。</p><p>&nbsp;</p><p>2. 转让</p><p>&nbsp;</p><p>随着我们业务的持续发展，我们有可能进行合并、收购、资产转让或类似交易，而儿童的个人信息有可能作为前述交易的一部分而被转移。我们会进行安全评估，并要求新的持有儿童个人信息的公司、组织继续受本政策的约束，否则，我们将要求该公司、组织重新向监护人征求授权同意。</p><p>&nbsp;</p><p>3. 披露</p><p>&nbsp;</p><p>我们仅会在以下情况，且采取符合业界标准的安全保护措施的前提下，披露儿童个人信息：</p><p>&nbsp;</p><p>（1） 根据监护人或儿童的需求并符合<strong>《“码上台球”平台用户协议》</strong>、<strong>《西安省钱兄网络科技有限公司“码上台球”平台隐私政策》</strong>的约定，在监护人同意的披露方式下，披露其指定的信息；</p><p>&nbsp;</p><p>（2） 根据法律法规的规定，强制性的行政执法或司法要求所必须提供儿童个人信息的情况下，我们可能会依据所要求的个人信息类型和披露方式披露儿童的个人信息。</p><p>&nbsp;</p><p>4. 共享、转让、披露个人信息时征得授权同意的例外</p><p>&nbsp;</p><p><strong>以下情形中，共享、转让、披露儿童的个人信息无需事先征得监护人的授权同意：</strong></p><p><strong>&nbsp;</strong></p><p><strong>（1） 与国家安全、国防安全直接相关的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（2） 与公共安全、公共卫生、重大公共利益直接相关的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（3） 与刑事侦查、起诉、审判和判决执行等司法或行政执法直接相关的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（4） 出于维护儿童或其他个人生命、财产等重大合法权益但又很难得到监护人同意的；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（5） 儿童或监护人自行向社会公众公开的信息；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（6） 从合法公开披露的个人信息中收集信息的，如合法的新闻报道、政府信息公开等渠道；</strong></p><p><strong>&nbsp;</strong></p><p><strong>（7） 与我们履行法律法规规定的义务相关的。</strong></p><p><strong>&nbsp;</strong></p><p><strong>六、 如何管理儿童个人信息</strong></p><p><strong>&nbsp;</strong></p><p><strong>1. 在儿童使用相关产品或服务期间，我们将在我们的单项服务中采取适当的操作设置、指引，帮助儿童或监护人便捷地访问、更正、删除个人信息或注销账号，具体详见《西安省钱兄网络科技有限公司“码上台球”平台隐私政策》</strong>。</p><p>&nbsp;</p><p>2. 若通过上述方式无法实现管理儿童个人信息的目的，监护人和/或儿童还可以通过本政策第九条“如何联系我们”中的方式，及时与我们联系。</p><p>&nbsp;</p><p>3. 在访问、更正、删除、申请注销账号时，为了更好的保护儿童个人信息安全，我们可能会要求监护人和/或儿童进行身份验证。</p><p>&nbsp;</p><p><strong>七、 本政策更新</strong></p><p><strong>&nbsp;</strong></p><p><strong>我们可能会适时对本政策进行修订。当本政策的条款发生变更时，我们会在版本更新时以适当的方式向监护人或儿童提示更新后的内容，监护人如果不同意该等变更，可以选择要求儿童停止使用相关产品或服务；若儿童仍然继续使用的，即表示监护人已充分阅读、理解并同意更新后的本政策。</strong></p><p><strong>&nbsp;</strong></p><p><strong>八、 效力</strong></p><p><strong>&nbsp;</strong></p><p><strong>本政策是专门针对儿童的个人信息及隐私保护政策，包含对儿童个人信息的特殊保护，如《“码上台球”平台用户协议》</strong>、<strong>《西安省钱兄网络科技有限公司“码上台球”平台隐私政策》</strong>与本政策存在不一致或矛盾之处，请以本政策为准。</p><p>&nbsp;</p><p><strong>九、 如何联系我们</strong></p><p><strong>&nbsp;</strong></p><p><strong>如果监护人或儿童对本政策有任何疑问、意见或建议，或者对我们收集、使用或披露儿童的个人信息有任何问题，请通过邮箱与我们联系。</strong></p><p><strong>&nbsp;</strong></p><p><strong>为保障及时处理和反馈监护人或儿童的问题，监护人或儿童需要提供书面请求及有效联系方式。我们有可能会需要验证监护人与儿童的身份关系。一般情况下，我们会在验证身份后的十五天内或法律法规规定的期限内作出答复及合理解释。</strong></p><p><strong>&nbsp;</strong></p><p><strong>西安省钱兄网络科技有限公司有任何疑问、意见或建议，或者对我们收集、使用或披露儿童的个人信息有任何问题，请邮箱与我们联系。</strong></p><p><strong>&nbsp;</strong></p><p><strong>&nbsp;</strong></p><p><strong>更新时间：2025年3月1日</strong></p><p><strong>&nbsp;</strong></p><p><strong>生效时间：2025年3月1日</strong></p><p><strong>&nbsp;</strong></p><p><strong>&nbsp;</strong></p><p>&nbsp;</p><p class="ql-align-center">&nbsp;</p><p>&nbsp;</p><p class="ql-align-center"><br></p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (824, '2025-03-30 00:06:07', NULL, '充值提示', 824, '会员卡仅限开台消费使用，充值金额不可提现,会员卡仅限开台消费使用，充值金额不可提现', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (826, '2021-07-19 11:32:12', NULL, '订单超时取消（分钟）', 826, '5', 'fuwufei');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (827, '2025-04-07 17:09:59', NULL, '抖音APPID', 827, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (828, '2025-04-07 17:10:25', NULL, '抖音AppSecret', 828, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (829, '2025-04-07 15:44:52', NULL, '抖音门店id', 829, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (830, '2021-07-19 11:32:12', NULL, '物联网accessId', 830, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (831, '2025-03-29 18:18:24', NULL, '音响token', 831, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (832, '2025-03-29 18:18:36', NULL, '音响id', 832, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (833, '2021-07-19 11:32:12', NULL, '空调username', 833, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (834, '2021-07-19 11:32:12', NULL, '空调password', 834, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (843, '2025-03-30 00:25:14', NULL, '美团核销码兑换说明', 843, '<p>一、核销码简介</p><p>美团核销码是美团平台为商家提供的一种虚拟凭证，用户通过美团平台购买商品或服务后，会获得相应的核销码，用于在商家处进行兑换。</p><p>二、核销码兑换流程</p><p>购买商品或服务：用户通过美团平台选择并购买所需的商品或服务。</p><p>获取核销码：购买成功后，用户会收到包含核销码的短信或电子凭证，请妥善保管。</p><p>到店核销：用户前往商家门店，向商家出示核销码进行验证。商家在美团商家端进行核销操作，确认核销码的有效性。</p><p>完成兑换：核销成功后，用户即可享受所购买的商品或服务。</p><p>三、注意事项</p><p>核对信息：在核销前，请用户仔细核对核销码信息，确保无误。</p><p>保护隐私：请勿将核销码泄露给他人，以免造成不必要的损失。</p><p>有效期限：核销码具有有效期限，请在有效期内使用，过期作废。</p><p>异常情况：如遇到核销码无法正常使用的情况，请及时联系美团客服进行处理。</p><p>四、商家操作指南</p><p>登录商家端：商家需使用美团商家账号登录美团商家端。</p><p>查看核销码：在商家端中，可以查看用户购买的商品或服务详情，包括核销码信息。</p><p>进行核销：商家在确认用户出示的核销码与商家端显示的核销码一致后，进行核销操作。</p><p>记录核销情况：商家需记录核销情况，以便后续核对和管理。</p><p>以上是美团核销码兑换的说明，如有任何疑问，请随时联系美团客服</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (844, '2025-03-30 00:25:23', NULL, '抖音核销码兑换说明', 844, '<p>抖音核销码兑换说明</p><p>一、核销码简介</p><p>抖音核销码是抖音平台为商家提供的一种虚拟凭证，用户通过抖音平台购买商品或服务后，会获得相应的核销码，用于在商家处进行兑换。</p><p>二、核销码兑换流程</p><p>购买商品或服务：用户在抖音平台选择并购买所需的商品或服务。</p><p>获取核销码：购买成功后，用户会收到包含核销码的短信或电子凭证，请妥善保管。</p><p>到店核销：用户前往商家门店，向商家出示核销码进行验证。商家在抖音商家端进行核销操作，确认核销码的有效性。</p><p>完成兑换：核销成功后，用户即可享受所购买的商品或服务。</p><p>三、注意事项</p><p>核对信息：在核销前，请用户仔细核对核销码信息，确保无误。</p><p>保护隐私：请勿将核销码泄露给他人，以免造成不必要的损失。</p><p>有效期限：核销码具有有效期限，请在有效期内使用，过期作废。</p><p>异常情况：如遇到核销码无法正常使用的情况，请及时联系抖音客服或商家进行处理。</p><p>四、商家操作指南</p><p>登录商家端：商家需使用抖音商家账号登录抖音商家端。</p><p>查看核销码：在商家端中，可以查看用户购买的商品或服务详情，包括核销码信息。</p><p>进行核销：商家在确认用户出示的核销码与商家端显示的核销码一致后，进行核销操作。商家可以通过扫描用户手机上的核销码或手动输入核销码进行验证。</p><p>记录核销情况：商家需记录核销情况，包括核销时间、核销商品或服务等信息，以便后续核对和管理。</p><p>五、联系客服</p><p>如在核销过程中遇到任何问题，用户或商家均可通过抖音平台提供的客服渠道进行咨询或投诉。抖音客服将竭诚为您解答疑问并提供帮助。</p><p>以上是抖音核销码兑换的说明，如有任何疑问，请随时联系抖音客服或商家进行咨询。</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (845, '2025-03-30 00:25:33', NULL, '大众点评核销码兑换说明', 845, '<p>大众点评核销码兑换说明</p><p>一、核销码简介</p><p>大众点评核销码是用户在大众点评平台上购买商品或服务后获得的一种虚拟凭证。用户可以通过出示核销码在商家处进行兑换，享受所购买的商品或服务。</p><p>二、核销码兑换流程</p><p>1. 打开大众点评APP</p><p>首先，请确保您已经下载并安装了大众点评APP，并且已经登录了您的账号。</p><p>2. 进入“我的”页面</p><p>在APP底部导航栏中，点击“我的”按钮，进入个人中心页面。</p><p>3. 找到并点击“卡券”选项</p><p>在个人中心页面中，找到并点击“卡券”选项，进入卡券管理页面。</p><p>4. 选择要使用的核销码</p><p>在卡券管理页面中，找到您想要使用的核销码，并点击进入核销码详情页面。</p><p>5. 出示核销码给商家</p><p>在核销码详情页面中，点击“去使用”按钮，向商家出示核销码。商家将使用扫码设备或手动输入核销码进行验证。</p><p>6. 等待核销成功</p><p>商家验证核销码后，如果核销码有效且未过期，系统将自动完成核销操作，并提示核销成功。此时，您可以享受所购买的商品或服务。</p><p>三、注意事项</p><p>1. 核对核销码信息</p><p>在出示核销码前，请确保您选择的核销码与您所购买的商品或服务相符，并且仔细核对核销码信息，避免因为输入错误导致核销失败。</p><p>2. 保护隐私</p><p>请勿将核销码泄露给他人，以免造成不必要的损失。同时，在出示核销码时，请注意保护个人隐私和信息安全。</p><p>3. 有效期限</p><p>核销码具有有效期限，请在有效期内使用。如果核销码已经过期，将无法进行兑换。请留意核销码的有效期限，并及时使用。</p><p>4. 异常情况处理</p><p>如遇到核销码无法正常使用的情况，如核销码被误删、丢失或损坏等，请及时联系大众点评客服或商家进行处理。我们将竭诚为您解答疑问并提供帮助。</p><p>四、总结</p><p>大众点评核销码兑换是一种方便快捷的购物方式，用户可以通过出示核销码在商家处进行兑换，享受所购买的商品或服务。在兑换过程中，请确保核对核销码信息、保护隐私、注意有效期限以及处理异常情况。如有任何疑问或需要帮助，请随时联系大众点评客服或商家</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (895, '2025-03-11 16:50:34', NULL, '美团email', 895, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (896, '2025-03-07 17:23:46', NULL, '美团password', 896, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (897, '2025-03-07 17:23:46', NULL, '美团店铺id', 897, '8673914', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (898, '2025-06-24 12:59:09', NULL, '活动免责', 898, '<p>一、声明:</p><p>1、该协议的目的是为了活动发起人、组织者或领队(以下协议中简称:发起者)和同行成员明确知晓活动所存在的风险，提高参加人员的抗风险和自律能力，免除活动的发起人和同行成员在活动中出现的相关赔偿及法律连带等责任，让活动更加安全、健康、快乐、有意义。</p><p>2、活动中，对于违反国家相关法规、恶意侵犯他人或其它涉及犯罪行为的事件，则不在此协议范围内，必须由个人承担相应的法律责任。</p><p>3、凡报名参加活动的人员，均视为具有完全民事行为能力的人，均视为你已经仔细阅读和完全理解并同意接受《码上台球组局免责协议书》的全部条款。</p><p>4、此协议为相关责任的豁免、权利的放弃、风险的承担和免赔的协议。你如果在此协议上签字，潜在代表你就已经完全知晓、理解和同意接受《码上台球组局免责协议书》全部条款，你就放弃了向活动发起者和其他成员提起诉讼的权利;永远免除活动的发起者和同行成员的赔偿及法律连带责任。</p><p>二、风险的承担:</p><p>我参加活动是在完全自愿的基础上，依签署此有关豁免、权利放弃和风险自担的协议为前提条件的。我知晓活动的发起者只是活动的联系人，并非职业的领队、向导或有许可证的急救人员。我知晓领队、向导和急救人员及同行的成员可能并没有参加过户外生存课程或急救的培训和经验。我知晓他们并不能对我安全负责。</p><p>三、关于豁免责任，放弃权利和赔偿的协议内容:</p><p>1、本次活动非商业活动，非旅行社组团旅游，也非赢利性质自助游活动;是属于个人设计的出行日期及路线，活动性质仅限共同爱好者自愿参加、共同参与、风险自担、责任自负;全程遵循“自助，互助，低碳环保”的原则;发起者和其它参加活动人员一样，同样也是自愿参与者，对其他参加者没有绝对的管理和支配权力;对于活动中突发的一切足以影响到大家切身安全和利益的不可抗因素等重大事项尚需参加者共同商定;活动中发起者仅有义务对全队活动进行组织协调，参加者也有义务服从活动的整体安排。</p><p>2、凡报名参加本次活动的人员均视为你已经仔细阅读、完全理解责任豁免、权利的放弃、风险的承担和赔偿的协议的全部条款内容。代他人报名需告知被代报人《码上台球组局免责协议书》的全部条款，参加活动的被代报人也视同接受了本协议全部条款。同时，我知晓报名前必须事先与自己的家属沟通，取得家属的理解和支持，知晓并同意接受本协议全部条款后，报名后视同其家属也已知情并同意。</p><p>3、户外运动不同于常规的旅游活动，采取的线路也不是一般的常规旅游线路，有时气象条件也非常规，而是具有一定的探险性质;该项运动本身最大的特点在于其具有一定的风险性;活动安排的时间是估算时间，实际活动时间可能出现较大的差异;报名前，我已阅读活动内容和《码上台球组局免责协议书》的全部条款，并经过合理判断，同意接受本活动计划和《户外活动免责协议书》全部条款，同意自行承担本次活动的所有的风险和后果;包括交通工具及其他第三方设施带来的风险;如果由于我的行为和我的参加而导致了第三方的财产损失或个人伤害，我同意免除所有其他人的赔偿连带责任。</p><p>4、参加本次活动，包括登山、徒步、郊游、住宿(露营)、就餐以及活动和装备的使用等都存在有潜在危险，可能会受到身体损伤、瘫痪、甚至死亡的风险，我也知道健身和休闲等活动也存在突发病、心脏病等甚至死亡的风险;我自愿参加这些活动和使用活动的装备，如果因为在参加本活动中受到伤害，我同意自行承担和接受任何和所有伤害的风险，甚至死亡的风险，并且，放弃追究和要求赔偿的</p><p>权利，同时我永远免除此次活动发起者和同行成员的赔偿及法律连带责任。</p><p>5、本次活动可能对身体和精神方面都有一定考验，可能遇到在恶劣天气中进行活动的可能，也可能远离救助和医疗服务地点，短时间(或24小时)内无法得到救援;我清楚的知晓活动发起者无法全面预见该活动中所有的风险和不利，我知道以上所列的风险并不完全包含本次活动中的所有可能的风险;当我万一遇到上述等伤害后，所有救援和医疗等全部费用我同意全部承担，活动的发者和其它参加者不承担所发生的一切费用和法律连带责任;同时我永远免除此次活动的发起者和同行成员的赔偿和法律责任，并不仅限于以上所列的风险中。</p><p>6、我清楚知道此次活动发起者不是保险公司，没有义务为我购买意外保险等险种，我知道自己购买保险的意义，以防万一;我知道发起者已在活动内容中提醒我本人，在活动开始前自己了解保险类别和购买保险;对于活动中可能出现的意外事故，可由投保的保险公司承担相应的责任。</p><p>8在此我谨宣布我自己身体健康，适合参加户外骑行活动，没有那些会影响我参加户外骑行活动或使用装备的不适、损伤、病痛、心脏病、突发病历或其它疾病;我理解我能够通过以下措施来减少我的风险:注意环境，注意我的身体和精神状况，注意所有与安全相关的衣物和装备的适用状况，只参加那些自己精神和身体能力之内的活动;我清楚知道，我的安全是我个人的责任和取决于我的警惕和良好的判断;我同意和保证，如果任何时候我认为是不安全的，那么我会立刻中断活动的进行。如我在活动中发生各种疾病或伤害，甚至死亡，我同意放弃追究和要求赔偿的权利，同时我永远免除活动发起者和同行成员的赔偿及法律连带责任。</p><p>9当由于出现各种意外事故、突发气候变化和急性疾病等不可预测因素造成身体损害时，发起者和同行成员有义务尽力救助，但如果造成了不可逆转的损害，我同意放弃追究和要求赔偿的权利，同时我永远免除此次活动发起者和同行成员的赔偿及法律连带责任;我完全理解，在同伴需要协助的时候，我将配合协助发起者及同行成员尽力协助我的同伴，但这只是在以下情况:在我的判断中，这是自己力所能及的范围内，并且不会给自己带来不合理的危险;我进一步理解到，我没有法律上的责任去协助别人，同时，发起者以及我的同伴或者其他成员也没有任何法律上的责任来协助我。</p><p>10、我在此特意和有意的声明:我已经年满18岁!我已阅读和理解以上声明和全部条款内容;我理解参加户外活动相关的风险;我同意承担由于选择和参加以后的户外活动而随之来的全部责任;对于参加户外活动而引发或与之连带的任何和所有的法律要求(包括第三方的法律要求)和无论出现任何形式和性质的害伤，无论这些法律要求是基于某方的疏忽或其它原因引起的，我永远免除活动发起者和同行成员、任何第三方的赔偿及法律责任</p><p>11、我知晓报名前必须事先与自己的家属沟通，取得家属的理解和支持，知道并同意接受本协议全部条款后，才能报名参加本次活动，报名后视同其家属也已知情并同意;我知晓，一旦签署，此协议将生效，也知晓本协议同样有效于我的继承人、近亲、执行人、管理人、个人代表和转让人。代为签名者视为已沟通并被授权，否则由代签人承担后果。</p><p>12、协议范围:凡是报名参加并注册成为会员者均视为已经阅读、理解，并完全接受该协议条款;该协议自注册成为会员后开始生效，至退出成员时终止。(至集合地点前和活动结束解散后的路途，已不属于活动范围内)。(协议中的本次活动指码上台球组局每次举行的活动)。所产生的效力直至本人退出活动为止。</p>', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (899, '2026-06-17 20:21:54', NULL, '充值提示', 899, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1099, '2023-01-17 18:05:41', NULL, '快手AppId', 1099, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1100, '2023-01-17 18:05:41', NULL, '快手AppSecret', 1100, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1101, '2026-01-13 14:42:14', NULL, '快手refresh_token', 1101, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1102, '2025-06-03 15:15:33', NULL, '快手核销说明', 1102, '', 'xieyi');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1108, '2023-01-17 18:05:41', NULL, '设备令牌', 1108, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1109, '2023-01-17 18:05:41', NULL, '设备请求地址', 1109, 'https://eqm.nxchiplink.com', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1116, '2023-03-02 15:10:31', NULL, '提现底部提示', 1116, '每月14和28号可提现,节假日顺延,其他时间无法提现', 'fuwufei');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1119, '2023-08-28 10:14:04', NULL, '设备秘钥', 1119, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1160, '2025-09-24 09:30:35', NULL, '智能设备地址', 1160, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1161, '2025-09-24 09:30:35', NULL, '智能设备账号', 1161, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1162, '2025-09-24 09:30:35', NULL, '智能设备密码', 1162, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1163, '2025-09-24 09:30:35', NULL, '电动车充电配置', 1163, '1,2,3,4,5,6', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1164, '2025-09-24 09:30:35', NULL, '充电协议', 1164, '请确认充电枪已拔出并结束订单', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1165, '2022-06-21 16:47:57', NULL, '车辆查询api', 1165, '', 'xitong');
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1166, '2022-12-30 11:36:05', NULL, '车型配置', 1166, '轿车,SUV,MPV', 'xitong');

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon` (
  `coupon_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `coupon_name` varchar(255) DEFAULT NULL COMMENT '优惠券名称',
  `coupon_picture` varchar(255) DEFAULT NULL COMMENT '优惠券图片',
  `end_date` varchar(255) DEFAULT NULL COMMENT '有效期天数',
  `min_money` varchar(255) DEFAULT NULL COMMENT '优惠券可使用订单最低金额',
  `money` decimal(10,2) DEFAULT NULL COMMENT '优惠券金额',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of coupon
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_activity
-- ----------------------------
DROP TABLE IF EXISTS `coupon_activity`;
CREATE TABLE `coupon_activity` (
  `coupon_activity_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '优惠券活动id',
  `activity_time` varchar(64) DEFAULT NULL COMMENT '活动时间',
  `end_activity_time` varchar(64) DEFAULT NULL,
  `coupon_ids` varchar(255) DEFAULT NULL COMMENT '赠送优惠券id',
  `activity_name` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `is_ok` int(11) DEFAULT NULL COMMENT '是否执行 1是',
  `privilege_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`coupon_activity_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of coupon_activity
-- ----------------------------
INSERT INTO `coupon_activity` (`coupon_activity_id`, `activity_time`, `end_activity_time`, `coupon_ids`, `activity_name`, `create_time`, `is_ok`, `privilege_type`) VALUES (6, '2025-04-11 00:00:00', '2025-04-16 00:00:00', '170', '试营业全场体验', '2025-04-10 17:55:33', NULL, 5);

-- ----------------------------
-- Table structure for coupon_card
-- ----------------------------
DROP TABLE IF EXISTS `coupon_card`;
CREATE TABLE `coupon_card` (
  `coupon_card_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '卡密优惠券',
  `coupon_card_no` varchar(64) DEFAULT NULL COMMENT '卡密',
  `coupon_card_type_id` varchar(255) DEFAULT NULL COMMENT '卡密类型id',
  `status` int(11) DEFAULT NULL COMMENT '状态 1待使用 2已领取',
  `user_id` int(11) DEFAULT NULL COMMENT '领取用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`coupon_card_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of coupon_card
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_card_type
-- ----------------------------
DROP TABLE IF EXISTS `coupon_card_type`;
CREATE TABLE `coupon_card_type` (
  `coupon_card_type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '卡密类型id',
  `coupon_card_name` varchar(255) DEFAULT NULL COMMENT '卡密类型名称',
  `coupon_ids` varchar(255) DEFAULT NULL COMMENT '包含优惠券',
  `status` varchar(255) DEFAULT NULL COMMENT '状态 1开启 2关闭',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`coupon_card_type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of coupon_card_type
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_issue
-- ----------------------------
DROP TABLE IF EXISTS `coupon_issue`;
CREATE TABLE `coupon_issue` (
  `coupon_issue_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发布优惠券',
  `coupon_ids` varchar(255) DEFAULT NULL COMMENT '优惠券ids',
  `coupon_issue_name` varchar(255) DEFAULT NULL COMMENT '发布优惠券名称',
  `coupon_issue_image` varchar(255) DEFAULT NULL COMMENT '发布优惠券图片',
  `coupon_content_image` varchar(255) DEFAULT NULL COMMENT '优惠券主页图',
  `classify` varchar(255) DEFAULT NULL COMMENT '分类 1活动优惠券 2售卖优惠券',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `pay_notice` longtext COMMENT '购买须知',
  `is_limit` varchar(255) DEFAULT NULL COMMENT '是否限购',
  `coupon_num` int(11) DEFAULT NULL COMMENT '发放数量',
  `status` int(11) DEFAULT NULL COMMENT '状态 1开启 2关闭',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`coupon_issue_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of coupon_issue
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_user
-- ----------------------------
DROP TABLE IF EXISTS `coupon_user`;
CREATE TABLE `coupon_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `coupon_name` varchar(255) DEFAULT NULL COMMENT '优惠券名称',
  `coupon_picture` varchar(255) DEFAULT NULL COMMENT '优惠券图片',
  `create_time` varchar(255) DEFAULT NULL COMMENT '优惠券领取时间',
  `employ_time` varchar(255) DEFAULT NULL COMMENT '优惠券使用时间',
  `expiration_time` varchar(255) DEFAULT NULL COMMENT '优惠券过期时间',
  `min_money` decimal(10,2) DEFAULT NULL COMMENT '优惠券可使用订单最低金额',
  `money` decimal(10,2) DEFAULT NULL COMMENT '优惠券金额',
  `status` int(11) DEFAULT NULL COMMENT '优惠券状态 0正常  1已使用  2已失效',
  `end_time` varchar(255) DEFAULT NULL COMMENT '有效天数',
  `coupon_issue_id` int(11) DEFAULT NULL COMMENT '领取优惠券的活动id',
  `error_msg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of coupon_user
-- ----------------------------

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `goods_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `tearoom_id` int(11) DEFAULT NULL COMMENT '茶室id',
  `room_id` int(11) DEFAULT NULL COMMENT '房间id',
  `goods_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `goods_cover_image` varchar(2000) DEFAULT NULL COMMENT '商品封面图',
  `goods_title_image` varchar(2000) DEFAULT NULL COMMENT '商品轮播图',
  `goods_type` varchar(255) DEFAULT NULL COMMENT '商品类型 使用人数',
  `price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `sales` int(11) DEFAULT NULL COMMENT '销量',
  `goods_details` text COMMENT '套餐详情',
  `end_time` varchar(500) DEFAULT NULL COMMENT '结束时间',
  `goods_tips` varchar(2000) DEFAULT NULL COMMENT '温馨提示',
  `details_image` text COMMENT '商品图文介绍',
  `status` int(11) DEFAULT NULL COMMENT '状态 1正常 2下架',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `room_no` varchar(64) DEFAULT NULL COMMENT '房间编号',
  `device_no` varchar(64) DEFAULT NULL COMMENT '设备组编号',
  `product_ids` varchar(255) DEFAULT NULL COMMENT '必选产品 ids',
  `room_wifi` varchar(255) DEFAULT NULL COMMENT '房间wifi',
  `room_pwd` varchar(255) DEFAULT NULL COMMENT '房间密码',
  `labels` varchar(255) DEFAULT NULL COMMENT '标签',
  `inner_code` varchar(255) DEFAULT NULL,
  `channels` varchar(255) DEFAULT NULL COMMENT '绑定灯和锁球器',
  `goods_status` int(255) DEFAULT NULL COMMENT '当前使用状态 1空闲  2正在使用',
  `orders_id` int(11) DEFAULT NULL COMMENT '正在占用的订单id',
  `goods_category` int(11) DEFAULT NULL COMMENT '类型 1快充 2慢充',
  `subscription` decimal(10,2) DEFAULT NULL COMMENT '预约费',
  `cash_pledge` decimal(10,2) DEFAULT NULL COMMENT '押金',
  `hour_price` decimal(10,2) DEFAULT NULL COMMENT '小时费',
  `minute_price` decimal(10,2) DEFAULT NULL COMMENT '分钟费用',
  `yy_token` varchar(255) DEFAULT NULL,
  `yy_id` varchar(255) DEFAULT NULL,
  `camera_id` varchar(64) DEFAULT NULL,
  `icon_img` varchar(255) DEFAULT NULL,
  `is_open` int(11) DEFAULT NULL,
  `weight_num` decimal(10,2) DEFAULT NULL,
  `weight_range` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (37, 20, NULL, '01号插座', 'https://tu.mianyazuju.com/20250415/5c4c1e005a694c118d7ceda93e2b1388.png', 'https://tu.mianyazuju.com/20250415/0e478b04abf546d799a5a96e942ac339.png', '01', NULL, 62, '', NULL, '', NULL, 1, '2025-02-21 15:52:04', 1, NULL, NULL, '', NULL, '', '', '', '', 1, 11413, 2, '50.00', '10.00', '1.50', '0.03', '', '', NULL, 'https://tu.mianyazuju.com/20250411/9a204d3f40b74635906aa7aa2ab75be3.jpg', 2, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (38, 20, NULL, '02号插座', 'https://tu.mianyazuju.com/20250415/b577252f538d485d9f8271fb6401842e.png', 'https://tu.mianyazuju.com/20250415/ff47df0257964958a2419ae21d0359cd.png', '02', NULL, 26, '', NULL, '', NULL, 1, '2025-02-21 15:53:59', 2, NULL, NULL, '', NULL, NULL, '', NULL, '', 1, 10504, 2, '50.00', '10.00', '1.50', '0.03', '', '', '', 'https://tu.mianyazuju.com/20250411/c169539c96fd4944bf7e1ffc8f3c4354.jpg', NULL, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (39, 20, NULL, '03号插座', 'https://tu.mianyazuju.com/20250415/22b78dc1d70344b4a3e09fd1adf7f94e.png', 'https://tu.mianyazuju.com/20250415/c906483baaff4319900670833bec613b.png', '03', NULL, 25, '', NULL, '', NULL, 1, '2025-02-21 15:54:16', 3, NULL, NULL, '', NULL, NULL, '', NULL, '', 1, 7550, 2, '50.00', '10.00', '1.50', '0.03', '', '', NULL, 'https://tu.mianyazuju.com/20250411/9a204d3f40b74635906aa7aa2ab75be3.jpg', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (40, 20, NULL, '04号插座', 'https://tu.mianyazuju.com/20250411/1dd014325ec94455a627ed971a795f9c.png', 'https://tu.mianyazuju.com/20250411/11350eb546ca49d2bf837817bb4c025b.png', '04', NULL, 30, '', NULL, '', NULL, 1, '2025-02-21 22:50:59', NULL, NULL, NULL, '', NULL, NULL, '', NULL, '', 1, 11906, 2, '20.00', '1.00', '1.50', '0.03', '', '', NULL, 'https://tu.mianyazuju.com/20250411/8009471dc9e941a4a8348d1b58d8f490.jpg', 2, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (42, 21, NULL, '01号插座', 'https://tu.mianyazuju.com/20250415/11a7d059e8b94774bbcc682bc29792cf.png', 'https://tu.mianyazuju.com/20250415/5cadb1391e2d435fbfed05dd70344304.png', '01', NULL, 550, '', NULL, 'live?port=11935&app=live&stream=sqx-006', NULL, 1, '2025-02-25 13:54:43', 1, NULL, NULL, '', NULL, NULL, '', '', '', 1, 11845, 2, '35.00', '0.00', '1.50', '0.03', '', '', '', 'https://tu.mianyazuju.com/20250411/0e2a5f0674564bed9ee8e4bafd332818.png', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (43, 21, NULL, '02号插座', 'https://tu.mianyazuju.com/20250415/6dcd6f3700ac4942929333cccb759a1c.png', 'https://tu.mianyazuju.com/20250415/7c8d6377feab43c48a17b460cd3ab5fb.png', '02', NULL, 5087, '', NULL, 'live?port=11935&app=live&stream=sqx-002', NULL, 1, '2025-02-25 13:56:42', 2, NULL, NULL, '', NULL, NULL, '', '', '', 1, 11874, 2, '25.00', '0.00', '1.50', '0.03', '', '', '', 'https://tu.mianyazuju.com/20250411/ac60f36b432b4273a239cb8d12ee9f84.png', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (44, 21, NULL, '03号插座', 'https://tu.mianyazuju.com/20250415/4c569f6e9a6947af9efded0f6142581e.png', 'https://tu.mianyazuju.com/20250415/c1127d005adf47edbccd4bd4f9e922d3.png', '03', NULL, 879, '', NULL, 'live?port=11935&app=live&stream=sqx-003', NULL, 1, '2025-02-25 13:57:26', 3, NULL, NULL, '', NULL, NULL, '', '', '', 1, 11870, 2, '25.00', '0.00', '1.50', '0.03', '', '', '', 'https://tu.mianyazuju.com/20250411/6550cebdd7c345819a71906b1bd15d34.png', 2, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (45, 21, NULL, '04号插座', 'https://tu.mianyazuju.com/20250403/1a5583e00e024cb3aba7f98f2f1b3b35.png', 'https://tu.mianyazuju.com/20250403/e0ecd55d82424a268706645adf5cfe81.png', '04', NULL, 180, '', NULL, '', NULL, 1, '2025-02-25 13:59:54', 8, NULL, NULL, '', NULL, NULL, '', NULL, '', 1, 11831, 2, '25.00', '0.00', '1.50', '0.03', '', '', NULL, 'https://tu.mianyazuju.com/20250411/3b74a74b53ca41df8d4602b1d4914cd9.png', NULL, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (63, 22, NULL, '快充01号枪', 'https://tu.mianyazuju.com/20250508/62291dbf801d45b5a8404f1b89a02123.png', 'https://tu.mianyazuju.com/20250508/3a3448b0ef56457d9ea6ee869dd31bfc.png', '01', NULL, 1, '', NULL, '', NULL, 1, '2025-05-08 11:03:50', 1, NULL, NULL, '', NULL, NULL, '', '', '', 1, 11920, 1, '25.00', '0.00', NULL, NULL, '', '', '', 'https://tu.mianyazuju.com/20250508/2cf8003a9f52452b8723b6524e614c7e.png', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (64, 22, NULL, '快充02号枪', 'https://tu.mianyazuju.com/20250508/dbcff14bfc6d40a68aecebfa49a053dd.png', 'https://tu.mianyazuju.com/20250508/a545a388f48747bd8aa9e05f4440a2ba.png', '02', NULL, 0, '', NULL, '', NULL, 1, '2025-05-08 11:05:44', 2, NULL, NULL, '', NULL, NULL, '', '', '', 1, 2534, 1, '25.00', '0.00', NULL, NULL, '', '', '', 'https://tu.mianyazuju.com/20250508/ce9997ce66da4beba639ed431616b45b.png', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (65, 22, NULL, '快充03号枪', 'https://tu.mianyazuju.com/20250508/dbcff14bfc6d40a68aecebfa49a053dd.png', 'https://tu.mianyazuju.com/20250508/a545a388f48747bd8aa9e05f4440a2ba.png', '03', NULL, 0, '', NULL, '', NULL, 1, '2025-05-08 11:06:15', 3, NULL, NULL, '', NULL, NULL, '', '', '', 1, 2535, 1, '25.00', '0.00', NULL, NULL, '', '', '', 'https://tu.mianyazuju.com/20250508/ce9997ce66da4beba639ed431616b45b.png', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (66, 22, NULL, '慢充01号枪', 'https://tu.mianyazuju.com/20250508/dd460bb2b9f7438abdb7c177b9b03de8.png', 'https://tu.mianyazuju.com/20250508/dddf1371c0444a9ab0a611ee125fa441.png', '04', NULL, 1, '', NULL, '', NULL, 1, '2025-05-08 16:02:06', 4, NULL, NULL, '', NULL, NULL, '', NULL, '', 1, 11922, 2, '25.00', '0.00', NULL, NULL, '', '', '', 'https://tu.mianyazuju.com/20250508/4e269523b69146cdb9bdc2f4c3689448.png', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (88, 23, NULL, '01号插座', '', '', '', NULL, 1, '', NULL, '', NULL, 1, '2026-01-29 10:40:40', 1, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, 11941, 2, NULL, NULL, '1.50', '0.03', '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (89, 24, NULL, '01号插座', '', '', '', NULL, 9, '', NULL, '', NULL, 1, '2026-03-04 10:36:30', 1, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, 11923, 2, NULL, NULL, '1.50', '0.03', '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (90, 24, NULL, '02号插座', '', '', '', NULL, 0, '', NULL, '', NULL, 1, '2026-03-04 11:04:01', 2, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, NULL, 2, NULL, NULL, '1.50', '0.03', '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (91, 24, NULL, '03号插座', '', '', '', NULL, 0, '', NULL, '', NULL, 1, '2026-03-04 11:04:16', 3, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, NULL, 2, NULL, NULL, '1.50', '0.03', '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (92, 20, NULL, '05号插座', '', '', '', NULL, 50, '', NULL, '', NULL, 1, '2026-03-04 13:40:14', 2, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, NULL, 2, NULL, NULL, '1.50', '0.03', '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (93, 25, NULL, '快充01号枪', '', '', '', NULL, 1000, '', NULL, '', NULL, 1, '2026-05-12 17:53:33', 1, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, NULL, 1, NULL, NULL, NULL, NULL, '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (94, 25, NULL, '快充02号枪', '', '', '', NULL, 5000, '', NULL, '', NULL, 1, '2026-05-12 17:53:47', 2, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, NULL, 1, NULL, NULL, NULL, NULL, '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (95, 25, NULL, '快充03号枪', '', '', '', NULL, 564231, '', NULL, '', NULL, 1, '2026-05-12 17:54:01', 3, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, NULL, 1, NULL, NULL, NULL, NULL, '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (96, 25, NULL, '慢充01号枪', '', '', '', NULL, 163544, '', NULL, '', NULL, 1, '2026-05-12 17:54:15', 4, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, NULL, 2, NULL, NULL, NULL, NULL, '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (97, 25, NULL, '慢充02号枪', '', '', '', NULL, 1324, '', NULL, '', NULL, 1, '2026-05-12 17:54:33', 5, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, NULL, 2, NULL, NULL, NULL, NULL, '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (98, 26, NULL, '01号插座', '', '', '', NULL, 11, '', NULL, '', NULL, 1, '2026-05-27 16:06:00', 1, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, 11935, 2, NULL, NULL, '1.50', '0.03', '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (99, 26, NULL, '02号插座', '', '', '', NULL, 1, '', NULL, '', NULL, 1, '2026-05-27 16:06:11', 2, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, 11926, 2, NULL, NULL, '1.50', '0.03', '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (100, 26, NULL, '03号插座', '', '', '', NULL, 1, '', NULL, '', NULL, 1, '2026-05-27 16:06:23', 3, NULL, NULL, '', NULL, NULL, '', NULL, '', 1, 11936, 2, NULL, NULL, '1.50', '0.03', '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (101, 28, NULL, '1号', '', '', '', NULL, 11, '', NULL, '', NULL, 1, '2026-10-30 22:24:10', 1, NULL, NULL, NULL, NULL, NULL, '', NULL, '', 1, 11940, 2, NULL, NULL, NULL, NULL, '', '', '', '', 1, NULL, NULL);
INSERT INTO `goods` (`goods_id`, `tearoom_id`, `room_id`, `goods_name`, `goods_cover_image`, `goods_title_image`, `goods_type`, `price`, `sales`, `goods_details`, `end_time`, `goods_tips`, `details_image`, `status`, `create_time`, `sort`, `room_no`, `device_no`, `product_ids`, `room_wifi`, `room_pwd`, `labels`, `inner_code`, `channels`, `goods_status`, `orders_id`, `goods_category`, `subscription`, `cash_pledge`, `hour_price`, `minute_price`, `yy_token`, `yy_id`, `camera_id`, `icon_img`, `is_open`, `weight_num`, `weight_range`) VALUES (102, 28, NULL, '2号', '', '', '', NULL, NULL, '', NULL, '', NULL, 1, '2026-10-30 23:37:58', 2, NULL, NULL, '', NULL, NULL, '', NULL, '', 1, NULL, 2, NULL, NULL, NULL, NULL, '', '', '', '', 1, NULL, NULL);

-- ----------------------------
-- Table structure for goods_time
-- ----------------------------
DROP TABLE IF EXISTS `goods_time`;
CREATE TABLE `goods_time` (
  `goods_time_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '电费id',
  `tearoom_id` int(11) DEFAULT NULL COMMENT '充电站id',
  `start_time` varchar(64) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(64) DEFAULT NULL COMMENT '结束时间',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `hour_price` decimal(10,2) DEFAULT NULL COMMENT '小时价',
  `minute_price` decimal(10,2) DEFAULT NULL COMMENT '分钟价',
  `member_hour_price` decimal(10,2) DEFAULT NULL COMMENT '会员价 小时价',
  `member_minute_price` decimal(10,2) DEFAULT NULL COMMENT '会员价 分钟价',
  `electricity_price` decimal(10,2) DEFAULT NULL COMMENT '用户每度电价格',
  `member_electricity_price` decimal(10,2) DEFAULT NULL COMMENT '会员每度电价格',
  PRIMARY KEY (`goods_time_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of goods_time
-- ----------------------------
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (20, 20, '00:00:00', '07:59:59', '2026-09-14 17:14:38', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (21, 20, '08:00:00', '21:59:59', '2026-09-14 17:14:38', '1.50', '0.03', '1.20', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (22, 20, '22:00:00', '23:59:59', '2026-09-14 17:14:38', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (23, 21, '00:00:00', '07:59:59', '2026-09-14 17:14:38', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (24, 21, '08:00:00', '21:59:59', '2026-09-14 17:14:38', '1.50', '0.03', '1.20', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (25, 21, '22:00:00', '23:59:59', '2026-09-14 17:14:38', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (26, 22, '00:00:00', '07:59:59', '2026-09-14 17:14:38', NULL, NULL, NULL, NULL, '0.78', '0.65');
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (27, 22, '08:00:00', '23:59:59', '2026-09-14 17:14:38', NULL, NULL, NULL, NULL, '1.28', '1.08');
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (28, 23, '00:00:00', '07:59:59', '2026-09-14 17:14:38', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (29, 23, '08:00:00', '21:59:59', '2026-09-14 17:14:38', '1.50', '0.03', '1.20', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (30, 23, '22:00:00', '23:59:59', '2026-09-14 17:14:38', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (31, 24, '00:00:00', '07:59:59', '2026-09-14 17:14:38', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (32, 24, '08:00:00', '21:59:59', '2026-09-14 17:14:38', '1.50', '0.03', '1.20', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (33, 24, '22:00:00', '23:59:59', '2026-09-14 17:14:38', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (34, 25, '00:00:00', '07:59:59', '2026-09-14 17:14:39', NULL, NULL, NULL, NULL, '0.78', '0.65');
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (35, 25, '08:00:00', '23:59:59', '2026-09-14 17:14:39', NULL, NULL, NULL, NULL, '1.28', '1.08');
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (36, 26, '00:00:00', '07:59:59', '2026-09-14 17:14:39', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (37, 26, '08:00:00', '21:59:59', '2026-09-14 17:14:39', '1.50', '0.03', '1.20', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (38, 26, '22:00:00', '23:59:59', '2026-09-14 17:14:39', '1.00', '0.02', '0.80', '0.02', NULL, NULL);
INSERT INTO `goods_time` (`goods_time_id`, `tearoom_id`, `start_time`, `end_time`, `create_time`, `hour_price`, `minute_price`, `member_hour_price`, `member_minute_price`, `electricity_price`, `member_electricity_price`) VALUES (39, 28, '00:00', '23:59', '2026-10-30 22:23:31', '0.60', '0.01', '0.06', '0.00', NULL, NULL);

-- ----------------------------
-- Table structure for help_classify
-- ----------------------------
DROP TABLE IF EXISTS `help_classify`;
CREATE TABLE `help_classify` (
  `help_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助中心分类',
  `help_classify_name` varchar(500) DEFAULT NULL COMMENT '分类名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `types` int(11) DEFAULT NULL,
  PRIMARY KEY (`help_classify_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of help_classify
-- ----------------------------
INSERT INTO `help_classify` (`help_classify_id`, `help_classify_name`, `sort`, `parent_id`, `create_time`, `types`) VALUES (21, '1.如何查找附近充电站？', 0, NULL, '2026-09-14 17:14:40', NULL);
INSERT INTO `help_classify` (`help_classify_id`, `help_classify_name`, `sort`, `parent_id`, `create_time`, `types`) VALUES (22, '2.如何启动充电？', 1, NULL, '2026-09-14 17:14:40', NULL);
INSERT INTO `help_classify` (`help_classify_id`, `help_classify_name`, `sort`, `parent_id`, `create_time`, `types`) VALUES (23, '3.电瓶车与汽车如何计费？', 2, NULL, '2026-09-14 17:14:40', NULL);
INSERT INTO `help_classify` (`help_classify_id`, `help_classify_name`, `sort`, `parent_id`, `create_time`, `types`) VALUES (24, '4.充电过程中可以离开吗？', 3, NULL, '2026-09-14 17:14:40', NULL);
INSERT INTO `help_classify` (`help_classify_id`, `help_classify_name`, `sort`, `parent_id`, `create_time`, `types`) VALUES (25, '5.如何结束订单与退款？', 4, NULL, '2026-09-14 17:14:40', NULL);
INSERT INTO `help_classify` (`help_classify_id`, `help_classify_name`, `sort`, `parent_id`, `create_time`, `types`) VALUES (26, '6.站点运营商如何入驻？', 5, NULL, '2026-09-14 17:14:40', NULL);

-- ----------------------------
-- Table structure for help_word
-- ----------------------------
DROP TABLE IF EXISTS `help_word`;
CREATE TABLE `help_word` (
  `help_word_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助文档id',
  `help_word_title` varchar(255) DEFAULT NULL COMMENT '帮助标题',
  `help_word_content` longtext COMMENT '帮助文档内容',
  `help_classify_id` int(11) DEFAULT NULL COMMENT '帮助分类id',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_word_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of help_word
-- ----------------------------
INSERT INTO `help_word` (`help_word_id`, `help_word_title`, `help_word_content`, `help_classify_id`, `sort`, `create_time`) VALUES (22, '如何查找附近充电站？', '<p>1.打开首页，系统会根据定位展示附近充电站；</p><p>2.可切换「电瓶车 / 汽车桩」查看不同类型站点；</p><p>3.支持按距离、快充、慢充、低价等条件筛选；</p><p>4.点击站点可查看详情、空闲插座数量并导航前往。</p>', 21, 0, '2026-09-14 17:14:40');
INSERT INTO `help_word` (`help_word_id`, `help_word_title`, `help_word_content`, `help_classify_id`, `sort`, `create_time`) VALUES (23, '如何启动充电？', '<p>1.到达站点后，在详情页选择空闲插座/充电枪；</p><p>2.确认计费规则后点击开始充电并完成支付；</p><p>3.按设备提示正确插枪，确认指示灯正常后即可离开；</p><p>4.充电完成后请及时拔枪并结束订单。</p>', 22, 1, '2026-09-14 17:14:40');
INSERT INTO `help_word` (`help_word_id`, `help_word_title`, `help_word_content`, `help_classify_id`, `sort`, `create_time`) VALUES (24, '电瓶车与汽车如何计费？', '<p>1.电瓶车一般按小时计费，不同时段价格可能不同；</p><p>2.汽车一般按电量（度）计费，峰谷电价不同；</p><p>3.会员用户可享受对应会员价；</p><p>4.具体以站点详情页「收费标准」为准。</p>', 23, 2, '2026-09-14 17:14:40');
INSERT INTO `help_word` (`help_word_id`, `help_word_title`, `help_word_content`, `help_classify_id`, `sort`, `create_time`) VALUES (25, '充电过程中可以离开吗？', '<p>可以。启动成功后可离开，请保管好车辆钥匙与随身物品。</p><p>建议在小程序中关注订单状态，充满或异常时及时处理。</p>', 24, 3, '2026-09-14 17:14:40');
INSERT INTO `help_word` (`help_word_id`, `help_word_title`, `help_word_content`, `help_classify_id`, `sort`, `create_time`) VALUES (26, '如何结束订单与退款？', '<p>1.在「我的-充电订单」中找到进行中的订单；</p><p>2.确认已拔枪后点击结束订单，系统自动结算；</p><p>3.如有预付余额或异常扣费，将按规则原路退回；</p><p>4.如遇设备故障，请联系站点客服协助处理。</p>', 25, 4, '2026-09-14 17:14:40');
INSERT INTO `help_word` (`help_word_id`, `help_word_title`, `help_word_content`, `help_classify_id`, `sort`, `create_time`) VALUES (27, '站点运营商如何入驻？', '<p>如您有电瓶车或汽车充电站资源，可通过平台客服提交站点信息、设备信息与资质材料，审核通过后即可上线运营。</p>', 26, 5, '2026-09-14 17:14:40');

-- ----------------------------
-- Table structure for invite
-- ----------------------------
DROP TABLE IF EXISTS `invite`;
CREATE TABLE `invite` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) DEFAULT NULL COMMENT '邀请者id',
  `invitee_user_id` int(11) DEFAULT NULL COMMENT '被邀请者id',
  `money` decimal(10,2) DEFAULT NULL COMMENT '收益',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `money_type` int(1) DEFAULT NULL COMMENT '1会员2陪玩',
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4158 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='邀请信息';

-- ----------------------------
-- Records of invite
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for invite_money
-- ----------------------------
DROP TABLE IF EXISTS `invite_money`;
CREATE TABLE `invite_money` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收益钱包id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `money_sum` decimal(10,2) DEFAULT NULL COMMENT '总获取收益',
  `money` decimal(10,2) DEFAULT NULL COMMENT '当前金额',
  `cash_out` decimal(10,2) DEFAULT NULL COMMENT '累计提现',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of invite_money
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `member_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员特权id',
  `member_img` varchar(255) DEFAULT NULL COMMENT '特权图标',
  `member_name` varchar(255) DEFAULT NULL COMMENT '特权名称',
  `sort` varchar(255) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`member_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` (`member_id`, `member_img`, `member_name`, `sort`) VALUES (3, '', '团队特权', '2');
INSERT INTO `member` (`member_id`, `member_img`, `member_name`, `sort`) VALUES (4, '', '九折专享', '1');
INSERT INTO `member` (`member_id`, `member_img`, `member_name`, `sort`) VALUES (6, '', '身份标识', '4');
INSERT INTO `member` (`member_id`, `member_img`, `member_name`, `sort`) VALUES (9, '', '免广告', '5');
INSERT INTO `member` (`member_id`, `member_img`, `member_name`, `sort`) VALUES (10, '', '专属客服', '6');
INSERT INTO `member` (`member_id`, `member_img`, `member_name`, `sort`) VALUES (12, '', '专属奖励', '8');

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '内容',
  `create_at` varchar(5000) DEFAULT NULL COMMENT '创建时间',
  `image` varchar(600) DEFAULT NULL COMMENT '图片',
  `is_see` varchar(600) DEFAULT NULL COMMENT '是否已读',
  `send_state` varchar(600) DEFAULT NULL COMMENT '未用到',
  `send_time` varchar(600) DEFAULT NULL COMMENT '未用到',
  `state` varchar(600) DEFAULT NULL COMMENT '分类',
  `title` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标题',
  `url` varchar(600) DEFAULT NULL COMMENT '地址',
  `type` varchar(600) DEFAULT NULL COMMENT '未用到',
  `platform` int(11) DEFAULT NULL COMMENT '来源',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户名',
  `audit_content` varchar(1000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '审核内容',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `by_user_id` int(11) DEFAULT NULL COMMENT '对方id',
  `platform_id` int(11) DEFAULT NULL COMMENT '来源id',
  `phone` varchar(255) DEFAULT NULL,
  `to_user_name` varchar(255) DEFAULT NULL,
  `from_user_name` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `wx_biz_msg_crypt` varchar(255) DEFAULT NULL,
  `type_name` varchar(255) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23804 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of message_info
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '短信验证码',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_name` (`code`,`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of msg
-- ----------------------------
INSERT INTO `msg` (`id`, `code`, `phone`) VALUES (3, '244016', '');
INSERT INTO `msg` (`id`, `code`, `phone`) VALUES (6, '622501', '');
INSERT INTO `msg` (`id`, `code`, `phone`) VALUES (8, '802830', '');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `orders_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `tearoom_id` int(11) DEFAULT NULL COMMENT '茶室id',
  `goods_id` varchar(255) DEFAULT NULL COMMENT '商品id',
  `goods_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `goods_cover_image` varchar(255) DEFAULT NULL COMMENT '商品封面图',
  `pay_money` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `status` int(11) DEFAULT NULL COMMENT '状态 1待支付 2待使用 3使用中 4已完成 5已取消 6待评价',
  `start_time` varchar(64) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(64) DEFAULT NULL COMMENT '结束时间',
  `room_id` int(11) DEFAULT NULL COMMENT '房间id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `pay_way` int(11) DEFAULT NULL COMMENT '支付方式 1微信 2支付宝 3零钱',
  `orders_no` varchar(64) DEFAULT NULL COMMENT '订单号',
  `pay_time` varchar(64) DEFAULT NULL COMMENT '支付时间',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `coupon_id` int(11) DEFAULT NULL COMMENT '优惠券id',
  `coupon_money` decimal(10,2) DEFAULT NULL COMMENT '优惠券抵扣',
  `coupon_name` varchar(255) DEFAULT NULL COMMENT '优惠券名称',
  `open_door` int(11) DEFAULT NULL COMMENT '是否拥有权限 1有 2没有',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `is_delete` int(11) DEFAULT NULL COMMENT '是否删除 1是',
  `zhi_user_id` int(11) DEFAULT NULL COMMENT '一级用户id',
  `zhi_money` decimal(10,2) DEFAULT NULL COMMENT '直属收益',
  `fei_user_id` int(11) DEFAULT NULL COMMENT '二级用户id',
  `fei_money` decimal(10,2) DEFAULT NULL COMMENT '二级收益',
  `money` decimal(10,2) DEFAULT NULL COMMENT '茶室收益',
  `add_hour` int(11) DEFAULT NULL COMMENT '加钟时长',
  `add_pay_money` decimal(10,2) DEFAULT NULL COMMENT '加钟支付金额',
  `hour` decimal(10,2) DEFAULT NULL COMMENT '时长',
  `one_pay_money` decimal(10,2) DEFAULT NULL COMMENT '一级收益',
  `orders_type` varchar(255) DEFAULT NULL COMMENT '订单类型 1包间 2商品',
  `small_hardware_id` int(11) DEFAULT NULL COMMENT '柜子id',
  `small_goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `is_subscription` int(11) DEFAULT NULL COMMENT '预约金是否退还 1是 2否',
  `minute_price` decimal(10,2) DEFAULT NULL COMMENT '每分钟价格',
  `minute_num` int(11) DEFAULT NULL COMMENT '超出时间',
  `minute_money` decimal(10,2) DEFAULT NULL COMMENT '扣除费用 超出时长后 按照每分钟进行计费',
  `sum_money` decimal(10,2) DEFAULT NULL COMMENT '剩余余额 默认为押金金额 超出时长后 按照分钟每分钟进行计费',
  `cash_pledge` decimal(10,2) DEFAULT NULL COMMENT '押金',
  `is_cash_msg` int(11) DEFAULT NULL COMMENT '押金时间即将结束 通知 1已通知',
  `is_hour_msg` int(11) DEFAULT NULL COMMENT '套餐时间即将结束 通知 1已通知',
  `hour_money` decimal(10,2) DEFAULT NULL,
  `shop_money` decimal(10,2) DEFAULT NULL,
  `user_money_minute_num` int(11) DEFAULT NULL,
  `user_money_minute_money` decimal(10,2) DEFAULT NULL,
  `is_shop` int(11) DEFAULT NULL,
  `goods_category` int(11) DEFAULT NULL COMMENT '类型 1台球 2棋牌',
  `vip_money` decimal(10,2) DEFAULT NULL COMMENT '会员优惠金额',
  `vip_index` int(11) DEFAULT NULL COMMENT '会员等级',
  `user_remark` varchar(2000) DEFAULT NULL,
  `platform_type` int(11) DEFAULT NULL COMMENT '订单优惠券平台 1美团2抖音',
  `is_luck` int(11) DEFAULT NULL,
  `is_birthday` int(11) DEFAULT NULL,
  `price_type` int(11) DEFAULT NULL COMMENT '订单计费方式 1到期关闭 2持续计费',
  `old_pay_money` decimal(10,2) DEFAULT NULL,
  `refund_money` decimal(10,2) DEFAULT NULL,
  `refund_remark` varchar(2000) DEFAULT NULL,
  `coupon_ids` varchar(64) DEFAULT NULL,
  `coupon_moneys` decimal(10,2) DEFAULT NULL,
  `coupon_names` varchar(2000) DEFAULT NULL,
  `open_user_id` int(11) DEFAULT NULL,
  `close_user_id` int(11) DEFAULT NULL,
  `shop_way` int(11) DEFAULT NULL,
  `shop_discount` varchar(64) DEFAULT NULL,
  `shop_pay_money` decimal(10,2) DEFAULT NULL,
  `orders_goods_id` int(11) DEFAULT NULL,
  `sys_user_id` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
  PRIMARY KEY (`orders_id`) USING BTREE,
  KEY `tearoom_id` (`tearoom_id`),
  KEY `goods_id` (`goods_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11942 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for orders_video
-- ----------------------------
DROP TABLE IF EXISTS `orders_video`;
CREATE TABLE `orders_video` (
  `orders_video_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单视频id',
  `orders_id` int(11) DEFAULT NULL COMMENT '订单id',
  `tearoom_id` int(11) DEFAULT NULL COMMENT '棋牌室id',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `video_url` varchar(2000) DEFAULT NULL COMMENT '视频内容',
  `video_time` varchar(64) DEFAULT NULL COMMENT '视频时间',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `classify` int(11) DEFAULT NULL,
  `tearoom_camera_id` varchar(64) DEFAULT NULL COMMENT '门店摄像头设备id',
  `goods_camera_id` varchar(64) DEFAULT NULL COMMENT '桌子摄像头id',
  PRIMARY KEY (`orders_video_id`) USING BTREE,
  KEY `orders_id` (`orders_id`),
  KEY `tearoom_id` (`tearoom_id`),
  KEY `goods_id` (`goods_id`),
  KEY `classify` (`classify`)
) ENGINE=InnoDB AUTO_INCREMENT=185857 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of orders_video
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for pay_classify
-- ----------------------------
DROP TABLE IF EXISTS `pay_classify`;
CREATE TABLE `pay_classify` (
  `pay_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '充值分类id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `give_money` decimal(10,2) DEFAULT NULL COMMENT '赠送数量',
  `money` decimal(10,2) DEFAULT NULL COMMENT '购买数量',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) DEFAULT NULL COMMENT '时间',
  `is_recommend` int(11) DEFAULT NULL,
  `pay_content` varchar(2000) DEFAULT NULL COMMENT '充值描述',
  PRIMARY KEY (`pay_classify_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of pay_classify
-- ----------------------------
INSERT INTO `pay_classify` (`pay_classify_id`, `price`, `give_money`, `money`, `sort`, `create_time`, `is_recommend`, `pay_content`) VALUES (5, '200.00', '0.00', '200.00', 3, NULL, 0, '八折银卡会员');
INSERT INTO `pay_classify` (`pay_classify_id`, `price`, `give_money`, `money`, `sort`, `create_time`, `is_recommend`, `pay_content`) VALUES (7, '500.00', '10.00', '510.00', 4, '2023-08-04 11:32:32', 0, '七折金卡会员');
INSERT INTO `pay_classify` (`pay_classify_id`, `price`, `give_money`, `money`, `sort`, `create_time`, `is_recommend`, `pay_content`) VALUES (8, '1000.00', '0.00', '1000.00', 5, '2025-03-13 11:03:08', NULL, '五折黑卡会员');
INSERT INTO `pay_classify` (`pay_classify_id`, `price`, `give_money`, `money`, `sort`, `create_time`, `is_recommend`, `pay_content`) VALUES (10, '2000.00', '0.00', '2000.00', 6, '2025-04-02 00:04:19', NULL, '五折黑卡会员');
INSERT INTO `pay_classify` (`pay_classify_id`, `price`, `give_money`, `money`, `sort`, `create_time`, `is_recommend`, `pay_content`) VALUES (11, '3000.00', '0.00', '3000.00', 7, '2025-04-03 12:23:57', NULL, '五折黑卡会员');
INSERT INTO `pay_classify` (`pay_classify_id`, `price`, `give_money`, `money`, `sort`, `create_time`, `is_recommend`, `pay_content`) VALUES (12, '5000.00', '0.00', '5000.00', 9, '2025-04-03 12:24:31', NULL, '五折黑卡会员');

-- ----------------------------
-- Table structure for pay_details
-- ----------------------------
DROP TABLE IF EXISTS `pay_details`;
CREATE TABLE `pay_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值id',
  `classify` varchar(4) DEFAULT NULL COMMENT '分类（ 1app微信 2微信公众号 3微信小程序 4支付宝）',
  `order_id` varchar(32) DEFAULT NULL COMMENT '订单编号',
  `trade_no` varchar(32) DEFAULT NULL COMMENT '支付宝支付单号',
  `money` decimal(10,2) DEFAULT NULL COMMENT '充值金额',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `state` int(4) DEFAULT NULL COMMENT '0待支付 1支付成功 2失败',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `pay_time` varchar(64) DEFAULT NULL COMMENT '支付时间',
  `type` int(4) DEFAULT NULL COMMENT '支付类型 1 用户  2会员',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `refund_money` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`),
  KEY `classify` (`classify`)
) ENGINE=InnoDB AUTO_INCREMENT=3012 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of pay_details
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '套餐id',
  `tearoom_id` int(11) DEFAULT NULL COMMENT '茶室id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '套餐名称',
  `product_num` int(11) DEFAULT NULL COMMENT '套餐时长',
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '套餐价格',
  `product_type` int(11) DEFAULT NULL COMMENT '未用到',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `product_old_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  PRIMARY KEY (`product_id`) USING BTREE,
  KEY `tearoom_id` (`tearoom_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (1, 1, '3小时', 3, '20.00', NULL, '2023-10-10 10:10:10', '100.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (2, 1, '6小时', 6, '40.00', NULL, '2023-10-10 10:10:10', '300.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (4, 3, '夜上海三小时套餐', 3, '100.00', NULL, '2023-11-10 15:24:49', '150.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (5, 3, '夜上海四小时套餐', 4, '180.00', NULL, '2023-11-10 15:33:23', '200.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (6, 3, '夜上海六小时套餐', 6, '250.00', NULL, '2023-11-10 15:33:58', '300.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (7, 3, '大纽约3小时套餐', 3, '250.00', NULL, '2023-11-14 13:50:10', '300.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (8, 3, '大纽约4小时套餐', 4, '300.00', NULL, '2023-11-14 13:50:31', '400.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (9, 8, '套餐1', 4, '200.00', NULL, '2024-01-17 09:30:07', '300.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (10, 9, '套餐1', 4, '150.00', NULL, '2024-01-17 14:18:58', '200.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (11, 10, '豪华小包（2小时）', 2, '0.10', NULL, '2024-01-18 13:20:39', '26.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (12, 11, '暴富厅（2小时）', 2, '28.00', NULL, '2024-01-18 13:30:36', '40.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (13, 11, '秋香厅（2小时）', 2, '48.00', NULL, '2024-01-18 13:31:18', '68.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (14, 12, '粤来粤豪茶馆', 4, '188.00', NULL, '2024-03-06 13:59:51', '388.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (15, 12, '东方红', 4, '128.00', NULL, '2024-03-06 14:03:57', '288.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (17, 12, '上海馆', 4, '118.00', NULL, '2024-03-06 14:31:48', '288.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (18, 12, '上海馆', 4, '118.00', NULL, '2024-03-06 14:31:48', '288.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (19, 12, '海南馆', 4, '128.00', NULL, '2024-03-06 16:00:12', '168.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (20, 12, '广西桂林', 2, '68.00', NULL, '2024-03-06 16:10:29', '128.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (26, 12, '测试', 12, '12.00', NULL, '2024-03-12 15:37:59', '12.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (33, 12, '111', 11, '1.00', NULL, '2024-03-12 15:45:48', '1.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (35, 12, '西安茶棋牌室', 2, '66.00', NULL, '2024-03-13 19:34:13', '88.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (36, 13, '套餐1', 2, '20.00', NULL, '2024-03-14 14:16:53', '15.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (37, 13, '套餐2', 4, '40.00', NULL, '2024-03-14 14:17:15', '30.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (38, 13, '套餐3', 6, '60.00', NULL, '2024-03-14 14:17:33', '45.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (39, 14, '下午茶', 2, '300.00', NULL, '2024-03-28 14:56:13', '500.00');
INSERT INTO `product` (`product_id`, `tearoom_id`, `product_name`, `product_num`, `product_price`, `product_type`, `create_time`, `product_old_price`) VALUES (40, 14, '宴请客户', 5, '500.00', NULL, '2024-03-28 14:56:40', '1000.00');

-- ----------------------------
-- Table structure for risk
-- ----------------------------
DROP TABLE IF EXISTS `risk`;
CREATE TABLE `risk` (
  `risk_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '风险记录',
  `risk_type` varchar(255) DEFAULT NULL COMMENT '风险类型（1ip属地风险 2动态评价风险 3聊天风险）',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `content` varchar(255) DEFAULT NULL COMMENT '触犯原因',
  `create_time` varchar(64) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`risk_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of risk
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设备id',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `room_name` varchar(255) DEFAULT NULL COMMENT '设备名称',
  `room_no` varchar(64) DEFAULT NULL COMMENT '设备编号',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `room_type` int(11) DEFAULT NULL COMMENT '设备类型 1包间门 2茶馆大门 2其他',
  `is_open` int(11) DEFAULT NULL COMMENT '是否开启',
  `room_select` varchar(64) DEFAULT NULL,
  `device_id` varchar(64) DEFAULT NULL,
  `room_weight` decimal(10,2) DEFAULT NULL,
  `serve_code` varchar(64) DEFAULT NULL,
  `equipment_code` varchar(64) DEFAULT NULL,
  `music_select_start` varchar(64) DEFAULT NULL,
  `music_select_end` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`room_id`) USING BTREE,
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (9, 5, '101室大门', '', '2023-11-16 15:41:33', 2, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (10, 5, '101室主灯', '', '2023-11-16 15:41:44', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (12, 5, '101室主风扇', '', '2023-11-16 15:42:18', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (13, 5, '101室大门', '', '2023-11-16 15:46:16', 1, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (14, 6, '101室大门', '', '2023-11-16 15:41:33', 2, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (15, 6, '101室主灯', '', '2023-11-16 15:41:44', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (16, 6, '101室主风扇', '', '2023-11-16 15:42:18', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (17, 6, '101室大门', '', '2023-11-16 15:46:16', 1, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (18, 7, '101室大门', '', '2023-11-16 15:41:33', 2, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (19, 7, '101室主灯', '', '2023-11-16 15:41:44', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (20, 7, '101室主风扇', '', '2023-11-16 15:42:18', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (21, 7, '101室大门', '', '2023-11-16 15:46:16', 1, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (22, 8, '101室大门', '', '2023-11-16 15:41:33', 2, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (23, 8, '101室主灯', '', '2023-11-16 15:41:44', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (24, 8, '101室主风扇', '', '2023-11-16 15:42:18', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (25, 8, '101室大门', '', '2023-11-16 15:46:16', 1, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (26, 9, '101室大门', '', '2023-11-16 15:41:33', 2, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (27, 9, '101室主灯', '', '2023-11-16 15:41:44', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (28, 9, '101室主风扇', '', '2023-11-16 15:42:18', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (29, 9, '101室大门', '', '2023-11-16 15:46:16', 1, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (34, 12, '101室大门', '', '2024-01-17 13:58:43', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (35, 12, '101室大门', '', '2024-01-17 13:58:49', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (36, 12, '101室主灯', '', '2024-01-17 13:58:57', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (37, 12, '101室主风扇', '', '2024-01-17 13:59:04', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (38, 13, '101室大门', '', '2024-01-17 14:21:51', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (39, 13, '101室大门', '', '2024-01-17 14:21:59', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (40, 13, '101室主风扇', '', '2024-01-17 14:22:06', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (41, 13, '101室主灯', '', '2024-01-17 14:22:14', 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (42, 17, '24V-电灯', '', '2024-03-08 18:04:29', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (43, 17, '24V-门禁', '', '2024-03-08 18:09:05', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (44, 18, '24V-电灯', '', '2024-03-12 15:32:15', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (45, 20, '24V-电灯', '', '2024-03-12 15:44:08', 1, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (46, 20, '24V-风扇', '', '2024-03-12 15:44:16', 1, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (48, 17, '24V-门禁', '', '2024-03-13 15:27:36', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (49, 23, '24V-门禁', '', '2024-03-14 14:42:37', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (50, 23, '24V-门禁', '', '2024-03-14 14:42:45', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (51, 23, '24V-电灯', '', '2024-03-14 14:42:56', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (53, 16, '空调', '', '2024-04-02 16:26:46', 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (54, 16, '电闸', '', '2024-04-17 14:26:37', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (55, 16, '门禁', '', '2024-04-27 11:31:28', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (57, 19, '空调1', '', '2024-07-05 15:05:12', 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (118, 51, '码到成功二号桌', '', '2025-04-01 14:01:05', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (120, 49, 'PS5靠街机区', '', '2025-04-01 14:03:52', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (122, 48, 'PS5靠吧台', '', '2025-04-01 14:07:43', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (123, 47, '棋牌机器人', '', '2025-04-01 14:08:05', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (124, 45, '码到成功1', '', '2025-04-01 14:08:29', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (130, 54, '24V-电灯', '', '2025-04-06 17:25:43', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (131, 53, '24V-电灯', '', '2025-04-06 17:26:38', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (134, 52, '24V-电灯', '', '2025-04-06 17:30:07', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (136, 44, '24V-电灯', '', '2025-04-06 17:32:56', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (139, 42, '24V-电灯', '', '2025-04-06 17:35:08', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (140, 43, '24V-电灯', '', '2025-04-06 17:35:20', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (146, 60, 'VR设备（代用赛车）', '', '2025-04-18 15:15:21', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (148, 62, '24V-电灯', '', '2025-04-30 20:45:02', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (155, 63, '24V-电灯', '', '2025-05-08 12:18:02', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (156, 64, '24V-电灯', '', '2025-05-08 12:18:19', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (157, 65, '24V-电灯', '', '2025-05-08 12:18:33', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (158, 75, '', '', '2025-06-24 16:48:48', 3, NULL, '1', '', NULL, 'SZB000000', 'EDA000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (159, 75, '', '', '2025-06-24 16:49:54', 5, NULL, '', '', '50.00', 'SZB000000', 'EJM000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (160, 75, '', '', '2025-06-24 16:52:00', 4, NULL, '', '', NULL, 'SZB000000', 'EMY000000', '1', '2');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (161, 77, '', '', '2025-06-25 14:55:14', 3, NULL, '3', '', NULL, 'SZB000000', 'EDA000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (162, 77, '', '', '2025-06-25 14:56:14', 4, NULL, '', '', NULL, 'SZB000000', 'EMY000000', '1', '2');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (163, 79, '', '', '2025-06-25 14:56:42', 3, NULL, '5', '', NULL, 'SZB000000', 'EDA000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (164, 79, '', '', '2025-06-25 14:56:54', 4, NULL, '', '', NULL, 'SZB000000', 'EMY000000', '1', '2');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (165, 76, '', '', '2025-06-27 18:00:13', 3, NULL, '2', '', NULL, 'SZB000000', 'EDA000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (166, 76, '', '', '2025-06-27 18:00:24', 4, NULL, '', '', NULL, 'SZB000000', 'EMY000000', '1', '2');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (167, 76, '', '', '2025-06-27 18:00:41', 5, NULL, '', '', '50.00', 'SZB000000', 'EJM000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (168, 77, '', '', '2025-06-27 18:01:45', 5, NULL, '', '', '50.00', 'SZB000000', 'EJM000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (169, 78, '', '', '2025-06-27 18:02:05', 3, NULL, '4', '', NULL, 'SZB000000', 'EDA000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (170, 78, '', '', '2025-06-27 18:02:23', 4, NULL, '', '', NULL, 'SZB000000', 'EMY000000', '1', '2');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (171, 78, '', '', '2025-06-27 18:02:36', 5, NULL, '', '', '50.00', 'SZB000000', 'EJM000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (172, 79, '', '', '2025-06-27 18:02:56', 5, NULL, '', '', '50.00', 'SZB000000', 'EJM000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (173, 80, '', '', '2025-06-27 18:03:13', 3, NULL, '6', '', NULL, 'SZB000000', 'EDA000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (174, 80, '', '', '2025-06-27 18:03:36', 4, NULL, '', '', NULL, 'SZB000000', 'EMY000000', '1', '2');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (175, 80, '', '', '2025-06-27 18:03:50', 5, NULL, '', '', '50.00', 'SZB000000', 'EJM000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (176, 82, '24V-电灯', '', '2025-07-09 09:27:35', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (177, 81, '', '', '2025-07-16 10:13:42', 5, NULL, '', '', NULL, 'SZB000000', 'EJM000000', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (185, 90, '24V-电灯', '', '2025-07-20 10:37:19', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (186, 91, '24V-电灯', '', '2025-07-20 10:37:19', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (187, 93, '24V-电灯', '', '2025-07-20 10:37:19', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (191, 83, '24V-电灯', '', '2025-07-20 10:43:21', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (198, 89, '24V-电灯', '', '2025-07-20 10:46:23', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (200, 88, '24V-电灯', '', '2025-07-20 10:49:43', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (212, 103, '24V-电灯', '', '2025-08-01 17:53:22', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (213, 104, '24V-电灯', '', '2025-08-01 17:53:22', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (214, 105, '24V-电灯', '', '2025-08-01 17:53:22', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (215, 94, '24V-电灯', '', '2025-08-01 17:53:48', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (216, 95, '24V-电灯', '', '2025-08-01 17:54:59', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (217, 96, '24V-电灯', '', '2025-08-01 17:55:13', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (218, 97, '24V-电灯', '', '2025-08-01 17:55:28', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (219, 98, '24V-电灯', '', '2025-08-01 17:55:41', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (220, 99, '24V-电灯', '', '2025-08-01 17:55:57', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (221, 100, '24V-电灯', '', '2025-08-01 17:56:11', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (222, 101, '24V-电灯', '', '2025-08-01 17:56:24', 1, NULL, '', '', NULL, '', '', '', '');
INSERT INTO `room` (`room_id`, `goods_id`, `room_name`, `room_no`, `create_time`, `room_type`, `is_open`, `room_select`, `device_id`, `room_weight`, `serve_code`, `equipment_code`, `music_select_start`, `music_select_end`) VALUES (223, 102, '24V-电灯', '', '2025-08-01 17:56:43', 1, NULL, '', '', NULL, '', '', '', '');

-- ----------------------------
-- Table structure for room_time
-- ----------------------------
DROP TABLE IF EXISTS `room_time`;
CREATE TABLE `room_time` (
  `room_time_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '房间时间id',
  `goods_id` int(11) DEFAULT NULL COMMENT '房间id',
  `room_date` varchar(64) DEFAULT NULL COMMENT '占用日期',
  `room_time` varchar(64) DEFAULT NULL COMMENT '占用小时',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `orders_no` varchar(64) DEFAULT NULL COMMENT '订单编号',
  PRIMARY KEY (`room_time_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1020 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of room_time
-- ----------------------------
INSERT INTO `room_time` (`room_time_id`, `goods_id`, `room_date`, `room_time`, `create_time`, `orders_no`) VALUES (458, 5, '2023-11-14', '19:00', '2023-11-14 18:36:06', '202311141836061054');
INSERT INTO `room_time` (`room_time_id`, `goods_id`, `room_date`, `room_time`, `create_time`, `orders_no`) VALUES (459, 5, '2023-11-14', '20:00', '2023-11-14 18:36:06', '202311141836061054');
INSERT INTO `room_time` (`room_time_id`, `goods_id`, `room_date`, `room_time`, `create_time`, `orders_no`) VALUES (460, 5, '2023-11-14', '21:00', '2023-11-14 18:36:06', '202311141836061054');

-- ----------------------------
-- Table structure for self_coupon
-- ----------------------------
DROP TABLE IF EXISTS `self_coupon`;
CREATE TABLE `self_coupon` (
  `coupon_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `coupon_name` varchar(255) DEFAULT NULL COMMENT '优惠券名称',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `goods_ids` varchar(255) DEFAULT NULL COMMENT '商品ids(多个)',
  `goods_images` varchar(255) DEFAULT NULL COMMENT '商品图片(多个)',
  `less_money` varchar(255) DEFAULT NULL COMMENT '优惠券面值',
  `min_money` varchar(255) DEFAULT NULL COMMENT '优惠券最低消费',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT NULL COMMENT '状态（1开启 2关闭）',
  `type` int(11) DEFAULT NULL COMMENT '充值金额',
  `valid_day` int(11) DEFAULT NULL COMMENT '优惠券有效期限(天)',
  `hour` int(11) DEFAULT NULL COMMENT '时长',
  `tearoom_id` varchar(64) DEFAULT NULL COMMENT '棋牌室id',
  `integral` int(11) DEFAULT NULL COMMENT '积分',
  `old_price` varchar(64) DEFAULT NULL COMMENT '原价',
  `coupon_type` int(11) DEFAULT NULL COMMENT '1优惠券 2实物',
  `min_hour` int(11) DEFAULT NULL COMMENT '最低使用时长',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='优惠券规则';

-- ----------------------------
-- Records of self_coupon
-- ----------------------------
INSERT INTO `self_coupon` (`coupon_id`, `coupon_name`, `create_time`, `goods_ids`, `goods_images`, `less_money`, `min_money`, `sort`, `status`, `type`, `valid_day`, `hour`, `tearoom_id`, `integral`, `old_price`, `coupon_type`, `min_hour`) VALUES (142, '满10减9', '2026-03-11 13:51:38', NULL, 'https://tu.mianyazuju.com/20260311/626c3258f25c4bf2b8cb97b8e2991de0.jpg', '9', '10', 0, 1, 2, 30, 4, NULL, 100, NULL, 1, NULL);
INSERT INTO `self_coupon` (`coupon_id`, `coupon_name`, `create_time`, `goods_ids`, `goods_images`, `less_money`, `min_money`, `sort`, `status`, `type`, `valid_day`, `hour`, `tearoom_id`, `integral`, `old_price`, `coupon_type`, `min_hour`) VALUES (143, '新人劵', '2026-05-27 17:34:08', NULL, 'https://tu.mianyazuju.com/20260527/f877407aaa19409092f99c076c861277.png', '10', '20', 2, 1, 3, 30, 4, NULL, 0, NULL, 1, NULL);

-- ----------------------------
-- Table structure for self_coupon_issue
-- ----------------------------
DROP TABLE IF EXISTS `self_coupon_issue`;
CREATE TABLE `self_coupon_issue` (
  `coupon_issue_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券发布id',
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '优惠券id',
  `coupon_name` varchar(255) DEFAULT NULL COMMENT '优惠券名称',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `goods_ids` varchar(255) DEFAULT NULL COMMENT '商品ids(多个)',
  `is_limit` int(11) DEFAULT NULL COMMENT '是否限量（1限量 2不限量）',
  `issue_number` int(11) DEFAULT NULL COMMENT '发布数量',
  `remain_number` int(11) DEFAULT NULL COMMENT '剩余数量',
  `status` int(11) DEFAULT NULL COMMENT '状态（1开启 2关闭）',
  `type` int(11) DEFAULT NULL COMMENT '优惠券类型（1团购 2积分兑换 3新人优惠券）',
  `hour` int(11) DEFAULT NULL COMMENT '时长',
  `tearoom_id` varchar(64) DEFAULT NULL COMMENT '茶室id',
  `team_rule` varchar(2000) DEFAULT NULL COMMENT '使用规则',
  `buy_rule` varchar(2000) DEFAULT NULL COMMENT '购买须知',
  `integral` int(11) DEFAULT NULL COMMENT '兑换积分',
  `valid_day` int(11) DEFAULT NULL COMMENT '有限天数',
  `coupon_type` int(11) DEFAULT NULL COMMENT '1优惠券 2实物',
  `min_hour` int(11) DEFAULT NULL COMMENT '最低使用时长',
  `is_limit_time` int(11) DEFAULT NULL,
  `weeks` varchar(255) DEFAULT NULL,
  `times` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`coupon_issue_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='优惠券发布';

-- ----------------------------
-- Records of self_coupon_issue
-- ----------------------------
INSERT INTO `self_coupon_issue` (`coupon_issue_id`, `coupon_id`, `coupon_name`, `create_time`, `goods_ids`, `is_limit`, `issue_number`, `remain_number`, `status`, `type`, `hour`, `tearoom_id`, `team_rule`, `buy_rule`, `integral`, `valid_day`, `coupon_type`, `min_hour`, `is_limit_time`, `weeks`, `times`) VALUES (242, 142, '满10减9', '2026-03-11 13:51:52', '89,90,91', 1, 1000, 1000, 1, 2, 4, '24', '111', '222', 100, 30, 1, NULL, 2, '', '');
INSERT INTO `self_coupon_issue` (`coupon_issue_id`, `coupon_id`, `coupon_name`, `create_time`, `goods_ids`, `is_limit`, `issue_number`, `remain_number`, `status`, `type`, `hour`, `tearoom_id`, `team_rule`, `buy_rule`, `integral`, `valid_day`, `coupon_type`, `min_hour`, `is_limit_time`, `weeks`, `times`) VALUES (243, 143, '新人劵', '2026-05-27 17:35:30', '98,99,100', 1, 100, 96, 1, 3, 4, '26', '电风扇舒舒服服都是', '烦都烦死电风扇', 0, 30, 1, NULL, 2, '', '');
INSERT INTO `self_coupon_issue` (`coupon_issue_id`, `coupon_id`, `coupon_name`, `create_time`, `goods_ids`, `is_limit`, `issue_number`, `remain_number`, `status`, `type`, `hour`, `tearoom_id`, `team_rule`, `buy_rule`, `integral`, `valid_day`, `coupon_type`, `min_hour`, `is_limit_time`, `weeks`, `times`) VALUES (244, 143, '新人劵', '2026-05-28 10:35:37', '91,90,89', 1, 2, 1, 1, 3, 4, '24', '11111111', '222222222222222222', 0, 30, 1, NULL, 2, '', '');
INSERT INTO `self_coupon_issue` (`coupon_issue_id`, `coupon_id`, `coupon_name`, `create_time`, `goods_ids`, `is_limit`, `issue_number`, `remain_number`, `status`, `type`, `hour`, `tearoom_id`, `team_rule`, `buy_rule`, `integral`, `valid_day`, `coupon_type`, `min_hour`, `is_limit_time`, `weeks`, `times`) VALUES (245, 142, '满10减9', '2026-05-29 13:54:24', '98,99,100', 1, 100, 97, 1, 2, 4, '26', 'eee', 'eee', 100, 30, 1, NULL, 2, '', '');

-- ----------------------------
-- Table structure for self_coupon_user
-- ----------------------------
DROP TABLE IF EXISTS `self_coupon_user`;
CREATE TABLE `self_coupon_user` (
  `coupon_user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券用户id',
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '优惠券id',
  `coupon_issue_id` bigint(20) DEFAULT NULL COMMENT '优惠券发布id',
  `coupon_name` varchar(255) DEFAULT NULL COMMENT '优惠券名称',
  `create_time` varchar(255) DEFAULT NULL COMMENT '领取时间',
  `failure_time` varchar(255) DEFAULT NULL COMMENT '失效时间',
  `goods_ids` varchar(255) DEFAULT NULL COMMENT '商品id',
  `less_money` decimal(10,2) DEFAULT NULL COMMENT '优惠券面值',
  `min_money` varchar(255) DEFAULT NULL COMMENT '优惠券最低消费',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户昵称',
  `status` int(11) DEFAULT NULL COMMENT '状态（1未使用 2已使用 3已过期）',
  `type` int(11) DEFAULT NULL COMMENT '优惠券类型（1通用券 2商品券）',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `hour` int(11) DEFAULT NULL COMMENT '时长',
  `tearoom_id` varchar(64) DEFAULT NULL COMMENT '茶室id',
  `team_rule` varchar(2000) DEFAULT NULL COMMENT '使用规则',
  `buy_rule` varchar(2000) DEFAULT NULL COMMENT '购买须知',
  `pay_way` int(11) DEFAULT NULL COMMENT '1app微信 2微信公众号 3微信小程序 4支付宝app 5支付宝h5 6零钱',
  `orders_id` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `pay_money` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `platform` varchar(255) DEFAULT NULL COMMENT '来源1新用户领取 2邀请新用户 3邀请用户下单 4购买 5兑换 6商家赠送',
  `platform_id` int(11) DEFAULT NULL COMMENT '来源id',
  `cdk_code` varchar(255) DEFAULT NULL COMMENT '兑换码',
  `shop_user_id` int(11) DEFAULT NULL COMMENT '核销用户id',
  `shop_tearoom_id` int(11) DEFAULT NULL COMMENT '商家id',
  `phone` varchar(255) DEFAULT NULL,
  `complete_time` varchar(64) DEFAULT NULL,
  `new_user_id` int(11) DEFAULT NULL,
  `min_hour` int(11) DEFAULT NULL COMMENT '最低使用时长',
  `platform_type` int(11) DEFAULT NULL COMMENT '订单优惠券平台 1美团2抖音',
  `is_limit_time` int(11) DEFAULT NULL,
  `weeks` varchar(255) DEFAULT NULL,
  `times` varchar(255) DEFAULT NULL,
  `error_msg` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`coupon_user_id`) USING BTREE,
  KEY `tearoom_id` (`tearoom_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28049 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='优惠券领取记录';

-- ----------------------------
-- Records of self_coupon_user
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统验证码';

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `param_key` (`param_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统配置信息表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` (`id`, `param_key`, `param_value`, `status`, `remark`) VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{"aliyunAccessKeyId":"","aliyunAccessKeySecret":"","aliyunBucketName":"","aliyunDomain":"","aliyunEndPoint":"","aliyunPrefix":"","qcloudBucketName":"","qcloudDomain":"","qcloudPrefix":"","qcloudSecretId":"","qcloudSecretKey":"","qiniuAccessKey":"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ","qiniuBucketName":"ios-app","qiniuDomain":"http://7xqbwh.dl1.z0.glb.clouddn.com","qiniuPrefix":"upload","qiniuSecretKey":"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV","type":1}', 0, '云存储配置信息');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '字典名称',
  `type` varchar(100) NOT NULL COMMENT '字典类型',
  `code` varchar(100) NOT NULL COMMENT '字典码',
  `value` varchar(1000) NOT NULL COMMENT '字典值',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `type` (`type`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='数据字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1057 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=510 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (2, 126, '管理员列表', 'sys/user', NULL, 1, 'admin', 10);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (3, 126, '角色管理', 'sys/role', NULL, 1, 'role', 11);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (4, 126, '菜单管理', 'sys/menu', NULL, 1, 'menu', 12);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (32, 0, '用户中心', 'userList', '', 1, 'yonghul', 1);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (33, 0, '数据中心', 'home', '', 1, 'shuju', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (34, 0, '财务中心', 'financeList', '', 1, 'caiwu', 2);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (35, 34, '查看', '', 'financeList:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (36, 34, '转账', '', 'financeList:transfer', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (37, 34, '退款', '', 'financeList:refund', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (38, 34, '退款', '', 'financeList:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (39, 34, '修改', '', 'financeList:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (40, 34, '删除', '', 'financeList:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (41, 0, '消息中心', 'message', '', 1, 'xiaoxi', 6);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (42, 41, '查看', '', 'message:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (43, 41, '消息推送', '', 'message:push', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (50, 0, '首页装修', 'bannerList', '', 1, 'shangpin', 6);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (51, 50, '查看', '', 'bannerList:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (52, 50, '添加', '', 'bannerList:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (53, 50, '修改', '', 'bannerList:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (54, 50, '删除', '', 'bannerList:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (57, 0, '系统配置', 'allocationList', '', 1, 'menu', 19);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (58, 57, '查看', '', 'allocationList:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (59, 57, '修改', '', 'allocationList:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (60, 32, '查看', '', 'userList:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (61, 32, '删除', '', 'userList:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (62, 0, '订单中心', 'orderCenter', '', 1, 'log', 3);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (63, 62, '查看', '', '', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (64, 62, '删除', '', 'orderCenter:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (69, 41, '添加', '', 'message:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (70, 41, '修改', '', 'message:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (71, 41, '删除', '', 'message:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (90, 32, '修改积分', '', 'userList:updatejf', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (91, 32, '修改用户状态', '', 'userList:updateStatus', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (94, 126, '升级配置', 'app', '', 1, 'sql', 9);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (95, 94, '查看', '', 'app:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (96, 94, '添加', '', 'app:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (97, 94, '修改', '', 'app:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (98, 94, '删除', '', 'app:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (105, 32, '修改积分', '', 'userList:updatebl', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (124, 62, '续单', '', 'orderCenter:accomplish', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (126, 0, '系统管理', '', '', 0, 'shezhi', 20);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (144, 126, '帮助中心', 'materialsList', '', 1, 'renwu', 6);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (145, 144, '查看', '', 'materialsList:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (146, 144, '添加', '', 'materialsList:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (147, 144, '修改', '', 'materialsList:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (148, 144, '删除', '', 'materialsList:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (171, 32, '修改信息', '', 'userList:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (177, 0, '台球馆中心', '', '', 0, 'pingtai', 4);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (178, 0, '充电桩列表', 'merchantList', '', 1, 'order', 5);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (179, 178, '查看', '', 'merchantList:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (180, 178, '添加', '', 'merchantList:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (181, 178, '修改', '', 'merchantList:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (182, 178, '删除', '', 'merchantList:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (183, 177, '入驻申请', 'merchantEnter', '', 1, 'editor', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (184, 177, '设备列表', 'devices', '', 1, 'tubiao', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (185, 184, '查看', '', 'devices:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (186, 184, '添加', '', 'devices:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (187, 184, '修改', '', 'devices:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (188, 184, '删除', '', 'devices:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (189, 0, '优惠券管理', '', '', 0, 'shuju', 7);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (190, 189, '优惠券制作', 'coupon', '', 1, 'caiwu', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (191, 190, '查看', '', 'coupon:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (192, 190, '添加', '', 'coupon:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (193, 190, '修改', '', 'coupon:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (194, 190, '删除', '', 'coupon:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (195, 190, '发布', '', 'coupon:issue', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (196, 189, '已发布优惠券', 'couponissue', '', 1, 'renwu', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (197, 196, '查看', '', 'couponissue:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (198, 189, '优惠券领取记录', 'couponuser', '', 1, 'bianji', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (199, 198, '查看', '', 'couponuser:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (203, 189, '验券列表', 'couponTg', '', 1, 'editor', 5);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (204, 203, '查看', '', 'couponTg:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (205, 203, '验券', '', 'couponTg:yanquan', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (206, 0, '注销信息', 'messageZx', '', 1, 'leibie', 6);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (207, 206, '查看', '', 'messageZx:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (208, 206, '注销', '', 'messageZx:shenhe', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (209, 62, '退款', '', 'orderAdmin:refund', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (210, 196, '删除', '', 'couponissue:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (211, 203, '添加', '', 'couponTg:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (212, 203, '修改', '', 'couponTg:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (213, 126, '城市管理', 'citys', '', 1, 'shuju', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (214, 213, '查看', '', 'citys:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (215, 213, '添加', '', 'citys:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (216, 213, '修改', '', 'citys:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (217, 213, '删除', '', 'citys:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (228, 0, '充值管理', '', '', 0, 'caiwu', 10);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (229, 228, '充值配置', 'IntegralGoods', '', 1, 'renwu', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (230, 229, '查看', '', 'IntegralGoods:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (231, 229, '添加', '', 'IntegralGoods:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (232, 229, '修改', '', 'IntegralGoods:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (233, 229, '删除', '', 'IntegralGoods:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (234, 228, '充值记录', 'exchangeList', '', 1, 'order', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (235, 234, '查看', '', 'exchangeList:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (236, 189, '发卡管理', 'couponFk', '', 1, 'shuju', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (237, 236, '查看', '', 'couponFk:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (238, 236, '添加', '', 'couponFk:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (239, 236, '修改', '', 'couponFk:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (240, 236, '删除', '', 'couponFk:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (241, 178, '结算', '', 'userList:updatejs', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (258, 198, '核销', '', 'couponuser:hexiao', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (259, 0, '积分明细', 'points', '', 1, 'ordercenter', 6);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (260, 259, '查看', '', 'points:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (264, 0, '会员配置', 'memberDetails', '', 1, 'caiwu', 8);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (275, 264, '查看', '', 'memberDetails:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (276, 264, '添加', '', 'memberDetails:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (277, 264, '修改', '', 'memberDetails:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (278, 264, '删除', '', 'memberDetails:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (282, 0, '认证管理', 'autonym', '', 1, 'order', 3);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (283, 282, '查看', '', 'autonym:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (284, 282, '修改', '', 'autonym:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (285, 282, '删除', '', 'autonym:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (286, 282, '审核', '', 'autonym:shenhe', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (287, 189, '优惠券活动', 'timedGiftCoupon', '', 1, 'editor', 6);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (288, 287, '查看', '', 'timedGiftCoupon:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (289, 287, '添加', '', 'timedGiftCoupon:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (290, 287, '修改', '', 'timedGiftCoupon:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (291, 287, '删除', '', 'timedGiftCoupon:delete', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (323, 0, '用户管理', '', '', 0, 'yonghul', 1);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (389, 198, '作废', '', 'couponuser:zuofei', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (390, 62, '转单', '', 'orderAdmin:zhuandan', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (391, 62, '换台', '', 'orderAdmin:huantai', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (392, 32, '添加会员值', '', 'userList:addHyz', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (393, 32, '删除会员值', '', 'userList:deleteHyz', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (394, 62, '修改', '', 'orderAdmin:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (505, 0, '会员特权', 'vipPrivilege', '', 1, 'shuju', 9);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (506, 505, '查看', '', 'vipPrivilege:list', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (507, 505, '添加', '', 'vipPrivilege:add', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (508, 505, '修改', '', 'vipPrivilege:update', 2, '', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (509, 505, '删除', '', 'vipPrivilege:delete', 2, '', 0);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文件上传';

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_user_id`, `create_time`) VALUES (1, '超级管理员', 'super', 2, '2021-06-04 17:38:28');
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_user_id`, `create_time`) VALUES (4, '普通用户', '普通用户', 14, '2021-09-10 15:33:47');
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_user_id`, `create_time`) VALUES (5, '测试1', '测试1', 6, '2025-09-12 17:57:30');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18432 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6436, 4, 33);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6437, 4, 32);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6438, 4, 60);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6439, 4, 61);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6440, 4, 90);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6441, 4, 91);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6442, 4, 105);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6443, 4, 171);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6444, 4, 35);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6445, 4, 36);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6446, 4, 37);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6447, 4, 38);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6448, 4, 63);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6449, 4, 124);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6450, 4, 42);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6451, 4, 51);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6455, 4, 179);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6456, 4, 185);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6457, 4, 186);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6458, 4, 145);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6459, 4, 146);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6460, 4, 147);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6461, 4, 214);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6462, 4, 215);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6463, 4, 58);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6464, 4, 95);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6465, 4, 15);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6466, 4, 19);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6467, 4, 23);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6468, 4, -666666);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6469, 4, 34);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6470, 4, 62);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6471, 4, 41);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6472, 4, 50);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6473, 4, 177);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6474, 4, 178);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6475, 4, 184);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6476, 4, 144);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6477, 4, 213);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6478, 4, 57);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6479, 4, 126);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6480, 4, 94);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6481, 4, 2);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6482, 4, 3);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (6483, 4, 4);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (17550, 5, 33);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (17552, 5, -666666);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18323, 1, 33);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18324, 1, 32);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18325, 1, 60);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18326, 1, 61);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18327, 1, 90);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18328, 1, 91);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18329, 1, 105);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18330, 1, 392);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18331, 1, 393);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18332, 1, 171);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18333, 1, 34);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18334, 1, 35);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18335, 1, 36);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18336, 1, 37);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18337, 1, 38);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18338, 1, 39);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18339, 1, 40);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18340, 1, 62);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18341, 1, 63);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18342, 1, 64);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18343, 1, 124);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18344, 1, 390);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18345, 1, 391);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18346, 1, 394);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18347, 1, 209);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18348, 1, 178);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18349, 1, 179);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18350, 1, 180);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18351, 1, 181);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18352, 1, 182);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18353, 1, 241);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18354, 1, 259);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18355, 1, 260);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18356, 1, 41);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18357, 1, 42);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18358, 1, 43);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18359, 1, 69);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18360, 1, 70);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18361, 1, 71);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18362, 1, 50);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18363, 1, 51);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18364, 1, 52);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18365, 1, 53);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18366, 1, 54);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18367, 1, 206);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18368, 1, 207);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18369, 1, 208);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18370, 1, 190);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18371, 1, 191);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18372, 1, 192);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18373, 1, 193);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18374, 1, 194);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18375, 1, 195);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18376, 1, 196);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18377, 1, 197);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18378, 1, 210);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18379, 1, 198);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18380, 1, 258);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18381, 1, 389);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18382, 1, 199);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18383, 1, 287);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18384, 1, 288);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18385, 1, 289);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18386, 1, 290);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18387, 1, 291);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18388, 1, 264);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18389, 1, 275);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18390, 1, 276);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18391, 1, 277);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18392, 1, 278);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18393, 1, 228);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18394, 1, 229);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18395, 1, 230);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18396, 1, 231);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18397, 1, 232);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18398, 1, 233);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18399, 1, 234);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18400, 1, 235);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18401, 1, 505);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18402, 1, 506);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18403, 1, 507);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18404, 1, 508);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18405, 1, 509);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18406, 1, 57);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18407, 1, 58);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18408, 1, 59);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18409, 1, 144);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18410, 1, 145);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18411, 1, 146);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18412, 1, 147);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18413, 1, 148);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18414, 1, 2);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18415, 1, 15);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18416, 1, 16);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18417, 1, 17);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18418, 1, 18);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18419, 1, 3);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18420, 1, 19);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18421, 1, 20);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18422, 1, 21);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18423, 1, 22);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18424, 1, 4);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18425, 1, 23);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18426, 1, 24);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18427, 1, 25);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18428, 1, 26);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18429, 1, -666666);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18430, 1, 189);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (18431, 1, 126);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `inviter_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `create_time`, `inviter_code`) VALUES (1, 'admin', 'b1672b04628a52e4a3d7a472c691564c7b9d92e3ee38abd96f78bab92446b9a2', 'gOQpg687tI4FgraTuw2v', 'admin@example.com', '13800000000', 1, 1, '2016-11-11 11:11:11', NULL);


-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (40, 1, 1);


-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户Token';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(500) DEFAULT NULL COMMENT '用户名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `sex` int(11) DEFAULT NULL COMMENT '性别 1男 2女',
  `age` int(4) DEFAULT NULL COMMENT '年龄',
  `open_id` varchar(500) DEFAULT NULL COMMENT '微信小程序openId',
  `wx_open_id` varchar(500) DEFAULT NULL COMMENT '微信App  openId',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  `apple_id` varchar(64) DEFAULT NULL COMMENT '苹果id',
  `sys_phone` int(11) DEFAULT NULL COMMENT '手机类型 1安卓 2ios',
  `status` int(11) DEFAULT NULL COMMENT '状态 1正常 2禁用',
  `platform` varchar(255) DEFAULT NULL COMMENT '来源 APP 小程序  公众号',
  `jifen` int(11) DEFAULT NULL COMMENT '积分',
  `invitation_code` varchar(255) DEFAULT NULL COMMENT '邀请码',
  `inviter_code` varchar(255) DEFAULT NULL COMMENT '邀请人邀请码',
  `clientid` varchar(255) DEFAULT NULL COMMENT 'app推送id',
  `zhi_fu_bao_name` varchar(255) DEFAULT NULL COMMENT '支付宝名称',
  `zhi_fu_bao` varchar(255) DEFAULT NULL COMMENT '支付宝账号',
  `is_authentication` int(11) DEFAULT NULL COMMENT '是否认证',
  `is_send_msg` int(11) DEFAULT NULL COMMENT '是否推送消息',
  `wx_img` varchar(600) DEFAULT NULL COMMENT '微信提现图片',
  `msg` text COMMENT '最近一条信息内容',
  `rate` decimal(10,2) DEFAULT NULL COMMENT '佣金比例',
  `zhi_rate` decimal(10,2) DEFAULT NULL COMMENT '直属佣金',
  `fei_rate` decimal(10,2) DEFAULT NULL COMMENT '非直属佣金',
  `consortia_id` int(11) DEFAULT NULL COMMENT '商家id 未用到',
  `consortia_orders_rate` decimal(10,2) DEFAULT NULL COMMENT '订单抽成 未用到',
  `consortia_gift_rate` decimal(10,2) DEFAULT NULL COMMENT '礼物抽成 未用到',
  `user_ip` varchar(255) DEFAULT NULL COMMENT '用户ip',
  `ip_address` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  `risk` int(11) DEFAULT NULL COMMENT '是否风险用户',
  `recently_data` varchar(255) DEFAULT NULL COMMENT '最近一次登录时间',
  `on_line` int(11) DEFAULT NULL COMMENT '是否在线',
  `is_new_coupon` int(11) DEFAULT NULL COMMENT '是否领取新人优惠券',
  `tearoom_id` int(11) DEFAULT NULL COMMENT '茶室id',
  `is_shop` int(11) DEFAULT NULL COMMENT '是否是商家 1是',
  `is_rate` int(11) DEFAULT NULL COMMENT '是否是推送用户 1是',
  `create_count` int(11) DEFAULT NULL COMMENT '发布次数',
  `vip_index` int(11) DEFAULT NULL COMMENT '会员等级',
  `is_sys_vip` int(11) DEFAULT NULL COMMENT '是否是系统设置会员等级 1是',
  `birthday` varchar(255) DEFAULT NULL,
  `user_background_image` varchar(255) DEFAULT NULL COMMENT '背景图',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `content` varchar(255) DEFAULT NULL COMMENT '自我介绍',
  `photo` varchar(2000) DEFAULT NULL COMMENT '相册',
  `credit_score` int(11) DEFAULT NULL COMMENT '信用分',
  `vip_no` varchar(64) DEFAULT NULL,
  `old_vip_index` int(11) DEFAULT NULL,
  `user_photo` varchar(255) DEFAULT NULL,
  `artificer_open_id` varchar(64) DEFAULT NULL,
  `shop_open_id` varchar(64) DEFAULT NULL,
  `user_goods_id` int(11) DEFAULT NULL,
  `is_open_goods` int(11) DEFAULT NULL,
  `cash_deposit_time` varchar(64) DEFAULT NULL,
  `shop_rate` decimal(10,2) DEFAULT NULL,
  `user_rate` decimal(10,2) DEFAULT NULL,
  `is_coupon` int(11) DEFAULT NULL,
  `is_cash_deposit` int(11) DEFAULT NULL,
  `is_agency` int(11) DEFAULT NULL,
  `is_artificer_authentication` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY `invitation_code` (`invitation_code`),
  KEY `inviter_code` (`inviter_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3800 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户';

-- ----------------------------
-- Records of tb_user
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for tearoom
-- ----------------------------
DROP TABLE IF EXISTS `tearoom`;
CREATE TABLE `tearoom` (
  `tearoom_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '茶室id',
  `tearoom_name` varchar(255) DEFAULT NULL COMMENT '茶室名称',
  `tearoom_cover_image` varchar(2000) DEFAULT NULL COMMENT '茶室封面图',
  `tearoom_title_image` varchar(2000) DEFAULT NULL COMMENT '茶室轮播图',
  `tearoom_label` varchar(64) DEFAULT NULL COMMENT '茶室标签',
  `tearoom_province` varchar(255) DEFAULT NULL COMMENT '省',
  `tearoom_city` varchar(255) DEFAULT NULL COMMENT '市',
  `tearoom_district` varchar(255) DEFAULT NULL COMMENT '区',
  `tearoom_address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `tearoom_longitude` varchar(64) DEFAULT NULL COMMENT '经度',
  `tearoom_latitude` varchar(64) DEFAULT NULL COMMENT '纬度',
  `start_time` varchar(64) DEFAULT NULL COMMENT '营业时间 开始时间',
  `end_time` varchar(64) DEFAULT NULL COMMENT '营业时间 结束时间',
  `score` decimal(10,2) DEFAULT NULL COMMENT '评分',
  `sales` int(11) DEFAULT NULL COMMENT '销量',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `status` int(11) DEFAULT NULL COMMENT '状态 1开启 2关闭',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `inner_code` varchar(255) DEFAULT NULL COMMENT '设备编号',
  `tearoom_phone` varchar(64) DEFAULT NULL COMMENT '茶室联系电话',
  `rate` decimal(10,2) DEFAULT NULL COMMENT '抽成',
  `room_name` varchar(255) DEFAULT NULL,
  `room_no` varchar(64) DEFAULT NULL,
  `key_code` varchar(255) DEFAULT NULL COMMENT '绑定设备组',
  `subscription` decimal(10,2) DEFAULT NULL COMMENT '预约费',
  `cash_pledge` decimal(10,2) DEFAULT NULL COMMENT '押金',
  `yy_token` varchar(64) DEFAULT NULL,
  `yy_id` varchar(64) DEFAULT NULL,
  `wifi_no` varchar(255) DEFAULT NULL,
  `wifi_pwd` varchar(255) DEFAULT NULL,
  `money_scope` int(11) DEFAULT NULL COMMENT '权限 1 有加减 2有加 3有减 4没有',
  `employee_user_ids` varchar(64) DEFAULT NULL COMMENT '员工',
  `is_reserve` int(11) DEFAULT NULL COMMENT '是否开启预定 1是 2否',
  `shop_ids` varchar(255) DEFAULT NULL,
  `broadcast_content` varchar(200) DEFAULT NULL,
  `camera_id` varchar(64) DEFAULT NULL,
  `wx_code` varchar(255) DEFAULT NULL,
  `qun_img` varchar(255) DEFAULT NULL,
  `is_open` int(11) DEFAULT NULL,
  `shop_type` int(11) DEFAULT NULL,
  `is_vip_privilege` int(11) DEFAULT NULL,
  `shop_poi_id` varchar(64) DEFAULT NULL,
  `is_welfare` int(11) DEFAULT NULL,
  `welfare_content` text,
  `welfare_img` varchar(2000) DEFAULT NULL,
  `is_face_recognition` int(11) DEFAULT NULL,
  `is_music` int(11) DEFAULT NULL,
  `ks_app_id` varchar(64) DEFAULT NULL,
  `ks_app_secret` varchar(64) DEFAULT NULL,
  `ks_refresh_token` varchar(2000) DEFAULT NULL,
  `ks_refresh_token_time` varchar(64) DEFAULT NULL,
  `mt_email` varchar(64) DEFAULT NULL,
  `mt_password` varchar(64) DEFAULT NULL,
  `mt_store_id` varchar(64) DEFAULT NULL,
  `dy_poi_id` varchar(64) DEFAULT NULL,
  `dy_app_id` varchar(64) DEFAULT NULL,
  `dy_secret` varchar(64) DEFAULT NULL,
  `lucky_sort` varchar(64) DEFAULT NULL,
  `lucky_coupon_ids` varchar(64) DEFAULT NULL,
  `lucky_content` varchar(255) DEFAULT NULL,
  `is_refund` int(11) DEFAULT NULL,
  `is_wx_pay` int(11) DEFAULT NULL,
  `is_qr_code` int(11) DEFAULT NULL,
  `tearoom_type` int(11) DEFAULT NULL,
  `tearoom_content` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`tearoom_id`) USING BTREE,
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tearoom
-- ----------------------------
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (20, '高新科技二路充电站', '', 'https://tu.mianyazuju.com/20260914/26f526b4f15647ec9d67811ea40b401a.png,https://tu.mianyazuju.com/20260914/532af9d424974382a64447e2c292db43.png,https://tu.mianyazuju.com/20260914/7c3fdec16ded47818391c467380fc43a.png,https://tu.mianyazuju.com/20260914/f683582f6dd34692b065e8b0ab995c0e.png,https://tu.mianyazuju.com/20260914/c8ea571f7f184540ad8ed2d708593a33.png', '对外开放,24小时营业,有雨棚', '陕西省', '西安市', '雁塔区', '科技二路68号朗臣大厦地下停车场', '108.898002', '34.238703', '00:00', '24:00', NULL, 4112, 981, 1, '2025-02-21 15:38:21', '', '', '0.80', '24V-柜子弹射器', '', '', NULL, NULL, '', '', '', '', 1, '1176,981,1560,3281,2591,3250', 1, '1,9,33,35,36,34', '欢迎陈总来码上台球考察，祝您玩的开心', '', '', '', 1, 2, 1, '1', 1, '', 'https://tu.mianyazuju.com/20250515/2c37f6f2503f47ff8c6664551c6e42a5.png', 1, 2, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 2, 1, 1, 1, '<p><strong>电瓶车充电收费标准</strong></p>\n<p>计费方式：按小时计费，充满自动断电。</p>\n<p>白天（08:00-22:00）：1.50元/小时，会员1.20元/小时</p>\n<p>夜间（22:00-08:00）：1.00元/小时，会员0.80元/小时</p>\n<p>温馨提示：请正确插枪，离开前确认结束订单；请勿占用他人车位。</p>');
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (21, '延平门地铁站充电站', '', 'https://tu.mianyazuju.com/20260914/26f526b4f15647ec9d67811ea40b401a.png,https://tu.mianyazuju.com/20260914/532af9d424974382a64447e2c292db43.png,https://tu.mianyazuju.com/20260914/7c3fdec16ded47818391c467380fc43a.png,https://tu.mianyazuju.com/20260914/f683582f6dd34692b065e8b0ab995c0e.png,https://tu.mianyazuju.com/20260914/c8ea571f7f184540ad8ed2d708593a33.png', '对外开放,24小时营业,地铁旁', '陕西省', '西安市', '雁塔区', '唐延路延平门地铁站C出口旁停车场', '108.890520', '34.237410', '00:00', '24:00', NULL, 10612, 1114, 1, '2025-02-25 10:48:54', '', '', '1.00', '24V-电灯', '', '', NULL, NULL, '', '', '', '', 1, '1114,453,1150,1153,130,1507,1560,1239,1362,1163,1649,981,3250', 1, '33', '欢迎各位老板光临码上台球俱乐部，代表码上台球对您的到来表示热烈欢迎，祝您玩的开心', '', '', '', 1, 1, 1, '3005721644429189636', 1, '', 'https://tu.mianyazuju.com/20250515/636c6a73c25c462eb14e75f339b9715f.png', 1, 2, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 1, 1, 1, 1, '<p><strong>电瓶车充电收费标准</strong></p>\n<p>计费方式：按小时计费，充满自动断电。</p>\n<p>白天（08:00-22:00）：1.50元/小时，会员1.20元/小时</p>\n<p>夜间（22:00-08:00）：1.00元/小时，会员0.80元/小时</p>\n<p>温馨提示：请正确插枪，离开前确认结束订单；请勿占用他人车位。</p>');
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (22, '未央凤城五路超级充电站', '', 'https://tu.mianyazuju.com/20260914/40b749919f5e4e52b6d71ed244076379.png,https://tu.mianyazuju.com/20260914/fa24e87ed84747c38ba8c75a6fddbb79.png,https://tu.mianyazuju.com/20260914/63e0da80bbd34743ab0cd6d711adf98f.png,https://tu.mianyazuju.com/20260914/1f52aa4187b24b6c833c8a93ec277c10.png,https://tu.mianyazuju.com/20260914/b095bec27dc34b0eaf60df110dd7e2a5.png', '对外开放,24小时营业,快充,有休息区', '河南省', '郑州市', '市辖区', '凤城五路与未央路交叉口西南侧停车场', '108.946180', '34.337250', '00:00', '24:00', NULL, 2, 1032, 1, '2025-05-08 10:33:48', '', '', '0.03', '24V-电灯', '', '', NULL, NULL, '', '', '', '', 1, '', 2, '', '', '', '', 'https://tu.mianyazuju.com/20250512/cc18932e925b48f2826e0b7bcbc521aa.jpg', 1, 2, 2, '', 2, '', '', 2, 2, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 2, 1, 2, 2, '<p><strong>汽车充电收费标准</strong></p>\n<p>计费方式：按电量（度）计费，服务费已含。</p>\n<p>谷时（00:00-08:00）：0.78元/度，会员0.65元/度</p>\n<p>峰时（08:00-24:00）：1.28元/度，会员1.08元/度</p>\n<p>温馨提示：请规范停放、正确插枪；结束后请及时挪车，文明充电。</p>');
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (23, '小寨赛格充电站', '', 'https://tu.mianyazuju.com/20260914/26f526b4f15647ec9d67811ea40b401a.png,https://tu.mianyazuju.com/20260914/532af9d424974382a64447e2c292db43.png,https://tu.mianyazuju.com/20260914/7c3fdec16ded47818391c467380fc43a.png,https://tu.mianyazuju.com/20260914/f683582f6dd34692b065e8b0ab995c0e.png,https://tu.mianyazuju.com/20260914/c8ea571f7f184540ad8ed2d708593a33.png', '对外开放,24小时营业,商场停车', '河南省', '郑州市', '市辖区', '长安中路小寨赛格国际购物中心P2停车场', '108.948360', '34.224870', '00:00', '24:00', NULL, 1, 3790, 1, '2026-01-29 09:38:30', NULL, '', '0.60', NULL, NULL, '', NULL, NULL, '', '', '', '', NULL, '', 2, '', '', '', '', 'https://tu.mianyazuju.com/20260129/1343755598a448669d0d250cd98ceae4.jpg', 1, 1, 2, '', 2, '', '', 2, 2, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 1, 1, 2, 1, '<p><strong>电瓶车充电收费标准</strong></p>\n<p>计费方式：按小时计费，充满自动断电。</p>\n<p>白天（08:00-22:00）：1.50元/小时，会员1.20元/小时</p>\n<p>夜间（22:00-08:00）：1.00元/小时，会员0.80元/小时</p>\n<p>温馨提示：请正确插枪，离开前确认结束订单；请勿占用他人车位。</p>');
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (24, '大雁塔南广场充电站', '', 'https://tu.mianyazuju.com/20260914/26f526b4f15647ec9d67811ea40b401a.png,https://tu.mianyazuju.com/20260914/532af9d424974382a64447e2c292db43.png,https://tu.mianyazuju.com/20260914/7c3fdec16ded47818391c467380fc43a.png,https://tu.mianyazuju.com/20260914/f683582f6dd34692b065e8b0ab995c0e.png,https://tu.mianyazuju.com/20260914/c8ea571f7f184540ad8ed2d708593a33.png', '对外开放,24小时营业,景区停车', '陕西省', '西安市', '雁塔区', '雁塔南路大雁塔南广场公共停车场', '108.964420', '34.218560', '00:00', '24:00', NULL, 9, 3250, 1, '2026-03-04 10:30:48', NULL, '', '0.10', NULL, NULL, '', NULL, NULL, '', '', '', '', NULL, '', 2, '', '', '', '', '', 1, 2, 2, '', 2, '', '', 2, 2, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 1, 1, 2, 1, '<p><strong>电瓶车充电收费标准</strong></p>\n<p>计费方式：按小时计费，充满自动断电。</p>\n<p>白天（08:00-22:00）：1.50元/小时，会员1.20元/小时</p>\n<p>夜间（22:00-08:00）：1.00元/小时，会员0.80元/小时</p>\n<p>温馨提示：请正确插枪，离开前确认结束订单；请勿占用他人车位。</p>');
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (25, '曲江池遗址公园充电站', '', 'https://tu.mianyazuju.com/20260914/40b749919f5e4e52b6d71ed244076379.png,https://tu.mianyazuju.com/20260914/fa24e87ed84747c38ba8c75a6fddbb79.png,https://tu.mianyazuju.com/20260914/63e0da80bbd34743ab0cd6d711adf98f.png,https://tu.mianyazuju.com/20260914/1f52aa4187b24b6c833c8a93ec277c10.png,https://tu.mianyazuju.com/20260914/b095bec27dc34b0eaf60df110dd7e2a5.png', '对外开放,24小时营业,快充,慢充', '陕西省', '西安市', '雁塔区', '曲江池南路曲江池遗址公园P1停车场', '108.992150', '34.200380', '00:00', '24:00', NULL, NULL, 1032, 1, '2026-05-12 17:53:06', NULL, '', '0.50', NULL, NULL, '', NULL, NULL, '', '', '', '', NULL, NULL, 2, NULL, '', '', '', '', 1, 2, 2, '', 2, '', '', 2, 2, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 1, 1, 2, 2, '<p><strong>汽车充电收费标准</strong></p>\n<p>计费方式：按电量（度）计费，服务费已含。</p>\n<p>谷时（00:00-08:00）：0.78元/度，会员0.65元/度</p>\n<p>峰时（08:00-24:00）：1.28元/度，会员1.08元/度</p>\n<p>温馨提示：请规范停放、正确插枪；结束后请及时挪车，文明充电。</p>');
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (26, '电子城街道充电站', '', 'https://tu.mianyazuju.com/20260914/26f526b4f15647ec9d67811ea40b401a.png,https://tu.mianyazuju.com/20260914/532af9d424974382a64447e2c292db43.png,https://tu.mianyazuju.com/20260914/7c3fdec16ded47818391c467380fc43a.png,https://tu.mianyazuju.com/20260914/f683582f6dd34692b065e8b0ab995c0e.png,https://tu.mianyazuju.com/20260914/c8ea571f7f184540ad8ed2d708593a33.png', '对外开放,24小时营业,社区停车', '河南省', '郑州市', '市辖区', '电子西街电子城社区停车场', '108.892740', '34.205610', '00:00', '24:00', NULL, 13, 3791, 1, '2026-05-27 16:04:32', NULL, '', '0.60', NULL, NULL, '', NULL, NULL, '', '', '', '', NULL, '', 2, '', '', '', '', '', 1, 2, 2, '', 2, '', '', 2, 2, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 1, 1, 2, 1, '<p><strong>电瓶车充电收费标准</strong></p>\n<p>计费方式：按小时计费，充满自动断电。</p>\n<p>白天（08:00-22:00）：1.50元/小时，会员1.20元/小时</p>\n<p>夜间（22:00-08:00）：1.00元/小时，会员0.80元/小时</p>\n<p>温馨提示：请正确插枪，离开前确认结束订单；请勿占用他人车位。</p>');
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (27, '11', '', 'https://tu.mianyazuju.com/20260915/8dc381ad065f4164abe478855466aec8.png', '', '北京市', '北京市', '西城区', '11', '', '', '00:00', '01:00', NULL, NULL, 3797, 1, '2026-09-15 11:59:15', NULL, '', '11.00', NULL, NULL, '', NULL, NULL, '', '', '', '', NULL, NULL, 2, NULL, '', '', '', '', 1, 1, 2, '', 2, '', '', 2, 1, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 1, 1, 2, 1, NULL);
INSERT INTO `tearoom` (`tearoom_id`, `tearoom_name`, `tearoom_cover_image`, `tearoom_title_image`, `tearoom_label`, `tearoom_province`, `tearoom_city`, `tearoom_district`, `tearoom_address`, `tearoom_longitude`, `tearoom_latitude`, `start_time`, `end_time`, `score`, `sales`, `user_id`, `status`, `create_time`, `inner_code`, `tearoom_phone`, `rate`, `room_name`, `room_no`, `key_code`, `subscription`, `cash_pledge`, `yy_token`, `yy_id`, `wifi_no`, `wifi_pwd`, `money_scope`, `employee_user_ids`, `is_reserve`, `shop_ids`, `broadcast_content`, `camera_id`, `wx_code`, `qun_img`, `is_open`, `shop_type`, `is_vip_privilege`, `shop_poi_id`, `is_welfare`, `welfare_content`, `welfare_img`, `is_face_recognition`, `is_music`, `ks_app_id`, `ks_app_secret`, `ks_refresh_token`, `ks_refresh_token_time`, `mt_email`, `mt_password`, `mt_store_id`, `dy_poi_id`, `dy_app_id`, `dy_secret`, `lucky_sort`, `lucky_coupon_ids`, `lucky_content`, `is_refund`, `is_wx_pay`, `is_qr_code`, `tearoom_type`, `tearoom_content`) VALUES (28, '测试电瓶车充电桩', '', 'https://tu.mianyazuju.com/20261030/fc73e991cbb3453aa7b23c593f90e13d.png', '', '湖南省', '长沙市', '市辖区', '雨湖区比一比家居西50米(潭锰路南)', '112.89232537150383', '27.89798452241019', '00:00', '24:00', NULL, 1, NULL, 1, '2026-10-30 22:21:47', NULL, '', '1.00', NULL, NULL, '', NULL, NULL, '', '', '', '', NULL, NULL, 2, NULL, '', '', '', '', 1, 1, 2, '', 2, '', '', 2, 1, '', '', NULL, NULL, '', '', '', '', '', '', '', '', '', 1, 1, 2, 1, NULL);

-- ----------------------------
-- Table structure for tearoom_user
-- ----------------------------
DROP TABLE IF EXISTS `tearoom_user`;
CREATE TABLE `tearoom_user` (
  `tearoom_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '门店员工id',
  `tearoom_id` int(11) DEFAULT NULL COMMENT '门店id',
  `user_id` int(11) DEFAULT NULL COMMENT '员工id',
  `start_time` varchar(64) DEFAULT NULL COMMENT '上班时间',
  `end_time` varchar(64) DEFAULT NULL COMMENT '下班时间',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `status` int(11) DEFAULT NULL COMMENT '状态 1正常 2上班 3下班',
  `is_yuyin` int(11) DEFAULT NULL COMMENT '语音播报 1开 2关',
  `is_status` int(11) DEFAULT NULL COMMENT '门店状态',
  `is_paihao` int(11) DEFAULT NULL COMMENT '排号',
  `is_tixian` int(11) DEFAULT NULL COMMENT '提现',
  `is_huiyuanquanyi` int(11) DEFAULT NULL COMMENT '会员权益',
  `is_shouyi` int(11) DEFAULT NULL COMMENT '总收益',
  `is_zhuotaiguanli` int(11) DEFAULT NULL COMMENT '桌台管理',
  `is_zhuotaishuju` int(11) DEFAULT NULL COMMENT '桌台数据',
  `is_shiwuhexiao` int(11) DEFAULT NULL COMMENT '实物核销',
  `is_huiyuanguanli` int(11) DEFAULT NULL COMMENT '会员管理',
  `is_shangjiakaitai` int(11) DEFAULT NULL COMMENT '商家开台',
  `is_diancanguanli` int(11) DEFAULT NULL COMMENT '点餐管理',
  `is_zengsongyouhuijuan` int(11) DEFAULT NULL COMMENT '赠送优惠券',
  `is_huodongzhongxin` int(11) DEFAULT NULL COMMENT '活动中心',
  `is_diange` int(11) DEFAULT NULL COMMENT '点歌管理',
  `is_mendianhuiyuan` int(11) DEFAULT NULL COMMENT '门店会员',
  `is_yuangongguanli` int(11) DEFAULT NULL COMMENT '员工管理',
  `is_shangjiakaitaitongji` int(11) DEFAULT NULL COMMENT '商家开台统计',
  `is_dingdanguanli` int(11) DEFAULT NULL COMMENT '订单管理',
  `is_daijiesuan` int(11) DEFAULT NULL COMMENT '待结算统计',
  `is_kaitaishouyi` int(11) DEFAULT NULL COMMENT '开台收益',
  `is_hexiaotongji` int(11) DEFAULT NULL COMMENT '核销统计',
  `is_xiaofeipaihang` int(11) DEFAULT NULL COMMENT '消费排行榜',
  `is_shichangtongji` int(11) DEFAULT NULL COMMENT '时长统计',
  `is_shouyimingxi` int(11) DEFAULT NULL COMMENT '收益明细',
  `is_zhangdantongji` int(11) DEFAULT NULL COMMENT '账单对账',
  PRIMARY KEY (`tearoom_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tearoom_user
-- ----------------------------
INSERT INTO `tearoom_user` (`tearoom_user_id`, `tearoom_id`, `user_id`, `start_time`, `end_time`, `create_time`, `status`, `is_yuyin`, `is_status`, `is_paihao`, `is_tixian`, `is_huiyuanquanyi`, `is_shouyi`, `is_zhuotaiguanli`, `is_zhuotaishuju`, `is_shiwuhexiao`, `is_huiyuanguanli`, `is_shangjiakaitai`, `is_diancanguanli`, `is_zengsongyouhuijuan`, `is_huodongzhongxin`, `is_diange`, `is_mendianhuiyuan`, `is_yuangongguanli`, `is_shangjiakaitaitongji`, `is_dingdanguanli`, `is_daijiesuan`, `is_kaitaishouyi`, `is_hexiaotongji`, `is_xiaofeipaihang`, `is_shichangtongji`, `is_shouyimingxi`, `is_zhangdantongji`) VALUES (4, 20, 1560, '11:15', '23:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for tearoom_user_record
-- ----------------------------
DROP TABLE IF EXISTS `tearoom_user_record`;
CREATE TABLE `tearoom_user_record` (
  `tearoom_user_record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '员工上班记录表id',
  `tearoom_id` int(11) DEFAULT NULL COMMENT '门店id',
  `user_id` int(11) DEFAULT NULL COMMENT '员工id',
  `start_time` varchar(64) DEFAULT NULL COMMENT '上班时间',
  `end_time` varchar(64) DEFAULT NULL COMMENT '下班时间',
  `sum_money` decimal(10,2) DEFAULT NULL COMMENT '总订单收益',
  `sum_count` int(11) DEFAULT NULL COMMENT '总订单数量',
  `wx_money` decimal(10,2) DEFAULT NULL COMMENT '商家微信订单收益',
  `wx_count` int(11) DEFAULT NULL COMMENT '商家微信订单数量',
  `rmb_money` decimal(10,2) DEFAULT NULL COMMENT '商家现金订单收益',
  `rmb_count` int(11) DEFAULT NULL COMMENT '商家现金订单数量',
  `member_money` decimal(10,2) DEFAULT NULL COMMENT '商家门店会员订单收益',
  `member_count` int(11) DEFAULT NULL COMMENT '商家门店会员订单数量',
  `mt_money` decimal(10,2) DEFAULT NULL COMMENT '商家美团收益',
  `mt_count` int(11) DEFAULT NULL COMMENT '商家美团数量',
  `ks_money` decimal(10,2) DEFAULT NULL COMMENT '商家快手收益',
  `ks_count` int(11) DEFAULT NULL COMMENT '商家快手数量',
  `dy_money` decimal(10,2) DEFAULT NULL COMMENT '商家抖音收益',
  `dy_count` int(11) DEFAULT NULL COMMENT '商家抖音数量',
  `smq_mt_money` decimal(10,2) DEFAULT NULL COMMENT '扫码器美团收益',
  `smq_mt_count` int(11) DEFAULT NULL COMMENT '扫码器美团数量',
  `smq_ks_money` decimal(10,2) DEFAULT NULL COMMENT '扫码器快手收益',
  `smq_ks_count` int(11) DEFAULT NULL COMMENT '扫码器快手数量',
  `smq_dy_money` decimal(10,2) DEFAULT NULL COMMENT '扫码器抖音收益',
  `smq_dy_count` int(11) DEFAULT NULL COMMENT '扫码器抖音数量',
  `smq_sys_money` decimal(10,2) DEFAULT NULL COMMENT '扫码器系统券收益',
  `smq_sys_count` int(11) DEFAULT NULL COMMENT '扫码器系统券数量',
  `yh_member_money` decimal(10,2) DEFAULT NULL COMMENT '用户门店会员订单收益',
  `yh_member_count` int(11) DEFAULT NULL COMMENT '用户门店会员订单数量',
  `yh_wx_money` decimal(10,2) DEFAULT NULL COMMENT '用户微信订单收益',
  `yh_wx_count` int(11) DEFAULT NULL COMMENT '用户微信订单数量',
  `yh_mt_money` decimal(10,2) DEFAULT NULL COMMENT '用户美团收益',
  `yh_mt_count` int(11) DEFAULT NULL COMMENT '用户美团数量',
  `yh_ks_money` decimal(10,2) DEFAULT NULL COMMENT '用户快手收益',
  `yh_ks_count` int(11) DEFAULT NULL COMMENT '用户快手数量',
  `yh_dy_money` decimal(10,2) DEFAULT NULL COMMENT '用户抖音收益',
  `yh_dy_count` int(11) DEFAULT NULL COMMENT '用户抖音数量',
  `yh_sys_money` decimal(10,2) DEFAULT NULL COMMENT '用户系统券收益',
  `yh_sys_count` int(11) DEFAULT NULL COMMENT '用户系统券数量',
  PRIMARY KEY (`tearoom_user_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tearoom_user_record
-- ----------------------------
INSERT INTO `tearoom_user_record` (`tearoom_user_record_id`, `tearoom_id`, `user_id`, `start_time`, `end_time`, `sum_money`, `sum_count`, `wx_money`, `wx_count`, `rmb_money`, `rmb_count`, `member_money`, `member_count`, `mt_money`, `mt_count`, `ks_money`, `ks_count`, `dy_money`, `dy_count`, `smq_mt_money`, `smq_mt_count`, `smq_ks_money`, `smq_ks_count`, `smq_dy_money`, `smq_dy_count`, `smq_sys_money`, `smq_sys_count`, `yh_member_money`, `yh_member_count`, `yh_wx_money`, `yh_wx_count`, `yh_mt_money`, `yh_mt_count`, `yh_ks_money`, `yh_ks_count`, `yh_dy_money`, `yh_dy_count`, `yh_sys_money`, `yh_sys_count`) VALUES (1, 20, 981, '2025-11-07 09:00:00', '2025-11-07 18:00:00', '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0, '0.00', 0);

-- ----------------------------
-- Table structure for trip
-- ----------------------------
DROP TABLE IF EXISTS `trip`;
CREATE TABLE `trip` (
  `trip_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '出行方式',
  `taxi_starting_fare` varchar(255) DEFAULT NULL COMMENT '出租起步价',
  `taxi_starting_km` varchar(255) DEFAULT NULL COMMENT '出租起步公里',
  `taxi_km_money` varchar(255) DEFAULT NULL COMMENT '出租每公里价格',
  `bus_starting_fare` varchar(255) DEFAULT NULL COMMENT '公交起步价',
  `bus_starting_km` varchar(255) DEFAULT NULL COMMENT '公交起步公里',
  `bus_km_money` varchar(255) DEFAULT NULL COMMENT '公交每公里价格',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`trip_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of trip
-- ----------------------------
INSERT INTO `trip` (`trip_id`, `taxi_starting_fare`, `taxi_starting_km`, `taxi_km_money`, `bus_starting_fare`, `bus_starting_km`, `bus_km_money`, `city`, `create_time`) VALUES (10, '10', '5', '2', '2', '5', '1', '西安市', '2025-05-27 17:13:49');
INSERT INTO `trip` (`trip_id`, `taxi_starting_fare`, `taxi_starting_km`, `taxi_km_money`, `bus_starting_fare`, `bus_starting_km`, `bus_km_money`, `city`, `create_time`) VALUES (11, '10', '5', '2', '2', '5', '1', '吴忠市', '2025-05-27 17:42:00');

-- ----------------------------
-- Table structure for user_browse
-- ----------------------------
DROP TABLE IF EXISTS `user_browse`;
CREATE TABLE `user_browse` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `by_browse_id` bigint(20) DEFAULT NULL COMMENT '浏览用户id',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  `taking_id` bigint(20) DEFAULT NULL COMMENT '接单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2569 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_browse
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `follow_user_id` bigint(20) DEFAULT NULL COMMENT '关注用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=352 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_follow
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for user_integral
-- ----------------------------
DROP TABLE IF EXISTS `user_integral`;
CREATE TABLE `user_integral` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `integral_num` int(11) DEFAULT NULL COMMENT '积分数量',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_integral
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for user_integral_details
-- ----------------------------
DROP TABLE IF EXISTS `user_integral_details`;
CREATE TABLE `user_integral_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '积分详情id',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `classify` int(255) DEFAULT NULL COMMENT '获取类型 1签到2报名',
  `type` int(255) DEFAULT NULL COMMENT '分类 1增加 2减少',
  `num` int(11) DEFAULT NULL COMMENT '数量',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `day` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4593 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_integral_details
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for user_money
-- ----------------------------
DROP TABLE IF EXISTS `user_money`;
CREATE TABLE `user_money` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '钱包id',
  `money` decimal(10,2) DEFAULT NULL COMMENT '钱包金额',
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户id',
  `shop_money` decimal(11,2) DEFAULT NULL COMMENT '商家金额',
  `sum_shop_money` decimal(10,2) DEFAULT NULL COMMENT '商家累计结算金额',
  `invite_money` decimal(10,2) DEFAULT NULL COMMENT '用户收益金额',
  `cash_deposit` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3737 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_money
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for user_money_details
-- ----------------------------
DROP TABLE IF EXISTS `user_money_details`;
CREATE TABLE `user_money_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包详情id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `by_user_id` int(11) DEFAULT NULL COMMENT '邀请用户id',
  `shop_user_id` int(11) DEFAULT NULL COMMENT '商家用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '标题',
  `classify` int(11) DEFAULT NULL COMMENT '1注册   2购买 3提现',
  `type` int(11) DEFAULT NULL COMMENT '类型（1充值 2.提现）',
  `state` int(11) DEFAULT '1' COMMENT '状态 1待支付 2已到账 3取消',
  `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `content` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '内容',
  `create_time` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `artificer_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`),
  KEY `shop_user_id` (`shop_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62632 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_money_details
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for user_vip
-- ----------------------------
DROP TABLE IF EXISTS `user_vip`;
CREATE TABLE `user_vip` (
  `vip_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员id',
  `vip_name_type` int(2) DEFAULT NULL COMMENT '会员类型0月1季2年',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(255) DEFAULT NULL COMMENT '购买时间',
  `end_time` varchar(255) DEFAULT NULL COMMENT '到期时间',
  `is_vip` int(255) DEFAULT NULL COMMENT '1是会员2不是',
  PRIMARY KEY (`vip_id`) USING BTREE,
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_vip
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for user_vip_details
-- ----------------------------
DROP TABLE IF EXISTS `user_vip_details`;
CREATE TABLE `user_vip_details` (
  `user_vip_details_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户会员值明细表',
  `classify` int(11) DEFAULT NULL COMMENT '1充值 2消费',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `create_time` varchar(64) DEFAULT NULL COMMENT '时间',
  `orders_no` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`user_vip_details_id`) USING BTREE,
  KEY `user_id` (`user_id`),
  KEY `classify` (`classify`)
) ENGINE=InnoDB AUTO_INCREMENT=5693 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_vip_details
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for user_visitor
-- ----------------------------
DROP TABLE IF EXISTS `user_visitor`;
CREATE TABLE `user_visitor` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '访客id',
  `user_id` bigint(32) DEFAULT NULL COMMENT '用户id',
  `by_user_id` bigint(32) DEFAULT NULL COMMENT '访问用户id',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1983 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_visitor
-- ----------------------------
-- (structure only, demo dump skips data)

-- ----------------------------
-- Table structure for vip
-- ----------------------------
DROP TABLE IF EXISTS `vip`;
CREATE TABLE `vip` (
  `vip_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员等级id',
  `vip_name` varchar(255) DEFAULT NULL COMMENT '会员名称',
  `vip_image` varchar(255) DEFAULT NULL COMMENT '会员图片',
  `vip_index` int(11) DEFAULT NULL COMMENT '会员等级',
  `user_img` varchar(2000) DEFAULT NULL,
  `money` decimal(10,2) DEFAULT NULL COMMENT '消费值',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `vip_icon` varchar(255) DEFAULT NULL COMMENT '会员图标',
  `font_color` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`vip_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of vip
-- ----------------------------
INSERT INTO `vip` (`vip_id`, `vip_name`, `vip_image`, `vip_index`, `user_img`, `money`, `create_time`, `vip_icon`, `font_color`) VALUES (1, '普通会员', 'https://tu.mianyazuju.com/20250319/4235c2d32c5543a2ae44f384e3a48e67.png', 1, 'https://tu.mianyazuju.com/20250319/017b703ece0f4beda76651d51152c47c.png', '0.00', '2025-03-03 15:37:39', '', '#377EF6');

-- ----------------------------
-- Table structure for vip_details
-- ----------------------------
DROP TABLE IF EXISTS `vip_details`;
CREATE TABLE `vip_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vip_name_type` int(2) DEFAULT NULL COMMENT '会员类型0月1季2年',
  `money` decimal(10,0) DEFAULT NULL COMMENT '会员价格',
  `vip_name` varchar(255) DEFAULT NULL COMMENT '会员名称',
  `rate` decimal(10,2) DEFAULT NULL,
  `award` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of vip_details
-- ----------------------------
INSERT INTO `vip_details` (`id`, `vip_name_type`, `money`, `vip_name`, `rate`, `award`) VALUES (2, 2, '38', '月会员', NULL, '20.00');
INSERT INTO `vip_details` (`id`, `vip_name_type`, `money`, `vip_name`, `rate`, `award`) VALUES (3, 1, '100', '季会员', NULL, '2.00');
INSERT INTO `vip_details` (`id`, `vip_name_type`, `money`, `vip_name`, `rate`, `award`) VALUES (8, 2, '380', '年会员', NULL, '30.00');

-- ----------------------------
-- Table structure for vip_discount
-- ----------------------------
DROP TABLE IF EXISTS `vip_discount`;
CREATE TABLE `vip_discount` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `discount` decimal(10,0) DEFAULT NULL COMMENT '优惠金币',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of vip_discount
-- ----------------------------
INSERT INTO `vip_discount` (`id`, `discount`) VALUES (1, '3');

-- ----------------------------
-- Table structure for vip_privilege
-- ----------------------------
DROP TABLE IF EXISTS `vip_privilege`;
CREATE TABLE `vip_privilege` (
  `vip_privilege_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员特权id',
  `vip_id` int(11) DEFAULT NULL COMMENT '会员id',
  `privilege_name` varchar(255) DEFAULT NULL COMMENT '特权名称',
  `privilege_content` varchar(255) DEFAULT NULL COMMENT '特权描述',
  `privilege_image` varchar(255) DEFAULT NULL COMMENT '特权图片',
  `privilege_classify` int(11) DEFAULT NULL COMMENT '特权类型 1赠送优惠券 2消费折扣 3开台问候 4专属图标 5专属赛事 6专属活动 7短信服务 8分销 9其他',
  `privilege_type` int(11) DEFAULT NULL COMMENT '触发频率 1每天 2每周 3每月 4每季度 5每年 6生日 7无',
  `parent` text COMMENT '对应上级 1优惠券id 2折扣比例 3问候语 4图片 7短信内容 8每笔抽成金额',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `is_ok` int(11) DEFAULT NULL COMMENT '是否已执行 1是',
  `details` longtext COMMENT '具体权益',
  `privilege_image2` varchar(255) DEFAULT NULL COMMENT '特权图片2',
  `privilege_image3` varchar(255) DEFAULT NULL COMMENT '特权图片3',
  `is_every_day` int(11) DEFAULT NULL,
  PRIMARY KEY (`vip_privilege_id`) USING BTREE,
  KEY `vip_id` (`vip_id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of vip_privilege
-- ----------------------------
INSERT INTO `vip_privilege` (`vip_privilege_id`, `vip_id`, `privilege_name`, `privilege_content`, `privilege_image`, `privilege_classify`, `privilege_type`, `parent`, `sort`, `create_time`, `is_ok`, `details`, `privilege_image2`, `privilege_image3`, `is_every_day`) VALUES (4, 2, '每月赠送饮品券', '每月赠送2张3元饮品券', 'https://tu.mianyazuju.com/20250319/9f4c70d52b5849c8813c1bc22edd64aa.png', 1, 3, '131,131', 1, '2025-03-04 18:02:07', 2, '<p>1.银卡会员每月一号可获得系统自动赠送的2张实物劵兑换饮料使用，3元以内的饮料;</p><p>2.优惠劵在有效期内进行核销;每次领取在前台核销</p><p>3.饮料券只能本人使用，不可转赠,一旦发现劵当场作废.</p>', 'https://tu.mianyazuju.com/20250319/76c3953e957440078befdc8de5fad668.png', 'https://tu.mianyazuju.com/20250319/0a8bdc9e3c7044ed92a307444d26b4f3.png', NULL);

SET FOREIGN_KEY_CHECKS = 1;
