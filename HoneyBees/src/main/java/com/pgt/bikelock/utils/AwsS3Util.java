/**
 * FileName:     AwsS3Util.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月21日 下午6:11:40/6:11:40 pm, June 21, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月21日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alipay.api.internal.util.StringUtils;
import com.aliyun.oss.OSSClient;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.ImageVo;

/**
 * @ClassName:     AwsS3Util
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月21日 下午6:11:40/6:11:40 pm, June 21, 2017
 *
 */
public class AwsS3Util {
	
	/**
	 * 
	 * @Title:        uploadFile 
	 * @Description:  S3文件上传/upload file S3
	 * @param:        @param request
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月15日 上午11:15:42/11:15:42 am, July 15, 2017
	 */
	public static String uploadFile(HttpServletRequest request){
		// 检测是否为多媒体上传/check whether it is multimedia uploading or not
	/*	if (!ServletFileUpload.isMultipartContent(request)) {
			// 如果不是则停止/stop if it is not multimedia uploading
			System.out.println("Error: 表单必须包含/sheet must include enctype=multipart/form-data");
			return null;
		}*/

		String filePath = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 创建s3实例/creat S3 case
		ClientConfiguration configuration = new ClientConfiguration();
		configuration.setConnectionTimeout(60 * 1000);// 连接超时，默认60秒/connection overtime, default 60s
		configuration.setSocketTimeout(60 * 1000);// socket超时，默认60秒/socket overtime, default 60s
		configuration.setMaxConnections(5);// 最大并发请求书，默认5个,/Maximum concurrent request, default 5
		configuration.setMaxErrorRetry(2);// 失败后最大重试次数，默认2次/maximum number of retries after failure, default 2 times
		AmazonS3 s3 =  new AmazonS3Client(new BasicAWSCredentials(OthersSource.getSourceString("aws_s3_access_keyid"),
				OthersSource.getSourceString("aws_s3_access_keysecret")),configuration);

		String s3Url = OthersSource.getSourceString("aws_s3_url");
		s3.setRegion(getRegion(s3Url));
		String bucketName = OthersSource.getSourceString("aws_s3_image_buckname");
		try {
			List<FileItem> fileList=upload.parseRequest(request);
			for(FileItem item : fileList){
				if(!item.isFormField() && item.getSize() > 0){
					// 上传文件流/upload file stream
					InputStream inputStream = item.getInputStream();
					String fileName = ValueUtil.getUUIDString()+FileUtil.getFileTypeName(item);
					ObjectMetadata omd = new ObjectMetadata();
					omd.setContentType(item.getContentType());
					omd.setContentLength(item.getSize());
					omd.setHeader("filename", item.getName());

					S3Object s3Object = new S3Object();
					ByteArrayInputStream bis = new ByteArrayInputStream(item.get());
					s3Object.setObjectContent(bis);
					s3.putObject(new PutObjectRequest(bucketName, fileName, bis, omd));
					s3Object.close();
					filePath = s3Url+OthersSource.getSourceString("aws_s3_image_buckname")+"/"+fileName;
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
	
		}

		return filePath;

	}
	
	/**
	 * 
	 * @Title:        createS3Client 
	 * @Description:  Create S3 Client
	 * @param:        @return    
	 * @return:       AmazonS3    
	 * @author        Albert
	 * @Date          2017年9月30日 下午1:44:45
	 */
	public static AmazonS3 createS3Client(){
		// 创建s3实例
		ClientConfiguration configuration = new ClientConfiguration();
		configuration.setConnectionTimeout(60 * 1000);// 连接超时，默认60秒/connection overtime, default 60s
		configuration.setSocketTimeout(60 * 1000);// socket超时，默认60秒/socket超时，默认60秒/socket overtime, default 60s
		configuration.setMaxConnections(5);// 最大并发请求书，默认5个/Maximum concurrent request, default 5
		configuration.setMaxErrorRetry(2);// 失败后最大重试次数，默认2次/maximum number of retries after failure, default 2 times
		AmazonS3 s3 =  new AmazonS3Client(new BasicAWSCredentials(OthersSource.getSourceString("aws_s3_access_keyid"),
				OthersSource.getSourceString("aws_s3_access_keysecret")),configuration);
		return s3;
	}
	
	/**
	 * 
	 * @Title:        uploadImage 
	 * @Description:  上传图片S3/upload image S3
	 * @param:        @param request
	 * @param:        @return    
	 * @return:       List<ImageVo>    
	 * @author        Albert
	 * @Date          2017年5月10日 下午2:37:17/2:37:17 pm, May 10, 2017
	 */
	public static List<ImageVo> uploadImage(HttpServletRequest request){
		// 检测是否为多媒体上传/check whether it is multimedia uploading or not
		/*	if (!ServletFileUpload.isMultipartContent(request)) {
			// 如果不是则停止/stop if it is not multimedia uploading
			System.out.println("Error: 表单必须包含/sheet must include enctype=multipart/form-data");
			return null;
		}*/

		List<ImageVo> filePaths = new ArrayList<ImageVo>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		AmazonS3 s3 =  createS3Client();

		String s3Url = OthersSource.getSourceString("aws_s3_url");
		s3.setRegion(getRegion(s3Url));
		String bucketName = OthersSource.getSourceString("aws_s3_image_buckname");
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

					ObjectMetadata omd = new ObjectMetadata();
					omd.setContentType(item.getContentType());
					omd.setContentLength(item.getSize());
					omd.setHeader("filename", item.getName());

					S3Object s3Object = new S3Object();
					ByteArrayInputStream bis = new ByteArrayInputStream(item.get());
					s3Object.setObjectContent(bis);
					s3.putObject(new PutObjectRequest(bucketName, fileName, bis, omd));
					s3Object.close();

					String fileUrl = s3Url+OthersSource.getSourceString("aws_s3_image_buckname")+"/"+fileName;
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
		}
		return filePaths;

	}
	
	/**
	 * 
	 * @Title:        getRegion 
	 * @Description:  获取S3地区
	 * @param:        @param s3url
	 * @param:        @return    
	 * @return:       Regions    
	 * @author        Albert
	 * @Date          2017年9月30日 上午11:46:46
	 */
	private static Region getRegion(String s3url){
		String regionStr = "";
		Regions regions = Regions.US_EAST_2;
		try {
			regionStr = s3url.split("://")[1].split(".amazonaws")[0];
		} catch (Exception e) {
			// TODO: handle exception
		}
		if("s3.us-east-2".equals(regionStr)){
			regions = Regions.US_EAST_2;
		}else if("s3-us-west-1".equals(regionStr)){
			regions = Regions.US_WEST_1;
		}else if("s3.eu-central-1".equals(regionStr)){
			regions = Regions.EU_CENTRAL_1;
		}
		return Region.getRegion(regions);
	}
	
	/**
	 * 
	 * @Title:        deleteFile 
	 * @Description:  Delete S3 File
	 * @param:        @param fileName    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月30日 下午1:45:41
	 */
	public static void deleteFile(String fileName){
		AmazonS3 s3 =  createS3Client();
		s3.deleteObject(new DeleteObjectRequest(OthersSource.getSourceString("aws_s3_image_buckname"), fileName));

	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().toUpperCase());
	}
}
