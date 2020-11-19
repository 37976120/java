###航通星空公车系统


| 姓名                 | 角色 | 时间       |
| -------------------- | ---- | ---------- |
| imguava@163.com      | 开发  | 2020-06-02 |
|                      |      |            |
|                      |      |            |

```
项目根目录
ovms
├── ovms-api-gateway -- api网关[9999]
├── ovms-auth -- 授权服务提供[3000]
├── ovms-client -- feign 客户端（微服务之间）
├    ├── ovms-device-client -- 设备服务client
├    ├── ovms-enterprise-client -- 企业服务client
├    ├── ovms-job-client -- 分布式定时任务client
├    ├── ovms-msg-client -- 消息服务client
├    ├── ovms-report-client -- 报表服务client
├    └── ovms-platform-client -- 平台服务client
├── ovms-common -- 系统公共模块 
├    ├── ovms-common-bom -- 公共依赖版本
├    ├── ovms-common-core -- 公共工具类核心包
├    ├── ovms-common-data -- 数据相关
├    ├── ovms-common-datasource -- 动态数据源相关
├    ├── ovms-common-job -- 定时任务
├    ├── ovms-common-gateway -- 动态路由定义
├    ├── ovms-common-log -- 日志服务
├    ├── ovms-common-minio -- 文件系统
├    └── ovms-common-security -- 安全工具类
├    └── ovms-common-sequence -- 全局发号器
├    └── ovms-common-swagger -- Swagger Api文档生成
├    └── ovms-common-transaction -- 分布式事务工具包
├── ovms-device-gateway -- 设备服务网关[10010]
├── ovms-register -- 注册中心、配置中心[8848]
├── ovms-sevice -- 业务微服务
├    ├── ovms-device-sevice -- 设备服务
├    ├── ovms-enterprise-sevice -- 企业服务
├    ├── ovms-platform-sevice -- 平台服务
├    ├── ovms-msg-sevice -- 消息服务
├    ├── ovms-job-sevice -- 分布式定时任务服务
├    └── ovms-report-sevice -- 报表服务
├── ovms-visual  -- 图形化模块 
├    ├── ovms-monitor -- Spring Boot Admin监控 [5001]
├    ├── ovms-xxl-job-admin -- 分布式调度中心管控台[5050]-->自集成
├    ├── ovms-daemon-quartz -- 分布式调度中心[quartz]
├    ├── ovms-code-gen -- 图形化代码生成[5003]
├    ├── ovms-sso-client-demo -- sso 客户端接入示例
├    ├── ovms-mp-manager -- 微信管理模块
├    ├── ovms-pay -- 微信支付宝收单模块
├    ├── ovms-tx-manager -- ovms分布式事务解决方案[5004]
├    └── ovms-activiti -- 工作流模块[5005]
```


