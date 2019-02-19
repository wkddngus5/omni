/**
 * FileName:     LockOrderBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年8月21日 下午5:14:13
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年8月21日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.omni.lock.upfile.UpFileUtil;
import com.omni.scooter.command.ICommand;
import com.omni.scooter.command.OmniCommand;
import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bike.lock.lib.up.DeviceType;
import com.pgt.bike.lock.lib.utils.CommandUtil;
import com.pgt.bikelock.dao.IBikeOrderRecord;
import com.pgt.bikelock.dao.impl.BikeDaoImpl;
import com.pgt.bikelock.dao.impl.BikeOrderRecordDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LockUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeVo;

/**
 * @ClassName:     LockOrderBo
 * @Description:锁指令控制器/lock order controller
 * @author:    Albert
 * @date:        2018年8月21日 下午5:14:13
 *
 */
public class LockOrderBo {

	static final int ORDER_VALID_SECOND = 120;

	public static int checkLockOnline(int bikeType,String imeiStr,boolean checkOrder){
		long imei = ValueUtil.getLong(imeiStr);
		if(bikeType == 1 && TCPService.getIoSession(imei)!= null){
			if(!checkOrder){
				return 1;
			}
			return checkLockOrder(imei);
		}else if(bikeType == 2 && com.omni.scooter.tcp.TCPService.getIoSession(imei)!= null){
			if(!checkOrder){
				return 1;
			}
			return checkLockOrder(imei);
		}
		return 0;
	}

	private static int checkLockOrder(long imei){
		if("1".equals(OthersSource.getSourceString("save_lock_order"))){
			IBikeOrderRecord orderDao = new BikeOrderRecordDaoImpl();
			//指令是否正常/command whether normal
			return orderDao.haveRecordInMinutes(BikeBo.BIKE_CONNECT_CHECK_SECONDS, imei)?1:0;
		}else{
			return 1;
		}
	}

	/**
	 * 
	 * @Title:        sendUnlockOrder 
	 * @Description:  解锁指令发送/send unlock order
	 * @param:        @param bikeType
	 * @param:        @param imei
	 * @param:        @param userId
	 * @param:        @param timestamp
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:24:56
	 */
	public static boolean sendUnlockOrder(int bikeType,String imei,Integer userId,long timestamp){
		if(timestamp == 0){
			timestamp = TimeUtil.getCurrentLongTime();
		}
		boolean flag = false;
		if(bikeType == 1){
			byte[] order= CommandUtil.getBGMLockCommand(Command.CODE,imei,
					Command.LOCK_ON,userId, timestamp);
			flag = TCPService.sendOrder(ValueUtil.getLong(imei), order) == 0?false:true;
		}else if(bikeType == 2){
			byte[] order = OmniCommand.getControlRequestCommand(imei+"", 0, ORDER_VALID_SECOND, userId, timestamp);
			flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
		}
		//分布式解锁，适用于锁连接服务和业务服务不在同一个服务器
		//		boolean flag = new BikeBo().sendBikeOrder(ValueUtil.getLong(bikeImei), 1001);
		return flag;
	}

	/**
	 * 
	 * @Title:        sendLockOrder 
	 * @Description:  发送关锁指令，目前适用于滑板车/send scooter lock order
	 * @param:        @param imei
	 * @param:        @param userId
	 * @param:        @param timestamp
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:35:28
	 */
	public static boolean sendLockOrder(String imei,Integer userId,long timestamp){
		if(timestamp == 0){
			timestamp = TimeUtil.getCurrentLongTime();
		}
		byte[] order = OmniCommand.getControlRequestCommand(imei+"", 1, ORDER_VALID_SECOND, userId, timestamp);
		return com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
	}

	/**
	 * 
	 * @Title:        sendLocationOrder 
	 * @Description:  发送定位指令/send location order
	 * @param:        @param bikeType
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:38:10
	 */
	public static boolean sendLocationOrder(int bikeType,String imei){
		boolean flag = false;
		if(bikeType == 1){
			byte[] order = CommandUtil.getLocationCommand(Command.CODE_GM, imei);
			flag = TCPService.sendOrder(ValueUtil.getLong(imei), order) == 0?false:true;
		}else if(bikeType == 2){
			byte[] order = OmniCommand.getLocationCommand(imei+"");
			flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
		}
		return flag;
	}

	/**
	 * 
	 * @Title:        sendInfoOrder 
	 * @Description:  信息指令发送/send info order
	 * @param:        @param bikeType
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:44:15
	 */
	public static boolean sendInfoOrder(int bikeType,String imei){
		boolean flag = false;
		if(bikeType == 1){
			byte[] order = CommandUtil.getInfoCommand(Command.CODE_OM, imei);
			flag = TCPService.sendOrder(ValueUtil.getLong(imei), order)== 0?false:true;
		}else if(bikeType == 2){
			byte[] order = OmniCommand.getScooterInfoCommand(imei+"");
			flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
			order = OmniCommand.getReadDeviceKey(imei+"");
			com.omni.scooter.tcp.TCPService.sendOrder(imei, order);
			order = OmniCommand.getIccidCommand(imei+"");
			com.omni.scooter.tcp.TCPService.sendOrder(imei, order);
		}
		return flag;
	}

	/**
	 * 
	 * @Title:        sendVersionOrder 
	 * @Description:  版本指令发送/send version order
	 * @param:        @param bikeType
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:46:29
	 */
	public static boolean sendVersionOrder(int bikeType,String imei){
		boolean flag = false;
		if(bikeType == 1){
			byte[] order = CommandUtil.getVersionCommand(Command.CODE_OM, imei);
			flag = TCPService.sendOrder(ValueUtil.getLong(imei), order)== 0?false:true;
		}else if(bikeType == 2){
			byte[] order = OmniCommand.getVersionCommand(imei+"");
			flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
		}
		return flag;
	}

	/**
	 * 
	 * @Title:        sendVoiceOrder 
	 * @Description:  播放声音指令发送/send voice order
	 * @param:        @param bikeType
	 * @param:        @param imei
	 * @param:        @param voiceType 播放内容 1:使出 Geofence 提示 2:找车提示 3:低电量提醒
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:50:04
	 */
	public static boolean sendVoiceOrder(int bikeType,String imei,int voiceType){
		boolean flag = false;
		if(bikeType == 1){
			//			// 3 ,响的时间/the time of the ring
			//			// 1, 灯闪烁的时间/light twinkle time
			byte[] order = CommandUtil.getFindBikeCommand(Command.CODE_OM,  imei, 3, 1);
			flag = TCPService.sendOrder(ValueUtil.getLong(imei), order)== 0?false:true;
		}else if(bikeType == 2){
			byte[] order = OmniCommand.getVoiceCommand(imei, voiceType);;
			flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
		}
		return flag;
	}

	/**
	 * 
	 * @Title:        sendShutdownOrder 
	 * @Description:  关机指令发送/send shutdown order
	 * @param:        @param bikeType
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:53:30
	 */
	public static boolean sendShutdownOrder(int bikeType,String imei){
		boolean flag = false;
		if(bikeType == 1){
			byte[] order = CommandUtil.getSleepCommand(Command.CODE_OM, imei);
			flag = TCPService.sendOrder(ValueUtil.getLong(imei), order)== 0?false:true;
		}else if(bikeType == 2){
			byte[] order = OmniCommand.getShutDownCommand(imei);
			flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
		}
		return flag;
	}

	/**
	 * 
	 * @Title:        sendReStartOrder 
	 * @Description:  重启指令发送，目前适用于滑板车/send startup order
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:54:56
	 */
	public static boolean sendReStartOrder(String imei){
		boolean flag = false;		
		byte[] order = OmniCommand.getReStartCommand(imei);		
		flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
		return flag;
	}

	/**
	 * 
	 * @Title:        sendMacOrder 
	 * @Description:  蓝牙地址指令发送/send mac order
	 * @param:        @param bikeType
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月21日 下午5:59:00
	 */
	public static boolean sendMacOrder(int bikeType,String imei){
		boolean flag = false;
		if(bikeType == 1){
			byte[] order = CommandUtil.getMacCommand(Command.CODE_OM, imei);
			flag = TCPService.sendOrder(ValueUtil.getLong(imei), order)== 0?false:true;
		}else if(bikeType == 2){
			byte[] order = OmniCommand.getMacCommand(imei);
			flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
		}
		return flag;
	}

	/**
	 * 
	 * @Title:        sendUpgradeOrder 
	 * @Description:  发送主动升级指令/send upgrade order
	 * @param:        @param bike
	 * @param:        @return
	 * @param:        @throws IOException    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年9月13日 下午2:31:11
	 */
	public static boolean sendUpgradeOrder(BikeVo bike) throws IOException{
		boolean flag = false;
		UpFileUtil.reLoad();
		byte[] order = null;
		long imei = ValueUtil.getLong(bike.getImei());
		//手动升级0不校验版本/manual upgrade 0 not verify version
		if(bike.getBikeType() == 2){
			//scooter
			order = LockUtil.getUploadVersionData(imei, 0, ICommand.DEVICE_TYPE_IOT_OMNI,false,2);
			if(order != null){
				flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;
			}
		}else{
			if(!StringUtils.isEmpty(bike.getVersion()) && bike.getVersion().toUpperCase().startsWith("A2")){
				//3合1，B模
				order = LockUtil.getUploadVersionData(imei, 0, DeviceType.VALUE_MODEL_GPRS_GPS_BLE,false,1);
			}else if(bike.getTypeVo().haveBleLock()){
				order = LockUtil.getUploadVersionData(imei, 0, DeviceType.VALUE_GPRS_GPS_BLE,false,1);
			}else if(bike.getTypeVo().haveGprsLock()){
				order = LockUtil.getUploadVersionData(imei, 0, DeviceType.VALUE_GPRS_GPS,false,1);
			}
			if(order != null){
				flag = TCPService.sendOrder(imei, order) == 0?false:true;
			}
		} 
		return flag;
	}

	/**
	 * 
	 * @Title:        sendTargetTypeUpgradeOrder 
	 * @Description:  升级目标类型滑板车IOT
	 * @param:        @param imei
	 * @param:        @param lockType
	 * @param:        @param versionType 2:scooter iot 3:scooter ctl
	 * @param:        @return
	 * @param:        @throws IOException    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2018年12月8日 上午10:32:41
	 */
	public static int sendTargetTypeUpgradeOrder(long imei,String lockType,int versionType,String lockFile) throws IOException{
		int result = 0;
		try {
			if(!StringUtils.isEmpty(lockFile)){
				if(versionType == 2){
					//iot
					UpFileUtil.setScooterPath(lockFile);
					if(StringUtils.isEmpty(lockType)){
						lockType = "8A";
					}
				}else if(versionType == 3){
					//ctl
					UpFileUtil.setControlXMPath(lockFile);
					if(StringUtils.isEmpty(lockType)){
						lockType = "20";
					}
				}
			}
			
			UpFileUtil.reLoad();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 2;
		}
		byte[] order = LockUtil.getUploadVersionData(imei, 0, lockType,false,versionType);
		if(order != null){
			result = com.omni.scooter.tcp.TCPService.sendOrder(imei, order);
		}
		return result;
	}

	/**
	 * 
	 * @Title:        sendIOTSetOrder 
	 * @Description:  IOT设置/iot settings
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月29日 上午11:37:21
	 */
	public static boolean sendIOTSetOrder(HttpServletRequest req){
		String imei = req.getParameter("imei");
		int sensitivity = ValueUtil.getInt(req.getParameter("sensitivity"));
		int uploadInRide = ValueUtil.getInt(req.getParameter("uploadInRide"));
		int uploadInterval = ValueUtil.getInt(req.getParameter("uploadInterval"));
		int heartInterval = ValueUtil.getInt(req.getParameter("heartInterval"));	
		byte[] order = OmniCommand.getIotSetCommand(imei, sensitivity, uploadInRide, heartInterval, uploadInterval);
		boolean flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;		
		return flag;
	}

	/**
	 * 
	 * @Title:        sendLightSetOrder 
	 * @Description:  灯、模式设置/light and mode settings
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月29日 上午11:48:38
	 */
	public static boolean sendLightSetOrder(HttpServletRequest req){

		String imei = req.getParameter("imei");
		int headLight = ValueUtil.getInt(req.getParameter("headLight"));
		int headLightTwinkling = ValueUtil.getInt(req.getParameter("headLightTwinkling"));
		int tailLightTwinkling = ValueUtil.getInt(req.getParameter("tailLightTwinkling"));
		int speedMode = ValueUtil.getInt(req.getParameter("speedMode"));		
		int acceleratorResponse = ValueUtil.getInt(req.getParameter("acceleratorResponse"));
		byte[] order = OmniCommand.getScooterSet1Command(imei, headLight, speedMode, acceleratorResponse, tailLightTwinkling);
		boolean flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;		
		return flag;
	}

	/**
	 * 
	 * @Title:        sendSpeedSetOrder 
	 * @Description:  速度设置指令/speed settings
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月29日 下午2:14:52
	 */
	public static boolean sendSpeedSetOrder(HttpServletRequest req){
		String imei = req.getParameter("imei");
		int inchSpeed = ValueUtil.getInt(req.getParameter("inchSpeed"));
		int cruiseSpeed = ValueUtil.getInt(req.getParameter("cruiseSpeed"));
		int startType = ValueUtil.getInt(req.getParameter("startType"));
		int bleBroadcast = ValueUtil.getInt(req.getParameter("bleBroadcast"));
		int buttonChangeMode = ValueUtil.getInt(req.getParameter("buttonChangeMode"));	
		int buttonChangeHeadLight = ValueUtil.getInt(req.getParameter("buttonChangeHeadLight"));	
		int lowSpeed = ValueUtil.getInt(req.getParameter("lowSpeed"));		
		int mediumSpeed = ValueUtil.getInt(req.getParameter("mediumSpeed"));		
		int highSpeed = ValueUtil.getInt(req.getParameter("highSpeed"));		
		byte[] order = OmniCommand.getScooterSet2Command(imei, inchSpeed, cruiseSpeed, startType, buttonChangeMode, 
				buttonChangeHeadLight, lowSpeed, mediumSpeed, highSpeed);
		boolean flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;		
		return flag;
	}

	/**
	 * 
	 * @Title:        sendLockKeySetOrder 
	 * @Description:  秘钥设置/key settings
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月29日 下午2:26:28
	 */
	public static boolean sendLockKeySetOrder(HttpServletRequest req){
		String imei = req.getParameter("imei");
		String bleKey = ValueUtil.getString(req.getParameter("bleKey"));
		byte[] order = OmniCommand.getSetDeviceKey(imei, bleKey);
		boolean flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;		
		return flag;
	}

	/**
	 * 
	 * @Title:        sendLockAddressSetOrder 
	 * @Description:  地址设置/address settings
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年9月12日 下午5:02:43
	 */
	public static boolean sendLockAddressSetOrder(HttpServletRequest req){
		String imei = req.getParameter("imei");
		String ip = req.getParameter("ip");
		int ipmode = ValueUtil.getInt(req.getParameter("ipmode"));
		int port = ValueUtil.getInt(req.getParameter("port"));
		byte[] order = OmniCommand.getSetAddressCommand(imei, ipmode, ip, port);
		boolean flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;		
		if(flag){
			//save address info
			JSONObject data = new JSONObject();
			data.put(BikeVo.LOCK_EXTEND_INFO_ADDRESS_MODE, ipmode);
			data.put(BikeVo.LOCK_EXTEND_INFO_ADDRESS_IP, ip);
			data.put(BikeVo.LOCK_EXTEND_INFO_ADDRESS_PORT, port);
			new BikeDaoImpl().updateExtendInfo(ValueUtil.getLong(imei), data);
		}
		return flag;
	}
	
	/**
	 * 
	 * @Title:        sendLockCardInfoSetOrder 
	 * @Description:  设置锁卡信息/set card info
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年12月13日 下午7:10:00
	 */
	public static boolean sendLockCardInfoSetOrder(HttpServletRequest req){
		String imei = req.getParameter("imei");
		int apnMode = 0;
		String apn = req.getParameter("apn");
		String user = req.getParameter("user");
		String password = req.getParameter("password");
		if(!StringUtils.isEmpty(user) && !StringUtils.isEmpty(password)){
			apnMode = 1;
		}
		byte[] order = OmniCommand.getSetAPNCommand(imei, apn, apnMode, user, password);
		boolean flag = com.omni.scooter.tcp.TCPService.sendOrder(imei, order) == 0?false:true;		
		if(flag){
			//save address info
			JSONObject data = new JSONObject();
			data.put(BikeVo.LOCK_EXTEND_INFO_ADDRESS_APN, apn);
			data.put(BikeVo.LOCK_EXTEND_INFO_APN_USER, user);
			data.put(BikeVo.LOCK_EXTEND_INFO_APN_PASSWORD, password);
			new BikeDaoImpl().updateExtendInfo(ValueUtil.getLong(imei), data);
		}
		return flag;
	}
}
