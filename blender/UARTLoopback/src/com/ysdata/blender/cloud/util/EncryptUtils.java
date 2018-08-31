package com.ysdata.blender.cloud.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {        
   /** 
    * md5加密方法 
    * @param password    
    * @return 
 * @throws NoSuchAlgorithmException 
     */  
    public static String md5String(String password) throws NoSuchAlgorithmException {  
  
        // 得到一个信息摘要器  
        MessageDigest digest = MessageDigest.getInstance("md5");  
        byte[] result = digest.digest(password.getBytes());  
        StringBuffer buffer = new StringBuffer();  
        // 把没一个byte 做一个与运算 0xff;  
        for (byte b : result) {  
            // 与运算  
            int number = b & 0xff;// 加盐  
            String str = Integer.toHexString(number);  
            if (str.length() == 1) {  
                buffer.append("0");  
                }  
                buffer.append(str);  
            }  
  
            // 标准的md5加密后的结果  
       return buffer.toString();  
    }  
    
    public static String JM(String inStr) {  
    	char[] a = inStr.toCharArray();  
    	for (int i = 0; i < a.length; i++) {  
    		a[i] = (char) (a[i] ^ 't');  
    	}  
    	String k = new String(a);  
    	return k;  
    }

}  


