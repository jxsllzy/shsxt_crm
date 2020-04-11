package com.shsxt.crm.utils;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import com.shsxt.crm.model.ResultInfo;

public class JsonUtil {
	
	public static void writeJson(HttpServletResponse response, ResultInfo resultInfo){
		//1、设置响应类型输出流
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json;charset=UTF-8");
		//2、将resuInfo对象转为字符串
		String str = JSON.toJSONString(resultInfo);
		//3、得到字符输出流
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(str);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(writer!=null){
				writer.close();
			}
		}

	}
}
