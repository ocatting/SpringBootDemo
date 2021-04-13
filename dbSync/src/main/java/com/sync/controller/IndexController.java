package com.sync.controller;

import com.sync.common.CommonResult;
import com.sync.common.Constant;
import com.sync.common.exception.ServiceException;
import com.sync.entity.SyncUser;
import com.sync.service.SyncUserService;
import com.sync.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description: 首页信息
 * @Author: Yan XinYu
 * @Date: 2021-03-16 19:56
 */
@Slf4j
@Validated
@Controller
public class IndexController {

    @Autowired
    private SyncUserService syncUserService;

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/toLogin")
    public String toLogin(HttpServletRequest request) {
        SyncUser syncUser = (SyncUser) request.getSession().getAttribute(Constant.SESSION_USER);
        if (syncUser != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping( "/login")
    @ResponseBody
    public CommonResult<String> login(HttpServletRequest request,HttpServletResponse response,
                                      String username,String password,boolean remember) throws ServiceException {

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            throw new ServiceException("用户名或密码不能为空");
        }

        SyncUser syncUser = syncUserService.loadByUserName(username);
        if (syncUser == null) {
            return CommonResult.failed("login fail");
        }
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!syncUser.getPassword().equals(passwordMd5)) {
            return CommonResult.failed("login fail");
        }

        String token = CookieUtil.makeToken(syncUser.getUsername(), syncUser.getPassword());
        CookieUtil.set(response,Constant.LOGIN_IDENTITY_KEY,token,remember);

        HttpSession session = request.getSession();
        session.setAttribute(Constant.SESSION_USER,syncUser);
        return CommonResult.success("login success");
    }

    @PostMapping("/logout")
    @ResponseBody
    public CommonResult<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute(Constant.SESSION_USER);
        session.invalidate();
        return CommonResult.success("logout success");
    }

}
