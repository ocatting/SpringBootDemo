package com.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@RestController
//@RequestMapping("/hello/{name}")
public class HelloController {

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name){
        return "Hello get " + name ;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() throws URISyntaxException {
        String urlOrForm = "https://qr.chinaums.com/netpay-portal/webpay/pay.do?totalAmount=1&msgType=WXPay.jsPay&requestTimestamp=2020-12-25+01%3A13%3A49&msgSrc=WWW.KMXYUANKJ.COM&sign=4B7C532519C2148406A6BEA3CF85D692E358610B8F2DB4047CF447A0DE9CBCDB&mid=898530175230175&notifyUrl=http%3A%2F%2F218.244.148.121%2Fcloud_parking_pay%2FyshangPayNotify&signType=SHA256&tid=53186739&merOrderId=10Z4XY2012250000000080&instMid=YUEDANDEFAULT";
        if(urlOrForm.startsWith("http")){
            System.out.println("重定向");
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(new URI(urlOrForm)).build();
    }
}
