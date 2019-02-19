package com.pgt.bikelock.servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pgt.bikelock.dao.impl.PromotionDaoImpl;
import com.pgt.bikelock.servlet.admin.BaseManage;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.PromotionVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class PromotionServlet extends BaseServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String promotionId = ValueUtil.getString((String) req.getAttribute("promotionId"));
        PromotionVO promotion = new PromotionDaoImpl().findById(promotionId);
        writeJSON(resp, promotion);
    }


    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        JsonObject body = new JsonParser().parse(req.getReader()).getAsJsonObject();

        PromotionVO promotion = new PromotionVO();
        promotion.setCityId(body.get("cityId").getAsString());
        promotion.setPolicy(body.get("policy").getAsString());
        promotion.setIsActivated(body.get("isActivated").getAsBoolean());
        promotion.setCreated(new Date());
        promotion.setUpdated(new Date());
        boolean isSuccess = new PromotionDaoImpl().insert(promotion);
        resp.setStatus(isSuccess ? 200 : 500);

    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        JsonObject body = new JsonParser().parse(req.getReader()).getAsJsonObject();
        String id = ValueUtil.getString((String) req.getAttribute("promotionId"));
        String policy = body.get("policy").getAsString();
        Boolean isActivated = body.get("isActivated").getAsBoolean();

        boolean isSuccess =  new PromotionDaoImpl().update(id, policy, isActivated);
        resp.setStatus(isSuccess ? 200 : 500);
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = ValueUtil.getString((String) req.getAttribute("promotionId"));
        boolean isSuccess =  new PromotionDaoImpl().delete(id);
        resp.setStatus(isSuccess ? 200 : 500);
    }
}
