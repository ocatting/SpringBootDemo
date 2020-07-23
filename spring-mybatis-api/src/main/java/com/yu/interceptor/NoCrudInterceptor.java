package com.yu.interceptor;

import com.alibaba.fastjson.JSON;
import com.yu.convert.MessageConvert;
import com.yu.domain.UrlStore;
import com.yu.executor.SQLExecutor;
import com.yu.handler.HttpResponseHandler;
import com.yu.handler.ResponseHandler;
import com.yu.handler.ResultHandler;
import com.yu.parsing.HttpServletParamsParser;
import com.yu.service.CacheService;
import com.yu.service.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.util.Map;

/**
 * @Description: 这里用MVC拦截器实现代码的转发与拦截
 * @Author Yan XinYu
 **/
@Slf4j
public class NoCrudInterceptor implements HandlerInterceptor {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SQLExecutor sqlExecutor;

    @Autowired
    private MessageConvert messageConvert;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        // 匹配拦截的URL,获取需要执行的SQL
        UrlStore urlStore = cacheService.selectUri(requestURI);
        if(urlStore == null){
            return true;
        }
        // 读取携带的参数
        HttpServletParamsParser parser = new HttpServletParamsParser(request);
        Map parse = parser.parse();
        if(log.isDebugEnabled()){
            log.info("NoCrud 请求地址:{} ==> 解析参数:{}",
                    requestURI, JSON.toJSONString(parse));
        }
        // 执行SQL；
        ResultSet execute = sqlExecutor.execute(urlStore, parse);
        // response 解析器
        ResponseHandler responseHandler = new HttpResponseHandler(response,execute);
        responseHandler.success();
        return false;
    }
}
