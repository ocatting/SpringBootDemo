package com.yu.security.config.json;

import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class WebJsonLoginAuthentication extends LoginUrlAuthenticationEntryPoint {

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public WebJsonLoginAuthentication(String loginFormUrl) {
        super(loginFormUrl);
    }
}
