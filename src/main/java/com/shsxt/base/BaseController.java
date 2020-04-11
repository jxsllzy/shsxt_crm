package com.shsxt.base;


import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseController {


    @ModelAttribute
    public void preHandler(HttpServletRequest request){
        request.setAttribute("ctx", request.getContextPath());
    }

    public ResultInfo success(String str, UserModel userModel){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg(str);
        resultInfo.setResult(userModel);
        return resultInfo;
    }


    public ResultInfo success(String str) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg(str);
        return resultInfo;
    }

    public ResultInfo success( ) {
        return new ResultInfo();
    }
}
