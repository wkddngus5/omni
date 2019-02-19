/**
 * FileName:     BikeErrorVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月11日 上午11:26:26/11:26:26 am, April 11, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     BikeErrorVo
 * @Description:单车故障实体/fault bicycle entity
 * @author:    Albert
 * @date:        2017年4月11日 上午11:26:26/11:26:26 am, April 11, 2017
 *
 */
public class BikeErrorVo {

	
	String id;
	String bid;
	String bnumber;
	String uid;
	UserVo userVo;
	int type;//报告类型 1：不能开锁 2：故障 3：违章 4:关锁后未结费   5.忘记关锁 6:被偷的车 7:被破坏的车 8：无人认领的车 9：其他问题//report type: 1:unable to open the lock 2: error 3: break rules and regulations 4:unable to pay after locked 5: forget to lock 6:stolen bicycle 7: broken bicycle 8:unclaimed bicycle 9: other problems
	String error_type;//故障类型//error type
	String content;//报告描述//report description 
	String image_ids;//图片集合，分割//picture collected, picture segmented
	String date;//报告日期 report date
	int status;//处理状态 0：待审核 1：正在审核 2：通过审核 3：未通过审核 4:已自动审核(适用于关锁未结费或不能开锁故障）//processing status 0: to be checked 1:checking 2:checked 4: automatically checked (for unable to pay after locked or unable to open the lock)
	String bike_useid;//骑行记录ID//cycling log ID
	String review_note;
	String review_date;//审核时间/check time
	double lat,lng;//故障车所在位置//fault bicycle location
	
	/**显示属性**//**display attributes**/
	String errorTypeStr;
	List<ImageVo> listImage;
	String typeStr;
	String statusStr;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	/**
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}
	/**
	 * @param bid the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the bnumber
	 */
	public String getBnumber() {
		return bnumber;
	}
	/**
	 * @param bnumber the bnumber to set
	 */
	public void setBnumber(String bnumber) {
		this.bnumber = bnumber;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the error_type
	 */
	public String getError_type() {
		return error_type;
	}
	/**
	 * @param error_type the error_type to set
	 */
	public void setError_type(String error_type) {
		this.error_type = error_type;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the image_ids
	 */
	public String getImage_ids() {
		return image_ids;
	}
	/**
	 * @param image_ids the image_ids to set
	 */
	public void setImage_ids(String image_ids) {
		this.image_ids = image_ids;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the errorTypeStr
	 */
	public String getErrorTypeSt2r() {
		return this.errorTypeStr;
	}
	
	/**
	 * @param errorTypeStr the errorTypeStr to set
	 */
	public void setErrorTypeStr() {
		String[] typeArr = this.error_type.split(",");
		String[] typeTitleArr = LanguageUtil.getDefaultValue("bike_report_error_type_value_en").split(",");
		if("zh_cn".equals(OthersSource.getSourceString("default_language"))){
			typeTitleArr = LanguageUtil.getDefaultValue("bike_report_error_type_value_zh").split(",");
		}
		for (int i = 0; i < typeArr.length; i++) {
			int type = ValueUtil.getInt(typeArr[i]);
			type = type>0?type-1:type;
			if(i == 0){
				if(type >= typeTitleArr.length || type < 0){
					this.errorTypeStr =  "UnKnow";
				}else{
					this.errorTypeStr = typeTitleArr[type];
				}
			}else{
				if(type >= typeTitleArr.length || type < 0){
					this.errorTypeStr =  this.errorTypeStr + ",UnKnow";
				}else{
					this.errorTypeStr = this.errorTypeStr +","+ typeTitleArr[type];
				}
			}
		}
	}
	
	
	
	/**
	 * @return the listImage
	 */
	public List<ImageVo> getListImage() {
		return listImage;
	}
	/**
	 * @param listImage the listImage to set
	 */
	public void setListImage(List<ImageVo> listImage) {
		this.listImage = listImage;
	}
	/**
	 * @return the userVo
	 */
	public UserVo getUserVo() {
		return userVo;
	}
	/**
	 * @param userVo the userVo to set
	 */
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}
	
	
	
	/**
	 * @return the bike_useid
	 */
	public String getBike_useid() {
		return bike_useid;
	}
	/**
	 * @param bike_useid the bike_useid to set
	 */
	public void setBike_useid(String bike_useid) {
		this.bike_useid = bike_useid;
	}
	
	/**
	 * @return the review_note
	 */
	public String getReview_note() {
		return review_note;
	}
	/**
	 * @param review_note the review_note to set
	 */
	public void setReview_note(String review_note) {
		this.review_note = review_note;
	}
	/**
	 * @return the review_date
	 */
	public String getReview_date() {
		return review_date;
	}
	/**
	 * @param review_date the review_date to set
	 */
	public void setReview_date(String review_date) {
		this.review_date = review_date;
	}
	
	
	
	
	/**
	 * @return the errorTypeStr
	 */
	public String getErrorTypeStr() {
		return errorTypeStr;
	}
	/**
	 * @param errorTypeStr the errorTypeStr to set
	 */
	public void setErrorTypeStr(String errorTypeStr) {
		this.errorTypeStr = errorTypeStr;
	}
	/**
	 * @return the typeStr
	 */
	public String getTypeStr() {
		return typeStr;
	}
	/**
	 * @param typeStr the typeStr to set
	 */
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	/**
	 * @return the statusStr
	 */
	public String getStatusStr() {
		return statusStr;
	}
	/**
	 * @param statusStr the statusStr to set
	 */
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
	
	
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	public BikeErrorVo(){
		
	}
	
	public BikeErrorVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.bid = BaseDao.getString(rst, "bid");
		this.bnumber = rst.getString("bnumber");
		this.uid = rst.getString("uid");
		this.type = rst.getInt("type");
		this.setTypeStr(LanguageUtil.getTypeStr("bike_report_type_value", this.type));
		if(this.type == 2){
			this.error_type = rst.getString("error_type");
			setErrorTypeStr();
		}

		this.content = rst.getString("content");
		this.image_ids = rst.getString("image_ids");
		this.date = rst.getString("date");
		this.status = rst.getInt("status");
		this.setStatusStr(LanguageUtil.getStatusStr(this.status,"bike_report_review_result_value"));
		this.review_note = BaseDao.getString(rst, "review_note");
		this.bike_useid = BaseDao.getString(rst, "bike_useid");
		this.lat = BaseDao.getDouble(rst, "lat");
		this.lng = BaseDao.getDouble(rst, "lng");
		this.userVo = new UserVo(rst);
		
	}

	public BikeErrorVo(String[] parms,HttpServletRequest req){
		this.uid = (String) req.getAttribute("userId");
		this.bnumber = req.getParameter(parms[0]);
		if(req.getParameter("content") != null){
			this.content = req.getParameter("content");
		}
		this.id = req.getParameter("id");
		this.status = ValueUtil.getInt(req.getParameter("result"));
		this.bike_useid = ValueUtil.getInt(req.getParameter("useId"))+"";
		this.review_note = req.getParameter("note");
		this.lat = ValueUtil.getDouble(req.getParameter("lat"));
		this.lng = ValueUtil.getDouble(req.getParameter("lng"));
		if(req.getParameter("errorType") != null){
			String[] type =  req.getParameter("errorType").split(",");
			for (int i = 0; i < type.length; i++) {
				if(i == 0){
					this.error_type = ValueUtil.getInt(type[i])+"";
				}else{
					this.error_type = this.error_type+","+ValueUtil.getInt(type[i]);
				}
			}
		}
		
		if(req.getParameter("imageUrls") != null){
			String[] imageUrls = req.getParameter("imageUrls").split(",");
			String[] imageWidths = req.getParameter("imageWidths").split(",");
			String[] imageHeights = req.getParameter("imageHeights").split(",");
			
			this.listImage = new ArrayList<ImageVo>();
			for (int i = 0; i < imageUrls.length; i++) {
				this.listImage.add(new ImageVo(imageUrls[i],ValueUtil.getDouble(imageWidths[i]) , ValueUtil.getDouble(imageHeights[i])));
			}
		}
	}
}
