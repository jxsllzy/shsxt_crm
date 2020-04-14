package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.dao.UserRoleMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.*;
import com.shsxt.crm.vo.User;
import com.shsxt.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService<User,Integer>{

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        /**
         * 更新用户数据
             * 参数校验
             *   id        非空  记录必须存在
             *   用户名    非空  唯一
             *   电话号码   非空  格式验证
             *   邮箱      非空  格式验证
             * 默认值
             *   updateDate 更新时间
             * 更新数据
         */
        AssertUtil.isTrue(user.getId()==null||userMapper.selectByPrimaryKey(user.getId())==null,"该用户不存在");
        checkParams(user.getUserName(),user.getPhone(),user.getEmail());
        User u = userMapper.queryUserByUserName(user.getUserName());
        if(u!=null&&u.getIsValid()==1){
            AssertUtil.isTrue(!(u.getId().equals(user.getId())),"该用户已存在");
        }
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"更新失败");

        /**
         *  先将之前的关联数据删除
         *  后添加新的数据
         */
        Integer userId = user.getId();
        addOrUpdateUserRole(userId, user.getRoleIds());

    }

    private void addOrUpdateUserRole(Integer userId, List<Integer> roleids) {
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        if(count>0){
            userRoleMapper.deleteByPrimaryKey(userId);
        }
        if(roleids !=null && roleids.size()>0){
            List<UserRole> list = new ArrayList<>();
            roleids.forEach(roleId->{
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setUpdateDate(new Date());
                userRole.setCreateDate(new Date());
                list.add(userRole);
            });
            AssertUtil.isTrue(userRoleMapper.insertBatch(list)<roleids.size(),"用户分配角色成功");
        }
    }


    public void deleteUser(Integer id) {
        /**
         * 判断id 非空 有数据
         */
        User user = userMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(id==null||user==null,"待删除记录不存在");
        user.setIsValid(0);
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户删除失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
        /**
         * 参数校验
         *   用户名    非空  唯一
         *   电话号码   非空  格式验证
         *   邮箱      非空  格式验证
         * 默认值
         *   isValue    1
         *   userPwd    123456
         *   createDate 创建时间
         *   updateDate 更新时间
         *   添加数据
         */
        checkParams(user.getUserName(),user.getPhone(),user.getEmail());
        User u = userMapper.queryUserByUserName(user.getUserName());
        AssertUtil.isTrue(u!=null&&u.getIsValid()==1,"该用户名已存在");
        user.setIsValid(1);
        user.setUserPwd(Md5Util.encode("123456"));
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());

        AssertUtil.isTrue(userMapper.insertHasKey(user)<1,"该用户添加失败");
        /**
         * 分配角色
         *      获取添加数据返回的主键
         *      根据用户id和roleIds添加数据到role_user表中数据
         */
        Integer userId = user.getId();
        List<Integer> roleids = user.getRoleIds();
        addOrUpdateUserRole(userId,roleids);
    }

    private void checkParams(String userName, String phone, String email) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"请输入用户名");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(phone)),"手机号格式不正确");
        AssertUtil.isTrue(!(EmailUtil.checkEmail(email)),"邮箱格式不正确");
    }
}
