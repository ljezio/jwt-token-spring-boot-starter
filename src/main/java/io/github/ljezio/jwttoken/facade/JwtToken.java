package io.github.ljezio.jwttoken.facade;

import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;
import io.github.ljezio.jwttoken.pojo.Token;
import io.github.ljezio.jwttoken.utils.JwtUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtToken {

    public static <T> Token create(T payload) {
        String accessToken = JwtUtil.create(payload, JwtTokenProperties.accessTokenExpireMinutes);
        String refreshToken = JwtUtil.create(payload, JwtTokenProperties.refreshTokenExpireDays);
        return new Token(accessToken, refreshToken);
    }

    public static <T> T decode(String accessToken, Class<T> clazz) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        return JwtUtil.decode(accessToken, clazz);
    }

    public static Token refresh(String refreshToken) throws TokenVerifierFailException, TokenAlreadyExpiredException {
        String payload = JwtUtil.verify(refreshToken).getPayload();
        String payloadClaimsJson = new String(Base64.getDecoder().decode(payload.getBytes(StandardCharsets.UTF_8)));
        String accessToken = JwtUtil.create(payloadClaimsJson, JwtTokenProperties.accessTokenExpireMinutes);
        return new Token(accessToken, refreshToken);
    }

}
