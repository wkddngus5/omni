/**
 * FileName:     TestTcpClient.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月11日 上午10:35:51
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.listener;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @ClassName:     TestTcpClient
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月11日 上午10:35:51
 *
 */
public class TestTcpClient {

	/** 
	 * @Title:        main 
	 * @Description:  TODO
	 * @param:        @param args    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月11日 上午10:35:51 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			public void run() {
				while(true){
					try {
						Socket client = new Socket("127.0.0.1",9679);
						Thread.sleep(50);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

}
