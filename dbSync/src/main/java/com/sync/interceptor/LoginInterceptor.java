package com.sync.interceptor;

import com.sync.common.Constant;
import com.sync.entity.SyncUser;
import com.sync.service.SyncUserService;
import com.sync.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 登录拦截
 * @Author: Yan XinYu
 * @Date: 2021-03-16 20:30
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SyncUserService syncUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }
        SyncUser syncUser = (SyncUser) request.getSession().getAttribute(Constant.SESSION_USER);
        if(syncUser != null){
            request.getSession().setAttribute(Constant.SESSION_USER,syncUser);
            return super.preHandle(request, response, handler);
        }
        // 记住密码功能
        String token = CookieUtil.getValue(request, Constant.LOGIN_IDENTITY_KEY);
        if(token != null){
            String username = CookieUtil.getUsername(token);
            syncUser = syncUserService.loadByUserName(username);
            if(syncUser != null){
                if(CookieUtil.makeToken(syncUser.getUsername(),syncUser.getPassword()).equals(token)){
                    request.getSession().setAttribute(Constant.SESSION_USER,syncUser);
                    return super.preHandle(request, response, handler);
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/toLogin");
        return false;
    }

}
