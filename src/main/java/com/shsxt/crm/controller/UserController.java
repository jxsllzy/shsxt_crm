package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class UserController extends BaseController {


    @Resource
    public UserService userService;

    @GetMapping("user")
    @ResponseBody
    public User queryUserByuserId(Integer userId){
       return userService.selectByPrimaryKey(userId);
    }

}
