CREATE DATABASE IF NOT EXISTS `oauth2-websocket` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `oauth2-websocket`;

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` ENUM('ADMIN', 'USER') NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 0,
  `ins_dtm` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `user`(`username`,`password`, `ins_dtm`) VALUE ('admin', '1', current_timestamp());

SELECT * FROM `user`;

ALTER USER 'root'@'localhost' IDENTIFIED BY 'Sql@dmin1';
