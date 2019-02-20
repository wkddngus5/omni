package com.omni.lock.upfile.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestAddInfoMain {

	public static void main(String[] args) throws IOException {
		String path ="D:/2in1A0.aes";
		String outPath = "D:/2in1A0_add.aes";
		 
		InputStream in = new FileInputStream(path);
		int len = in.available();
		 
		byte [] allData=new byte[len];
		byte[] buff =new byte[len];
		while((in.read(buff))!=-1){
		}
		System.arraycopy(buff, 0, allData, 0, buff.length);
		
		// 在数据后面添加标记
		String flag="OMNI_FILE_START_FLAG,A0,V1,OMNI_FILE_END_FLAG";
		byte[] fbytes=flag.getBytes();
		
		byte[] encData = add(allData,fbytes);
		System.out.println("添加信息完成");
		
		// 输出成文件
		FileOutputStream fos = new FileOutputStream(outPath);
		fos.write(encData);
		fos.flush();
		fos.close();
		System.out.println("添加信息完成");
		

	}
	
	private static byte[] add(byte[] b1,byte[] b2){
		byte[] result =new byte[b1.length+b2.length];
		System.arraycopy(b1,0, result, 0, b1.length);
		System.arraycopy(b2,0, result, b1.length, b2.length);
		return result;
	}
	

}
