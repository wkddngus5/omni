/**
 * FileName:     PurviewManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月1日 上午9:39:09
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月1日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.alipay.api.internal.util.StringUtils;
import com.omni.purview.dao.IGroupFunctionDao;
import com.omni.purview.dao.IUserGroupDao;
import com.omni.purview.dao.impl.FunctionDaoImpl;
import com.omni.purview.dao.impl.GroupDaoImpl;
import com.omni.purview.dao.impl.GroupFunctionDaoImpl;
import com.omni.purview.dao.impl.UserGroupDaoImpl;
import com.omni.purview.vo.FunctionVo;
import com.omni.purview.vo.GroupVo;
import com.pgt.bikelock.dao.INotificationMappingDao;
import com.pgt.bikelock.dao.impl.NotificationMappingDaoImpl;
import com.pgt.bikelock.listener.ExportDataCallBack;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.NotificationMappingVo;

/**
 * 接口号起点100/interface number start point 100
 * @ClassName:     PurviewManage
 * @Description:权限控制/right control
 * @author:    Albert
 * @date:        2017年6月1日 上午9:39:09
 *
 */
public class PurviewManage extends BaseManage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
		setModelParams(getCurrentLangValue("system_config_manager_group_list"), "purview_list");
		switch (getRequestType(request)) {
		case 101:
			getGroupList(request, response);
			break;
		case 102:
			toEditGroup(request, response);
			break;
		case 104:
			toEditUserGroup(request, response);
		default:
			break;
		}
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
		setModelParams(getCurrentLangValue("system_config_manager_group_list"), "purview_list");
		switch (getRequestType(request)) {
		case 101:
			getGroupList(request, response);
			break;
		case 102:
			editGroup(request, response);
			break;
		case 103:
			deleteGroup(request, response);
			break;
		case 104:
			editUserGroup(request, response);
			break;
		default:
			break;
		}

	}


	/**
	 * 101
	 * @Title:        getGroupList 
	 * @Description:  用户组列表/user item list
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月3日 下午5:12:37
	 */
	private void getGroupList(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		List<GroupVo> groupList = new GroupDaoImpl().getGroupList(null,getCityId(request));
		
		if(request.getParameter("export") != null){//导出/export
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("common_name_title"),getCurrentLangValue("common_note_title")
					,getCurrentLangValue("common_date_title")},
					groupList,request,response,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<GroupVo> groupList = (List<GroupVo>)list;
					for (int i = 0; i < groupList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						GroupVo groupVo = groupList.get(i);  
						//创建单元格，并设置值/create unit excel and set up value  
						row.createCell( 0).setCellValue(groupVo.getId());  
						row.createCell(1).setCellValue(groupVo.getName());  
						row.createCell(2).setCellValue(groupVo.getNote());  
						row.createCell(3).setCellValue(groupVo.getDate());  
					}  
				}
			});
		}else{
			request.setAttribute("groupList",groupList);
			returnDataList(request, response, null,"purview/groupList.jsp");
		}

	}
	
	/**
	 * 102
	 * get
	 * @Title:        toEditGroup 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月3日 下午5:46:15
	 */
	private void toEditGroup(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{

		List<String> groupFuncIdList = null;
		List<NotificationMappingVo> notifyList = new ArrayList<NotificationMappingVo>();
		String[] notifyTypeArr = getCurrentLangValue("notification_management_config_type").split(",");
		for (int i = 1; i < notifyTypeArr.length; i++) {
			notifyList.add(new NotificationMappingVo(notifyTypeArr[i],i));
		}
		
		HttpServletRequest httpRequest=(HttpServletRequest)request;
		HttpSession session=httpRequest.getSession(true);
		//当前管理员权限组
		String userGroup = (String) session.getAttribute("userGroup");
		
		if(request.getParameter("id") != null){//编辑/edit
			int groupId = Integer.parseInt(request.getParameter("id"));

			GroupVo groupVo = new GroupDaoImpl().getGroupInfo(groupId);
			request.setAttribute("groupVo",groupVo);

			//当前权限/current right
			groupFuncIdList = new GroupFunctionDaoImpl().getGroupFunctionIdList(groupId);
			request.setAttribute("funcIds",groupFuncIdList.toString());
			//通知/Notification
			List<String> tempList = new NotificationMappingDaoImpl().getMappingTypeList(groupId+"");
			for (int i = 0; i < notifyList.size(); i++) {
				NotificationMappingVo mappingVo = notifyList.get(i);
				if(tempList.contains(mappingVo.getType()+"")){
					mappingVo.setChecked(true);
					notifyList.set(i, mappingVo);
				}
			}
			request.setAttribute("notifyTypes",tempList.toString());
		}
		//所有权限/current right
		List<FunctionVo> funcList = new ArrayList<FunctionVo>();
		
		if(!StringUtils.isEmpty(userGroup)){
			funcList = new FunctionDaoImpl().getFunctionList(true,groupFuncIdList, LanguageUtil.getDefaultResource(),userGroup);
		}else{
			funcList = new FunctionDaoImpl().getFunctionList(true,groupFuncIdList, LanguageUtil.getDefaultResource());
		}
		
		request.setAttribute("funcList",funcList);
		request.setAttribute("notifyList",notifyList);

		
		request.getRequestDispatcher("purview/editGroupDialog.jsp").forward(request, response);
	}

	/**
	 * 102
	 * post
	 * @Title:        editGroup 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月3日 下午5:46:29
	 */
	private void editGroup(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		if(!adminSecurityCheck(request, response)){
			return;
		}
		
		String[] parms = new String[]{"name","note"};
		if(!checkRequstParams(request, response, parms)){
			return;
		}
		DelType type;
		IGroupFunctionDao functionDao = new GroupFunctionDaoImpl();
		INotificationMappingDao notifyMappingDao = new NotificationMappingDaoImpl();
		
		String[] funcIds = ValueUtil.getTreeIds(request.getParameter("funcIds"));
		List<String> funList = ValueUtil.arrToList(funcIds);
		//check parent id
		funList = new FunctionDaoImpl().checkParentFunction(ValueUtil.listToString(funList), funList);
		
		String[] notifyTypes = ValueUtil.getTreeIds(request.getParameter("notifyTypes"));
		

		GroupVo group = new GroupVo();
		group.setName(request.getParameter(parms[0]));
		group.setNote(request.getParameter(parms[1]));
		group.setCityId(getCityId(request));
		
		if(request.getParameter("id") != null && !"".equals(request.getParameter("id"))){//编辑/edit
			type = DelType.DelType_Update;
			group.setId(Integer.parseInt(request.getParameter("id")));
			//删除旧权限/delete old right
			functionDao.deleteGroupFunction(group.getId());
	
			//删除旧通知/delete old notification
			notifyMappingDao.deleteMapping(group.getId()+"");
			
			new GroupDaoImpl().updateGroup(group);
		}else{
			type = DelType.DelType_Add;
			group.setId(new GroupDaoImpl().addGroup(group));
		}
		String ids = functionDao.addGroupFunction(group.getId(), funList);
		
		if(notifyTypes.length > 0){
			notifyMappingDao.addMapping(group.getId()+"", notifyTypes);
		}
		
		setModelParams(getCurrentLangValue("system_config_manager_group_list"), "purview_group_list");
		returnData(response, type, ids == null ?false:true);
		addLogForAddData(request, response, ids);
	}

	/**
	 * 103
	 * @Title:        deleteGroup 
	 * @Description:  删除权限/delete right
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月3日 下午10:34:41
	 */
	private void deleteGroup(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(request, response, parms)){
			return;
		}
		int groupId = Integer.parseInt(request.getParameter(parms[0]));
		//删除旧权限/delete old right
		boolean flag = new GroupFunctionDaoImpl().deleteGroupFunction(groupId);
		flag = new GroupDaoImpl().deleteGroup(groupId);
		setModelParams(getCurrentLangValue("system_config_manager_group_list"), "purview_group_list");
		returnData(response, DelType.DelType_Delete, flag);
	}

	/**
	 * 104
	 * get
	 * @Title:        toEditUserGroup 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月3日 下午5:46:15
	 */
	private void toEditUserGroup(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{

		String[] parms = new String[]{"id"};
		if(!checkRequstParams(request, response, parms)){
			return;
		}

		int adminId = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("id",adminId);


		//当前权限/current right
		List<String> groupIdList  = new UserGroupDaoImpl().getUserGroupIdList(adminId);
		request.setAttribute("groupIds",groupIdList.toString());

		//所有组/all group
		List<GroupVo> groupList = new GroupDaoImpl().getGroupList(groupIdList,getCityId(request));
		request.setAttribute("groupList",groupList);

		request.getRequestDispatcher("purview/editUserGroupDialog.jsp").forward(request, response);
	}

	/**
	 * 104
	 * post
	 * @Title:        editGroup 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月3日 下午5:46:29
	 */
	private void editUserGroup(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		if(!adminSecurityCheck(request, response)){
			return;
		}
		
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(request, response, parms)){
			return;
		}

		int adminId = Integer.parseInt(request.getParameter(parms[0]));

		DelType type;

		String[] groupIds = ValueUtil.getTreeIds(request.getParameter("groupIds"));

		IUserGroupDao groupDao = new UserGroupDaoImpl();

		type = DelType.DelType_Update;
		//删除旧权限/delete old right
		groupDao.deleteUserGroup(adminId);
		if(groupIds.length > 0){
			String gIds = groupDao.addUserGroup(adminId, groupIds);
		}
		returnData(response, type,true);
		setModelParams(getCurrentLangValue("system_config_manager_group_list"), "admin_list");
		
//		addLogForAddData(request, response, gIds);
	}
}
