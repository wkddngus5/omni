/**
 * FileName:     FileUtil.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月6日 上午9:43:23/9:43:23 am, June 6, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.ImageVo;

 /**
 * @ClassName:     FileUtil
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月6日 上午9:43:23/9:43:23 am, June 6, 2017
 *
 */
public class FileUtil {
	
	public final static long MAX_SIZE = 10 * 1024 * 1024 * 1024;// 设置上传文件最大为 10G/max upload file is 10G
	
	/**
	 * 下载文件/download file
	 * @Title:        downloadFile 
	 * @Description:  TODO
	 * @param:        @param fileName
	 * @param:        @param request
	 * @param:        @param response    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月8日 下午4:18:07/4:18:07 pm, June 8, 2017
	 */
	public static void downloadFile(String fileName, HttpServletRequest request,HttpServletResponse response) {
		try {
			String path = OthersSource.EXPORT_FILE_PATH+fileName;
			// path是指欲下载的文件的路径。/path maeans the path of the file to be downloaded
			File file = new File(path);
			// 取得文件名。/get the file name
			String filename = file.getName();
			// 以流的形式下载文件。/download the file in the steam way
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response/clear response
			response.reset();
			// 设置response的Header/set the response header
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String( filename.getBytes("gb2312"), "ISO8859-1" ));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 获取文件类型名/get file type name
	 * @Title:        getFileTypeName 
	 * @Description:  TODO
	 * @param:        @param item
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年6月30日 下午12:07:14/12:07:14 pm, June 30, 2017
	 */
	public static String getFileTypeName(FileItem item){
		String fileName = item.getName();

		/**
		 *  注意：不同的浏览器提交的文件名是不一样的/ note: the file name is different according to the different browser
		 *  有的提交上来的文件名是带有路径的，如 c:/a/1.txt/some submitted files are with pathes, eg c:/a/1.txt/
		 *  有的提交上的只有单纯的文件名，如  1.txt /some submitted files are just file names
		 */
		//处理获取到的文件名，只保留文件名部分/processing the file names, reserve the file name only
		if(fileName.lastIndexOf(".") >= 0){
			fileName = fileName.substring(fileName.lastIndexOf("."));
		}
		
		return fileName;
	}
	
	/**
	 * 
	 * @Title:        uploadImage 
	 * @Description:  上传图片/upload image
	 * @param:        @param request
	 * @param:        @return    
	 * @return:       List<ImageVo>    
	 * @author        Albert
	 * @Date          2017年7月10日 下午5:44:26/5:44:26 pm, July 10, 2017
	 */
	public static List<ImageVo> uploadImage(HttpServletRequest request){
		
		if(OthersSource.getSourceString("alipay_oss_access_keyid") != null){
			return AliOSSUtil.uploadImages(request);
		}else if(OthersSource.getSourceString("aws_s3_access_keyid") != null){
			return AwsS3Util.uploadImage(request);
		}else{
			return defalutUploadImages(request);
		}
		
	}
	
	/**
	 * 
	 * @Title:        defalutUploadImages 
	 * @Description:  默认路径多图上传/default path multi image upload
	 * @param:        @param request
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月10日 下午6:35:25/6:35:25 pm, July 10, 2017
	 */
	public static List<ImageVo> defalutUploadImages(HttpServletRequest request){

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(8192) ; 
		factory.setRepository(new File(OthersSource.EXPORT_FILE_PATH));
		// 2.创建一个文件上传解析器/2.creat a file upload resolver
		ServletFileUpload upload = new ServletFileUpload();
		// 设置单个文件的最大上传值/Set the maximum upload value for a single file
		upload.setSizeMax(MAX_SIZE);  // 文件上传上限10G/max upload is 10G
		upload.setFileItemFactory(factory);
		List<ImageVo> filePaths = new ArrayList<ImageVo>();
		try {
			List<FileItem> list=upload.parseRequest(request);
			for(FileItem item : list){
				if(item.isFormField() && !StringUtils.isEmpty(item.getString())){
					//set Attribute value
					request.setAttribute(item.getFieldName(), new String(item.getString().getBytes("iso-8859-1"), "utf-8"));
				}else {
					// 上传文件流/upload file stream
					InputStream inputStream = item.getInputStream();
					//get image size
					BufferedImage img = javax.imageio.ImageIO.read(inputStream);
					if(img != null){
						//can't use inputstream after get image size,need init inputstream from BufferedImage
						ByteArrayOutputStream os = new ByteArrayOutputStream();  
						String fileName = System.currentTimeMillis()+FileUtil.getFileTypeName(item);
						ImageIO.write(img, fileName.substring(fileName.lastIndexOf(".")+1), os);  

						File savedFile = new File(OthersSource.EXPORT_FILE_PATH, fileName);  
						savedFile.setWritable(true,false);
						item.write(savedFile);  
						String fileUrl = String.format("http://%s:%s/%s/%s",request.getServerName(),request.getServerPort(),"File",fileName);
						System.out.println("url="+fileUrl);
						
						ImageVo imageVo = new ImageVo(fileUrl, img.getWidth(), img.getHeight());
						
						filePaths.add(imageVo);
					}
				}
			}


		} catch (FileUploadException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filePaths;
	}
	
	/**
	 * 
	 * @Title:        defalutUploadImage 
	 * @Description:  默认单图上传/default single image upload
	 * @param:        @param request
	 * @param:        @return
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年8月2日 下午3:55:34/3:55:34 pm, August 2, 2017
	 */
	public static String defalutUploadImage(HttpServletRequest request)
			throws ServletException, IOException {
		// 1.create a DiskFileItemFactory  
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(8192) ; 
		factory.setRepository(new File(OthersSource.EXPORT_FILE_PATH));
		// 2.创建一个文件上传解析器/2.creat a file upload resolver
		ServletFileUpload upload = new ServletFileUpload();
		// 设置单个文件的最大上传值/Set the maximum upload value for a single file
		upload.setSizeMax(FileUtil.MAX_SIZE);  // 文件上传上限10G/max upload is 10G
		upload.setFileItemFactory(factory);
		
		String fileUrl = "";
		
		try {
			List<FileItem> list=upload.parseRequest(request);
			for(FileItem item : list){
				if(!item.getFieldName().equals("file")){
					//set Attribute value
					request.setAttribute(item.getFieldName(), new String(item.getString().getBytes("iso-8859-1"), "utf-8"));
				}else{
					String fileName = ValueUtil.getUUIDString()+FileUtil.getFileTypeName(item);
					File savedFile = new File(OthersSource.EXPORT_FILE_PATH, fileName);  
					savedFile.setWritable(true,false);
					item.write(savedFile);  
					fileUrl = String.format("http://%s:%s/%s/%s",request.getServerName(),request.getServerPort(),"File",fileName);
					System.out.println("url="+fileUrl);
					//删除临时目录的临时文件/delete temporary files in temporary directories
					item.delete();
				}
			
			}
		} catch (FileUploadException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileUrl;
	}
	
	
	/**
	 * 
	 * @Title:        uploadLockFile 
	 * @Description:  上传锁文件
	 * @param:        @param request
	 * @param:        @return
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2018年12月10日 下午4:45:25
	 */
	public static String uploadLockFile(HttpServletRequest request)
			throws ServletException, IOException {
		// 1.create a DiskFileItemFactory  
		String filder = OthersSource.getSourceString("lock_upload_path");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(8192) ; 
		factory.setRepository(new File(filder));
		// 2.创建一个文件上传解析器/2.creat a file upload resolver
		ServletFileUpload upload = new ServletFileUpload();
		// 设置单个文件的最大上传值/Set the maximum upload value for a single file
		upload.setSizeMax(FileUtil.MAX_SIZE);  // 文件上传上限10G/max upload is 10G
		upload.setFileItemFactory(factory);
		
		String fileUrl = "";
		
		try {
			List<FileItem> list=upload.parseRequest(request);
			for(FileItem item : list){
				if(!item.getFieldName().equals("file")){
					//set Attribute value
					request.setAttribute(item.getFieldName(), new String(item.getString().getBytes("iso-8859-1"), "utf-8"));
				}else{
					String fileName = item.getName();// ValueUtil.getUUIDString()+FileUtil.getFileTypeName(item);
					File savedFile = new File(filder, fileName);  
					savedFile.setWritable(true,false);
					item.write(savedFile);  
					fileUrl = String.format("%s%s",filder,fileName);
					System.out.println("url="+fileUrl);
					//删除临时目录的临时文件/delete temporary files in temporary directories
					item.delete();
				}
			
			}
		} catch (FileUploadException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileUrl;
	}
	
	/**
	 * 
	 * @Title:        deleteDefaultFile 
	 * @Description:  Delete local server file
	 * @param:        @param fileName    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月30日 下午1:48:05
	 */
	public static void deleteDefaultFile(String fileName){
		File file = new File(OthersSource.EXPORT_FILE_PATH, fileName);
		file.delete();
	}
	
	/**
	 * 
	 * @Title:        getFileNameByUrl 
	 * @Description:  通过URL获取文件名/get file by URL 
	 * @param:        @param url
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年5月10日 下午6:41:21/6:41:21 pm, May 10, 2017
	 */
	private static String getFileNameByUrl(String url){
		String fileName = "";

		if(url.lastIndexOf("/") >= 0){
			fileName = url.substring(url.lastIndexOf("/")+1);
		}
		return fileName;
	}
	
	/**
	 * 
	 * @Title:        deleteFile 
	 * @Description:  Delete File（Automatic Select Server）
	 * @param:        @param fileUrl    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月30日 下午1:48:59
	 */
	public static void deleteFile(String fileUrl){
		String fileName = getFileNameByUrl(fileUrl);
		if(OthersSource.getSourceString("alipay_oss_access_keyid") != null){
			AliOSSUtil.deleteFile(fileName);
		}else if(OthersSource.getSourceString("aws_s3_access_keyid") != null){
			AwsS3Util.deleteFile(fileName);
		}else{
			deleteDefaultFile(fileName);
		}
	}
}
