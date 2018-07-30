package net.dgg.cloud.controller;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Enzo Cotter on 2018/5/11.
 */
@Controller
@RequestMapping("/unicomTest/")
public class UnicomTestController {
    /**
     * @author  yinyao
     * @create  2018/5/11 10:24
     *  * @param 呼叫请求和振铃回调
     * @desc
     **/
    @RequestMapping("callreq")
    @ResponseBody
    public Object callreq(@RequestBody JSONObject jsonObject){
        Map map = jsonObject;
        System.out.println("呼叫请求和振铃回调=="+jsonObject.toString());
        return map;
    }

    /**
     * @author  yinyao
     * @create  2018/5/11 10:24
     *  * @param 呼叫建立回调
     * @desc
     **/
    @RequestMapping("callestablish")
    @ResponseBody
    public Object callestablish(@RequestBody JSONObject jsonObject){
        Map map = jsonObject;
        System.out.println("呼叫建立回调=="+jsonObject.toString());
        return map;
    }

    /**
     * @author  yinyao
     * @create  2018/5/11 10:24
     *  * @param 呼叫失败或挂机通知回调
     * @desc
     **/
    @RequestMapping("callhangup")
    @ResponseBody
    public Object callhangup(@RequestBody JSONObject jsonObject){
        Map map = jsonObject;
        System.out.println("呼叫失败或挂机通知回调=="+jsonObject.toString());
        return map;
    }

    /**
     * @author  yinyao
     * @create  2018/5/11 10:24
     *  * @param 通话录音就绪回调
     * @desc
     **/
    @RequestMapping("callrecordready")
    @ResponseBody
    public Object callrecordready(@RequestBody JSONObject jsonObject){
        Map map = jsonObject;
        System.out.println("通话录音就绪回调=="+jsonObject.toString());
        return map;
    }
}
