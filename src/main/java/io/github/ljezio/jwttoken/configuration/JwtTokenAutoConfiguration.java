package io.github.ljezio.jwttoken.configuration;

import io.github.ljezio.jwttoken.common.Constant;
import io.github.ljezio.jwttoken.interceptor.TokenCheckInterceptor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({JwtTokenProperties.class, InterceptorProperties.class})
public class JwtTokenAutoConfiguration {

    @Bean("jwtTokenRunner")
    public ApplicationRunner runner(JwtTokenProperties jwtTokenProperties, InterceptorProperties interceptorProperties) {
        return args -> {
            if (jwtTokenProperties.isShowBanner()) {
                System.out.printf(Constant.BANNER, interceptorProperties.isEnable() ? " and interceptor" : "");
            }
        };
    }

    @Bean("jwtTokenInterceptorWebMvcConfigurer")
    @ConditionalOnProperty(prefix = Constant.INTERCEPTION_PROPERTIES_PREFIX, name = "enable", havingValue = "true")
    public WebMvcConfigurer webConfigurer(InterceptorProperties properties) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                InterceptorRegistration registration = registry.addInterceptor(new TokenCheckInterceptor())
                        .order(properties.getOrder())
                        .addPathPatterns(properties.getPath());
                if (properties.getExcludePath() != null && !properties.getExcludePath().isEmpty()) {
                    registration.excludePathPatterns(properties.getExcludePath());
                }
            }
        };
    }

}
