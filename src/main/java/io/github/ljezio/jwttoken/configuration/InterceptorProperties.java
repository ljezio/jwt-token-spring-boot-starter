package io.github.ljezio.jwttoken.configuration;

import io.github.ljezio.jwttoken.common.Constant;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = Constant.INTERCEPTION_PROPERTIES_PREFIX)
public class InterceptorProperties {

    /**
     * 是否启用拦截器
     */
    public static boolean enable = false;

    /**
     * 拦截器执行顺序
     */
    public static int order = -1000;

    /**
     * 安全请求头key
     */
    public static String header = "Authorization";

    /**
     * 安全请求头value前缀
     */
    public static String headerValuePrefix = "Bearer ";

    /**
     * 需要检查token的url
     */
    public static List<String> path = List.of("/**");

    /**
     * 不需要检查token的url
     */
    public static List<String> excludePath;

    /**
     * token检查失败返回json
     */
    public static CheckFailJson checkFailJson = new CheckFailJson();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        InterceptorProperties.enable = enable;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        InterceptorProperties.order = order;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        InterceptorProperties.header = header;
    }

    public String getHeaderValuePrefix() {
        return headerValuePrefix;
    }

    public void setHeaderValuePrefix(String headerValuePrefix) {
        InterceptorProperties.headerValuePrefix = headerValuePrefix;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        InterceptorProperties.path = path;
    }

    public List<String> getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(List<String> excludePath) {
        InterceptorProperties.excludePath = excludePath;
    }

    public CheckFailJson getCheckFailJson() {
        return checkFailJson;
    }

    public void setCheckFailJson(CheckFailJson checkFailJson) {
        InterceptorProperties.checkFailJson = checkFailJson;
    }

    public static class CheckFailJson {
        /**
         * token过期返回json
         */
        public static String expired = "{\"code\":452,\"msg\":\"token已过期\",\"data\":null}";

        /**
         * token校验失败返回json
         */
        public static String verifierFail = "{\"code\":453,\"msg\":\"token无效\",\"data\":null}";

        public String getExpired() {
            return expired;
        }

        public void setExpired(String expired) {
            CheckFailJson.expired = expired;
        }

        public String getVerifierFail() {
            return verifierFail;
        }

        public void setVerifierFail(String verifierFail) {
            CheckFailJson.verifierFail = verifierFail;
        }
    }

}
