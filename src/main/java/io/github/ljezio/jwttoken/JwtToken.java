package io.github.ljezio.jwttoken;

import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;
import io.github.ljezio.jwttoken.utils.JwtUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtToken {

    public static <T> Token create(T payload) {
        String accessToken = JwtUtil.create(payload, JwtTokenProperties.accessTokenExpireMinutes);
        String refreshToken = JwtUtil.create(payload, JwtTokenProperties.refreshTokenExpireDays);
        return new Token(accessToken, refreshToken);
    }

    public static <T> T decode(String token, Class<T> clazz) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        return JwtUtil.decode(token, clazz);
    }

    public static Token refresh(Token oldToken) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        return refresh(oldToken.refreshToken());
    }

    public static Token refresh(String refreshToken) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        String payload = JwtUtil.verify(refreshToken).getPayload();
        String jsonStr = new String(Base64.getDecoder().decode(payload.getBytes(StandardCharsets.UTF_8)));
        String accessToken = JwtUtil.create(jsonStr, JwtTokenProperties.accessTokenExpireMinutes);
        return new Token(accessToken, refreshToken);
    }

}
