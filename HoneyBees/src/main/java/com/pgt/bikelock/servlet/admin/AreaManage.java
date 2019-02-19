package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IAreaDao;
import com.pgt.bikelock.dao.ICityDao;
import com.pgt.bikelock.dao.impl.AreaDaoImpl;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.AMapUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.AreaVo;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;

public class AreaManage extends BaseManage {

	private static final long serialVersionUID = 2L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
		setModelParams(getCurrentLangValue("bike_area_title"), "area_list");
		switch (getRequestType(request)) {
		case 80001:
			getBikeAreaList(request,response);
			break;
		case 80002:
			toEditBikeArea(request,response);
			break;
		case 80004:
			previewArea(request,response);
			break;
		case 80005:
			changeCurrentCity(request, response);
			break;
		case 80006:
			getCityList(request, response);
			break;
		case 80007:
			toEditCity(request, response);
			break;
		}


	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);
		setModelParams(getCurrentLangValue("bike_area_title"), "area_list");
		switch (getRequestType(request)) {
		case 80001:
			getBikeAreaList(request,response);
			break;
		case 80002:
			editBikeArea(request,response);
			break;
		case 80003:
			deleteBikeArea(request,response);
			break;
		case 80006:
			getCityList(request, response);
			break;
		case 80007:
			editCity(request, response);
			break;
		case 80008:
			deleteCity(request, response);
			break;
		}


	}

	/**
	 * 80001
	 * @Title:        getBikeAreaList 
	 * @Description:  停车区域获取/parking area gain
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 上午11:35:25
	 */
	private void getBikeAreaList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		RequestListVo requestVo = new RequestListVo(req,false,true);
		if(req.getParameter("lookup") != null){
			requestVo.setType(1);//only show parking area
		}
		List<AreaVo> areaList = new AreaDaoImpl().getAreaList(requestVo);
		req.setAttribute("areaList",areaList);
		requestVo.setTotalCount(BaseDao.getCount(IAreaDao.TABLE_NAME,requestVo,new String[]{IAreaDao.COLUMN_NAME,IAreaDao.COLUMN_NOTE}));
		
		if(req.getParameter("lookup") != null){
			boolean only = false;
			if(req.getParameter("only") != null){
				only = true;
			}
			req.setAttribute("only", only);
			req.setAttribute("lookup", true);
			returnDataList(req, resp, requestVo,"area/areaListLookup.jsp");
		}else{
			req.setAttribute("lookup", false);
			returnDataList(req, resp, requestVo,"area/areaList.jsp");
		}
		
		
		
	}

	/**
	 * 80002
	 * get
	 * @Title:        toEditBikeArea 
	 * @Description:  加载编辑停车位页/loading edit parking position page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 上午11:37:26
	 */
	private void toEditBikeArea(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		if(!StringUtils.isEmpty(req.getParameter("id"))){
			AreaVo areaVo = new AreaDaoImpl().findById(ValueUtil.getInt(req.getParameter("id")));
			if(areaVo != null){
				req.setAttribute("areaVo",areaVo);
				req.setAttribute("centerLat",areaVo.getLat());
				req.setAttribute("centerLng",areaVo.getLng());
				req.setAttribute("tagCityId", areaVo.getCity_id());
				if(!StringUtils.isEmpty(areaVo.getDetail())){
					List<LatLng> list=AMapUtil.decode(areaVo.getDetail());
					req.setAttribute("latLngList",list);
				}
			}

		}else{
			setDefaultLocation(req, resp);
		}
		
		req.getRequestDispatcher("area/editAreaMap.jsp").forward(req, resp);
	}

	/**
	 * 80002
	 * post
	 * @Title:        editBikeArea 
	 * @Description:  新增、编辑停车位/add, edit parking position
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 上午11:40:36
	 */
	private void editBikeArea(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String[] parms = new String[]{"name","detail","cityId","type"};
		if(!checkRequstParams(request, response, parms)){
			return;
		}
		
		IAreaDao areaDao = new AreaDaoImpl();
		AreaVo areaVo = new AreaVo(parms,request);
		DelType delType;
		boolean flag = false;
		if(areaVo.getId() != 0){
			flag = areaDao.updateArea(areaVo);
			delType = DelType.DelType_Update;
		}else{
			String areaId = areaDao.addArea(areaVo);
			if(areaId != null){
				addLogForAddData(request, response, areaId);
				flag = true;
			}
			delType = DelType.DelType_Add;
		}		
		
		returnAjaxData(response, getReturnMessage(delType, flag));
	}

	/**
	 * 80003
	 * @Title:        deleteBikeArea 
	 * @Description:  删除停车位/delete parking area
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 上午11:43:37
	 */
	private void deleteBikeArea(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(request, response, parms)){
			return;
		}
		boolean flag = BaseDao.deleteRecord(IAreaDao.TABLE_NAME, request.getParameter(parms[0]));
		returnData(response, DelType.DelType_Delete,flag);
	}

	/**
	 * 80004
	 * @Title:        previewArea 
	 * @Description:  预览区域/preview area
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 上午11:44:59
	 */
	private void previewArea(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String[] parms = new String[]{"id","type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		int id = ValueUtil.getInt(req.getParameter(parms[0]));
		int type = ValueUtil.getInt(req.getParameter(parms[1]));
		List<LatLng> list =null ;
		if(type == 1){//车位区域预览/parking area preview
			AreaVo area = new AreaDaoImpl().findById(id);
			list=AMapUtil.decode(area.getDetail());
			req.setAttribute("area",area);
			req.setAttribute("mapZoom",19);
			 
		}else if(type == 2){//城市区域预览/city area preview
			CityVo cityVo = new CityDaoImpl().getCityInfo(req.getParameter(parms[0]));
			list=AMapUtil.decode(cityVo.getArea_detail());
			req.setAttribute("area",cityVo);
			req.setAttribute("mapZoom",10);
			
			//parking area
			List<AreaVo> areaList = new AreaDaoImpl().getAreaList(ValueUtil.getInt(cityVo.getId()));
			List<Object> areaLine = new ArrayList<Object>();
			for (AreaVo areaVo : areaList) {
				Map<String, Object> areaMap = new HashMap<String, Object>();
				areaMap.put("path", AMapUtil.decode(areaVo.getDetail()));
				areaMap.put("icon", AreaVo.parkingTypeIcon[areaVo.getType()-1]);
				areaMap.put("lat", areaVo.getLat());
				areaMap.put("lng", areaVo.getLng());
				areaMap.put("color", areaVo.getType()==2?"#FF0000":"#0ABBB5");
				areaLine.add(areaMap);
			}
			if(areaLine.size() > 0){
				req.setAttribute("areaLine",areaLine);
			}
		}
		if(list.size() > 0){
			req.setAttribute("centerLat",list.get(0).latitude);
			req.setAttribute("centerLng",list.get(0).longitude);
		}else{
			setDefaultLocation(req, resp);
		}
		req.setAttribute("latLngList",list);
		req.getRequestDispatcher("area/areaMap.jsp").forward(req, resp);

	}

	/**
	 * 80005
	 * @Title:        changeCurrentCity 
	 * @Description:  改变当前城市/change current city
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月28日 下午3:22:36
	 */
	protected void changeCurrentCity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"cityId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		HttpSession session = req.getSession();
		session.setAttribute("cityId", req.getParameter(parms[0]));
		req.setAttribute("functionList", session.getAttribute("functionList"));
		req.setAttribute("cityList",session.getAttribute("cityList"));
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}

	/**
	 * 80006
	 * @Title:        getCityList 
	 * @Description:  城市列表/city list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 下午5:12:58
	 */
	private void getCityList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		RequestListVo requestVo = new RequestListVo(req,false,false);
		List<CityVo> cityList = new CityDaoImpl().getCityLlist(requestVo);
		req.setAttribute("cityList",cityList);
		requestVo.setTotalCount(BaseDao.getCount(ICityDao.TABLE_NAME,requestVo,new String[]{ICityDao.CLOUMN_ID,ICityDao.CLOUMN_NAME}));
		returnDataList(req, resp, requestVo,"area/cityList.jsp");
	}

	/**
	 * 80007
	 * get
	 * @Title:        toEditCity 
	 * @Description:  编辑城市/edit city
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 下午5:46:00
	 */
	private void toEditCity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		if(!StringUtils.isEmpty(req.getParameter("id"))){
			CityVo cityVo = new CityDaoImpl().getCityInfo(req.getParameter("id"));
			req.setAttribute("cityVo",cityVo);
			if(cityVo.getArea_lat() != 0){
				req.setAttribute("centerLat",cityVo.getArea_lat());
				req.setAttribute("centerLng",cityVo.getArea_lng());
			}
			if(!StringUtils.isEmpty(cityVo.getArea_detail())){
				List<LatLng> list=AMapUtil.decode(cityVo.getArea_detail());
				req.setAttribute("latLngList",list);
			}

		}
		if(req.getAttribute("centerLat") == null){
			setDefaultLocation(req, resp);
		}
		req.getRequestDispatcher("area/editCity.jsp").forward(req, resp);
	}

	/**
	 * 80007
	 * post
	 * @Title:        editCity 
	 * @Description:  编辑城市/edit city
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 下午5:49:42
	 */
	private void editCity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String[] parms = new String[]{"name"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		
		CityVo cityVo = new CityVo(req);
		DelType delType = DelType.DelType_Update;
		boolean flag = false;
		if(StringUtils.isEmpty(cityVo.getId())){
			delType = DelType.DelType_Add;
			String cityId = new CityDaoImpl().addCity(cityVo);
			if(cityId != null){
				addLogForAddData(req, resp, cityId);
				flag = true;
			}
		}else{
			flag = new CityDaoImpl().updateCity(cityVo);
		}
		if(flag){
			//更新城市缓存信息
			List<CityVo> cityList = new CityDaoImpl().getCityLlist(null);
			HttpSession session = req.getSession();
			session.setAttribute("cityList",cityList);
		}
		setModelParams(getCurrentLangValue("bike_city_title"), "bike_city_list");
		returnAjaxData(resp, getReturnMessage(delType, flag));
	}

	/**
	 * 80008
	 * @Title:        deleteCity 
	 * @Description:  删除城市/edit city
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 下午5:52:53
	 */
	private void deleteCity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(request, response, parms)){
			return;
		}
		boolean flag = BaseDao.deleteRecord(ICityDao.TABLE_NAME, request.getParameter(parms[0]));
		if(flag){
			//更新城市缓存信息
			List<CityVo> cityList = new CityDaoImpl().getCityLlist(null);
			HttpSession session = request.getSession();
			session.setAttribute("cityList",cityList);
		}
		setModelParams(getCurrentLangValue("bike_city_title"), "bike_city_list");
		returnData(response, DelType.DelType_Delete, flag);
	}
}
