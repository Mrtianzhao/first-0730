package net.dgg.cloud.utils;

import net.dgg.framework.utils.ResourceUtils;

/**
 *
 *
 * @author zyou
 */
public class CloudUtils {

	/**
	 * 判断变量是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj){
		if(null == obj || "".equals(obj.toString()) || "".equals(obj.toString().trim()) || "null".equalsIgnoreCase(obj.toString())){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断变量是否为空
	 *
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (null == s || "".equals(s) || "".equals(s.trim()) || "null".equalsIgnoreCase(s)) {
			return true;
		} else {
			return false;
		}
	}
	public static String getHost() {
		//加载配置文件
		String fastdfsHost = ResourceUtils.getResource("constants").getMap().get("fasdfs.host");
		String port=ResourceUtils.getResource("constants").getMap().get("fastdfs.tracker_http_port");
		String host=fastdfsHost+":"+port+"/";
		return host;
	}
}
