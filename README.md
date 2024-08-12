# spring-cloud-demo

## 技术栈
* dubbo
* nacos
* seata
* sentinel
* spring-cloud
* zipkin

## nacos
http://127.0.0.1:81
<br>
nacos/nacos

## seata-server
http://127.0.0.1:7091
<br>
seata/seata

## sentinel-dashboard
http://127.0.0.1:8084
<br>
sentinel/sentinel

## zipkin-server
http://127.0.0.1:9411

### seata.properties配置文件说明
Data ID: seata.properties
<br>
Group: SEATA_GROUP

## local run
1. 编译 dubbo-spring-boot-observability-autoconfigure 模块<br>
```bash
cd dubbo-spring-boot-observability-autoconfigure
mvn clean compile
```
2. 修改启动类<br>
Modify options -> Modify classpath -> +<br>
Include&nbsp;&nbsp;&nbsp;&nbsp;[dubbo-spring-boot-observability-autoconfigure/target/classes](./dubbo-spring-boot-observability-autoconfigure/target/classes)