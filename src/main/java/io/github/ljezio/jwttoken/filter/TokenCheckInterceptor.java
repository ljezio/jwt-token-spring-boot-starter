package io.github.ljezio.jwttoken.filter;

import io.github.ljezio.jwttoken.configuration.InterceptorProperties;
import io.github.ljezio.jwttoken.exception.TokenAlreadyExpiredException;
import io.github.ljezio.jwttoken.exception.TokenVerifierFailException;
import io.github.ljezio.jwttoken.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

public class TokenCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(InterceptorProperties.headerName);
        if (token == null || token.equals("")) {
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

    private void returnJson(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        } catch (IOException ignored) {
        }
    }
}
