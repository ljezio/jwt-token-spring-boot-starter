package io.github.ljezio.jwttoken.exception;

public class TokenVerifierFailException extends Exception {

    public TokenVerifierFailException(Throwable cause) {
        super("token verifier failed", cause);
    }
}
