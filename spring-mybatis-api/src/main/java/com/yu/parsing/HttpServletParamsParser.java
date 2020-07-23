package com.yu.parsing;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: HttpServlet 参数解析
 * @Author Yan XinYu
 **/
public class HttpServletParamsParser {

    private HttpServletRequest request;

    public HttpServletParamsParser(HttpServletRequest request){
        this.request = request;
        initialParser();
    }

    /**
     * 请求地址
     */
    private String requestURI = null;

    private void initialParser() {
         this.requestURI = request.getRequestURI();
    }

    public boolean isGet(){
        String method = request.getMethod();
        return HttpMethod.GET == HttpMethod.resolve(method);
    }

    public boolean isPost(){
        String method = request.getMethod();
        return HttpMethod.POST == HttpMethod.resolve(method);
    }

    public Map parse(){
        Map resultMap = null;
        try{
            if(isGet()){
                resultMap = getParse();
            } else if(isPost()) {
                resultMap = postParse();
            }
        } catch (Exception e){
            throw new IllegalArgumentException("发起请求:" +requestURI+" 参数解析异常 " + e.getMessage());
        }
        return resultMap;
    }

    /**
     * GET 方法  参数解析
     * @return
     */
    public Map getParse() {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            String value = request.getParameter(key);
            resultMap.put(key,value);
        }
        return resultMap.isEmpty()?null:resultMap;
    }

    /**
     * 解析 POST 请求参数
     * @return
     * @throws IOException
     */
    public Map postParse() throws IOException {
        StringBuffer sb = new StringBuffer();
        String line = null;
        InputStream input = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            input = request.getInputStream();
            byte[] bytes = new byte[1024 * 2];
            int len;
            while ((len = input.read(bytes)) != -1 )
                out.write(bytes, 0, len);
        } catch (IOException e) {

        } finally {
            if(input!=null){
                input.close();
            }
            out.close();
        }
        String text = new String(out.toByteArray(), Charset.forName("UTF-8"));
        return (Map) JSON.parse(text);
    }

}
