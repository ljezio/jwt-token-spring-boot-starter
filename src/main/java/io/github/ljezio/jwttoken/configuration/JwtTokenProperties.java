package io.github.ljezio.jwttoken.configuration;

import io.github.ljezio.jwttoken.common.Constant;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = Constant.COMMON_PROPERTIES_PREFIX)
public class JwtTokenProperties {

    /**
     * 密钥
     */
    public static String secret = "1234ezio";

    /**
     * accessToken过期时间
     */
    public static int accessTokenExpireMinutes = 30;

    /**
     * refreshToken过期时间
     */
    public static int refreshTokenExpireDays = 180;

    /**
     * 是否显示banner图
     */
    public static boolean showBanner = true;

    /**
     * token过期时间
     */

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        JwtTokenProperties.secret = secret;
    }

    public int getAccessTokenExpireMinutes() {
        return accessTokenExpireMinutes;
    }

    public void setAccessTokenExpireMinutes(int accessTokenExpireMinutes) {
        JwtTokenProperties.accessTokenExpireMinutes = accessTokenExpireMinutes;
    }

    public int getRefreshTokenExpireDays() {
        return refreshTokenExpireDays;
    }

    public void setRefreshTokenExpireDays(int refreshTokenExpireDays) {
        JwtTokenProperties.refreshTokenExpireDays = refreshTokenExpireDays;
    }

    public boolean isShowBanner() {
        return showBanner;
    }

    public void setShowBanner(boolean showBanner) {
        JwtTokenProperties.showBanner = showBanner;
    }

}
