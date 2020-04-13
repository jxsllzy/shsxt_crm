package com.shsxt.crm.service;

import com.shsxt.base.BaseQuery;
import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.CusDevPlanMapper;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private SaleChanceService saleChanceService;

    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        /**
         *  saleChanceId 营销机会id
         *  planDate    日期  非空
         *  planItem    计划内容    非空
         *  exeAffect   计划效果    非空
         */
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanDate(),cusDevPlan.getPlanItem(),cusDevPlan.getExeAffect());
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(cusDevPlan)<1,"计划项记录添加失败");
    }

    private void checkParams(Integer id,Date planDate, String planItem, String exeAffect) {
        AssertUtil.isTrue(id==null||null == saleChanceService.selectByPrimaryKey(id),"请设置营销机会id");
        AssertUtil.isTrue(StringUtils.isBlank(exeAffect),"请输入计划项效果");
        AssertUtil.isTrue(StringUtils.isBlank(planItem),"请输入计划项内容");
        AssertUtil.isTrue(planDate==null,"请指定计划项日期");
    }

    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanDate(),cusDevPlan.getPlanItem(),cusDevPlan.getExeAffect());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录添加失败");
    }

    public void deleteCusDevPlan(Integer id){
        CusDevPlan cusDevPlan =selectByPrimaryKey(id);
        AssertUtil.isTrue(null==id || null ==cusDevPlan ,"待删除记录不存在");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录删除失败");

    }
}
