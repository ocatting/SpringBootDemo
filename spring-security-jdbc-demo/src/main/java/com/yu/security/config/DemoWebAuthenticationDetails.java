package com.yu.security.config;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author Yan XinYu
 **/
//@Component
public class DemoWebAuthenticationDetails extends WebAuthenticationDetails {

    private final String verificationCode;

    public DemoWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        verificationCode = request.getParameter("code");
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
