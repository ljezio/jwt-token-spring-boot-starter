package io.github.ljezio.jwttoken;

import io.github.ljezio.jwttoken.utils.JsonUtil;
import io.github.ljezio.jwttoken.utils.StringUtil;

public class PayloadHolder {

    private static final ThreadLocal<String> payloadThreadLocal = new ThreadLocal<>();

    public static void set(String payloadJsonStr) {
        payloadThreadLocal.set(payloadJsonStr);
    }

    /**
     * 获取上下文的token负载
     *
     * @param clazz token负载类型
     * @return token负载
     */
    public static <T> T get(Class<T> clazz) {
        String payloadJson = payloadThreadLocal.get();
        if (StringUtil.isEmpty(payloadJson)) {
            return null;
        }
        return JsonUtil.toBean(payloadJson, clazz);
    }

    public static void remove() {
        payloadThreadLocal.remove();
    }

}
