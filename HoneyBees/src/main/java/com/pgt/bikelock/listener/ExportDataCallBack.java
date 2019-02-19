/**
 * FileName:     ExportDataCallBack.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月8日 下午5:41:06
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月8日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialziation
 */
package com.pgt.bikelock.listener;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

 /**
 * @ClassName:     ExportDataCallBack
 * @Description:导出数据接口/export the data interface
 * @author:    Albert
 * @date:        2017年6月8日 下午5:41:06
 *
 */
public interface ExportDataCallBack {
	
	
	/**
	 * 
	 * @Title:        setExportData 
	 * @Description:  设置数据/set up data
	 * @param:        @param sheet
	 * @param:        @param row
	 * @param:        @param list    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月8日 下午5:41:47
	 */
	void setExportData(HSSFSheet sheet,HSSFRow row,Object list);
}
