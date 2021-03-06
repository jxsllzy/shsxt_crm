package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.exception.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
        UserModel userModel = userService.login(userName,userPwd);
        return success("用户登录成功",userModel);
    }

    @PostMapping("user/updatePassword")
    @ResponseBody
    public ResultInfo updatePassword(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){
        userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request),oldPassword,newPassword,confirmPassword);
        return success("密码更新成功");
    }


}
