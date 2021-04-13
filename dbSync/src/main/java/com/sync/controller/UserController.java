package com.sync.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sync.common.CommonResult;
import com.sync.common.Constant;
import com.sync.common.PageVo;
import com.sync.entity.SyncUser;
import com.sync.service.SyncUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户管理
 * @Author: Yan XinYu
 * @Date: 2021-03-18 9:45
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    public SyncUserService syncUserService;

    @GetMapping
    public String info(){
        return "user/user.index";
    }

    @PostMapping("/page")
    @ResponseBody
    public PageVo<SyncUser> page (Integer current, Integer size){
        Page<SyncUser> page = new Page<>(current == null ? 1:current,size == null ? 10:size);
        syncUserService.page(page);
        return PageVo.create(page.getRecords(),page.getTotal(),page.getTotal());
    }

    @PostMapping("/add")
    @ResponseBody
    public CommonResult<String> add(HttpServletRequest request, @RequestBody SyncUser syncUser){

        Assert.hasLength(syncUser.getUsername(),"账户不能为空");
        Assert.hasLength(syncUser.getPassword(),"密码不能为空");

        SyncUser user = (SyncUser) request.getSession().getAttribute(Constant.SESSION_USER);
        // 管理员
        if (user.getRole() != 1) {
            return CommonResult.failed();
        }

        String passwordMd5 = DigestUtils.md5DigestAsHex(syncUser.getPassword().getBytes());
        syncUser.setPassword(passwordMd5);
        syncUserService.save(syncUser);
        return CommonResult.success();
    }

    @PostMapping("/update")
    @ResponseBody
    public CommonResult<String> update(HttpServletRequest request, @RequestBody SyncUser syncUser){

        Assert.hasLength(syncUser.getUsername(),"账户不能为空");
        Assert.hasLength(syncUser.getPassword(),"密码不能为空");

        SyncUser user = (SyncUser) request.getSession().getAttribute(Constant.SESSION_USER);
        // 管理员
        if (user.getRole() == 1) {
            syncUser.setPassword(DigestUtils.md5DigestAsHex(syncUser.getPassword().getBytes()));
            syncUserService.updateById(syncUser);
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @GetMapping("/del/{id}")
    @ResponseBody
    public CommonResult<String> del(HttpServletRequest request,@PathVariable("id") Integer id){

        SyncUser user = (SyncUser) request.getSession().getAttribute(Constant.SESSION_USER);

        // 管理员
        if (user.getRole() == 1) {
            syncUserService.removeById(id);
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

}
