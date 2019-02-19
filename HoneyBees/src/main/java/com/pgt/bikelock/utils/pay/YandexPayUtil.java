/**
 * FileName:     YandexPayUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年8月11日 下午1:55:17/1:55:17 pm, August 11, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年8月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils.pay;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.formula.functions.T;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alipay.api.internal.util.StringUtils;

/**
 * @ClassName:     YandexPayUtil
 * @Description:Yandex支付工具类/Yandex payment tool
 * @author:    Albert
 * @date:        2017年8月11日 下午1:55:17/1:55:17 pm, August 11, 2017
 *
 */
public class YandexPayUtil {
	//通用/universal
	static final String PARAM_INVOICE_ID = "invoiceId";
	static final String PARAM_SHOP_ID = "shopId";

	//请求/request
	static final String PARAM_REQUEST_DATE = "requestDatetime";

	//响应/response
	static final String ROOT_CHECK_ORDER = "checkOrderResponse";
	static final String PARAM_RESPONSE_DATE = "performedDatetime";
	static final String PARAM_RESPONSE_CODE = "code";

	/**
	 * 
	 * @Title:        checkOrder 
	 * @Description:  校验订单/verified order
	 * @param:        @param resp
	 * @param:        @param reqParams    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月11日 下午4:53:23/4:53:23 pm, August 11, 2017
	 */
	public static void checkOrder(HttpServletResponse resp,Map<String, String> reqParams){

	}
	
	/**
	 * 
	 * @Title:        cancelOrder 
	 * @Description:  取消支付/cancel payment
	 * @param:        @param resp
	 * @param:        @param reqParams    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月11日 下午4:53:31/4:53:31 pm, August 11, 2017
	 */
	public static void cancelOrder(HttpServletResponse resp,Map<String, String> reqParams){

	}
	
	/**
	 * 
	 * @Title:        paymentAviso 
	 * @Description:  支付成功/payment success
	 * @param:        @param resp
	 * @param:        @param reqParams    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月11日 下午4:53:42/4:53:42 pm, August 11, 2017
	 */
	public static void paymentAviso(HttpServletResponse resp,Map<String, String> reqParams){

	}


	/**
	 * 
	 * @Title:        responseSuccess 
	 * @Description:  成功响应/reponse success
	 * @param:        @param rootName 响应类型节点名/node name of the response type
	 * @param:        @param reqParams 请求参数/request parameter   
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月11日 下午4:09:59/4:09:59 pm, August 11, 2017
	 */
	public static void responseSuccess(HttpServletResponse resp,String rootName,Map<String, String> reqParams){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PARAM_RESPONSE_DATE, reqParams.get(PARAM_REQUEST_DATE));
		map.put(PARAM_INVOICE_ID, reqParams.get(PARAM_INVOICE_ID));
		map.put(PARAM_SHOP_ID, reqParams.get(PARAM_SHOP_ID));
		map.put(PARAM_RESPONSE_CODE, "0");
		responseInfo(resp,rootName, map);
	}

	/**
	 * 
	 * @Title:        responseFail 
	 * @Description:  失败响应/response fail
	 * @param:        @param rootName 响应类型节点名/node name of the response type
	 * @param:        @param reqParams 请求参数/request parameter   
	 * @param:        @param message 失败说明（必填）/fail description (must fill)
	 * @param:        @param techMessage 失败备注（选填）fail remarks (fill for option)
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月11日 下午4:12:58/4:12:58 pm, August 11, 2017
	 */
	public static void responseFail(HttpServletResponse resp,String rootName,Map<String, String> reqParams,String message,String techMessage){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PARAM_RESPONSE_DATE, reqParams.get(PARAM_REQUEST_DATE));
		map.put(PARAM_INVOICE_ID, reqParams.get(PARAM_INVOICE_ID));
		map.put(PARAM_SHOP_ID, reqParams.get(PARAM_SHOP_ID));
		map.put(PARAM_RESPONSE_CODE, "1");
		map.put("message", message);
		if(techMessage != null){
			map.put("techMessage", techMessage);
		}
		responseInfo(resp,rootName, map);
	}

	/**
	 * 
	 * @Title:        getRequestInfo 
	 * @Description:  解析请求参数/resolve the request parameter
	 * @param:        @param node
	 * @param:        @return    
	 * @return:       Map<String,String>    
	 * @author        Albert
	 * @Date          2017年8月11日 下午3:54:47/3:54:47 pm, August 11, 2017 
	 */
	public static Map<String, String> getRequestInfo(Element node){
		Map<String, String> params = new HashMap<String, String>(0); 
		// 获取当前节点的所有属性节点/get all the attribute nodes under the current nodes 
		List<Attribute> list = node.attributes();  
		// 遍历属性节点/traverse attribute nodes   
		for (Attribute attribute : list) {
			params.put(attribute.getName(),attribute.getText());  
		}
		return params;
	}

	/**
	 * 
	 * @Title:        responseInfo 
	 * @Description:  组装响应参数/assemble response parameter
	 * @param:        @param paramsMap
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年8月11日 下午3:55:05/3:55:05 pm, August 11, 2017 
	 */
	private static String responseInfo(HttpServletResponse resp,String rootName,Map<String, Object> paramsMap){
		StringBuffer xmlString = new StringBuffer("");
		if (paramsMap != null && paramsMap.size() > 0) {  
			Map<String, Object> params = new TreeMap<String, Object>(new Comparator<String>() {  
				public int compare(String s1, String s2) {  
					return s1.compareTo(s2);  
				}  
			});  
			params.putAll(paramsMap);
			xmlString.append("<"+rootName);
			for (Entry<String, Object> param : params.entrySet()) {  
				if (!"key".equals(param.getKey())) {  
					xmlString.append(" "+param.getKey() + " = ").append("\""+param.getValue()+"\"");  
				}  
			}

			xmlString.append(" />");  
			System.out.println("response:"+xmlString);

		}
		if(resp != null){
			PrintWriter out;
			try {
				out = resp.getWriter();
				out.write(xmlString.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return xmlString.toString();
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("requestDatetime", "2017年8月11日14:48:39");
		map.put("invoiceId", "1234567");
		try {
			Document document = DocumentHelper.parseText(responseInfo(null,ROOT_CHECK_ORDER,map));
			Map<String, String> params = getRequestInfo(document.getRootElement());
			System.out.println(params.get("requestDatetime"));
			System.out.println(params.get("invoiceId"));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
