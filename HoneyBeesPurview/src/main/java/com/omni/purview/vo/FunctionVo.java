/**
 * FileName:     FunctionVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月18日 下午5:28:28
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月18日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import com.omni.purview.dao.impl.BaseDao;


 /**
 * @ClassName:     FunctionVo
 * @Description:功能实体
 * @author:    Albert
 * @date:        2017年5月18日 下午5:28:28
 *
 */
public class FunctionVo {
	int id;
	int parent_id;
	String name;
	String href;//功能链接
	String ref;//功能标记
	int index;
	int idparams;//是否有ID参数
	int external;
	String action_class;
	String action_target;
	String action_title;
	String others_tag;
	
	/**显示属性**/
	List<FunctionVo> subList;//子菜单
	boolean checked;//是否选中
	boolean checkMore;//知否支持多选（列表批处理）
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the parent_id
	 */
	public int getParent_id() {
		return parent_id;
	}
	/**
	 * @param parent_id the parent_id to set
	 */
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}
	/**
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}
	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}
	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
	
	/**
	 * @return the subList
	 */
	public List<FunctionVo> getSubList() {
		return subList;
	}
	/**
	 * @param subList the subList to set
	 */
	public void setSubList(List<FunctionVo> subList) {
		this.subList = subList;
	}
	
	
	
	
	
	/**
	 * @return the idparams
	 */
	public int getIdparams() {
		return idparams;
	}
	/**
	 * @param idparams the idparams to set
	 */
	public void setIdparams(int idparams) {
		this.idparams = idparams;
	}
	
	/**
	 * @return the external
	 */
	public int getExternal() {
		return external;
	}
	/**
	 * @param external the external to set
	 */
	public void setExternal(int external) {
		this.external = external;
	}
	
	
	/**
	 * @return the action_class
	 */
	public String getAction_class() {
		return action_class;
	}
	/**
	 * @param action_class the action_class to set
	 */
	public void setAction_class(String action_class) {
		this.action_class = action_class;
	}
	/**
	 * @return the action_target
	 */
	public String getAction_target() {
		return action_target;
	}
	/**
	 * @param action_target the action_target to set
	 */
	public void setAction_target(String action_target) {
		this.action_target = action_target;
	}
	/**
	 * @return the action_title
	 */
	public String getAction_title() {
		return action_title;
	}
	/**
	 * @param action_title the action_title to set
	 */
	public void setAction_title(String action_title) {
		this.action_title = action_title;
	}
	
	
	
	
	/**
	 * @return the others_tag
	 */
	public String getOthers_tag() {
		return others_tag;
	}
	/**
	 * @param others_tag the others_tag to set
	 */
	public void setOthers_tag(String others_tag) {
		this.others_tag = others_tag;
	}
	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
	/**
	 * @return the checkMore
	 */
	public boolean isCheckMore() {
		return checkMore;
	}
	/**
	 * @param checkMore the checkMore to set
	 */
	public void setCheckMore(boolean checkMore) {
		this.checkMore = checkMore;
	}
	public FunctionVo(int parentId,String name){
		this.parent_id = parentId;
		this.name = name;
	}
	
	public FunctionVo(ResultSet rst,List<String> existFunc,ResourceBundle rb)  throws SQLException{
		this.id = BaseDao.getInt(rst, "id");
		this.parent_id = rst.getInt("parent_id");
		if(rb != null){
			this.name = rb.getString(rst.getString("name"));
			if(rst.getString("action_title") != null && !"".equals(rst.getString("action_title")))
			this.action_title = rb.getString(rst.getString("action_title"));
		}else{
			this.name = rst.getString("name");
			this.action_title = rst.getString("action_title");
		}
		
		this.index = rst.getInt("findex");
		this.href = rst.getString("href");
		this.ref = rst.getString("ref");
		this.idparams = rst.getInt("idparams");
		this.external = rst.getInt("external");
		this.action_class = rst.getString("action_class");
		this.action_target = rst.getString("action_target");
		this.others_tag = rst.getString("others_tag");
		if(existFunc != null && existFunc.contains(this.id+"")){
			this.checked = true;
		}
		if(this.others_tag != null && this.others_tag.contains("postType=\"string\"")){
			this.checkMore = true;
		}
	}
	
}
