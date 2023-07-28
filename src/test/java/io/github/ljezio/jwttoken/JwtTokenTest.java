package io.github.ljezio.jwttoken;

import io.github.ljezio.jwttoken.common.BeanContent;
import io.github.ljezio.jwttoken.configuration.JwtTokenAutoConfiguration;
import io.github.ljezio.jwttoken.exception.TokenExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifyFailedException;
import io.github.ljezio.jwttoken.interceptor.TokenCheckInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@SpringBootTest(
        properties = "jwt-token.interception.enable=true",
        classes = {JwtTokenAutoConfiguration.class}
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtTokenTest {

    private static final Payload payload = new Payload(1234, "ezio");
    private static Token token;

    @Test
    @Order(1)
    public void create() {
        token = JwtToken.create(payload);
        System.out.println("now:\n" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("accessToken:\n" + token.accessToken());
        System.out.println("refreshToken:\n" + token.refreshToken());
        Assertions.assertNotNull(token.accessToken());
        Assertions.assertNotNull(token.refreshToken());
    }

    @Test
    @Order(10)
    public void decode() throws TokenVerifyFailedException, TokenExpiredException {
        Payload decodePayload = JwtToken.decode(token.accessToken(), Payload.class);
        Assertions.assertEquals(payload, decodePayload);
    }

    @Test
    @Order(10)
    public void refresh() throws TokenVerifyFailedException, TokenExpiredException, InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        Token refreshedToken = JwtToken.refresh(token);
        System.out.println("refreshedAccessToken:\n" + refreshedToken.accessToken());
        Assertions.assertNotEquals(token, refreshedToken);
    }

    @Test
    @Order(10)
    public void interceptor() {
        String headerValue = BeanContent.interceptorProp.getHeaderValuePrefix() + token.accessToken();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(BeanContent.interceptorProp.getHeader(), headerValue);
        TokenCheckInterceptor interceptor = new TokenCheckInterceptor(BeanContent.interceptorProp);
        boolean result = interceptor.preHandle(request, new MockHttpServletResponse(), null);
        Assertions.assertTrue(result);
    }

    @Test
    @Order(10)
    public void payloadHolder() {
        String headerValue = BeanContent.interceptorProp.getHeaderValuePrefix() + token.accessToken();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(BeanContent.interceptorProp.getHeader(), headerValue);
        TokenCheckInterceptor interceptor = new TokenCheckInterceptor(BeanContent.interceptorProp);
        interceptor.preHandle(request, new MockHttpServletResponse(), null);
        Assertions.assertEquals(payload, PayloadHolder.get(Payload.class));
        interceptor.afterCompletion(null, null, null, null);
        Assertions.assertNull(PayloadHolder.get(Payload.class));
    }

}
