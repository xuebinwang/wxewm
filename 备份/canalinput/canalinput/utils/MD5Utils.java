package com.primeton.di.trans.steps.canalinput.utils;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Utils {
	
	
	
	public static void main(String[] args) {
		System.out.println(getRowKey("table_test"));
		System.out.println(getRowKey("table_test1"));
		System.out.println(getRowKey("table_test2"));
		System.out.println(getRowKey("table_test3"));
		System.out.println(getRowKey("table_test4"));
		
		System.out.println(getRowKey("table_test1"));
		System.out.println(getRowKey("table_test2"));
		System.out.println(getRowKey("table_test3"));
		System.out.println(getRowKey("table_test4"));
	}
	public static String getRowKey(String input) {
		  try {
			// 生成一个MD5加密计算摘要
			  MessageDigest md = MessageDigest.getInstance("MD5");
			  // 计算md5函数
			  md.update(input.getBytes());
			  // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			  // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			  return new BigInteger(1, md.digest()).toString(16);
		  } catch (Exception e) {
			  System.out.println(("MD5加密出现错误"));
			  e.printStackTrace();
			 return input;
		  }
		}
}
