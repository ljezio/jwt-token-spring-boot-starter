package io.github.ljezio.jwttoken;

import io.github.ljezio.jwttoken.common.BeanContent;
import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;
import io.github.ljezio.jwttoken.utils.JwtUtil;

import java.time.temporal.ChronoUnit;

public class JwtToken {

    private static final JwtTokenProperties jwtTokenProp = BeanContent.jwtTokenProp;

    public static <T> Token create(T payload) {
        String accessToken = JwtUtil.create(payload, jwtTokenProp.getAccessTokenExpireMinutes(), ChronoUnit.MINUTES);
        String refreshToken = JwtUtil.create(payload, jwtTokenProp.getRefreshTokenExpireDays(), ChronoUnit.DAYS);
        return new Token(accessToken, refreshToken);
    }

    public static <T> T decode(String token, Class<T> clazz) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        return JwtUtil.decode(token, clazz);
    }

    public static Token refresh(Token oldToken) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        return refresh(oldToken.refreshToken());
    }

    public static Token refresh(String refreshToken) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        String json = JwtUtil.decode(refreshToken);
        String accessToken = JwtUtil.create(json, jwtTokenProp.getAccessTokenExpireMinutes(), ChronoUnit.MINUTES);
        return new Token(accessToken, refreshToken);
    }

}
