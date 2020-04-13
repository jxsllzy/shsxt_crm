package com.shsxt.crm.utils;

import com.shsxt.base.BaseMapper;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.exception.ParamsException;
import com.shsxt.crm.vo.User;

import java.io.PrintWriter;
import java.security.MessageDigest;

import java.util.Base64;


public class Md5Util {
	
	public static String  encode(String msg){
		try {
			MessageDigest messageDigest=MessageDigest.getInstance("md5");
			return Base64.getEncoder().encodeToString(messageDigest.digest(msg.getBytes())) ;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
