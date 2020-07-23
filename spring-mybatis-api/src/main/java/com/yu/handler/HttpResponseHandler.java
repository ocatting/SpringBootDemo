package com.yu.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yu.common.CommonResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class HttpResponseHandler extends BaseResultHandler implements ResponseHandler{

    private HttpServletResponse response;
    private ResultSet resultSet;

    public HttpResponseHandler(HttpServletResponse response, ResultSet resultSet){
        this.response = response;
        this.resultSet = resultSet;
    }

    @Override
    public void success() throws IOException {
        List<JSONObject> list = super.handler(resultSet);
        response.setHeader("Content-Type","application/json");
        response.setCharacterEncoding("UTF-8");
        String result = JSON.toJSONString(CommonResult.success(list));
        response.getWriter().print(result);
    }

    @Override
    public void fail() throws IOException {
        response.setHeader("Content-Type","application/json");
        response.setCharacterEncoding("UTF-8");
        String result = JSON.toJSONString(CommonResult.failed("sql execute fail"));
        response.getWriter().print(result);
    }
}
