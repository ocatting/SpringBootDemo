package com.sync.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * @Description: Cookie 工具
 * @Author: Yan XinYu
 * @Date: 2021-03-18 18:18
 */

public class CookieUtil {

    // 默认缓存时间,单位/秒。
    private static final int COOKIE_MAX_AGE = 3 * 24 * 60 * 60;
    // 盐值
    private static final String SALT = "sync-db-01";

    public static String getValue(HttpServletRequest request, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    private static Cookie get(HttpServletRequest request, String key) {
        Cookie[] arrCookie = request.getCookies();
        if (arrCookie != null && arrCookie.length > 0) {
            for (Cookie cookie : arrCookie) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 设置 Cookie
     * @param response
     * @param key
     * @param value
     * @param remember
     */
    public static void set(HttpServletResponse response, String key, String value, boolean remember) {
        if(remember){
            set(response, key, value, null, "/", COOKIE_MAX_AGE);
        }
    }

    /**
     * 设置 Cookie
     * @param response request
     * @param key key
     * @param value val
     * @param domain 限定域名
     * @param path 限定路径
     * @param maxAge 有效时长
     */
    private static void set(HttpServletResponse response, String key, String value, String domain, String path, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 制作token
     * @param username 用户
     * @param password 密码
     * @return
     */
    public static String makeToken(String username,String password){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        String md5 = DigestUtils.md5DigestAsHex((username + "|"+password).getBytes(StandardCharsets.UTF_8));
        jsonObject.put("password",md5);
        return new BigInteger(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8)).toString(16);
    }

    public static String getUsername(String token){
        String tokenJson = new String(new BigInteger(token, 16).toByteArray());
        JSONObject jsonObject = JSON.parseObject(tokenJson);
        return jsonObject.getString("username");
    }

}
