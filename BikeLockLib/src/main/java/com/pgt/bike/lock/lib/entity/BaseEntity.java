package com.pgt.bike.lock.lib.entity;

public class BaseEntity {
	/**
	 * 指令对象
	 */
	private String content;
	protected String[] tCont;
	
	public BaseEntity( ){
	}

	public BaseEntity(String content){
		this.content =  content ;
		tCont = content.split(","); 
	}
	public BaseEntity(byte[] command){
		content = new String (command);
		tCont = new String(command,0,command.length).split(","); 
	}
	public String  getImei(){
		return tCont[2];
	}
	
	
	
	
	public String getContent() {
		return content;
	}
 

	protected Short getShort(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return Short.parseShort(content.trim());
		}else{
			return Short.parseShort(content.substring(0,index).trim());
		}
	}
	
	protected int getInt(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return Integer.parseInt(content.trim());
		}else{
			return Integer.parseInt(content.substring(0,index).trim());
		}
	}
	
	
	protected long getLong(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return Long.parseLong(content.trim());
		}else{
			return Long.parseLong(content.substring(0,index).trim());
		}
	}
	
	protected String getString(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return content.trim();
		}else{
			return content.substring(0,index).trim();
		}
	}
	
}
