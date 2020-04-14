package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.exception.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController {

    @Resource
    public UserService userService;

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

    @RequestMapping("user/index")
    public String index(){
        return "user";
    }

    @PostMapping("user/list")
    @ResponseBody
    public Map<String,Object> queryUser(UserQuery userQuery){
        return userService.queryByParams(userQuery);
    }

    @RequestMapping("user/save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        userService.saveUser(user);
        return success("用户添加成功");
    }

    @RequestMapping("user/update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功");
    }

    @RequestMapping("user/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer id){
        userService.deleteUser(id);
        return success("用户删除成功");
    }

}
