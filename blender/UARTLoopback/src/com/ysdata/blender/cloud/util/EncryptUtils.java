package com.ysdata.blender.cloud.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {        
   /** 
    * md5���ܷ��� 
    * @param password    
    * @return 
 * @throws NoSuchAlgorithmException 
     */  
    public static String md5String(String password) throws NoSuchAlgorithmException {  
  
        // �õ�һ����ϢժҪ��  
        MessageDigest digest = MessageDigest.getInstance("md5");  
        byte[] result = digest.digest(password.getBytes());  
        StringBuffer buffer = new StringBuffer();  
        // ��ûһ��byte ��һ�������� 0xff;  
        for (byte b : result) {  
            // ������  
            int number = b & 0xff;// ����  
            String str = Integer.toHexString(number);  
            if (str.length() == 1) {  
                buffer.append("0");  
                }  
                buffer.append(str);  
            }  
  
            // ��׼��md5���ܺ�Ľ��  
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


