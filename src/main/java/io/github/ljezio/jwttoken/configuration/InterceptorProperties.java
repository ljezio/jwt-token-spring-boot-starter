package io.github.ljezio.jwttoken.configuration;

import io.github.ljezio.jwttoken.common.Constant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = Constant.INTERCEPTION_PROPERTIES_PREFIX)
public class InterceptorProperties {

    /**
     * 是否启用拦截器
     */
    private boolean enable = false;

    /**
     * 拦截器执行顺序
     */
    private int order = -1000;

    /**
     * 安全请求头key
     */
    private String header = "Authorization";

    /**
     * 安全请求头value前缀
     */
    private String headerValuePrefix = "Bearer ";

    /**
     * 需要检查token的url
     */
    private List<String> path = List.of("/**");

    /**
     * 不需要检查token的url
     */
    private List<String> excludePath;

    /**
     * token检查失败返回json
     */
    @NestedConfigurationProperty
    private CheckFailJson checkFailJson = new CheckFailJson();

    @Getter
    @Setter
    public static class CheckFailJson {
        /**
         * token过期返回json
         */
        private String expired = "{\"code\":452,\"msg\":\"token已过期\",\"data\":null}";

        /**
         * token校验失败返回json
         */
        private String verifierFail = "{\"code\":453,\"msg\":\"token无效\",\"data\":null}";
    }
}
