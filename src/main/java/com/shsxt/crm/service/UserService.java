package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService extends BaseService<User,Integer>{

    @Resource
    private UserMapper userMapper;

    public UserModel login(String userName, String userPwd){
        /**
         * 登录业务
         *  1、传入用户名密码，参数校验
         *  2、根据用户查询数据
         *  3、查看数据是否存在
         *  4、验证密码
         *  5、返回模型数据，用户id，用户名，用户状态
         */
        checkLoginParams(userName,userPwd);
        User user = userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(user==null,"用户已注销或不存在！");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userPwd))),"密码错误");
        return packagUserModel(user);
    }

    /**
     * 5、返回模型数据，用户id，用户名，用户状态
     * @param user
     */
    private UserModel packagUserModel(User user) {
        return new UserModel(user.getId(),user.getUserName(),user.getIsValid());
    }

    /**
     * 1、传入用户名密码，参数校验
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }

}
