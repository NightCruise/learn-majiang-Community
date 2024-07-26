# 学习码匠社区

## p1-p4
### 创建 Spring Boot 项目
导包：
~~~maven
<!-- spring boot starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<!-- Thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
~~~
按照教程https://spring.io/guides/gs/serving-web-content 写出 Spring Boot 项目入门的案例

创建Github repository：

我创建的仓库名字是 **learn-majiang-Community**，创建完成后会出现一个Quick setup：

![image-20240726235820945](README.assets/image-20240726235820945.png)

初始化Git仓库：在IDEA相应文件夹点击 open in terminal，输入`git init`

生成.gitignore文件插件：

![image-20240727001043322](README.assets/image-20240727001043322.png)

![image-20240727001227882](README.assets/image-20240727001227882.png)

添加所有文件的更改：在 terminal 输入 `git add .`

按照之前图片里提出的Quick Setup设置一下远程仓库，然后创建README.md，然后push上去。
