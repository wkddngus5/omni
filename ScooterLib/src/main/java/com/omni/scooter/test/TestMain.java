package com.omni.scooter.test;

import java.io.IOException;
import java.util.Scanner;

import com.omni.lock.upfile.DeviceType;
import com.omni.lock.upfile.UpFileUtil;
import com.omni.scooter.command.ICommand;
import com.omni.scooter.command.NinebotCommand;
import com.omni.scooter.command.OmniCommand;
import com.omni.scooter.hand.NinebotScooterHandler;
import com.omni.scooter.hand.OmniScooterHandler;
import com.omni.scooter.listener.NinebotScooterListener;
import com.omni.scooter.tcp.TCPService;

public class TestMain {

	public static void main(String[] args) throws IOException {

		
		UpFileUtil.setScooterPath("E:/3in1A1.aes");
		UpFileUtil.reLoad();
		
		//1-omni ;   2-segway iot
		int testMode = 1;

		
		if(testMode==1){
			TCPService tcpService = new TCPService(9666, new MyListenter(),"GF");
			tcpService.start();
		}else{
			TCPService tcpService = new TCPService(9666,new NinebotScooterHandler(), new MyNinetbotListener());
			tcpService.start();
		}
		
		
		Scanner sc = new Scanner(System.in);
		System.out.println("输入操作值，0退出");
		System.out.println("0退出。1-开锁请求,2-关锁请求,3-语音播报,4-定位,5-获取版本,6-升级IOT,7-获取MAC,8-获取iccid,9-获取滑板车信息,10-滑板车设置1,11-滑板车设置2,14-关机");
		
		int type = 0;
		
//		String imei = "863158022988725";
//		String imei = "861477038755938";
		String imei = "867584030362854";
		
		while( (type = sc.nextInt() )!=0){
			 
			System.out.println("0退出。1-开锁请求(开始预约),2-关锁请求(取消预约),3-语音播报,4-定位,5-获取版本,6-升级IOT,7-获取MAC,8-获取iccid,9-获取滑板车信息,10-滑板车设备1,11-滑板车设置2");
			if(type==1){
				// 开锁请求
				
				long requestId = System.currentTimeMillis()/1000;
				byte[] command =null;
				if(testMode==1){
					command= OmniCommand.getEventNotice(imei, ICommand.EVENT_TYPE_RESERVATION_START);
					
//					command = OmniCommand.getControlRequestCommand(imei, ICommand.CONTROL_REQUEST_ACTION_OPEN, 30, 1, requestId);
				}else{
					
					command = NinebotCommand.getControlRequestCommand(imei, ICommand.CONTROL_REQUEST_ACTION_OPEN, 30, 1, requestId);
				}								
				
				TCPService.sendOrder(imei, command);
			}else if(type==2){
				long requestId = System.currentTimeMillis()/1000;
				byte[] command =null;
				if(testMode==1){
					command = OmniCommand.getEventNotice(imei, ICommand.EVENT_TYPE_RESERVATION_CANCEL);
//					command = OmniCommand.getControlRequestCommand(imei, ICommand.CONTROL_REQUEST_ACTION_CLOSE, 30, 1, requestId);
				}else{
					command = NinebotCommand.getControlRequestCommand(imei, ICommand.CONTROL_REQUEST_ACTION_CLOSE, 30, 1, requestId);
				}
				TCPService.sendOrder(imei, command);
			}else if(type==3){
				byte[] command =null;
				if(testMode==1){
					command = OmniCommand.getVoiceCommand(imei, ICommand.VOICE_FIND);
				}else{
					command = NinebotCommand.getVoiceCommand(imei, ICommand.VOICE_FIND);
				}
			 
				TCPService.sendOrder(imei, command);
			}else if(type==4){
				byte[] command = OmniCommand.getLocationCommand(imei);
				TCPService.sendOrder(imei, command);
			}else if(type==5){
				byte[] command = OmniCommand.getVersionCommand(imei);
				TCPService.sendOrder(imei, command);
			}else if(type==6){
				int totalPack =UpFileUtil.getTotalPack(DeviceType.SCOOTER_IOT_OMNI);
				int realLength = UpFileUtil.getRealLen(DeviceType.SCOOTER_IOT_OMNI);
				int crcInt = UpFileUtil.getAllCRC(DeviceType.SCOOTER_IOT_OMNI);
				String deviceType = ICommand.DEVICE_TYPE_IOT_OMNI;
				String upCode = "Vgz7";
				
				System.out.println(" crcInt all="+crcInt);
				System.out.println(" crcInt all  real ="+UpFileUtil.getRealAllCRC(DeviceType.SCOOTER_IOT_OMNI));
				
				byte[] command = OmniCommand.getStartUpComm(imei, totalPack, realLength, crcInt, deviceType, upCode);
				TCPService.sendOrder(imei, command);
			}else if(type==7){
				byte[] command = OmniCommand.getMacCommand(imei);
				TCPService.sendOrder(imei, command);
			}else if(type==8){
				byte[] command = OmniCommand.getIccidCommand(imei);
				TCPService.sendOrder(imei, command);
			}else if(type==9){
				byte[] command =null;
				if(testMode==1){
					command= OmniCommand.getScooterInfoCommand(imei);
				}else{
					command=NinebotCommand.getScooterInfo1Command(imei);
				}
				TCPService.sendOrder(imei, command);
			}else if(type==10){
				if(testMode==1){
					System.out.println("输入速度档位");
					
					int lightStatus = 0;//大灯开关 0:无效（不设置） 1:关闭 2:开启.默认值: 1:关闭
					int speedMode = 1;//模式设置 0:无效（不设置） 1:低速 2:中速 3:高速.默认值: 2: 中速
					int throttleStatus=0; //油门响应 0:无效（不设置） 1:关闭 2:开启.默认值: 1:关闭
					int tailLight= 0;//后灯闪烁 0:无效（不设置） 1:关闭 2:开启.默认值: 1:关闭
					
					lightStatus = sc.nextInt();
					byte[] command= OmniCommand.getScooterSet1Command(imei, lightStatus, speedMode, throttleStatus, tailLight);
					TCPService.sendOrder(imei, command);
				}else{
					byte[] command= NinebotCommand.getScooterSet1Command(imei, 1, 2, 3, 4,5);
					TCPService.sendOrder(imei, command);
				}
			}else if(type==11){
				if(testMode==1){
					System.out.println("高速限速值");
					int maxSpeed = sc.nextInt();
					byte[] command= OmniCommand.getScooterSet2Command(imei, 1, 2, 3, 4,5,6,7,maxSpeed);
					TCPService.sendOrder(imei, command);
				}else{
					byte[] command= NinebotCommand.getScooterSet2Command(imei, 1, 2, 3, 4,5,6,7);
					TCPService.sendOrder(imei, command);
				}
				
			}else if(type==12){
				byte[] command= OmniCommand.getReadDeviceKey(imei);
				TCPService.sendOrder(imei, command);
			}else if(type==13){
				byte[] command= OmniCommand.getSetDeviceKey(imei, "12345678");
				TCPService.sendOrder(imei, command);
			}else if(type==14){
				byte[] command= OmniCommand.getShutDownCommand(imei);
				TCPService.sendOrder(imei, command);
			}else if(type==15){
				byte[] command= OmniCommand.getReStartCommand(imei);
				TCPService.sendOrder(imei, command);
			}else if(type==16){
				byte[] command= OmniCommand.getSetAddressCommand(imei, 0, "120.24.228.199", 10233);
				TCPService.sendOrder(imei, command);
			}else if(type==17){
				byte[] command= OmniCommand.getSetAPNCommand(imei, "internet",0, "user","password");
				TCPService.sendOrder(imei, command);
			}else if(type==18){
				byte[] command= NinebotCommand.getScooterInfo2Command(imei) ;
				TCPService.sendOrder(imei, command);
			}
		}
		
		System.out.println("输入端 退出");
	
		System.exit(0);
		
//		System.out.println("TimeMillis="+System.currentTimeMillis());
//		System.out.println("nanoTime="+System.nanoTime());

	}

}
