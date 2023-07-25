package io.github.ljezio.jwttoken.common;

import io.github.ljezio.jwttoken.configuration.InterceptorProperties;
import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import org.springframework.stereotype.Component;

@Component
public class BeanContent {

    public BeanContent(JwtTokenProperties jwtTokenProp, InterceptorProperties interceptorProp) {
        BeanContent.jwtTokenProp = jwtTokenProp;
        BeanContent.interceptorProp = interceptorProp;
    }

    public static JwtTokenProperties jwtTokenProp;

    public static InterceptorProperties interceptorProp;
}
