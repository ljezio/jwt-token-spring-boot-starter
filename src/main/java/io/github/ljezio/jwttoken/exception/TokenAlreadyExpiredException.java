package io.github.ljezio.jwttoken.exception;

public class TokenAlreadyExpiredException extends Exception {

    public TokenAlreadyExpiredException(Throwable cause) {
        super("token already expired", cause);
    }

}
