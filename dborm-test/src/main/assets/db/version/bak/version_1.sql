--用户信息
CREATE TABLE `login_user` (
  `id` varchar(64) NOT NULL DEFAULT '',
  `is_boy` int(11) DEFAULT NULL,
  `user_name` varchar(64) DEFAULT NULL,
  `birthday` bigint(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `login_num` int(11) DEFAULT '0',
  `user_id` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`,`user_id`)
);

--调查问卷
CREATE TABLE `qsm_info` (
  `content` varchar(64) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `question_id` varchar(64) DEFAULT NULL
);

--调查问卷选项
CREATE TABLE `qsm_option` (
  `content` varchar(64) DEFAULT NULL,
  `show_order` float DEFAULT NULL,
  `attachment_count` int(11) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `option_id` varchar(64) NOT NULL DEFAULT '',
  `question_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`option_id`)
);