package io.github.ljezio.jwttoken.exception;

public class TokenVerifyFailedException extends Exception {

    public TokenVerifyFailedException(Throwable cause) {
        super("token verifier failed", cause);
    }
}
