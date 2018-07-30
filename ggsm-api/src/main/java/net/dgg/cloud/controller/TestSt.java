package net.dgg.cloud.controller;

import net.dgg.cloud.dto.CallRecord;
import net.dgg.cloud.dto.PhTestDto;
import net.dgg.cloud.utils.Base64U;
import net.dgg.cloud.utils.MD5Utils;
import net.dgg.framework.utils.HttpRequest;
import net.dgg.framework.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anziwen on 2018/4/18.
 */
@Controller
@RequestMapping("/t")
public class TestSt {
    private static String APPID ="8a216da862cc8f910162d2626bfe03a3";
    private static String ACCOUNTSID ="8aaf07085f004cdb015f0f97e44b06e2";  //账户ID
    private static String AUTHTOKEN ="344a1bc293484d25b505f1a35fdc0c0e";  //账户授权ken
    private static String TYPE ="axb";  //号码类型
    private static String OP ="setnumber";  //操作名
    private static String ip ="http://115.29.163.144:3051/2013-12-26"; //访问地址
    private static String notifyUrl ="http://101.207.120.64:8889/t/list"; //   话单推送地址



    @ResponseBody
    @RequestMapping("/list")
    public Map getAllRegular(@RequestBody CallRecord callRecord) {
        System.out.println("----进入通话记录------"+callRecord.getCallId());
        Map m  = new HashMap();
        m.put("statusCode","000000");
        return m;
    }

    @ResponseBody
    @RequestMapping("/te")
    public String getRegular() {
        //String url = "/Accounts/{accountSid}/nme/axb/{operatorId}/set";
        String s = "";
        long d = new Date().getTime();
        String m=ACCOUNTSID+AUTHTOKEN+d;
        String sig = MD5Utils.string2MD5(m);
        String url = "/Accounts/"+ACCOUNTSID+"/nme/"+TYPE+"/cu12/set";
        //String url = "/Accounts/"+ACCOUNTSID+"/nme/"+TYPE+"/cu12/"+OP+"?sig="+sig.toUpperCase();
        System.out.println(sig+"---------"+sig.toUpperCase());

        Base64U base64U = new Base64U();
        String acc = ACCOUNTSID+":"+d;
        System.out.println("----"+base64U.getBase64(acc));
        String au = base64U.getBase64(acc);

        PhTestDto ph = new PhTestDto();
        ph.setAppId(APPID);
        ph.setaNumber("18380285053"); //用户A的电话号码
        ph.setbNumber("18380413398"); //用户B的电话号码
        ph.setxNumber("17071057637"); //	指定的服务号（中间号）
        ph.setExpirationSetting("3"); //中止设定。0：未设定；1：一次呼叫后中止，2：一次成功呼叫后中止，3：指定时长后中止
        ph.setNeedRecord("true");  //是否需要录音
        ph.setCdrNotifyUrl(notifyUrl); //话单推送地址

        s = JsonUtils.toJSONString(ph);
        try {
            System.out.println("URL----==========-----"+ip+url);
            System.out.println("json数据----===-------===-----"+s);
            System.out.println("Authorization----===-------===-----"+au);
            String t = HttpRequest.httpPostString(ip+url, s,au);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1111111";
    }
}
