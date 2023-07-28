package io.github.ljezio.jwttoken.interceptor;

import io.github.ljezio.jwttoken.PayloadHolder;
import io.github.ljezio.jwttoken.configuration.InterceptorProperties;
import io.github.ljezio.jwttoken.exception.TokenExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifyFailedException;
import io.github.ljezio.jwttoken.utils.JwtUtil;
import io.github.ljezio.jwttoken.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

public class TokenCheckInterceptor implements HandlerInterceptor {

    public TokenCheckInterceptor(InterceptorProperties interceptorProp) {
        this.interceptorProp = interceptorProp;
    }

    private final InterceptorProperties interceptorProp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request);
        if (token == null) {
            returnJson(response, interceptorProp.getCheckFailJson().getVerifierFail());
            return false;
        }
        try {
            String payloadJson = JwtUtil.decode(token);
            PayloadHolder.set(payloadJson);
        } catch (TokenExpiredException e) {
            returnJson(response, interceptorProp.getCheckFailJson().getExpired());
            return false;
        } catch (TokenVerifyFailedException e) {
            returnJson(response, interceptorProp.getCheckFailJson().getVerifierFail());
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        PayloadHolder.remove();
    }

    private String extractToken(HttpServletRequest request) {
        String headerValue = request.getHeader(interceptorProp.getHeader());
        if (StringUtil.isEmpty(headerValue)) {
            return null;
        }
        if (StringUtil.isEmpty(interceptorProp.getHeaderValuePrefix())) {
            return headerValue;
        }
        if (!headerValue.startsWith(interceptorProp.getHeaderValuePrefix())
                || headerValue.length() == interceptorProp.getHeaderValuePrefix().length()) {
            return null;
        }
        return headerValue.substring(interceptorProp.getHeaderValuePrefix().length());
    }

    private void returnJson(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        } catch (IOException ignored) {
        }
    }
}
