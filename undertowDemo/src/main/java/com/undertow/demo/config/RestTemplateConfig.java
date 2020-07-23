package com.undertow.demo.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @Description: 若使用ribbon 该方法不被创建
 * Springboot2.0 中 RestTemplateBuilder 方法基本已经被废弃
 * @Author Yan XinYu
 **/
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "my.rest")
public class RestTemplateConfig {

    private int connectTimeout = 3000;

    private int readTimeout = 3000;

    private List<UrlParam> urls = new ArrayList<>();

    // 这里 必须是 public static 否则失败
    public static class UrlParam{

        private String host;
        private List<String> uri;

        public UrlParam(){}

        public UrlParam(String text){
            System.out.println(text);
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public List<String> getUri() {
            return uri;
        }

        public void setUri(List<String> uri) {
            this.uri = uri;
        }
    }

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(connectTimeout);
        simpleClientHttpRequestFactory.setReadTimeout(readTimeout);
        return simpleClientHttpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate(SimpleClientHttpRequestFactory simpleClientHttpRequestFactory){
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        restTemplate.setInterceptors(Arrays.asList(new defaultClientHttpRequestInterceptor()));
        return restTemplate;
    }

    class defaultClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            //修改增加 Head等，负载均衡等可以在这实现
            filterUrl(request);
            addHead(request,body);
            ClientHttpResponse execute = execution.execute(request, body);
            //验证 Head等
            checkResponseHead(execute);
            return execute;
        }

        // 负载均衡
        private void filterUrl(HttpRequest request) {
//            if(urls!=null && !urls.isEmpty()){
//                URI uri = request.getURI();
//                String reqPath = uri.getPath();
//                log.info("调用服务名称:{},请求地址：{}",uri.getHost(),reqPath);
//                List<String> instances = getInstances(uri.getHost());
//                if(CollectionUtils.isEmpty(instances)) {
////                    如果实例为空，则直接调用远程服务
//                    return ;
//                }
//                // 修改实例地址
//                String s = lbIp(instances);
////                request.
//            }
        }


        // URI地址缓存
        Map<String,List<String>> cacheList ;

        //获取实例名称
        public List<String> getInstances(String name){
            if(cacheList == null){
                cacheList = new HashMap<>();
//                for(UrlParam urlParam:urls){
//                    cacheList.put(urlParam.getHost(),urlParam.getUri());
//                }
            }
            return cacheList.get(name);
        }

        // 随机一个IP
        public String lbIp(List<String> list){
            Random random = new Random();
            Integer randomIndex = random.nextInt(list.size());
            return list.get(randomIndex);
        }

        private void addHead(HttpRequest request, byte[] body) throws IOException {
            // 拦截增加head
        }

        private void checkResponseHead(ClientHttpResponse response) throws IOException{
            //拦截
        }
    }

    public static void main(String[] args) {
        String url = "http://product-center:9008/selectProductInfoById/";
        URI uri = URI.create(url);
        System.out.println(uri.getHost());

        System.out.println(uri.toString());

    }


}
