
package com.pgt.bikelock.servlet;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.mina.core.session.IdleStatus;

import com.alipay.api.internal.util.StringUtils;
import com.omni.lock.upfile.DeviceType;
import com.omni.lock.upfile.UpFileUtil;
import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bikelock.bo.BikeBo;
import com.pgt.bikelock.bo.SystemConfigBo;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.listener.MyServerHandler;
import com.pgt.bikelock.listener.MyTcpCallBack;
import com.pgt.bikelock.listener.ScooterServiceCallback;
import com.pgt.bikelock.resource.OthersSource;

/**
 * 
 * @ClassName:     AutoStartServlet
 * @Description:自启动服务类/start server class
 * @author:    Albert
 * @date:        2017年4月6日 下午8:36:04
 *
 */
public class AutoStartServlet extends HttpServlet {


	private TCPService tcpService;
	private com.omni.scooter.tcp.TCPService scooterService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void init() throws ServletException {
		
		try {
			//设备连接服务 9679/device connect service 9679
//			tcpService = new TCPService(9679,new MyServerHandler(), new MyTcpCallBack());
			tcpService = new TCPService(9679,new MyServerHandler(), new MyTcpCallBack(),"EO");
			tcpService.setIdleTime(IdleStatus.READER_IDLE, 8*60);// 8分钟读写为空闲/8 minutes unread is empty
			tcpService.start();
			
			scooterService = new com.omni.scooter.tcp.TCPService(9680, new ScooterServiceCallback());
			scooterService.setIdleTime(IdleStatus.READER_IDLE, 8*60);
			scooterService.start();
			
		} catch (IOException e) { 
			e.printStackTrace();
		}
	
		//配置初始化/configuration initialization
		OthersSource.init();
		
		try {
			
			if(!StringUtils.isEmpty(OthersSource.getSourceString("lock_gprs_version_file_path"))){//2合1/2 in 1
				UpFileUtil.setGprsPath(OthersSource.getSourceString("lock_gprs_version_file_path"));
			}
			
			//升级文件初始化/upgrade file initialization
			if(!StringUtils.isEmpty(OthersSource.getSourceString("lock_gprs_ble_version_file_path"))){//3合1/3 in 1
				UpFileUtil.setGprsBlePath(OthersSource.getSourceString("lock_gprs_ble_version_file_path"));
			}			
			
			if(!StringUtils.isEmpty(OthersSource.getSourceString("lock_gprs_ble_b_version_file_path"))){//3合1，B模
				UpFileUtil.setNGGBPath(OthersSource.getSourceString("lock_gprs_ble_b_version_file_path"));
				System.out.println("init data:"+OthersSource.getSourceString("lock_gprs_ble_b_version_file_path"));
			}
			
			if(!StringUtils.isEmpty(OthersSource.getSourceString("scooter_iot_file_path"))){//scooter iot
				UpFileUtil.setScooterPath(OthersSource.getSourceString("scooter_iot_file_path"));
				System.out.println("init data:"+OthersSource.getSourceString("scooter_iot_file_path"));
			}
			
			if(!StringUtils.isEmpty(OthersSource.getSourceString("scooter_ctl_file_path"))){//scooter ctl
				UpFileUtil.setControlXMPath(OthersSource.getSourceString("scooter_ctl_file_path"));
				System.out.println("init data:"+OthersSource.getSourceString("scooter_ctl_file_path"));
			}
			
			UpFileUtil.reLoad();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			public void run() {
				//start server
				startCheckServer();
				//init system config
				SystemConfigBo.getInstance();
			}
		}).start();
		
	}
	
	public static void main(String[] args) throws IOException {
		if(!StringUtils.isEmpty(OthersSource.getSourceString("scooter_ctl_file_path"))){//scooter ctl
			UpFileUtil.setControlXMPath(OthersSource.getSourceString("scooter_ctl_file_path"));
			System.out.println("init data:"+OthersSource.getSourceString("scooter_ctl_file_path"));
			UpFileUtil.reLoad();
		}
		int size = UpFileUtil.getRealLen(DeviceType.CONTROL_XM);
		System.out.println(size);
		int totalPack = UpFileUtil.getTotalPack(DeviceType.CONTROL_XM);
		System.out.println(totalPack);
	}


	public void destroy () {
		// Do a little cleanup. Important! when server is redeploying code, if
		// daemon tcp-service does not unbind from port quick enough, the port will still be
		// in use on new deployment and the new tcp-service will fail to start!
		System.out.println("AutoStartServlet destroy");
		tcpService.stop();
		scooterService.stop();
		super.destroy();
	}

	/**
	 * 
	 * @Title:        startCheckServer 
	 * @Description:  启动检查服务/start check service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 下午8:37:34
	 */
	private void startCheckServer(){
		//单车检查服务启动/bike check server startup
		BikeBo.getInstance().startBikeCheckService();;
		
		startCleanStaticPropertyService();
	}
	
	/**
	 * 
	 * @Title:        startCleanStaticPropertyService 
	 * @Description:  全局属性清除服务/public static property clean service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月1日 下午6:11:24
	 */
	public void startCleanStaticPropertyService(){
		Runnable runnable = new Runnable() {  
			public void run() {  
				// task to run goes here  
				ParamsFilter.clientMap.clear();
				ParamsFilter.tokenMap.clear();
			}  
		};
		ScheduledExecutorService service = Executors  
				.newSingleThreadScheduledExecutor();  
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间/second parameter is first carry out delay time, third parameter is  timed execution interval time
		service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.DAYS); 
	}
	

}

