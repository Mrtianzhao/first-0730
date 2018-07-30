package net.dgg.framework.utils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资源文件工具类
 * @author nature
 *
 */
public class ResourceUtils {

	private ResourceBundle resourceBundle;

	private ResourceUtils(String resource) {
		resourceBundle = ResourceBundle.getBundle(resource);
	}

	/**
	 * 获取资源
	 * 
	 * @param resource
	 *            资源
	 * @return 解析
	 */
	public static ResourceUtils getResource(String resource) {
		return new ResourceUtils(resource);
	}

	/**
	 * 根据key取得value
	 * 
	 * @param key
	 *            键值
	 * @param args
	 *            value中参数序列，参数:{0},{1}...,{n}
	 * @return
	 */
	public String getValue(String key, Object... args) {
		String temp = resourceBundle.getString(key);
		return MessageFormat.format(temp, args);
	}

	/**
	 * 获取所有资源的Map表示
	 * 
	 * @return 资源Map
	 */
	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (String key : resourceBundle.keySet()) {
			map.put(key, resourceBundle.getString(key));
		}
		return map;
	}

	//获取2者之间随机数
	public static int getRandom(int begin,int end){
		Random random=new Random();
		int s = random.nextInt(end-begin)+begin; //m+n-1之间的随机数
		return s;
	}

	//返回计算好的时间
	public static Date getJisuan(Date date){
		long datetime = date.getTime()/1000;
		long s = (long) getRandom(3600,7200);
		datetime=datetime+s;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String da = sdf.format(new Date(Long.valueOf(datetime+"000")));
		Date d = null;
		try {
			d = sdf.parse(da);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return d;
	}
}