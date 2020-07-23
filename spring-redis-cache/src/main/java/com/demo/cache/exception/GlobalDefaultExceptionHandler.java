package com.demo.cache.exception;

import com.demo.cache.redis.model.ResponseMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description: 全局异常处理机制
 * @Author Yan XinYu
 **/
@Slf4j
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * 处理参数异常，一般用于校验body参数
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseMsg handleValidationBodyException(Exception e) {
        log.info("统一异常处理");
        return ResponseMsg.fail("网络异常");
    }

}
