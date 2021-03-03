这是一个基于自己封装的简易orm框架和简易web框架实现的网上书城项目及后台管理系统(Maven项目)
使用到的知识
前端:html  css  js  Ajax等等
后端:集合  IO  反射  注解  异常  枚举  JSON  cookie等等

请求全部使用Ajax技术  通过JS发送异步请求(其中有上传文件  我们使用Ajax的form-data方式发送文件)  
后端返回的响应信息全部转换成JSON发送到浏览器
JS得到后端JSON形式的响应信息  解析响应信息并最终展示在浏览器
使用cookie及拦截器实现了用户成功登录一次后免登录功能

此项目使用jetty服务器作为web服务器
使用jetty服务器的原因 
因为jetty服务器是轻量级的服务器  相比于tomcat来说  测试速度更快
所以我们测试项目一般使用jetty服务器

为了简化项目的操作  我们自己封装了另外一套简易orm框架和简易web框架
没有使用SSM框架  而使用自己封装的小框架  
因为使用自己封装的小框架才能更好的理解和把握整个项目体系
也能够对自己封装的框架查缺补漏

orm框架实现

封装了JDBC的必要步骤
提供三个注解
@Table("")  domain对象数据库表的关系
@Column("")  domain属性与数据库表中字段名的对应关系
@ID  标注这个属性对应表中的主键
DBUtil中对增删改查提供多种重载方法  保证数据库操作的各种情况
用户只需输入domain的类对象以及sql中的必要条件
框架会自动解析domain中的注解  并根据条件拼接出正确的sql语句并执行
也会将最终的结果(增删改  查询)包装成对应的类型(例如对象  集合等)返回给用户

web框架实现

提供两个注解
@Controller  标识这个类是一个Controller类
@Path("请求名")  放在Controller方法上  装载请求与方法的对应关系

保留了request对象  response对象  异常等
ControllerManager这个类使用反射解析注解
找到请求对应的方法  并让此方法执行
ResponseUtil这个类并对响应信息做了处理  全部转换成JSON类型
并使用枚举类对响应信息状态做了标识

此框架的封装避免了我们为每一个请求配置web.xml文件
并且避免了我们处理响应信息


项目需求分析(实现的功能)

网上书城系统

1.用户登录注册(使用cookie及拦截器实现用户免登录)
2.首页商品展示 
3.添加商品到购物车
4.查看购物车商品
5.下订单
6.收藏商品
7.查看收藏商品
8.查看浏览记录
9.询问客服  聊天

后台管理系统

1.管理员的登录注册(使用cookie及拦截器实现用户免登录)
2.录入商品  查询商品  删除商品  修改商品内容
3.录入商品类别  查询类别  删除类别  修改类别名称
4.查看订单  修改订单状态  处理订单
5.多用户同时在线的客服(可能带有机器人自动回复)

构建数据库

```sql
create database panda default character set = 'utf8';
```

管理员表

```sql
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(64) NOT NULL COMMENT '账号',
  `password` varchar(128) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

用户表

```sql
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键\n',
  `account` varchar(64) NOT NULL COMMENT '账户',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `email` varchar(64) NOT NULL COMMENT '邮箱',
  `address` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

商品表

```sql
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '书名',
  `author` varchar(32) NOT NULL COMMENT '作者',
  `count` int(11) NOT NULL COMMENT '库存',
  `price` double(4,2) NOT NULL,
  `detail` longtext NOT NULL,
  `cime` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

标签表

```sql
#标签表
CREATE TABLE `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `ctime` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



商品标签的关系映射表(多对多)

```sql
CREATE TABLE `book_tags_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) NOT NULL,
  `tags_id` int(11) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



收藏表

```sql
CREATE TABLE `favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



浏览记录表

```sql
CREATE TABLE `view_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



订单表

```sql
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_num` varchar(256) NOT NULL,
  `total_price` double(6,2) NOT NULL,
  `user_id` int(11) NOT NULL,
  `address` varchar(128) NOT NULL,
  `status` int(11) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



商品订单关系映射表(虽然订单与商品是一对多关系  我们最初想到多端设置外键  但是多端独立字段过多  需要增加关系映射表)

```sql
CREATE TABLE `order_book_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  `price` double(6,2) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



购物车表

```sql
CREATE TABLE `cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



评价表(及回复评价)

```sql
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) NOT NULL,
  `star` int(11) NOT NULL,
  `content` varchar(256) NOT NULL,
  `reply` varchar(256) NOT NULL COMMENT '回复',
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



#用户反馈表

```sql
CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `email` varchar(64) NOT NULL,
  `content` varchar(256) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



聊天记录表

```sql
CREATE TABLE `panda`.`chat_record` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `content` VARCHAR(256) NOT NULL,
  `from` INT NOT NULL,
  `ctime` INT NOT NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



#正在聊天

```sql
CREATE TABLE `now_chat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ctime` int(11) NOT NULL,
  `utime` int(11) NOT NULL,
  `sign` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```






