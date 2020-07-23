package com.yu.service;

import com.yu.common.CommonResult;
import com.yu.domain.UrlStore;

/**
 * @Description: 持久化信息
 * @Author Yan XinYu
 **/
public class StoreService {

    /**
     * 地址 绑定
     * @param urlStore
     * @return
     */
    public CommonResult register(UrlStore urlStore){

        return CommonResult.success("注册成功");
    }

    /**
     * 地址 更新
     * @param urlStore
     * @return
     */
    public CommonResult update(UrlStore urlStore){

        return CommonResult.success("更新成功");
    }


    /**
     * 地址 删除
     * @param id
     * @return
     */
    public CommonResult delete(String id){

        return CommonResult.success("删除成功");
    }


}
