### JWT TOKEN
开箱即用的jwt token生成、校验、刷新和**接口token检查**

#### 引入

```xml
<dependency>
    <groupId>io.github.ljezio</groupId>
    <artifactId>jwt-token-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

#### 使用

```java
// 生成一个token
Token create(T payload)
// 从token中取出负载
T decode(String token, Class<T> clazz) throws TokenVerifierFailException, TokenAlreadyExpiredException
// 刷新token
Token refresh(Token oldToken) throws TokenVerifierFailException, TokenAlreadyExpiredException
Token refresh(String refreshToken) throws TokenVerifierFailException, TokenAlreadyExpiredException
```

#### 配置

```yaml
jwt-token:
  secret: 1234
  access-token-expire-minutes: 30
  refresh-token-expire-days: 180
  interception:
    enable: true
    exclude-path:
      - /register
      - /login
```

| 配置                                                 | 说明                        | 类型    | 默认值                                                       |
| ---------------------------------------------------- | --------------------------- | ------- | ------------------------------------------------------------ |
| jwt-token.secret                                     | 密钥                        | String  | 1234ezio                                                     |
| jwt-token.access-token-expire-minutes                | accessToken过期时间（分钟） | int     | 30                                                           |
| jwt-token.refresh-token-expire-days                  | refreshToken过期时间（天）  | int     | 180                                                          |
| jwt-token.show-banner                                | 是否显示banner图            | boolean | true                                                         |
| jwt-token.interception.enable                        | 是否启用拦截器              | boolean | false                                                        |
| jwt-token.interception.header-name                   | 安全请求头key               | String  | Authorization                                                |
| jwt-token.interception.order                         | 拦截器执行顺序              | int     | -1000                                                        |
| jwt-token.interception.path                          | 需要检查token的url          | List    | /**                                                          |
| jwt-token.interception.exclude-path                  | 不需要检查token的url        | List    |                                                              |
| jwt-token.interception.check-fail-json.expired       | token过期返回json           | String  | {<br/>  "code": 452,<br/>  "msg": "token已过期",<br/>  "data": null<br/>} |
| jwt-token.interception.check-fail-json.verifier-fail | token校验失败返回json       |         | {<br/>  "code": 453,<br/>  "msg": "token无效",<br/>  "data": null<br/>} |

#### 拦截器

`jwt-token.interception.enable`设为`true`时启用拦截器，token校验不通过时接口自动返回错误信息
