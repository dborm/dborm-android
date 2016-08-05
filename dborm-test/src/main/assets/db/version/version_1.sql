
CREATE TABLE `book_info` (
  `id` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL DEFAULT '',
  `price` REAL DEFAULT NULL,
  `looked` TEXT DEFAULT NULL,
  `read_time` INTEGER DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`)
);


CREATE TABLE `user_info` (
  `id` varchar(64) NOT NULL DEFAULT '',
  `name` varchar(64) NOT NULL,
  `nickname` varchar(64) DEFAULT NULL,
  `age` int(11) DEFAULT '18',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);
