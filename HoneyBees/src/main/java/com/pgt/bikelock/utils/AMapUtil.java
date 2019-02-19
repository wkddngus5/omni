package com.pgt.bikelock.utils;

import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.LatLng;


public class AMapUtil {

	private static double PI = 3.14159265;
	private static double EARTH_RADIUS = 6378137;
	private static double RAD = Math.PI / 180.0;


	/// <summary>
	/// 根据提供的经度和纬度、以及半径，取得此半径内的最大最小经纬度/obtain the maximum and minimum latitude and longitude within the radius according to the provided longitude and latitude, and the radius
	/// </summary>
	/// <param name="lat">纬度</param>/latitude
	/// <param name="lon">经度</param>/longtitue
	/// <param name="raidus">半径(米)</param>/radius (meter)
	/// <returns></returns>
	public static double[] GetAround(LatLng point, double raidus)
	{

		Double latitude = point.getLatitude();
		Double longitude = point.longitude;

		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, minLng, maxLat, maxLng };
	}

	/// <summary>
	/// 根据提供的两个经纬度计算距离(米)/calculate the distance (m) according to the two latitude and longitude
	/// </summary>
	/// <param name="lng1">经度1</param>/longtitude 1
	/// <param name="lat1">纬度1</param>/latitude 1
	/// <param name="lng2">经度2</param>/longtitude 2
	/// <param name="lat2">纬度2</param>/latitude 2
	/// <returns></returns>
	public static double GetDistance(double lng1, double lat1, double lng2, double lat2)
	{
		double radLat1 = lat1 * RAD;
		double radLat2 = lat2 * RAD;
		double a = radLat1 - radLat2;
		double b = (lng1 - lng2) * RAD;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
				Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/**
	 * 
	 * @Title:        calculateLineDistance 
	 * @Description:  计算经纬度距离/calculate the distance of latitude and longitude
	 * @param:        @param lat0
	 * @param:        @param lng0
	 * @param:        @param lat1
	 * @param:        @param lng1
	 * @param:        @return    
	 * @return:       float  距离（米）/distance(m)
	 * @author        Albert
	 * @Date          2017年9月18日 下午8:35:31/8:35:31 pm, September 18,2017
	 */
	public static float calculateLineDistance(double lat0,double lng0,double lat1,double lng1){
		final double v2 = 0.01745329251994329D;
		double v4=lng0;
		double v6=lat0;
		double v8=lng1;
		double v10=lat1;
		v4 *=v2;
		v6 *=v2;
		v8 *=v2;
		v10 *=v2;
		double v12 = Math.sin(v4);
		double v14=Math.sin(v6);
		double v16=Math.cos(v4);
		double v18 = Math.cos(v6);
		double v20=Math.sin(v8);
		double v22 = Math.sin(v10);
		double v24 = Math.cos(v8);
		double v26=Math.cos(v10);

		double[] var28 = new double[3];
		double[] var29=new double[3];
		var28[0]=v18 * v16;
		var28[1]=v18*v12;
		var28[2]=v14;
		var29[0]=v26*v24;
		var29[1]=v26*v20;
		var29[2]=v22;
		double v30=Math.sqrt((var28[0]-var29[0])*(var28[0]-var29[0]) + (var28[1]-var29[1])*(var28[1]-var29[1])+ (var28[2]-var29[2])*(var28[2]-var29[2]) );
		return (float) (Math.asin(v30/2.0D)*1.2742001579844E7D);

	}


	public static List<LatLng> decode(final String encodePath){
		int len = encodePath.length();
		final List<LatLng> path = new ArrayList<LatLng>();
		int index = 0;
		int lat = 0;
		int lng = 0;
		while(index<len){
			int result = 1;
			int shift = 0;
			int b;
			do{
				b = encodePath.charAt(index++) -63-1;
				result +=b << shift;
				shift += 5;
			}while(b>=0x1f);
			lat +=(result&1) !=0 ?~(result>>1) : (result>>1);

				result =1;
				shift = 0;
				do{
					b = encodePath.charAt(index++) -63-1;
					result += b<<shift;
					shift +=5;

				}while(b>=0x1f);

				lng +=  (result & 1) !=0  ? ~(result>>1) : (result>>1);
					path.add(new LatLng(lat*1e-5, lng * 1e-5));


		}
		return path;

	}


	public static String encode(final List<LatLng> path){
		long lastLat = 0;
		long lastLng = 0;
		final StringBuffer result = new StringBuffer();
		for(final LatLng point :path){
			long lat = Math.round(point.latitude * 1e5);
			long lng = Math.round(point.longitude * 1e5);

			long dLat = lat - lastLat;
			long dLng = lng - lastLng;

			encode(dLat,result);
			encode(dLng,result);

			lastLat = lat;
			lastLng = lng;

		}
		return result.toString();	
	}

	public static String encode(final LatLng point){
		long lastLat = 0;
		long lastLng = 0;
		final StringBuffer result = new StringBuffer();

		long lat = Math.round(point.latitude * 1e5);
		long lng = Math.round(point.longitude * 1e5);

		long dLat = lat - lastLat;
		long dLng = lng - lastLng;

		encode(dLat,result);
		encode(dLng,result);

		lastLat = lat;
		lastLng = lng;


		return result.toString();	
	}


	private static void encode(long v, StringBuffer result) {
		v = v<0?~(v<<1): v<<1;
		while(v>=0x20){
			result.append(Character.toChars( (int)(( 0x20 |(v&0x1f))+63 )));
			v >>=5;
		}
		result.append(Character.toChars((int)(v+63)));

	}

	/**
	 * 
	 * @Title:        checkPointInArea 
	 * @Description:  判断坐标是否在区域中/check the coordinate in the area or not
	 * @param:        @param point
	 * @param:        @param areaPath
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月7日 下午3:28:15/3:28:15 pm, July 7, 2017
	 */
	public static boolean checkPointInArea(LatLng point,String areaPath){
		List<LatLng> list = decode(areaPath);
		//创建多边形画板/create polygon drawing board
		GeneralPath  gPath = new GeneralPath();
		gPath.moveTo(list.get(0).getLatitude(), list.get(0).getLongitude());  

		for (LatLng latLng : list) {
			gPath.lineTo(latLng.getLatitude(), latLng.getLongitude());
			//poly.addPoint(((int)latLng.getLatitude()*polyPercent), ((int)latLng.getLongitude()*polyPercent));
		}
		gPath.closePath();
		return gPath.contains(point.getLatitude(),point.getLongitude());
	}

	/**
	 * 
	 * @Title:        checkPointInArea 
	 * @Description:  判断坐标是否在区域中或区域指定边界范围/check whether the coordinate in the area or area specified boundary range
	 * @param:        @param point 目标点/target point
	 * @param:        @param areaPath 区域/area
	 * @param:        @param raidus 边界范围(米)/boundary range(meter)
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月18日 下午4:06:55/4:06:55 pm, September 18, 2017
	 */
	public static boolean checkPointInArea(LatLng point,String areaPath,double raidus){
		List<LatLng> list = decode(areaPath);
		//创建多边形画板/create polygon drawing board
		GeneralPath  gPath = new GeneralPath();
		gPath.moveTo(list.get(0).getLatitude(), list.get(0).getLongitude());  

		for (LatLng latLng : list) {
			gPath.lineTo(latLng.getLatitude(), latLng.getLongitude());
			//poly.addPoint(((int)latLng.getLatitude()*polyPercent), ((int)latLng.getLongitude()*polyPercent));
			//边界范围判断/check the boundary range
			float distance = calculateLineDistance(point.getLatitude(),point.getLongitude(),latLng.getLatitude(),latLng.getLongitude());
			if(raidus > 0 && distance <= raidus){
				gPath.closePath();
				return true;
			}
		}
		gPath.closePath();
		return gPath.contains(point.getLatitude(),point.getLongitude());
	}

	/**
	 * 
	 * @Title:        checkPointInAreaWithDefaultRaidus 
	 * @Description:  判断坐标是否在区域中或区域在默认边界范围/check whether the coordinate in the area or area default boundary range
	 * @param:        @param point
	 * @param:        @param areaPath
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月18日 下午9:18:09/9:18:09 pm, September 18, 2017
	 */
	public static boolean checkPointInAreaWithDefaultRaidus(LatLng point,String areaPath){
		return checkPointInArea(point, areaPath, getBorderRaidus("area_border_distance"));
	}

	/**
	 * 
	 * @Title:        getBorderRaidus 
	 * @Description:  获取边界范围/get boundary range
	 * @param:        @return    
	 * @return:       double    
	 * @author        Albert
	 * @Date          2017年9月18日 下午9:16:34/9:16:34 pm, September 18, 2017
	 */
	public static double getBorderRaidus(String borderKey){
		if(OthersSource.getSourceString(borderKey) != null){
			if(OthersSource.getSourceString("area_border_distance_unit_percent") != null){
				double distance = ValueUtil.getDouble(OthersSource.getSourceString(borderKey));
				double percent = Float.parseFloat(OthersSource.getSourceString("area_border_distance_unit_percent"));
				return distance*percent*1000.00;
			}else{
				return ValueUtil.getDouble(OthersSource.getSourceString(borderKey))*1000.00;
			}

		}else{
			return 100;
		}
	}

	public static void main(String[] args) {
		/*		System.out.println(checkPointInAreaWithDefaultRaidus(new LatLng(22.63431672280278,114.1256207959779), "shfiC_nvwTrLrDbJgF~A{ZaUsHeDdFNl^"));
		//		System.out.println(calculateLineDistance(22.6370320519,114.1393895175, 22.6399434329,114.1372866657));
		System.out.println(GetDistance(40.42419,-86.912248, 40.44419,-86.912248));

		LatLng loca = new LatLng(22.6317873381,114.1319310665);
		double location[] = GetAround(loca, 100000);

		System.out.println("lat:"+location[0]+";lng:"+location[1]
				+";dlat:"+(loca.getLatitude()-location[0])+";dlng:"+(loca.getLongitude()-location[1]));


		List<LatLng> list = decode("owciCcdqwT");
		for (LatLng latLng : list) {
			if(!checkPointInAreaWithDefaultRaidus(latLng, "shfiC_nvwTrLrDbJgF~A{ZaUsHeDdFNl^")){
				System.out.println("out area:"+latLng.getLatitude()+","+latLng.getLongitude());
			}else{
				System.out.println("in area:"+latLng.getLatitude()+","+latLng.getLongitude());
			}
		}*/

		List<LatLng> list = decode("i}}~Fbwz|OGKGG?_@?m@C]?KPS^]RW\\i@Pu@Nc@Aw@Qy@C{@D}@NgA?{@?s@?Y?a@j@?\\?b@?");
		//		list.add(new LatLng(40.4254639, -86.9262606));
		for (LatLng latLng : list) {

			System.out.println(" area:"+latLng.getLatitude()+","+latLng.getLongitude());
			if(!checkPointInAreaWithDefaultRaidus(latLng, "so__G`h~|OxoAIj@uW~BmX~BePdCqNjAmPd@qRQwQm@}SKwMPaV?iUQuIEkHkJV_E|DyCs@wDsDkI~ByBcEeEoBe@{K_@gQkAgQs@qGeGPQpl@qS~BSrh@pVQd@xv@wFFMdy@XxUEfBqZ|AQzdALI")){
				System.out.println("out area:"+latLng.getLatitude()+","+latLng.getLongitude());
			}

			/*if(checkPointInArea(latLng,"qkwuFbjpqOT{MxA@|@@~@Fp@?`@A\\LTD`@VXZCtJaLPG@")){
				System.out.println("in no parking area:"+latLng.getLatitude()+","+latLng.getLongitude());
			}*/
		}

	}

}
