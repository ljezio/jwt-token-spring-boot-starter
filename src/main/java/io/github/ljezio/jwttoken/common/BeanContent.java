package io.github.ljezio.jwttoken.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ljezio.jwttoken.configuration.InterceptorProperties;
import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import org.springframework.stereotype.Component;

@Component
public class BeanContent {

    public BeanContent(ObjectMapper objectMapper, JwtTokenProperties jwtTokenProp, InterceptorProperties interceptorProp) {
        BeanContent.objectMapper = objectMapper;
        BeanContent.jwtTokenProp = jwtTokenProp;
        BeanContent.interceptorProp = interceptorProp;
    }

    public static ObjectMapper objectMapper;

    public static JwtTokenProperties jwtTokenProp;

    public static InterceptorProperties interceptorProp;
}
