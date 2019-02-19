
package com.pgt.bikelock.servlet;

import java.util.List;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgt.bikelock.dao.impl.MembershipPlanDAO;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.vo.MembershipPlanVO;
import com.pgt.bikelock.vo.UserDetailVo;


public class MembershipServlet extends BaseServlet {


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{

		String path = req.getPathInfo();

		if (path == null) {
			path = "/";
		}

		switch (path) {

			case "/":

				String cityId = getCityId(req) + "";
				//validate education
				UserDetailVo detailVo = new UserDetailDaoImpl().getUserDetail(getUserId(req));
				boolean isEducation = false;
				if(detailVo != null && detailVo.getEmailAuth() == 2){
					isEducation = true;
				}
				List<MembershipPlanVO> plans = new MembershipPlanDAO().getActiveMembershipPlansForCity(cityId,isEducation);
				writeJSON(res, plans);
				break;

			default:
				writeJSONCode(res, 404);
				break;

		}

	}

}