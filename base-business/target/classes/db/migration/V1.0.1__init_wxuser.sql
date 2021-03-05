DROP TABLE IF EXISTS `wxuser`;
CREATE TABLE `wxuser` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `accountno` varchar(50) DEFAULT NULL COMMENT '账号',
  `pwd` varchar(50) NOT NULL COMMENT '密码',
  `openid` varchar(50) DEFAULT NULL COMMENT '微信openid',
  `avatarurl` varchar(200) DEFAULT NULL COMMENT '用户头像',
  `provice` varchar(10) DEFAULT NULL COMMENT '省',
  `city` varchar(10) DEFAULT NULL COMMENT '市',
  `country` varchar(10) DEFAULT NULL COMMENT '区',
  `gender` varchar(10) DEFAULT NULL COMMENT 'gender',
  `nickname` varchar(10) DEFAULT NULL COMMENT '昵称',
  `thirdsession` varchar(50) DEFAULT NULL COMMENT '三方session',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  `createby` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updateby` varchar(50) DEFAULT NULL COMMENT '更新人',
  `isdeleted` varchar(1) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
