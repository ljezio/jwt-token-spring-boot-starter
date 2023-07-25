package io.github.ljezio.jwttoken.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ljezio.jwttoken.common.BeanContent;
import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class JwtUtil {

    private static final JwtTokenProperties jwtTokenProp = BeanContent.jwtTokenProp;
    private static final ObjectMapper objectMapper = BeanContent.objectMapper;

    @SneakyThrows
    public static <T> String create(T payload, long expire, ChronoUnit chronoUnit) {
        return create(objectMapper.writeValueAsString(payload), expire, chronoUnit);
    }

    public static String create(String json, long expire, ChronoUnit chronoUnit) {
        return JWT.create()
                .withPayload(json)
                .withExpiresAt(OffsetDateTime.now().plus(expire, chronoUnit).toInstant())
                .sign(jwtTokenProp.getAlgorithm().getJwtAlgorithm(jwtTokenProp.getSecret()));
    }

    @SneakyThrows({JsonMappingException.class, JsonProcessingException.class})
    public static <T> T decode(String token, Class<T> clazz) throws TokenAlreadyExpiredException, TokenVerifierFailException {
        return objectMapper.readValue(decode(token), clazz);
    }

    public static String decode(String token) throws TokenAlreadyExpiredException, TokenVerifierFailException {
        String payload = JwtUtil.verify(token).getPayload();
        return new String(Base64.getDecoder().decode(payload.getBytes(StandardCharsets.UTF_8)));
    }

    public static DecodedJWT verify(String token) throws TokenAlreadyExpiredException, TokenVerifierFailException {
        try {
            return JWT.require(jwtTokenProp.getAlgorithm().getJwtAlgorithm(jwtTokenProp.getSecret())).build().verify(token);
        } catch (TokenExpiredException e) {
            throw new TokenAlreadyExpiredException(e);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw new TokenVerifierFailException(e);
        }
    }
}
