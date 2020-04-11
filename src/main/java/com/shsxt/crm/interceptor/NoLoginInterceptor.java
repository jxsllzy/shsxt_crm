package com.shsxt.crm.interceptor;

import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.CookieUtil;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    //拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 查找cookie是否存在，并且能查找到数据
         */
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        if(userId==0||user==null){
            response.sendRedirect(request.getContextPath()+"/index");
            return false;
        }
        return true;
    }
}
