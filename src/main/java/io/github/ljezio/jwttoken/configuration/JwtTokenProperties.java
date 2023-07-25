package io.github.ljezio.jwttoken.configuration;

import io.github.ljezio.jwttoken.common.AlgorithmEnum;
import io.github.ljezio.jwttoken.common.Constant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = Constant.COMMON_PROPERTIES_PREFIX)
public class JwtTokenProperties {

    /**
     * 加密算法
     */
    private AlgorithmEnum algorithm = AlgorithmEnum.HMAC256;

    /**
     * 密钥
     */
    private String secret = "1234ezio";

    /**
     * accessToken过期时间
     */
    private int accessTokenExpireMinutes = 30;

    /**
     * refreshToken过期时间
     */
    private int refreshTokenExpireDays = 180;

    /**
     * 是否显示banner图
     */
    private boolean showBanner = true;
}
