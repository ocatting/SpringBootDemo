package com.yu.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class WebAuthenticationProvider extends DaoAuthenticationProvider {

    @Getter @Setter
    private CacheManager cacheManager;

    private Cache passwordRetryCache;

    /**
     * 鉴定 如:验证码的认证
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        return super.authenticate(authentication);
    }

    /**
     * 检查认证信息，父类验证了密码，必须调用
     * @param userDetails
     * @param authentication
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
       /* // 添加用户锁定的功能，用户尝试登录密码错误太多次锁定账号
        String username = userDetails.getUsername();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username, AtomicInteger.class);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        int retryLimit = Login.RETRY_LIMIT;
        if(retryCount.incrementAndGet() > retryLimit) {
            //if retry count > retryLimit
            logger.warn("username: " + username + " tried to login more than " + retryLimit + " times in period");
            UserLockService userLockService = this.getUserLockService();
            userLockService.updateLockUser((AuthUser) userDetails);
            throw new LocalizedException("login.retry.limit", new Object[]{username, retryLimit},
                    "User " + username + " password has been incorrectly typed more than " + retryLimit + " times, the account has been locked.");
        } else {
            passwordRetryCache.put(username, retryCount);
        }

        //clear retry data
        passwordRetryCache.evict(username);
        */
        /**
         * 校验密码
         */
        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    /**
     * 装载 后置处理器
     */
    @Override
    protected void doAfterPropertiesSet() {
        this.passwordRetryCache = cacheManager.getCache(Login.RETRY_LIMIT_CACHENAME);
        Assert.notNull(this.passwordRetryCache, "retryLimitCache retryLimitCacheName: " + Login.RETRY_LIMIT_CACHENAME + " is not config.");
    }

//    private DreamUserDetailsService getUserLockService() {
//        UserDetailsService userDetailsService = super.getUserDetailsService();
//        return userDetailsService;
//    }

    /**
     * 登录参数的控制
     */
    public static class Login {

        private static int RETRY_LIMIT = 5;
        /**
         * 登录重试锁定cache名，默认：retryLimitCache
         */
        private static String RETRY_LIMIT_CACHENAME = "retryLimitCache";
    }
}
