package com.shsxt.crm;

import com.shsxt.crm.exception.NoLoginException;
import com.shsxt.crm.exception.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.utils.JsonUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GlobalExceptionHandle implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        /**
         * 解决未登录异常
         */
        if(e instanceof NoLoginException){
            NoLoginException ne = (NoLoginException) e;
            modelAndView.setViewName("no_login");
            modelAndView.addObject("msg",ne.getMsg());
            modelAndView.addObject("ctx",request.getContextPath());
            return modelAndView;
        }


        /**
         *  方法返回值类型判断
         *  如果没有方法，返回300错误
         *  判断是json还是视图出现异常@ResponseBody
         *  json出现异常
         *      返回json数据
         *  视图出现异常
         *      跳转404页面
         */

        modelAndView.setViewName("error");
        modelAndView.addObject("msg","系统错误，请稍后重试");
        modelAndView.addObject("code",400);
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            ResponseBody responseBody = hm.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if(responseBody==null){
                //返回视图
                if(e instanceof ParamsException){
                    ParamsException ex = (ParamsException) e;
                    modelAndView.addObject("code",ex.getCode());
                    modelAndView.addObject("msg",ex.getMsg());
                }
                return modelAndView;
            }else {
                //返回json
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统错误，请稍后重试");
                if(e instanceof ParamsException){
                    ParamsException ex = (ParamsException) e;
                    resultInfo.setCode(ex.getCode());
                    resultInfo.setMsg(ex.getMsg());
                }
                JsonUtil.writeJson(response,resultInfo);
                return null;
            }
        }else {
            return modelAndView;
        }
    }
}
