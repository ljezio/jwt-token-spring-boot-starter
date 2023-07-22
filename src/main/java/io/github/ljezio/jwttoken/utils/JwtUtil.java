package io.github.ljezio.jwttoken.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.ljezio.jwttoken.configuration.JwtTokenProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;

import java.time.OffsetDateTime;
import java.util.Map;

public class JwtUtil {

    public static <T> String create(T payload, long expire) {
        return JWT.create()
                .withPayload(ObjectMapUtil.objectToMap(payload))
                .withExpiresAt(OffsetDateTime.now().plusMinutes(expire).toInstant())
                .sign(Algorithm.HMAC256(JwtTokenProperties.secret));
    }

    public static String create(String payloadClaimsJson, long expire) {
        return JWT.create()
                .withPayload(payloadClaimsJson)
                .withExpiresAt(OffsetDateTime.now().plusMinutes(expire).toInstant())
                .sign(Algorithm.HMAC256(JwtTokenProperties.secret));
    }

    public static DecodedJWT verify(String token) throws TokenAlreadyExpiredException, TokenVerifierFailException {
        try {
            return JWT.require(Algorithm.HMAC256(JwtTokenProperties.secret)).build().verify(token);
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
