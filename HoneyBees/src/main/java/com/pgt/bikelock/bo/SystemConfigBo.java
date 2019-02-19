/**
 * FileName:     SystemConfigBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年5月18日 下午2:49:43
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年5月18日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.impl.SystemConfigDaoImpl;
import com.pgt.bikelock.vo.admin.SystemConfigVo;

/**
 * @ClassName:     SystemConfigBo
 * @Description:System config controller
 * @author:    Albert
 * @date:        2018年5月18日 下午2:49:43
 *
 */
public class SystemConfigBo {


	public static double SYSTEM_CONFIG_NEAR_BIKE_SURROUND = 0.1;//100km
	public static int SYSTEM_CONFIG_NEAR_BIKE_COUNT = 500;
	public static int SYSTEM_CONFIG_NEAR_BIKE_SHOWALL = 0;
	public static int SYSTEM_CONFIG_RIDE_GROUP_MAX_COUNT = 5;
	public static BigDecimal SYSTEM_CONFIG_RIDE_AMOUNT_MAX = new BigDecimal(100);
	public static int SYSTEM_CONFIG_RIDE_ADMIN_END_TIME = 10;

	public static int SYSTEM_CONFIG_ORDER_HEART_SAVE = 0;
	public static int SYSTEM_CONFIG_ORDER_HEART_UPDATE = 0;
	public static int SYSTEM_CONFIG_ORDER_HEART_CHECK_RIDE = 0;
	public static int SYSTEM_CONFIG_ORDER_LOCATION_CHECK_RIDE = 0;

	static SystemConfigBo configBo;
	static Map<String, JSONObject> configMap = new HashMap<String, JSONObject>();


	public static synchronized  SystemConfigBo getInstance(){
		if(configBo == null){

			synchronized (BikeBo.class) {    
				if (configBo == null) {    
					configBo = new SystemConfigBo();
				}    
			}

		}
		return configBo;
	}

	/**
	 * init value
	 */
	public SystemConfigBo(){
		List<SystemConfigVo> configList = new SystemConfigDaoImpl().getConfigList(true);
		for (SystemConfigVo systemConfigVo : configList) {
			if(!StringUtils.isEmpty(systemConfigVo.getKey()) && !StringUtils.isEmpty(systemConfigVo.getConfig())){
				JSONObject configObject = JSON.parseObject(systemConfigVo.getConfig());
				setConfigValue(systemConfigVo.getKey(),configObject);
			}
		}
	}

	/**
	 * 
	 * @Title:        setConfigValue 
	 * @Description:  TODO
	 * @param:        @param key
	 * @param:        @param values    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年5月18日 下午3:19:50
	 */
	public void setConfigValue(String key,JSONObject values){
		
		if(values == null){
			return;
		}
		
		configMap.put(key,values);
		
			if("SYSTEM_CONFIG_NEAR_BIKE".equals(key)){
			if(values.getDoubleValue("SYSTEM_CONFIG_NEAR_BIKE_SURROUND") != SYSTEM_CONFIG_NEAR_BIKE_SURROUND){
				SYSTEM_CONFIG_NEAR_BIKE_SURROUND = values.getDoubleValue("SYSTEM_CONFIG_NEAR_BIKE_SURROUND");
			}
			if(values.getIntValue("SYSTEM_CONFIG_NEAR_BIKE_COUNT") != SYSTEM_CONFIG_NEAR_BIKE_COUNT){
				SYSTEM_CONFIG_NEAR_BIKE_COUNT = values.getIntValue("SYSTEM_CONFIG_NEAR_BIKE_COUNT");
			}
			if(values.getIntValue("SYSTEM_CONFIG_NEAR_BIKE_SHOWALL") != SYSTEM_CONFIG_NEAR_BIKE_SHOWALL){
				SYSTEM_CONFIG_NEAR_BIKE_SHOWALL = values.getIntValue("SYSTEM_CONFIG_NEAR_BIKE_SHOWALL");
			}
		}else if("SYSTEM_CONFIG_RIDE".equals(key)){
			if(values.getIntValue("SYSTEM_CONFIG_RIDE_GROUP_MAX_COUNT") != SYSTEM_CONFIG_RIDE_GROUP_MAX_COUNT){
				SYSTEM_CONFIG_RIDE_GROUP_MAX_COUNT = values.getIntValue("SYSTEM_CONFIG_RIDE_GROUP_MAX_COUNT");
				//如要在测试环境测试多骑行，请把此设置为大于1
//				SYSTEM_CONFIG_RIDE_GROUP_MAX_COUNT = 5;
			}else if(values.getBigDecimal("SYSTEM_CONFIG_RIDE_AMOUNT_MAX").compareTo(SYSTEM_CONFIG_RIDE_AMOUNT_MAX) != 0){
				SYSTEM_CONFIG_RIDE_AMOUNT_MAX = values.getBigDecimal("SYSTEM_CONFIG_RIDE_AMOUNT_MAX");
			}else if(values.getInteger("SYSTEM_CONFIG_RIDE_ADMIN_END_TIME")  != SYSTEM_CONFIG_RIDE_ADMIN_END_TIME){
				SYSTEM_CONFIG_RIDE_ADMIN_END_TIME = values.getInteger("SYSTEM_CONFIG_RIDE_ADMIN_END_TIME");
			}
		}else if("SYSTEM_CONFIG_ORDER_HEART".equals(key)){
			if(values.getIntValue("SYSTEM_CONFIG_ORDER_HEART_SAVE") != SYSTEM_CONFIG_ORDER_HEART_SAVE){
				SYSTEM_CONFIG_ORDER_HEART_SAVE = values.getIntValue("SYSTEM_CONFIG_ORDER_HEART_SAVE");
			}else if(values.getIntValue("SYSTEM_CONFIG_ORDER_HEART_UPDATE") != SYSTEM_CONFIG_ORDER_HEART_UPDATE){
				SYSTEM_CONFIG_ORDER_HEART_UPDATE = values.getIntValue("SYSTEM_CONFIG_ORDER_HEART_UPDATE");
			}else if(values.getInteger("SYSTEM_CONFIG_ORDER_HEART_CHECK_RIDE") != SYSTEM_CONFIG_ORDER_HEART_CHECK_RIDE){
				SYSTEM_CONFIG_ORDER_HEART_CHECK_RIDE = values.getInteger("SYSTEM_CONFIG_ORDER_HEART_CHECK_RIDE");
			}else if(values.getInteger("SYSTEM_CONFIG_ORDER_LOCATION_CHECK_RIDE") != SYSTEM_CONFIG_ORDER_LOCATION_CHECK_RIDE){
				SYSTEM_CONFIG_ORDER_LOCATION_CHECK_RIDE = values.getInteger("SYSTEM_CONFIG_ORDER_LOCATION_CHECK_RIDE");
			}
		}
	}


}
