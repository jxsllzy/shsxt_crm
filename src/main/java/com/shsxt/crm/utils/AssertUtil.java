package com.shsxt.crm.utils;

import com.shsxt.crm.exception.ParamsException;

public class AssertUtil {

    /**
     * 如果判断为真，则手动触发异常
     * @param flag
     * @param msg
     */
    public static void isTrue(boolean flag,String msg){
        if (flag){
            throw new ParamsException(msg);
        }
    }
}
