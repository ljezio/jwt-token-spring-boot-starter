package io.github.ljezio.jwttoken.pojo;

public record Token(
        String accessToken,
        String refreshToken
) {
}
