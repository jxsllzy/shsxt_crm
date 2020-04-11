package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        return new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getIsValid());
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


    @Transactional(propagation = Propagation.REQUIRED) //开启事务
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        /**
         * 修改密码
         * 1、判断数据
         *  根据id查询用户数据
         *  userId  非空    判断用户id是否存在，根据id能查询到数据
         *  oldPassword   非空  查询数据库匹配老密码
         *  newPassword   非空  老密码和新密码是否一致
         *  confirmPassword 非空 新密码和确认密码是否一致
         * 2、设置用户名新密码
         * 3、修改密码
         */
        checkUpdatePwdParams(userId, oldPassword, newPassword, confirmPassword);
        User user = userMapper.selectByPrimaryKey(userId);
        user.setUserPwd(Md5Util.encode(newPassword));
        updateByPrimaryKeySelective(user);
    }

    private void checkUpdatePwdParams(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(userId==null||user==null,"用户不存在或已注销");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"原始密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"确认密码不能为空");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"确认密码与新密码不一致");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码与原始密码相同");
    }


}
