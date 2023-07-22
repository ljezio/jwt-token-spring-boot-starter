package io.github.ljezio.jwttoken;

public record Token(
        String accessToken,
        String refreshToken
) {
}
