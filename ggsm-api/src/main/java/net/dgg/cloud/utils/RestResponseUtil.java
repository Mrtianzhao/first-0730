package net.dgg.cloud.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *Rest响应工具类
 */
public class RestResponseUtil {

    /**
     * 组装一个成功的返回报文
     * @param data
     * @return
     */
    public static Map<String, Object> getSuccessResult(Object data){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("msg", "成功");
        resultMap.put("data",data);
        return resultMap;
    }

    /**
     * 组装一个失败的响应报文
     * @param msg
     * @return
     */
    public static Map<String, Object> getFailResult(String msg){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 1);
        resultMap.put("data", "");
        resultMap.put("msg", msg);
        return resultMap;
    }
}
