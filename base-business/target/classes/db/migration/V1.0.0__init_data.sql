
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for apikey_info
-- ----------------------------
DROP TABLE IF EXISTS `apikey_info`;
CREATE TABLE `apikey_info` (
  `uuid` varchar(32) NOT NULL COMMENT 'uuid',
  `uid` varchar(32) DEFAULT NULL COMMENT 'user id',
  `api_key` varchar(32) DEFAULT NULL COMMENT 'APIKEY',
  `create_time` datetime DEFAULT NULL COMMENT 'create time',
  `expire` datetime DEFAULT NULL COMMENT 'expire time',
  `remarks` varchar(100) DEFAULT NULL COMMENT 'remarks',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of apikey_info
-- ----------------------------
INSERT INTO `apikey_info` VALUES ('3b3de777e798499c89b1a2c269b16d5f', '00000000000000000000000000000000', null, '2020-04-19 16:38:10', '4899-01-01 00:00:00', 'test');

-- ----------------------------
-- Table structure for apikey_permission
-- ----------------------------
DROP TABLE IF EXISTS `apikey_permission`;
CREATE TABLE `apikey_permission` (
  `apikey` varchar(32) NOT NULL COMMENT 'apikey',
  `permissions` text COMMENT 'permissions',
  `create_time` datetime DEFAULT NULL COMMENT 'create time',
  `update_time` datetime DEFAULT NULL COMMENT 'update time',
  PRIMARY KEY (`apikey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of apikey_permission
-- ----------------------------
INSERT INTO `apikey_permission` VALUES ('3b3de777e798499c89b1a2c269b16d5f', 'api:kss:shareCheck,api:kss:undoCheck,api:kss:cancelKeyBack,api:kss:invalidNotify,api:kss:deleteNotify,api:kss:apipki:create', '2020-04-19 16:38:10', '2020-04-19 16:38:10');

-- ----------------------------
-- Table structure for mail_validate
-- ----------------------------
DROP TABLE IF EXISTS `mail_validate`;
CREATE TABLE `mail_validate` (
  `id` char(32) DEFAULT NULL,
  `target_mail` varchar(64) DEFAULT NULL COMMENT 'email',
  `expiry_time` varchar(32) DEFAULT NULL COMMENT 'expiry time',
  `token` varchar(255) DEFAULT NULL COMMENT 'token',
  `state` int(32) DEFAULT NULL COMMENT 'state',
  `create_by` char(32) DEFAULT NULL COMMENT 'create people',
  `create_date` datetime DEFAULT NULL COMMENT 'create data',
  `update_by` char(32) DEFAULT NULL COMMENT 'update people',
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  `remarks` varchar(255) DEFAULT NULL COMMENT 'remark',
  `del_flag` int(32) DEFAULT NULL COMMENT 'del flag'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail_validate
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `role_name` varchar(50) DEFAULT NULL COMMENT 'role name',
  `ch_name` varchar(50) DEFAULT NULL COMMENT 'ch name',
  `create_by` varchar(50) DEFAULT NULL COMMENT 'create people',
  `create_date` datetime DEFAULT NULL COMMENT 'create data',
  `update_by` varchar(50) DEFAULT NULL COMMENT 'update people',
  `update_date` datetime DEFAULT NULL COMMENT 'update data',
  `remarks` varchar(100) DEFAULT NULL COMMENT 'remark',
  `del_flag` varchar(1) DEFAULT NULL COMMENT 'del flag',
  `permissions` text DEFAULT NULL COMMENT 'permissions',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('26fcd6210c2b4c92830e4b22e6523062', 'super_admin', 'super_admin', '', '2018-11-20 16:50:21', '', '2018-11-20 16:50:21', '', '0', '[\"apikey:list\",\"apikey:add\",\"apikey:update\",\"apikey:detail\",\"apikey:delete\",\"manager:list\",\"manager:add\",\"manager:update\",\"manager:delete\",\"role:list\",\"role:add\",\"role:update\",\"role:detail\",\"role:delete\"]');
INSERT INTO `role` VALUES ('26fcd6210c2b4c92830e4b22e652306c', 'normal_user', 'nprmal_user', '', '2018-11-20 16:50:21', '', '2018-11-20 16:50:21', '', '0', null);

-- ----------------------------
-- Table structure for sms
-- ----------------------------
DROP TABLE IF EXISTS `sms`;
CREATE TABLE `sms` (
  `id` varchar(32) NOT NULL COMMENT 'Primary key',
  `type` varchar(10) DEFAULT NULL COMMENT 'type:1.verification code 2.audit inform 3.share inform 4.unbind inform',
  `code` varchar(10) DEFAULT NULL COMMENT 'Verification Code',
  `content` varchar(100) DEFAULT NULL COMMENT 'Content of short message',
  `user_id` varchar(32) DEFAULT NULL COMMENT 'User ID',
  `mobile` varchar(20) DEFAULT NULL COMMENT 'Cell-phone number',
  `create_time` datetime DEFAULT NULL COMMENT 'creation time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sms
-- ----------------------------
INSERT INTO `sms` VALUES ('a66fbc71f5dd4977b52c75ac005de640', '1', '666666', 'verification code: 666666', null, '13636363636', '2020-05-22 07:12:32');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `mobile` varchar(30) DEFAULT NULL COMMENT 'mobile',
  `user_type` varchar(1) DEFAULT NULL COMMENT 'user_type 0,1,2',
  `user_name` varchar(50) DEFAULT NULL COMMENT 'user_name',
  `pass_word` varchar(100) DEFAULT NULL COMMENT 'pass_word',
  `status` varchar(1) DEFAULT NULL COMMENT 'status',
  `usestore` varchar(20) DEFAULT NULL COMMENT 'usestore',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ',
  `declaration` text COMMENT 'declaration',
  `birthday` varchar(25) DEFAULT NULL COMMENT 'birthday',
  `url` varchar(100) DEFAULT NULL COMMENT 'url',
  `create_time` datetime DEFAULT NULL COMMENT 'create_time',
  `update_time` datetime DEFAULT NULL COMMENT 'update_time',
  `create_by` varchar(50) DEFAULT NULL COMMENT 'create_by',
  `update_by` varchar(50) DEFAULT NULL COMMENT 'update_by',
  `is_deleted` varchar(1) DEFAULT NULL COMMENT 'is_deleted',
  `store` varchar(50) DEFAULT NULL COMMENT 'store',
  PRIMARY KEY (`id`),
  KEY `idx_user_user_name` (`user_name`),
  KEY `idx_user_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('b6146368ac834f228f43e25e6b528eb6', '13671955975', '0', 'storage_9z9lZ3', null, '1', null, null, null, null, 'http://localhost:8080/img/head.jpg', '2020-05-22 07:12:36', null, null, null, '0', '5');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `username` varchar(64) DEFAULT NULL COMMENT 'username',
  `password` varchar(100) DEFAULT NULL COMMENT 'password',
  `email` varchar(30) DEFAULT NULL COMMENT 'email',
  `state` int(1) DEFAULT NULL COMMENT 'state',
  `create_by` varchar(50) DEFAULT NULL COMMENT 'create_by',
  `create_date` datetime DEFAULT NULL COMMENT 'create_date',
  `update_by` char(32) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL COMMENT 'update_date',
  `remarks` varchar(50) DEFAULT NULL COMMENT 'remarks',
  `del_flag` varchar(1) DEFAULT NULL COMMENT 'del_flag',
  `company` varchar(40) DEFAULT NULL COMMENT 'company',
  `position` varchar(40) DEFAULT NULL,
  `location` varchar(40) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL COMMENT 'mobile',
  `api_key` varchar(32) DEFAULT NULL COMMENT 'api_key',
  `api_key_secret` varchar(100) DEFAULT NULL COMMENT 'api_key_secret',
  `api_token` varchar(255) DEFAULT NULL COMMENT 'api_token',
  `roles` varchar(1000) DEFAULT NULL COMMENT 'roles',
  `is_deleted` varchar(2) DEFAULT NULL COMMENT 'is_deleted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('00000000000000000000000000000000', 'admin', '571dbb8210641eac2e1c3482bcb9dc037bacc9b43997ab538d56fb052bfa782cf1b68b9b9b5479d1', 'liu@qq.com', '1', '', '2018-12-28 14:51:56', '', '2018-12-28 14:51:56', '', null, 'frankcompany', 'manager', 'shanghai', '', '1234567890', 'qwertyuiop', 'hGb69QoIX9KzV2bGBEs2gAo3aVGaDs3xw6nL9S+DB3NbCSdLEMp8LiQ3pKz9nvQU4/ronsmNosWXBQSiWzLxXOsKAumPnU9GN0xMyfGkC/dH5FVDSIvtH44c98cO0XkTTh7a7FJbotUuEkOMTHfsKYc5k42Y6A6fIQi/di3HRtk=', 'super_admin', null);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` varchar(32) DEFAULT NULL COMMENT 'userid',
  `role_id` varchar(32) DEFAULT NULL COMMENT 'roleid'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('00000000000000000000000000000000', '26fcd6210c2b4c92830e4b22e652306c');
