package io.github.ljezio.jwttoken.interceptor;

import io.github.ljezio.jwttoken.configuration.InterceptorProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;
import io.github.ljezio.jwttoken.utils.JwtUtil;
import io.github.ljezio.jwttoken.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

public class TokenCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request);
        if (token == null) {
            returnJson(response, InterceptorProperties.checkFailJson.getVerifierFail());
            return false;
        }
        try {
            JwtUtil.verify(token);
        } catch (TokenAlreadyExpiredException e) {
            returnJson(response, InterceptorProperties.checkFailJson.getExpired());
            return false;
        } catch (TokenVerifierFailException e) {
            returnJson(response, InterceptorProperties.checkFailJson.getVerifierFail());
            return false;
        }
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String headerValue = request.getHeader(InterceptorProperties.header);
        if (StringUtil.isEmpty(headerValue)) {
            return null;
        }
        if (StringUtil.isEmpty(InterceptorProperties.headerValuePrefix)) {
            return headerValue;
        }
        if (!headerValue.startsWith(InterceptorProperties.headerValuePrefix) ||
                headerValue.length() == InterceptorProperties.headerValuePrefix.length()) {
            return null;
        }
        return headerValue.substring(InterceptorProperties.headerValuePrefix.length());
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
