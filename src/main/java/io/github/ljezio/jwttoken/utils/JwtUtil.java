package io.github.ljezio.jwttoken.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class JwtUtil {

    public static <T> String create(T payload, long expire, ChronoUnit chronoUnit) {
        return JWT.create()
                .withPayload(ObjectMapUtil.objectToMap(payload))
                .withExpiresAt(OffsetDateTime.now().plus(expire, chronoUnit).toInstant())
                .sign(JwtTokenProperties.algorithm.getJwtAlgorithm(JwtTokenProperties.secret));
    }

    public static String create(String json, long expire, ChronoUnit chronoUnit) {
        return JWT.create()
                .withPayload(json)
                .withExpiresAt(OffsetDateTime.now().plus(expire, chronoUnit).toInstant())
                .sign(JwtTokenProperties.algorithm.getJwtAlgorithm(JwtTokenProperties.secret));
    }

    public static DecodedJWT verify(String token) throws TokenAlreadyExpiredException, TokenVerifierFailException {
        try {
            return JWT.require(JwtTokenProperties.algorithm.getJwtAlgorithm(JwtTokenProperties.secret)).build().verify(token);
        } catch (TokenExpiredException e) {
            throw new TokenAlreadyExpiredException(e);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw new TokenVerifierFailException(e);
        }
    }

    public static <T> T decode(String token, Class<T> clazz) throws TokenAlreadyExpiredException, TokenVerifierFailException {
        Map<String, Claim> claims = verify(token).getClaims();
        return ObjectMapUtil.claimMapToObject(claims, clazz);
    }
}
