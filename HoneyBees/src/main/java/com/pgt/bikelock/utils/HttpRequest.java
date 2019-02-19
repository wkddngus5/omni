package com.pgt.bikelock.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.authorize.util.LogHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * 网络请求工具类/internet request tool
 * @author apple
 *2017年03月07日15:10:11/15:10:11, March 7, 2017
 */
public class HttpRequest {
	/**
	 * 向指定URL发送GET方法的请求/ Send a GET request to the specified URL
	 * 
	 * @param url
	 *            发送请求的URL/the URL for sending request
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。/request parameter, which format should be name1=value1&name2=value2
	 * @return URL 所代表远程资源的响应结果/represents the response result of the remote source 
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			if(!StringUtils.isEmpty(param)){
				urlNameString += "?" + param;
			}
			
			URL realUrl = new URL(urlNameString);
			System.out.println(realUrl);
			// 打开和URL之间的连接/open the connection with the URL
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性/set the universal request attribute
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接/creat the actual connection
			connection.connect();
			// 获取所有响应头字段/get all the response header field
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段/traverse all the response header field
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应/ define BufferedReader input stream read the URL response
			in = new BufferedReader(new InputStreamReader(connection  
					.getInputStream()));  
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流/close the input stream with finally module
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求/send request of POST method to the specified URL
	 * 
	 * @param url
	 *            发送请求的 URL/URL sending request
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。/request parameter, which should be the format of name1=value1&name2=value2
	 * @return 所代表远程资源的响应结果/represents the response result of the remote source
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接/open the connection with the URL
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性/set the universal request attribute
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("encoding", "utf-8");
			// 发送POST请求必须设置如下两行/set as the following two lines if sending the POST request
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流/get the output stream of the URLConnection
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
			// 发送请求参数/sending request parameter
			out.print(param);
			// flush输出流的缓冲/buffer of the flush output stream
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应/get URL response by defining BufferReader input stream
			in = new BufferedReader(new InputStreamReader(conn  
					.getInputStream()));  
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("post response:"+result);
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！/abnormal when send POST request"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流/close the output stream, input stream by finally module
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}


	public static String sendPost(String url, String param,Map<String, String> headParamsMap) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接/open the connection with the URL
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性/set the universal request attribute
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("encoding", "utf-8");
			if(headParamsMap.containsKey("Authorization")){
				conn.setRequestProperty("Authorization",headParamsMap.get("Authorization").toString());
			}
			if(headParamsMap.containsKey("Content-Type")){
				conn.setRequestProperty("Content-Type",headParamsMap.get("Content-Type").toString());
			}
			if(headParamsMap.containsKey("timestamp")){
				conn.setRequestProperty("X-timestamp", headParamsMap.get("timestamp").toString());
			}

			// 发送POST请求必须设置如下两行/set as the following two lines if sending the POST request
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流/get the output stream of the URLConnection
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
			// 发送请求参数/sending request parameter
			out.print(param);
			// flush输出流的缓冲/buffer of the flush output stream
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应/get URL response by defining BufferReader input stream
			in = new BufferedReader(new InputStreamReader(conn  
					.getInputStream()));  
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("post response:"+result);
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流/close the output stream, input stream by finally module
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void sendHttpPost(String url,Map<String, Object> paramsMap){

		HttpPost httpPost = null;
		try {
			URI postUrl = new URI(url);
			httpPost = new HttpPost(postUrl);
			if(paramsMap != null){
				int i = 0;
				for (Entry<String, Object> param : paramsMap.entrySet()) {
					//httpPost.getParams().setParameter(param.getKey(), param.getValue());
					
					if(i == 0){
						url += "?"+param.getKey()+"="+param.getValue();
					}else{
						url += "&"+param.getKey()+"="+param.getValue();
					}
					i++;
				}
			}
			System.out.println("url"+url);
			if(paramsMap.containsKey("signature")){
				httpPost.setHeader("Authorization", "SIGNATURE VENDOR_CODE:"+paramsMap.get("signature"));
			}
			if(paramsMap.containsKey("timestamp")){
				httpPost.setHeader("X-timestamp", paramsMap.get("timestamp").toString());
			}
			DefaultHttpClient httpClient = new DefaultHttpClient();
			// execute the request
			HttpResponse httpResponse = httpClient.execute(httpPost);
			String rawResponseString = null;
			if(httpResponse != null) {
				HttpEntity entity = httpResponse.getEntity();

				// get the raw data being received
				InputStream instream = entity.getContent();
				rawResponseString = convertStreamToString(instream);
				
			}
			System.out.println("rawResponseString:"+rawResponseString);
			httpClient.getConnectionManager().shutdown();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Converts a response inputstream into a string.
	 *
	 * @param is
	 * @return String
	 */
	public static String convertStreamToString(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line).append("\n");
	        }
	    } catch (IOException e) {
	    	//LogHelper.warn(logger, "Exception reading data from Stream: '%s'", e.getMessage());
	    } finally {
	    	if ( null != reader){
	    		try {
	    			reader.close();
		        } catch (IOException e) {
		        	//LogHelper.warn(logger, "Exception closing BufferedReader: '%s'", e.getMessage());
		        }
	    	}

	    	if ( null != is) {
		    	try {
		            is.close();
		        } catch (IOException e) {
		        	//LogHelper.warn(logger, "Exception closing InputStream: '%s'", e.getMessage());
		        }
	    	}
	    }
	    return sb.toString();
	}

	
	public static void lockWithSms(){
		//		String[] cardNo = {"1064715185745","1064715185556","1064715185254","1064715184996","1064715184814","1064715184696","1064715184818"};
		//		String[] cardId = {"898607B1191750665745","898607B1191750665556","898607B1191750665254","898607B1191750664996","898607B1191750664814"
		//				,"898607B1191750664696","898607B1191750664818"};


		//		String[] cardNo = {"1064715185556","1064715185254","1064715184996","1064715184696","1064715184818"};
		//		String[] cardId = {"898607B1191750665556","898607B1191750665254","898607B1191750664996",
		//				"898607B1191750664696","898607B1191750664818"};

		String[] cardNo = {"1064888648677","1064890740002","1064890740213","1064890740315",
				"1064890740923","1064890741362","1064890741561","1064890741955","1064890742266","1064890742467","1064890742744","1064890744570",};
		String[] cardId = {"864501037584341","864501036379495","864501035602632","864501036010165","863725035967353",
				"864501036022061","864501036360941","864501036154617","864501037226604","863725036044871"
				,"863725032611863","864501035762964"};


		int j = 3;

		while (j > 0) {
			for (int i = 0; i < cardId.length; i++) {
				String no = cardNo[i];
				String id = cardId[i];
				String content = "MCHNum=23073001&M2MCardNo=%s&content=*CMDS,OM,%s,161201150000,L0,0#";
				content = String.format(content, no,id);
				System.out.println(content);
				//发送 GET 请求/Send GET request
				String s=HttpRequest.sendPost("http://m2m.zwyxit.com/api/MSG/sendMSG", content);
				System.out.println(s);
			}
			j --;
		}
	}

	
	public static void main(String[] args) {
		//发送 POST 请求/send POST request

		String sr = HttpRequest.sendPost("http://localhost:8080/OmniBikeLock/app/user?requestType=20025&amount=10&token=CDBA3C0A81420CAC8D66A0D84DD8BE628D4633F2EC380D52E5F949E68E143251BF9F3705D4575DA2BA3D27D39CD5A48833E87332F4CFBC966A67FC356BADA9D51BD3180897B325632F981677ADC330B79D8EA1E3FD225F34466CEBC9407D41378D3B3D464A5FE0E7269D0842334BE6BD&number=66755000210",null);
		//String sr=HttpRequest.sendGet("http://localhost:8080/OmniBikeLock/app/bike?requestType=30004&token=CDBA3C0A81420CAC8D66A0D84DD8BE622D073A2151DE07C2FB7CE342E66359ABE9A5E367A58CB1FEA61993F2090EEF487550A005D1C1382C832A9654112490BEFB3990209EB6EFC63C289ECCC5DA37ED86AA894C8CE4C2609F899828CD41D15AC53CE35C7D943CA7ACC66143875682B6&bikeNumber=66755000634&startLat=32.22&startLng=32.45",null);
		//String sr=HttpRequest.sendPost("http://localhost:8080/OmniBikeLock/app/bike?requestType=30005&token=CDBA3C0A81420CAC8D66A0D84DD8BE622D073A2151DE07C2FB7CE342E66359ABC12406E2C54E74E8A3E86A13991843FE7550A005D1C1382C832A9654112490BEFB3990209EB6EFC63C289ECCC5DA37ED86AA894C8CE4C2609F899828CD41D15AC53CE35C7D943CA7ACC66143875682B6&bikeNumber=66755000634&lat=32.22&lng=32.45",null);
		//sr = HttpRequest.sendPost("http://13.58.167.90:8088/OmniTest/test?type=103&customerId=0A6AD591B7D05636C1701E91A3C557E2", null);
		System.out.println(sr);	

		//		lockWithSms();

		//		String[] imei = new String[]{"864501036430223","864501035916131","864501036198044",
		//				"864501035896317","864501035904087","864501035609629",
		//				"864501035774373","864501035603937","864501036623041",
		//				"864501036149724"};
		//		for (int i = 0; i < imei.length; i++) {
		//			 String sr=HttpRequest.sendPost("http://g9o2t6.56fm.com:8088/BikeServer/bike?type=1001&imei="+imei[i],null);
		//		        System.out.println(sr);
		//		}

	}
}
