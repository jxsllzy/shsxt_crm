package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.enums.DevResult;
import com.shsxt.crm.enums.StateStatus;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> result = new HashMap<>();
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(selectByParams(saleChanceQuery));
        result.put("total",pageInfo.getTotal());
        result.put("rows",pageInfo.getList());
        return result;
    }

    public void saveSaleChancesByParams(SaleChance saleChance){
        /**
         * customer_name 客户名称   非空
         * link_man 联系人     非空
         * link_phone  手机号  非空
         *  create_man   创建人     通过cookie查询userId获得
         *  assign_man   分配人
         *      assign_time 分配时间 如果有分配人，则设置当前时间
         *      state        分配状态   如果有分配人，则为已分配1，否则为0
         *      dev_result   开发结果   如果有分配人，则为开发中1，否则为0
         *  is_valid     有效状态    默认有效1
         *  create_date  创建时间    当前时间
         */
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        saleChance.setState(StateStatus.UNSTATE.getType());
        saleChance.setDevResult(DevResult.DEVING.getStatus());
        if(!StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEV_SUCCESS.getStatus());
            saleChance.setState(StateStatus.STATED.getType());
        }
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"机会数据添加失败");
    }

    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系人手机号不能为空");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(linkPhone)),"手机号不存在");
    }

    public void updateSaleChancesByParams(SaleChance saleChance){
        /**
         * id  是否存在内容
         * customer_name 客户名称   非空
         * link_man 联系人     非空
         * link_phone  手机号  非空
         *  如果已经有分配人了：更新后没有分配人
         *      assign_man   非空
         *      assign_time   null
         *      state        分配状态0
         *      dev_result   开发结果1
         *  如果没有分配人了：更新后有分配人了
         *      assign_man   非空
         *      assign_time 分配时间 则设置当前时间
         *      state        分配状态  已分配1
         *      dev_result   开发结果   开发中2
         *  update_time  更新时间    当前时间
            更新数据
         */
        AssertUtil.isTrue(saleChance.getId()==null,"代更新记录不存在");
        SaleChance sal = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(sal==null,"待更新记录不存在");
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        if(StringUtils.isNoneBlank(sal.getAssignMan())&&StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setAssignMan("");
            saleChance.setAssignTime(null);
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else if(StringUtils.isBlank(sal.getAssignMan())&&StringUtils.isNoneBlank(saleChance.getAssignMan())){
            saleChance.setAssignTime(new Date());
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"机会数据更新失败");
    }

    public void deleteSaleChancesByParams(Integer[] ids) {
        AssertUtil.isTrue(ids==null||ids.length==0,"请选择待删除的机会数据");
        AssertUtil.isTrue(deleteBatch(ids)<ids.length,"机会数据删除失败");

    }
}









