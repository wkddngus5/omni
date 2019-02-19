package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.AliOSSUtil;
import com.pgt.bikelock.utils.AwsS3Util;
import com.pgt.bikelock.utils.FileUtil;
import com.stripe.model.FileUpload;


/**
 * 
 * @ClassName:     UploadServlet
 * @Description:后台文件上传(编辑器)/background file upload
 * @author:    Albert
 * @date:        2017年7月15日 上午11:28:15
 *
 */
public class AdminUploadServlet extends BaseManage {

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
		
		//返回路径/back routline
		if(filePath != null){
			PrintWriter out;
			try {
				out = response.getWriter();
				JSONObject object = new JSONObject();
				object.put("err", 0);
				object.put("msg", filePath);
				out.write(object.toJSONString());
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	


}
