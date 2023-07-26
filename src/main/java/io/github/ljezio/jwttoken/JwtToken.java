package io.github.ljezio.jwttoken;

import io.github.ljezio.jwttoken.common.BeanContent;
import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;
import io.github.ljezio.jwttoken.utils.JwtUtil;

import java.time.temporal.ChronoUnit;

public class JwtToken {

    private static final JwtTokenProperties jwtTokenProp = BeanContent.jwtTokenProp;

    /**
     * 创建一个token
     *
     * @param payload 负载
     * @return token
     */
    public static <T> Token create(T payload) {
        String accessToken = JwtUtil.create(payload, jwtTokenProp.getAccessTokenExpireMinutes(), ChronoUnit.MINUTES);
        String refreshToken = JwtUtil.create(payload, jwtTokenProp.getRefreshTokenExpireDays(), ChronoUnit.DAYS);
        return new Token(accessToken, refreshToken);
    }

    /**
     * 解析token
     *
     * @param token token
     * @param clazz 负载对象类型
     * @return 负载对象
     * @throws TokenVerifierFailException   token无效
     * @throws TokenAlreadyExpiredException token过期
     */
    public static <T> T decode(String token, Class<T> clazz) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        return JwtUtil.decode(token, clazz);
    }

    /**
     * 刷新token
     *
     * @param oldToken 原始token
     * @return 新生成的token
     * @throws TokenVerifierFailException   token无效
     * @throws TokenAlreadyExpiredException token过期
     */
    public static Token refresh(Token oldToken) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        return refresh(oldToken.refreshToken());
    }

    /**
     * 刷新token
     *
     * @param refreshToken refreshToken字符串
     * @return 新生成的token
     * @throws TokenVerifierFailException   token无效
     * @throws TokenAlreadyExpiredException token过期
     */
    public static Token refresh(String refreshToken) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        String json = JwtUtil.decode(refreshToken);
        String accessToken = JwtUtil.create(json, jwtTokenProp.getAccessTokenExpireMinutes(), ChronoUnit.MINUTES);
        return new Token(accessToken, refreshToken);
    }

}
