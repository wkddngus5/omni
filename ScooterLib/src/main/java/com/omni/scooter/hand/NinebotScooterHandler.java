package com.omni.scooter.hand;


/**
 * omni 追踪器 指令解析处理适配器
 * 基于协议版本1.2.8
 * @author cxiaox
 * 2018-08-03
 *
 */
public class NinebotScooterHandler extends BaseServerHandler{
	 
	 
	
	@Override
	protected String getFilterCommandHead() {
		return "*HBCR";
	}

}
