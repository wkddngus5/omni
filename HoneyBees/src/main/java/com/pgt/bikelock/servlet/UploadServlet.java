package com.pgt.bikelock.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.AliOSSUtil;
import com.pgt.bikelock.utils.AwsS3Util;
import com.pgt.bikelock.utils.FileUtil;

/**
 * 
 * @ClassName:     UploadServlet
 * @Description:APP文件上传/app file upload
 * @author:    Albert
 * @date:        2017年7月15日 上午11:27:45
 *
 */
public class UploadServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String filePath = null;
		if(OthersSource.getSourceString("alipay_oss_access_keyid") != null){
			filePath = AliOSSUtil.uploadFile(request);
		}else if(OthersSource.getSourceString("aws_s3_access_keyid") != null){
			filePath = AwsS3Util.uploadFile(request);
		}else{
			//默认上传/default upload
			filePath = FileUtil.defalutUploadImage(request);
		}
		setData(response, filePath);
	}


	public void doPost2(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.create a DiskFileItemFactory  
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(8192) ; 
		factory.setRepository(new File(OthersSource.EXPORT_FILE_PATH));
		// 2.创建一个文件上传解析器/create a file upload parser
		ServletFileUpload upload = new ServletFileUpload();
		// 设置单个文件的最大上传值/set up unit file biggest upload value
		upload.setSizeMax(FileUtil.MAX_SIZE);  // 文件上传上限10G
		upload.setFileItemFactory(factory);
		try {
			List<FileItem> list=upload.parseRequest(request);
			for(FileItem item : list){
				if(item.isFormField() && !StringUtils.isEmpty(item.getString())){
					//set Attribute value
					request.setAttribute(item.getFieldName(), new String(item.getString().getBytes("iso-8859-1"), "utf-8"));
				}else{
					String fileName = System.currentTimeMillis()+FileUtil.getFileTypeName(item);
					File savedFile = new File(OthersSource.EXPORT_FILE_PATH, fileName);  
					savedFile.setWritable(true,false);
					item.write(savedFile);  
					String fileUrl = String.format("http://%s:%s/%s/%s",request.getServerName(),request.getServerPort(),"File",fileName);
					System.out.println("url="+fileUrl);
					setData(response, fileUrl);
					//删除临时目录的临时文件/delete temporary catalogue temporary file
					item.delete();
				}
				
			}
		} catch (FileUploadException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
