# Host: 127.0.0.1  (Version: 5.1.70-community)
# Date: 2015-07-15 11:10:28
# Generator: MySQL-Front 5.3  (Build 2.42)

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

DROP DATABASE IF EXISTS `ledpro`;
CREATE DATABASE `ledpro` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ledpro`;

#
# Source for table "lp_customer"
#

DROP TABLE IF EXISTS `lp_customer`;
CREATE TABLE `lp_customer` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户编号',
  `real_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `nickname` varchar(20) DEFAULT NULL COMMENT '昵称',
  `head_img` varchar(50) DEFAULT NULL COMMENT '头像照片大图文件名',
  `customer_sex` int(2) DEFAULT '0' COMMENT '用户性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `cellphone` varchar(30) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱地址',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='终端用户信息';

#
# Data for table "lp_customer"
#


#
# Source for table "lp_eval"
#

DROP TABLE IF EXISTS `lp_eval`;
CREATE TABLE `lp_eval` (
  `eval_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评价编号',
  `eval_obj_type` int(2) DEFAULT NULL COMMENT '评价对象类型',
  `eval_obj_id` int(11) DEFAULT NULL COMMENT '对应评价对象编号',
  `eval_value` float(4,2) DEFAULT '0.00' COMMENT '评分值',
  `user_id` int(11) DEFAULT '0' COMMENT '评价人编号',
  `user_name` varchar(50) DEFAULT NULL COMMENT '评价人',
  `eval_time` datetime DEFAULT NULL COMMENT '评价时间',
  `eval_content` varchar(128) DEFAULT NULL COMMENT '评价内容',
  PRIMARY KEY (`eval_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='评价信息';

#
# Data for table "lp_eval"
#


#
# Source for table "lp_eval_sum"
#

DROP TABLE IF EXISTS `lp_eval_sum`;
CREATE TABLE `lp_eval_sum` (
  `eval_sum_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评分总计编号',
  `eval_obj_type` int(2) DEFAULT NULL COMMENT '评价对象类型',
  `eval_obj_id` int(11) DEFAULT NULL COMMENT '评价对象编号',
  `eval_sum_value` double(20,2) DEFAULT '0.00' COMMENT '总评分',
  `eval_count` bigint(20) DEFAULT NULL COMMENT '评价次数',
  PRIMARY KEY (`eval_sum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='评价累计';

#
# Data for table "lp_eval_sum"
#


#
# Source for table "lp_product"
#

DROP TABLE IF EXISTS `lp_product`;
CREATE TABLE `lp_product` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品编号',
  `product_name` varchar(50) DEFAULT NULL COMMENT '产品名称',
  `product_desc` varchar(255) DEFAULT NULL COMMENT '产品描述',
  `product_score` float(5,2) DEFAULT '0.00' COMMENT '产品评分',
  `product_catg` varchar(20) DEFAULT NULL COMMENT '产品种类',
  `product_type` int(2) DEFAULT NULL COMMENT '产品类别',
  `use_location` varchar(255) DEFAULT NULL COMMENT '使用地点',
  `mounting_base` varchar(255) DEFAULT NULL COMMENT '底座类型',
  `sub_catg` varchar(255) DEFAULT NULL COMMENT '子类别',
  `product_size` varchar(255) DEFAULT NULL COMMENT '产品尺寸',
  `lumen_output` float(11,2) DEFAULT '0.00' COMMENT '输出光照',
  `input_watt` float(7,2) DEFAULT '0.00' COMMENT '输入功率',
  `warranty` float(6,2) DEFAULT '0.00' COMMENT '保修期/年',
  `manufacture` varchar(100) DEFAULT NULL COMMENT '生产厂商名称',
  `product_img` varchar(50) DEFAULT NULL COMMENT '产品图片',
  `product_img_sm1` varchar(50) DEFAULT NULL COMMENT '产品小图1',
  `product_img_sm2` varchar(50) DEFAULT NULL COMMENT '产品小图2',
  `product_img_sm3` varchar(50) DEFAULT NULL COMMENT '产品小图3',
  `shop_id` int(11) DEFAULT NULL COMMENT '店铺编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人编号',
  `related_product_ids` varchar(255) DEFAULT NULL COMMENT '关联产品编号',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='产品';

#
# Data for table "lp_product"
#

INSERT INTO `lp_product` VALUES (2,'Dining-hall Leds','No description',0.00,NULL,NULL,'Dining-hall','normal',NULL,'20*20',2000.00,18.00,2.00,'Apple','55a3a8fcb2ab2.jpg','55a3a900c96a9.png','55a071ee5503d.jpg','55a071f1cf8b8.jpg',NULL,NULL,NULL,NULL),(3,'Living Room Leds','Good Leds',5.00,NULL,NULL,'Living Room','normal',NULL,'30*30',2000.00,18.00,3.00,'SUNSANG','55a073a8d2ffa.jpg','55a073ac5d680.jpg','55a073b0e3d89.jpg','55a073b48725b.jpg',NULL,NULL,NULL,NULL);

#
# Source for table "lp_project"
#

DROP TABLE IF EXISTS `lp_project`;
CREATE TABLE `lp_project` (
  `project_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目编号',
  `project_name` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `project_img` varchar(50) DEFAULT NULL COMMENT '项目图片',
  `total_catg` int(5) DEFAULT NULL COMMENT '种类总数',
  `total_num` int(11) DEFAULT NULL COMMENT '总数量',
  `total_watt` float(10,2) DEFAULT '0.00' COMMENT '总功率',
  `total_tco` float(7,2) DEFAULT '0.00' COMMENT '总成本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人编号',
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目';

#
# Data for table "lp_project"
#


#
# Source for table "lp_subfolder"
#

DROP TABLE IF EXISTS `lp_subfolder`;
CREATE TABLE `lp_subfolder` (
  `subfolder_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件夹编号',
  `project_id` int(11) DEFAULT NULL COMMENT '项目编号',
  `subfolder_name` varchar(100) DEFAULT NULL COMMENT '文件夹名称',
  `subfolder_parent_id` int(11) DEFAULT NULL COMMENT '文件夹父编号',
  `subfolder_note` varchar(255) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`subfolder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目文件夹';

#
# Data for table "lp_subfolder"
#


#
# Source for table "lp_subfolder_detail"
#

DROP TABLE IF EXISTS `lp_subfolder_detail`;
CREATE TABLE `lp_subfolder_detail` (
  `subfolder_detail_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件详情编号',
  `subfolder_id` int(11) DEFAULT NULL COMMENT '文件夹编号',
  `product_id` int(11) DEFAULT NULL COMMENT '产品编号',
  `product_num` int(5) DEFAULT NULL COMMENT '产品数量',
  PRIMARY KEY (`subfolder_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件夹详情';

#
# Data for table "lp_subfolder_detail"
#


#
# Source for table "misp_button"
#

DROP TABLE IF EXISTS `misp_button`;
CREATE TABLE `misp_button` (
  `button_id` varchar(255) NOT NULL DEFAULT '' COMMENT '按钮编号，由代码生成的UUID',
  `menu_id` varchar(255) DEFAULT NULL COMMENT '按钮对应的菜单编号',
  `button_name` varchar(255) DEFAULT NULL COMMENT '按钮名称',
  `button_value` varchar(255) DEFAULT NULL COMMENT '按钮显示值',
  `button_type` varchar(255) DEFAULT NULL COMMENT '按钮类型',
  `button_css` varchar(255) DEFAULT NULL COMMENT '按钮样式',
  `button_icon` varchar(255) DEFAULT NULL COMMENT '按钮图标',
  `button_method` varchar(255) DEFAULT NULL COMMENT '按钮点击方法',
  `button_url` varchar(255) DEFAULT NULL COMMENT '按钮url',
  PRIMARY KEY (`button_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "misp_button"
#


#
# Source for table "misp_client_version"
#

DROP TABLE IF EXISTS `misp_client_version`;
CREATE TABLE `misp_client_version` (
  `version_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '版本编号',
  `company_id` int(11) DEFAULT NULL COMMENT '公司编号',
  `app_name` varchar(50) DEFAULT NULL COMMENT '客户端名称',
  `apk_name` varchar(50) DEFAULT NULL COMMENT '安装包名称',
  `version_name` varchar(50) DEFAULT NULL COMMENT '版本名称',
  `version_code` int(11) DEFAULT NULL COMMENT '版本号',
  `client_type` varchar(20) DEFAULT NULL COMMENT '客户端类型',
  `apk_url` varchar(255) DEFAULT NULL COMMENT '安装包下载地址',
  `qr_code` varchar(50) DEFAULT NULL COMMENT '安装包二维码图片',
  `version_status` varchar(20) DEFAULT NULL COMMENT '版本状态',
  PRIMARY KEY (`version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "misp_client_version"
#


#
# Source for table "misp_company"
#

DROP TABLE IF EXISTS `misp_company`;
CREATE TABLE `misp_company` (
  `company_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公司编号',
  `company_name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `company_web_site` varchar(50) DEFAULT NULL COMMENT '公司网站',
  `company_addr` varchar(50) DEFAULT NULL COMMENT '公司地址',
  `company_desp` varchar(255) DEFAULT NULL COMMENT '公司描述',
  `service_phone` varchar(20) DEFAULT NULL COMMENT '客服电话',
  `alipay_seller` varchar(50) DEFAULT NULL COMMENT '支付宝账号',
  `alipay_partner` varchar(50) DEFAULT NULL COMMENT '合作者ID',
  `alipay_private_key` varchar(255) DEFAULT NULL COMMENT '商家私钥',
  `company_status` varchar(50) DEFAULT NULL COMMENT '商家状态',
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "misp_company"
#


#
# Source for table "misp_enum"
#

DROP TABLE IF EXISTS `misp_enum`;
CREATE TABLE `misp_enum` (
  `enum_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '常量编号',
  `table_name` varchar(255) DEFAULT NULL COMMENT '表名称',
  `field_name` varchar(255) DEFAULT NULL COMMENT '字段名称',
  `enum_sort_id` int(11) DEFAULT NULL COMMENT '排序编号',
  `enum_key` int(11) DEFAULT NULL COMMENT '常量',
  `enum_value` varchar(255) DEFAULT NULL COMMENT '常量对应的值',
  `company_id` int(11) DEFAULT NULL COMMENT '公司编号',
  PRIMARY KEY (`enum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

#
# Data for table "misp_enum"
#


#
# Source for table "misp_menu"
#

DROP TABLE IF EXISTS `misp_menu`;
CREATE TABLE `misp_menu` (
  `menu_id` varchar(255) NOT NULL DEFAULT '' COMMENT '菜单编号',
  `menu_name` varchar(20) DEFAULT NULL COMMENT '菜单名称',
  `menu_value` varchar(20) DEFAULT NULL COMMENT '菜单显示值',
  `menu_type` varchar(20) DEFAULT NULL COMMENT '菜单类型',
  `menu_icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `menu_external` varchar(50) DEFAULT NULL COMMENT '菜单扩展',
  `menu_url` varchar(50) DEFAULT NULL COMMENT '菜单url',
  `menu_parent_id` varchar(11) DEFAULT NULL COMMENT '父节点编号',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "misp_menu"
#

INSERT INTO `misp_menu` VALUES ('1000','SystemManage','系统管理',NULL,NULL,NULL,'#','0'),('1010','UserManage','用户管理',NULL,'icon-user',NULL,'User/Index.html','1000'),('2000','BusinessManage','BusinessManage',NULL,NULL,NULL,'#','0'),('2010','ProductManage','ProductManage',NULL,'icon-project',NULL,'Product/Index.html','2000');

#
# Source for table "misp_operate_log"
#

DROP TABLE IF EXISTS `misp_operate_log`;
CREATE TABLE `misp_operate_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作日志编号',
  `user_name` varchar(50) DEFAULT NULL COMMENT '操作者账号',
  `log_name` varchar(20) DEFAULT NULL COMMENT '操作名称',
  `obj_name` varchar(20) DEFAULT NULL COMMENT '操作对象名称',
  `result` varchar(20) DEFAULT NULL COMMENT '操作结果',
  `oper_desp` varchar(50) DEFAULT NULL COMMENT '操作描述',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "misp_operate_log"
#


#
# Source for table "misp_privilege"
#

DROP TABLE IF EXISTS `misp_privilege`;
CREATE TABLE `misp_privilege` (
  `privilege_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限编号',
  `master_type` varchar(20) DEFAULT NULL COMMENT '权限主体类型',
  `master_value` varchar(50) DEFAULT NULL COMMENT '权限主题编号',
  `access_obj_type` varchar(20) DEFAULT NULL COMMENT '权限访问对象类型',
  `access_obj_value` varchar(50) DEFAULT NULL COMMENT '权限访问对象编号',
  `operation` varchar(50) DEFAULT NULL COMMENT '操作',
  PRIMARY KEY (`privilege_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

#
# Data for table "misp_privilege"
#

INSERT INTO `misp_privilege` VALUES (1,'ROLE','1','LOGIN','1',NULL),(2,'ROLE','1','MENU','2000',NULL),(3,'ROLE','1','MENU','2010',NULL);

#
# Source for table "misp_role"
#

DROP TABLE IF EXISTS `misp_role`;
CREATE TABLE `misp_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `role_type_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色类型编号',
  `role_type_name` varchar(255) DEFAULT NULL COMMENT '角色类型名称',
  `org_code` varchar(255) DEFAULT NULL COMMENT '组织机构编号',
  `company_id` int(11) NOT NULL DEFAULT '0' COMMENT '公司编号',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Data for table "misp_role"
#


#
# Source for table "misp_system_id_type"
#

DROP TABLE IF EXISTS `misp_system_id_type`;
CREATE TABLE `misp_system_id_type` (
  `name` varchar(10) NOT NULL DEFAULT '' COMMENT '名称',
  `step` int(11) DEFAULT '1' COMMENT '每次变化步长',
  `inc_mode` int(11) DEFAULT '1' COMMENT '增减模式',
  `length` int(11) DEFAULT '10' COMMENT 'ID长度',
  `current_id` int(11) DEFAULT '1' COMMENT '当前使用ID',
  `prefix` varchar(10) DEFAULT NULL COMMENT 'ID前缀',
  `suffix` varchar(10) DEFAULT NULL COMMENT 'ID后缀',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "misp_system_id_type"
#


#
# Source for table "misp_token"
#

DROP TABLE IF EXISTS `misp_token`;
CREATE TABLE `misp_token` (
  `token_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'token编号,由代码生成36位的UUID',
  `obj_id` varchar(255) DEFAULT NULL COMMENT '对象ID',
  `token_type` varchar(255) DEFAULT NULL COMMENT '对象类型，用户或者子系统',
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "misp_token"
#


#
# Source for table "misp_user"
#

DROP TABLE IF EXISTS `misp_user`;
CREATE TABLE `misp_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户账号',
  `password` varchar(50) DEFAULT NULL COMMENT '用户密码',
  `reg_date` datetime DEFAULT NULL COMMENT '用户注册时间',
  `role_id` int(11) DEFAULT NULL COMMENT '用户角色编号',
  `org_code` varchar(255) DEFAULT NULL COMMENT '组织机构编号',
  `company_id` int(11) DEFAULT NULL COMMENT '公司编号',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

#
# Data for table "misp_user"
#

INSERT INTO `misp_user` VALUES (1,'admin','123456','2015-05-08 00:00:00',1,'0',0);

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
