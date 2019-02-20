package com.omni.scooter.hand;


/**
 * omni 追踪器 指令解析处理适配器
 * @author cxiaox
 * 2018-08-03
 *
 */
public class OmniScooterHandler extends BaseServerHandler{
	 
	 
	
	@Override
	protected String getFilterCommandHead() {
		return "*SCOR";
	}

}
