package io.github.ljezio.jwttoken.exception;

public class TokenExpiredException extends Exception {

    public TokenExpiredException(Throwable cause) {
        super("token already expired", cause);
    }

}
