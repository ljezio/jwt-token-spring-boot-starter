package io.github.ljezio.jwttoken.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.ljezio.jwttoken.common.BeanContent;
import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import io.github.ljezio.jwttoken.exception.TokenExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifyFailedException;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class JwtUtil {

    private static final JwtTokenProperties jwtTokenProp = BeanContent.jwtTokenProp;

    public static <T> String create(T payload, long expire, ChronoUnit chronoUnit) {
        return create(JsonUtil.toJson(payload), expire, chronoUnit);
    }

    public static String create(String json, long expire, ChronoUnit chronoUnit) {
        try {
            return JWT.create()
                    .withPayload(json)
                    .withExpiresAt(OffsetDateTime.now().plus(expire, chronoUnit).toInstant())
                    .sign(jwtTokenProp.getAlgorithm().instance(jwtTokenProp.getSecret()));
        } catch (JWTCreationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T decode(String token, Class<T> clazz) throws TokenExpiredException, TokenVerifyFailedException {
        return JsonUtil.toBean(decode(token), clazz);
    }

    public static String decode(String token) throws TokenExpiredException, TokenVerifyFailedException {
        String payload = JwtUtil.verify(token).getPayload();
        return new String(Base64.getDecoder().decode(payload.getBytes(StandardCharsets.UTF_8)));
    }

    public static DecodedJWT verify(String token) throws TokenExpiredException, TokenVerifyFailedException {
        try {
            return JWT.require(jwtTokenProp.getAlgorithm().instance(jwtTokenProp.getSecret())).build().verify(token);
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException(e);
        } catch (JWTVerificationException e) {
            throw new TokenVerifyFailedException(e);
        }
    }
}
