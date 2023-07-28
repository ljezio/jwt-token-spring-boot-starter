package io.github.ljezio.jwttoken.common;

import com.auth0.jwt.algorithms.Algorithm;

public enum AlgorithmEnum {
    HMAC256, HMAC384, HMAC512;

    public Algorithm instance(String secret) {
        return switch (this) {
            case HMAC256 -> Algorithm.HMAC256(secret);
            case HMAC384 -> Algorithm.HMAC384(secret);
            case HMAC512 -> Algorithm.HMAC512(secret);
        };
    }
}
