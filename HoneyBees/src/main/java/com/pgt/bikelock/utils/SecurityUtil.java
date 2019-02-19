/**
 * FileName:     SecurityUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 下午2:42:32/2:42:32 pm, March 24, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.alipay.api.internal.util.AlipaySignature;

/**
 * @ClassName:     SecurityUtil
 * @Description: 数据加密安全处理类/security tool
 * @author:    Albert
 * @date:        2017-3-24 下午2:42:32/2:42:32 pm, March 24, 2017
 *
 */
public class SecurityUtil {

	//对称加密密钥/symmetrical encrypt key（已经过MD5单向加密/one-way encrypted the MD5，原文/original text："this is a key for pgt bike lock,create time:2017年03月24日15:01:45"）
	public static final String DoubKey = "bE2upm4s71Ad0o4O+liY7g==";
	public static final String Default_DoubMethod = "AES";//默认双向对称加密算法（下一代的加密算法标准，速度快，安全级别高/Default bi-directional symmetric encryption algorithm (next generation encryption algorithm standard, fast, high security level）


	/**
	 * 加密/encrypt
	 * 
	 * @param content
	 *            待加密内容/to be encrypted content
	 * @param key
	 *            加密的密钥/encrypted key
	 * @return
	 */
	public static String encrypt(String content, String key) {
		try {

			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(key.getBytes());
			kgen.init(128, random);

			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] byteRresult = cipher.doFinal(byteContent);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteRresult.length; i++) {
				String hex = Integer.toHexString(byteRresult[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				sb.append(hex.toUpperCase());
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密/decrypt
	 * 
	 * @param content
	 *            待解密内容/to be decrypt content
	 * @param key
	 *            解密的密钥/key to the decrypt
	 * @return
	 */
	public static String decrypt(String content, String key) {
		if (content.length() < 1)
			return null;
		byte[] byteRresult = new byte[content.length() / 2];
		for (int i = 0; i < content.length() / 2; i++) {
			int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
			byteRresult[i] = (byte) (high * 16 + low);
		}
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(key.getBytes());
			kgen.init(128, random);

			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] result = cipher.doFinal(byteRresult);
			return new String(result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}


	/***
	 *  利用Apache的工具类实现SHA-256加密/encrypted SHA-256 by Apache tool
	 * @param str 加密后的报文 documents after encrypted
	 * @return
	 */
	public static String getSHA256Str(String str){
		MessageDigest messageDigest;
		String encdeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
			encdeStr = Hex.encodeHexString(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encdeStr;
	}


	/**
	 * 
	 * @Title:        sha256_HMAC 
	 * @Description:  TODO
	 * @param:        @param message
	 * @param:        @param secret 密钥/key
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年8月28日 上午11:40:55/11:40:55 am, August 28, 2017
	 */
	public static String sha256_HMAC(String message, String secret) {     
		String hash = "";      
		try {         
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");     
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");      
			sha256_HMAC.init(secret_key);       
			byte[] bytes = sha256_HMAC.doFinal(message.getBytes());   
			hash = byteArrayToHexString(bytes);          
		} catch (Exception e) {     
			System.out.println("Error HmacSHA256 ===========" + e.getMessage());   
		}       
		return hash;   
	}     

	private static String byteArrayToHexString(byte[] b) {  
		StringBuilder hs = new StringBuilder();     
		String stmp;      
		for (int n = 0; b!=null && n < b.length; n++) {   
			stmp = Integer.toHexString(b[n] & 0XFF);    
			if (stmp.length() == 1)           
				hs.append('0');      
			hs.append(stmp);    
		}      
		return hs.toString().toLowerCase();   
	}   

	public static void main(String[] args) {
		String content = "15817434707";
		String password ="1259632153695615";// "AEStest";
		System.out.println("密　钥/key：" + password);
		System.out.println("加密前/before encrypt：" + content);
		// 加密/encrypt
		String encryptResult = encrypt(content, password);
		System.out.println("加密后/encrypted：" + encryptResult);
		// 解密/decrypt
		String decryptResult = decrypt(encryptResult, password);
		System.out.println("解密后/decrypted：" + decryptResult);


		System.out.println("SHA256:"+getSHA256Str("test"));
		
	
	}
}

