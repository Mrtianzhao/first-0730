package net.dgg.cloud.controller;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.utils.ZhPath;
import net.dgg.framework.utils.HttpClientUtils;
import net.dgg.framework.utils.ResourceUtils;
import net.dgg.framework.utils.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Enzo Cotter on 2018/5/17.
 */
@Controller
@RequestMapping("/api/zh/")
public class ZhController {
    /**
     * @author  yinyao
     * @create  2018/5/17 16:20
     *  * @param 双向回拨
     * @desc
     **/
    @RequestMapping("/twoWayRedial")
    @ResponseBody
    public Object twoWayRedial(@RequestBody JSONObject jsonObject,@RequestHeader String token){
        Map<String,Object> retMap = new HashMap<>();
        if (StringUtils.isBlank(token)){
            retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
            retMap.put("msg","登录失效，请重新登录！");
            return retMap;
        }
        Map<String,String> param = jsonObject;
        String[] keys = {"from","to","number"};
        String errorKey = "";
        String kongKey = "";
        for (int i = 0; i < keys.length; i++) {
            if(!param.containsKey(keys[i])){
                if("".equals(errorKey)){
                    errorKey = keys[i];
                }else{
                    errorKey += ","+keys[i];
                }
            }else{
                String value = param.get(keys[i]);
                if(StringUtils.isBlank(value)){
                    if("".equals(kongKey)){
                        kongKey = keys[i];
                    }else {
                        kongKey += ","+keys[i];
                    }
                }
            }
        }
        String msg = "";
        if(!"".equals(errorKey)){
            msg = errorKey+"参数缺少";
        }
        if(!"".equals(kongKey)){
            if("".equals(msg)){
                msg += kongKey+"参数值为空!";
            }else{
                msg += ","+kongKey+"参数值为空!";
            }
        }
        if(!"".equals(errorKey)||!"".equals(kongKey)){
            retMap.put("code", -1);
            retMap.put("msg",msg);
            return retMap;
        }
        if(!StringUtils.isMobile(param.get("from"))){
            retMap.put("code", -1);
            retMap.put("msg","主叫号码格式不对!");
            return retMap;
        }
        String to = param.get("to");
        if("0".equals(to.substring(0, 1))){
            if(!StringUtils.checkTelephone(to)){
                retMap.put("code", -1);
                retMap.put("msg","被叫号码格式不对!");
                return retMap;
            }
        }else{
            if(!StringUtils.isMobile(to)){
                retMap.put("code", -1);
                retMap.put("msg","被叫号码格式不对!");
                return retMap;
            }
        }
        //String url = ZhPath.getZhPath(param.get("number"))+"/tp32/index.php?s=/Home/dial/two";
        Map<String, String> constantMap = ResourceUtils.getResource("constants").getMap();
        String url = constantMap.get("app.call.path");
        Map<String, Object> result = HttpClientUtils.URLPost(url, param);
        if(result.get("response")==null || (String)result.get("response")==""){
            retMap.put("code", -1);
            retMap.put("msg","拨打电话调用接口发生网络异常!");
            return retMap;
        }
        JSONObject resJson = JSONObject.fromObject(result.get("response"));
        System.out.println(resJson);
        return resJson;
    }

    /**
     * @author  yinyao
     * @create  2018/5/17 16:20
     *  * @param 获取录音地址
     * @desc
     **/
    @RequestMapping("/getZhPlayRecord")
    @ResponseBody
    public Object getZhPlayRecord(@RequestParam String seatNumber){
        String record = "D8HSEwOPrYdVh3XMZ%2FOSDfqDkYifgSuxgkqiHBOIWx2BI2CU32Gp%2BjErHqNBb%2BdQiJBwwVHfHCiCx%2FCRL7yKLQ%3D%3D";
        String zhPlayPath = ZhPath.getAllPlayPath(seatNumber, record);
        return zhPlayPath;
    }
}
