package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.CookieUtil;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @RequestMapping("index")
    public String index(){
        return "sale_chance";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> result = saleChanceService.querySaleChancesByParams(saleChanceQuery);
        return result;
    }

    @Resource
    private UserService userService;

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSaleChancesByParams(HttpServletRequest request,SaleChance saleChance){
        saleChance.setCustomerName( userService.selectByPrimaryKey(LoginUserUtil.releaseUserIdFromCookie(request)).getUserName());
        saleChanceService.saveSaleChancesByParams(saleChance);
        return success("机会数据添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChancesByParams(SaleChance saleChance){
        saleChanceService.updateSaleChancesByParams(saleChance);
        return success("机会数据更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChancesByParams(Integer[] ids){
        saleChanceService.deleteSaleChancesByParams(ids);
        return success("机会数据删除成功");
    }

}
