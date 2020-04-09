package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.exception.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("user/login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd){
        ResultInfo resultInfo = new ResultInfo();
        try {
            UserModel userModel = userService.login(userName,userPwd);
            resultInfo.setObject(userModel);
        }catch (ParamsException e){
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            resultInfo.setCode(500);
            resultInfo.setMsg("登录异常");
        }
        return resultInfo;
    }

}
