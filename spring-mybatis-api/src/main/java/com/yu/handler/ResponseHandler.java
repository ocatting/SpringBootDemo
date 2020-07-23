package com.yu.handler;

import java.io.IOException;

/**
 * @Description: 返回结果处理
 * @Author Yan XinYu
 **/
public interface ResponseHandler {

    void success() throws IOException;

    void fail() throws IOException;

}
