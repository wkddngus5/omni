/**
 * FileName:     AliOSSUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月9日 下午6:39:55/6:39:55 pm, May 9, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月9日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alipay.api.internal.util.StringUtils;
import com.aliyun.oss.OSSClient;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.ImageVo;

/**
 * @ClassName:     AliOSSUtil
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月9日 下午6:39:55/6:39:55 pm, May 9, 2017
 * 
 *
 */
public class AliOSSUtil {
	
	/**
	 * 
	 * @Title:        uploadFile 
	 * @Description:  OSS文件上传/upload file OSS
	 * @param:        @param request
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月15日 上午11:15:42/11:15:42 am, July 15, 2017
	 */
	public static String uploadFile(HttpServletRequest request){
		// 检测是否为多媒体上传/check whether it is mutimedia uploading or not
	/*	if (!ServletFileUpload.isMultipartContent(request)) {
			// 如果不是则停止/stop if is is not multimedia uploading
			System.out.println("Error: 表单必须包含/sheet must include enctype=multipart/form-data");
			return null;
		}*/

		String filePath = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 创建OSSClient实例/case of creating OSSClient
		OSSClient ossClient = new OSSClient(OthersSource.getSourceString("alipay_oss_endpoint"), 
				OthersSource.getSourceString("alipay_oss_access_keyid"), 
				OthersSource.getSourceString("alipay_oss_access_keysecret"));
		try {
			List<FileItem> fileList=upload.parseRequest(request);
			for(FileItem item : fileList){
				if(!item.isFormField() && item.getSize() > 0){
					// 上传文件流/upload file stream
					InputStream inputStream = item.getInputStream();
					String fileName = ValueUtil.getUUIDString()+FileUtil.getFileTypeName(item);
					ossClient.putObject(OthersSource.getSourceString("alipay_oss_bucket_name"), fileName, inputStream);
					filePath = "http://"+OthersSource.getSourceString("alipay_oss_bucket")+"/"+fileName;
					System.out.println("upload file:"+filePath);
				}else if(!StringUtils.isEmpty(item.getString())){
					//set Attribute value
					request.setAttribute(item.getFieldName(), new String(item.getString().getBytes("iso-8859-1"), "utf-8"));
				}
			}

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			// 关闭client/close client
			ossClient.shutdown();
		}

		return filePath;

	}

	
	
	/**
	 * 
	 * @Title:        uploadImages 
	 * @Description:  上传多图片OSS/upload multi images OSS
	 * @param:        @param request
	 * @param:        @return    
	 * @return:       List<ImageVo>    
	 * @author        Albert
	 * @Date          2017年5月10日 下午2:37:17/2:37:17 pm, May 10, 2017
	 */
	public static List<ImageVo> uploadImages(HttpServletRequest request){
		// 检测是否为多媒体上传/check whether it is multimedia uploading or not
	/*	if (!ServletFileUpload.isMultipartContent(request)) {
			// 如果不是则停止/stop if is is not multimedia uploading
			System.out.println("Error: 表单必须包含/sheet must include enctype=multipart/form-data");
			return null;
		}*/

		List<ImageVo> filePaths = new ArrayList<ImageVo>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 创建OSSClient实例/case of creating OSSClient
		OSSClient ossClient = new OSSClient(OthersSource.getSourceString("alipay_oss_endpoint"), 
				OthersSource.getSourceString("alipay_oss_access_keyid"), 
				OthersSource.getSourceString("alipay_oss_access_keysecret"));
		try {
			List<FileItem> fileList=upload.parseRequest(request);
			for(FileItem item : fileList){
				if(!item.isFormField() && item.getSize() > 0){
					// 上传文件流/upload file stream
					InputStream inputStream = item.getInputStream();
					//get image size
					BufferedImage img = javax.imageio.ImageIO.read(inputStream);
					//can't use inputstream after get image size,need init inputstream from BufferedImage
					ByteArrayOutputStream os = new ByteArrayOutputStream();  
					String fileName = ValueUtil.getUUIDString()+FileUtil.getFileTypeName(item);
					ImageIO.write(img, fileName.substring(fileName.lastIndexOf(".")+1), os);  

					ossClient.putObject(OthersSource.getSourceString("alipay_oss_bucket_name"), fileName, new ByteArrayInputStream(os.toByteArray()));
					String fileUrl = "http://"+OthersSource.getSourceString("alipay_oss_bucket")+"/"+fileName;
					System.out.println("upload file:"+fileUrl);
					
					
					ImageVo imageVo = new ImageVo(fileUrl, img.getWidth(), img.getHeight());
					
					filePaths.add(imageVo);
				}else if(!StringUtils.isEmpty(item.getString())){
					//set Attribute value
					request.setAttribute(item.getFieldName(), new String(item.getString().getBytes("iso-8859-1"), "utf-8"));
				}
			}



		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			// 关闭client/close client
			ossClient.shutdown();
		}

		return filePaths;

	}

	
	/**
	 * 
	 * @Title:        deleteFile 
	 * @Description:  删除文件/delete file
	 * @param:        @param fileUrl    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月10日 下午6:41:48/6:41:48 pm, May 10, 2017
	 */
	public static void deleteFile(String fileName){
		OSSClient ossClient = new OSSClient(OthersSource.getSourceString("alipay_oss_endpoint"),
				OthersSource.getSourceString("alipay_oss_access_keyid"), 
				OthersSource.getSourceString("alipay_oss_access_keysecret"));

		// 删除Object/delete Object
		ossClient.deleteObject(OthersSource.getSourceString("alipay_oss_bucket_name"), fileName);

		// 关闭client/close client
		ossClient.shutdown();

	}

	public static final String getContentType(String fileName){    
		String fileExtension = fileName.substring(fileName.lastIndexOf("."));  
		if("bmp".equalsIgnoreCase(fileExtension)) return "image/bmp";  
		if("gif".equalsIgnoreCase(fileExtension)) return "image/gif";  
		if("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)  || "png".equalsIgnoreCase(fileExtension) ) return "image/jpeg";  
		if("html".equalsIgnoreCase(fileExtension)) return "text/html";  
		if("txt".equalsIgnoreCase(fileExtension)) return "text/plain";  
		if("vsd".equalsIgnoreCase(fileExtension)) return "application/vnd.visio";  
		if("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) return "application/vnd.ms-powerpoint";  
		if("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) return "application/msword";  
		if("xml".equalsIgnoreCase(fileExtension)) return "text/xml";  
		return "text/html";    
	}
	
	public static void main(String[] args) {
		
	}
}
