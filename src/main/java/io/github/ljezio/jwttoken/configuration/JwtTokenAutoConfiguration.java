package io.github.ljezio.jwttoken.configuration;

import io.github.ljezio.jwttoken.common.Constant;
import io.github.ljezio.jwttoken.filter.TokenCheckInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenAutoConfiguration.class);

    @Bean("jwtTokenRunner")
    public ApplicationRunner runner(JwtTokenProperties properties) {
        return args -> {
            if (properties.isShowBanner()) {
                System.out.println(Constant.BANNER);
            }
        };
    }

    @Bean("jwtTokenInterceptorWebMvcConfigurer")
    @ConditionalOnProperty(prefix = Constant.INTERCEPTION_PROPERTIES_PREFIX, name = "enable", havingValue = "true")
    public WebMvcConfigurer webConfigurer(InterceptorProperties properties) {
        logger.info("jwt token interception is working");
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
