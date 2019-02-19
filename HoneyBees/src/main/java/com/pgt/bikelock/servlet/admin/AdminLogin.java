package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.omni.purview.dao.impl.FunctionDaoImpl;
import com.omni.purview.dao.impl.GroupFunctionDaoImpl;
import com.omni.purview.dao.impl.UserGroupDaoImpl;
import com.omni.purview.dao.impl.UserLogDaoImpl;
import com.omni.purview.vo.FunctionVo;
import com.omni.purview.vo.UserLogVo;
import com.pgt.bikelock.dao.IAdminDao;
import com.pgt.bikelock.dao.impl.AdminDaoImpl;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.MD5Utils;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.admin.AdminVo;

/**
 * 业务起点 10000
 * @ClassName:     AdminLogin
 * @Description:管理员管理/administrator manage
 * @author:    Albert
 * @date:        2017年4月18日 下午4:33:55
 *
 */
public class AdminLogin extends BaseManage {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		super.doPost(req, resp);

		String[] parms = new String[]{"username","password"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		String userName = req.getParameter(parms[0]);
		String password = req.getParameter(parms[1]);

		IAdminDao loginDao =  new AdminDaoImpl();
		AdminVo user = loginDao.find(userName, MD5Utils.getMD5(password));

		if(user != null){

			// log in success - setup session
			HttpSession session = req.getSession();
			session.setMaxInactiveInterval(9000); //15 minitues
			session.setAttribute("industryId", user.getIndustryId());
			session.setAttribute("adminId", user.getId()+"");
			session.setAttribute("adminNickName", user.getNickname());
			session.setAttribute("userName", userName);
			session.setAttribute("isLogin","true");
			session.setAttribute("cityId", user.getCityId()+"");


			// function ACL
			List<String> groupIds = new UserGroupDaoImpl().getUserGroupIdList(user.getId());
			String groupIdStr = groupIds.toString().replace("[", "").replace("]","");
			session.setAttribute("userGroup", groupIdStr);
			

			List<FunctionVo> functionList = null;
			ResourceBundle rb = LanguageUtil.getDefaultResource();

			if("admin".equals(userName) || "omni".equals(userName) || user.getIsAdmin() == 1){
				// returns all. the whole family
				functionList = new FunctionDaoImpl().getFunctionList(false, null, rb);
				session.setAttribute("admin", 1);
			}else{
				functionList = new GroupFunctionDaoImpl().getGroupsFunctionList(groupIdStr, rb);
				session.setAttribute("admin", 0);
			}

			req.setAttribute("functionList", functionList);
			session.setAttribute("functionList",functionList);


			List<CityVo> cityList = new CityDaoImpl().getCityLlist(null);
			req.setAttribute("cityList",cityList);
			session.setAttribute("cityList",cityList);
			

			req.getRequestDispatcher("index.jsp").forward(req, resp);

			//增加登陆日志/add log in log
			new UserLogDaoImpl().addUserLog(new UserLogVo(user.getId()+"", 1, null));
			
		}else{
//			resp.sendRedirect("login.jsp");
			req.setAttribute("error_tips", getCurrentLangValue("admin_account_password_check_error"));
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		}

	}


}
