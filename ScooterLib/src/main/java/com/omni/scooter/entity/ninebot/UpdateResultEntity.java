package com.omni.scooter.entity.ninebot;

public class UpdateResultEntity extends com.omni.scooter.entity.omni.UpdateResultEntity{
	
	public UpdateResultEntity() {
		super();
	}
	public UpdateResultEntity(String content) {
		super(content);
	}

	/**
	 *升级设备识别码Iot 设备识别码（固定 85）,具体参数内容如下
	 * <li>{@link com.omni.scooter.command.ICommand#DEVICE_TYPE_IOT_SEGWAY ICommand.DEVICE_TYPE_IOT_SEGWAY}</li>
	 * <li>{@link com.omni.scooter.command.ICommand#DEVICE_TYPE_SEGWAY_CONTROL ICommand.DEVICE_TYPE_SEGWAY_CONTROL}</li>
	 * <li>{@link com.omni.scooter.command.ICommand#DEVICE_TYPE_SEGWAY_DISPLAY ICommand.DEVICE_TYPE_SEGWAY_DISPLAY}</li>
	 * <li>{@link com.omni.scooter.command.ICommand#DEVICE_TYPE_SEGWAY_BATTERY1 ICommand.DEVICE_TYPE_SEGWAY_BATTERY1}</li>
	 * <li>{@link com.omni.scooter.command.ICommand#DEVICE_TYPE_SEGWAY_BATTERY2 ICommand.DEVICE_TYPE_SEGWAY_BATTERY2}</li>
	 * 
	 * @return  字符串。"85" ninebot iot 设备 。 "20"->控制器 ,"21"->仪表,"22"->内置电池, "23"->外置电池
	 */
	public String getDeviceCode() {
		return super.getDeviceCode();
	}

}
