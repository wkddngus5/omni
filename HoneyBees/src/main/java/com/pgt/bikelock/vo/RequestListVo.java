package com.pgt.bikelock.vo;


import javax.servlet.http.HttpServletRequest;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;


/**
 * 请求列表实体/request list entity
 * @author apple
 * 2017年03月17日14:05:19/14:05:19 March 17, 2017
 */
public class RequestListVo {
	/**请求参数**//**request parameter**/
	int pageNo;//页码/page
	int pageSize;//页面大小/page size
	int startPage;//开始索引/start reference
	String keyWords;//搜索关键字/search key words
	int type;//类型/type
	int status;
	int way;
	int extendType1;//扩展类型1
	String startTime;//开始时间/start time
	String endTime;//结束时间/end time
	int cityId;//当前城市ID/city ID
	String tagIds;//目标多个ID/target multi ID
	String orderField;//排序字段/order field
	String orderDirection;//排序方式/order type
	/**返回参数**//**return parameter**/
	int totalCount;//总数量/total count
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		if(!StringUtils.isEmpty(keyWords)){
			this.keyWords = keyWords.trim();
		}
		
	}

	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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
	 * @return the way
	 */
	public int getWay() {
		return way;
	}
	/**
	 * @param way the way to set
	 */
	public void setWay(int way) {
		this.way = way;
	}
	
	
	
	/**
	 * @return the extendType1
	 */
	public int getExtendType1() {
		return extendType1;
	}
	/**
	 * @param extendType1 the extendType1 to set
	 */
	public void setExtendType1(int extendType1) {
		this.extendType1 = extendType1;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * @return the cityId
	 */
	public int getCityId() {
		return cityId;
	}
	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
	
	/**
	 * @return the tagIds
	 */
	public String getTagIds() {
		return tagIds;
	}
	
	
	
	
	/**
	 * @return the orderField
	 */
	public String getOrderField() {
		if(StringUtils.isEmpty(orderField)){
			return "id";
		}
		return orderField;
	}
	/**
	 * @param orderField the orderField to set
	 */
	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}
	/**
	 * @return the orderDirection
	 */
	public String getOrderDirection() {
		if(StringUtils.isEmpty(orderDirection)){
			return "desc";
		}
		return orderDirection;
	}
	/**
	 * @param orderDirection the orderDirection to set
	 */
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
	public RequestListVo(HttpServletRequest request,boolean formateDate,boolean cityCheck){
		this.orderField = request.getParameter("orderField");
		this.orderDirection =  request.getParameter("orderDirection");
		if(StringUtils.isEmpty(this.orderDirection)){
			this.orderDirection = "desc";
		}
		
		this.pageNo = ValueUtil.getInt(request.getParameter("pageNum")); // 页数/page
		this.pageNo = this.pageNo == 0?1:this.pageNo;
		
		this.pageSize = ValueUtil.getInt(request.getParameter("numPerPage")); // 页数/page
		if(this.pageSize == 0){
			if(ValueUtil.getInt(OthersSource.getSourceString("default_page_size")) > 0){
				this.pageSize = ValueUtil.getInt(OthersSource.getSourceString("default_page_size"));
			}else{
				this.pageSize = 10;
			}
		}
	
		this.type = ValueUtil.getInt(request.getParameter("type")); // 类型/type
		this.status = ValueUtil.getInt(request.getParameter("status")); // 状态/status
		this.way = ValueUtil.getInt(request.getParameter("way")); // 方式/way
		this.extendType1 = ValueUtil.getInt(request.getParameter("extendType1"));
		if(cityCheck){
			this.cityId = ValueUtil.getInt(request.getAttribute("cityId")); //当期城市/city ID
		}
		
		
		String keyWords = request.getParameter("keyWords");//查询关键字/search key words
		
		//开始索引计算/start index calculating
		this.startPage = this.pageSize*(this.pageNo-1);
		
		if(keyWords == null){
			this.keyWords = "";
		}else{
			this.keyWords = keyWords.trim();
		}
		
		//起始时间/start time
		if(!StringUtils.isEmpty(request.getParameter("startTime"))){
			if(formateDate){
				this.startTime = TimeUtil.formateStrDateToLongStr(request.getParameter("startTime"), TimeUtil.Formate_YYYY_MM_dd_HH_mm);
			}else{
				this.startTime = request.getParameter("startTime");
			}
			
		}
		
		
		if(!StringUtils.isEmpty(request.getParameter("endTime"))){
			if(formateDate){
				this.endTime = TimeUtil.formateStrDateToLongStr(request.getParameter("endTime"), TimeUtil.Formate_YYYY_MM_dd_HH_mm);
			}else{
				this.endTime = request.getParameter("endTime");
			}
			
		}
		this.tagIds = request.getParameter("tagIds");
	}
}
