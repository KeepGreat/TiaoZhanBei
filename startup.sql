CREATE DATABASE IF NOT EXISTS lddb;#leucoderma diagnosis database
USE lddb;

#用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码'
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#信息表
DROP TABLE IF EXISTS `info`;
CREATE TABLE `info` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `image1` LONGBLOB NOT NULL COMMENT '图片一',
  `image1_type` VARCHAR(10) COMMENT '图片一类型',
  `image2` LONGBLOB NOT NULL COMMENT '图片二',
  `image2_type` VARCHAR(10) COMMENT '图片二类型',
  `patient_gender` CHAR(1) NOT NULL COMMENT '病人性别',
  `patient_age` TINYINT NOT NULL COMMENT '病人年龄',
  `keyword` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '诊断疾病关键字',
  `diagnosis` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '诊断结果',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间'
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;