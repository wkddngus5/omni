package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.AreaVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;

/**
 * 停车区域接口定义/parking area protocol definations
 * @ClassName:     IAreaDao
 * @Description:TODO
 * @author:    
 * @date:        
 *
 */
public interface IAreaDao {
	String TABLE_NAME="t_area";
	String COLUMN_ID="id";
	String COLUMN_NOTE="note";
	String COLUMN_NAME="name";
	String COLUMN_DETAIL="detail";
	
	/**
	 * 
	 * @Title:        getAreaList 
	 * @Description:  停车区域列表获取（前台）/parking area list obtain(front desk)
	 * @param:        @return    
	 * @return:       List<AreaVo>    
	 * @author        Albert
	 * @Date          2017年4月11日 下午4:57:30
	 */
	List<AreaVo> getAreaList(int cityId);
	
	/**
	 * 
	 * @Title:        getAreaList 
	 * @Description:  TODO
	 * @param:        @param ids
	 * @param:        @return    
	 * @return:       List<AreaVo>    
	 * @author        Albert
	 * @Date          2017年7月18日 下午6:13:45
	 */
	List<AreaVo> getAreaList(String ids,double lng,double lat,int cityId);
	
	/**
	 * 
	 * @Title:        getAreaList 
	 * @Description:  停车区域列表获取（后台）/parking area list obtain(backstage)
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<AreaVo>    
	 * @author        Albert
	 * @Date          2017年7月6日 下午7:05:05
	 */
	List<AreaVo> getAreaList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getAreaList 
	 * @Description:  Get near area list
	 * @param:        @param cityId
	 * @param:        @param position
	 * @param:        @return    
	 * @return:       List<AreaVo>    
	 * @author        Albert
	 * @Date          2017年10月23日 上午11:11:21
	 */
	List<AreaVo> getAreaList(int cityId,LatLng position,int type);
	
	
	/**
	 * 
	 * @Title:        findById 
	 * @Description:  查找区域/find area
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       AreaVo    
	 * @author        Albert
	 * @Date          2017年7月6日 下午7:01:32
	 */
	AreaVo findById(int id);
	
	/**
	 * 
	 * @Title:        addArea 
	 * @Description:  新增区域/add new area
	 * @param:        @param area
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月6日 下午7:01:19
	 */
	String addArea(AreaVo area);


	/**
	 * 
	 * @Title:        updateArea 
	 * @Description:  修改停车位/modify parking position
	 * @param:        @param areaVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月6日 下午7:06:01
	 */
	boolean updateArea(AreaVo areaVo);
}
