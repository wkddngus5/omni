package com.pgt.bikelock.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MD5Utils {
	// 十六进制下数字到字符的映射数组/array of numbers to characters under hexadecimal 
	private final static String[] HEXDIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d","e", "f" };  

	public final static String getMD5(String str){  
		if (str != null) {  
			try {  
				// 创建具有指定算法名称的信息摘要 /create info summary with the specified algorithm name
				MessageDigest md = MessageDigest.getInstance("MD5");  
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算/use the specified byte array to finalize the digest, and then complete the summary calculation
				byte[] results = md.digest(str.getBytes("utf-8")); // 将得到的字节数组变成字符串返回/make byte array into a string and return 
				StringBuffer resultSb = new StringBuffer();  
				String a = "";  
				for (int i = 0; i < results.length; i++) {  
					int n = results[i];  
					if (n < 0)  
						n = 256 + n;  
					int d1 = n / 16;  
					int d2 = n % 16;  
					a = HEXDIGITS[d1] + HEXDIGITS[d2];  
					resultSb.append(a.toUpperCase());  
				}  
				return resultSb.toString();  
			} catch (Exception ex) {  
				ex.printStackTrace();  
			}  
		}  
		return null;  
	}  

	public static String getHmacMd5(String key, String message) {

		byte[] keyBytes = key.getBytes();

		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacMD5");

		try {
			// Create a MAC object using HMAC-MD5 and initialize with key
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);

			// Encode the string into bytes using utf-8 and digest it
			byte[] utf8 = message.getBytes("UTF8");
			byte[] digest = mac.doFinal(utf8);

			// convert the digest into a string
			StringBuffer digestString = new StringBuffer();
			final String hex = "0123456789abcdef";
			byte b[] = digest;

			for (int i = 0; i < 16; i++) {
				int c = ((b[i]) >>> 4) & 0xf;
				digestString.append(hex.charAt(c));
				c = ((int)b[i] & 0xf);
				digestString.append(hex.charAt(c));
			}

			return digestString.toString();
		} catch (InvalidKeyException e) {
			return "";
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static void main(String[] args) {
		String key = getMD5("this is a aes key for honneybes 234234");
//		System.out.println(key);
		System.out.println(key.substring(0, 16)+";"+key.substring(16, 32));
//		System.out.println(getMD5("12345678"));
	}
}
