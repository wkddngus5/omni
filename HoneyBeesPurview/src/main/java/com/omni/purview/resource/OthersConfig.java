/**
 * FileName:     OthersConfig.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月18日 下午5:16:31
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月18日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.resource;

import java.util.ResourceBundle;

 /**
 * @ClassName:     OthersConfig
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月18日 下午5:16:31
 *
 */
public class OthersConfig {
	
	/**
	 * 
	 * @Title:        getSourceString 
	 * @Description:  获取资源
	 * @param:        @param key
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年5月12日 下午6:26:10
	 */
	public static String getSourceString(String key){
		ResourceBundle rb = ResourceBundle.getBundle("jdbc");
		if(rb.containsKey(key)){
			return rb.getString(key);
		}
		return null;
	}
}
