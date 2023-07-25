package io.github.ljezio.jwttoken;

import io.github.ljezio.jwttoken.configuration.InterceptorProperties;
import io.github.ljezio.jwttoken.configuration.JwtTokenAutoConfiguration;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;
import io.github.ljezio.jwttoken.interceptor.TokenCheckInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = JwtTokenAutoConfiguration.class, properties = "jwt-token.interception.enable=true")
class JwtTokenTest {

    private static final InterceptorProperties interceptorProp = new InterceptorProperties();
    private static final Payload payload = new Payload(1234, "ezio");
    private static Token token;

    @BeforeAll
    public static void create() {
        token = JwtToken.create(payload);
        System.out.println("now:\n" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("accessToken:\n" + token.accessToken());
        System.out.println("refreshToken:\n" + token.refreshToken());
    }

    @Test
    public void decode() throws TokenVerifierFailException, TokenAlreadyExpiredException {
        Payload decodePayload = JwtToken.decode(token.accessToken(), Payload.class);
        Assertions.assertEquals(payload, decodePayload);
    }

    @Test
    public void refresh() throws TokenVerifierFailException, TokenAlreadyExpiredException, InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        Token refreshedToken = JwtToken.refresh(token);
        System.out.println("refreshedAccessToken:\n" + refreshedToken.accessToken());
        Assertions.assertNotEquals(token, refreshedToken);
    }

    @Test
    public void interceptor() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(interceptorProp.getHeader(), interceptorProp.getHeaderValuePrefix() + token.accessToken());
        TokenCheckInterceptor interceptor = new TokenCheckInterceptor();
        boolean result = interceptor.preHandle(request, new MockHttpServletResponse(), null);
        Assertions.assertTrue(result);
    }

}
