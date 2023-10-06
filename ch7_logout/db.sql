

create database springsecurity;

use springsecurity;

CREATE TABLE `springsecurity`.`users` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `username` varchar(50) NOT NULL,
                                      `password` varchar(60) NOT NULL,
                                      `enable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '用户是否可用',
                                      `roles` text CHARACTER SET utf8 COMMENT '用户角色，多个角色之间用逗号隔开',
                                      PRIMARY KEY (`id`),
                                      KEY `username` (`username`)
);

insert into `springsecurity`.`users`(`username`, `password`, `roles`) values("admin", "$2a$10$13kJFBbcsDVT3G8ya54sA.6npwIT9KBD7ENSLgbZuUICSsK5t.jXu", "ROLE_ADMIN,ROLE_USER");
insert into `springsecurity`.`users`(`username`, `password`, `roles`) values("user", "$2a$10$13kJFBbcsDVT3G8ya54sA.6npwIT9KBD7ENSLgbZuUICSsK5t.jXu", "ROLE_USER");

