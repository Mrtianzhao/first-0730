package net.dgg;

import net.dgg.cloud.utils.Base64U;
import net.dgg.cloud.utils.MD5Utils;
import net.dgg.framework.utils.HttpRequest;
import net.sf.json.JSONObject;

import java.util.Date;

/**
 * Created by Enzo Cotter on 2018/5/10.
 */
public class UnicomTest {
    //调用版本
    private static String version = "20170703";
    //调用地址
    private static String ip = "apiusertest.emic.com.cn";
    //主账号Id
    private static String accountid = "ea8a91f85ce8804ce5de0f8cfe218520";
    //主账号授权令牌
    private static String authtoken = "77d3f8d4aefe41923012e57ddef9df44";
    private static String zongNum = "02863120241";
    //子账号Id
    private static String subAccountSid = "70eefef62966f768240b6e2b869ff494";
    //子账号授权令牌
    private static String subAccountSidtoken = "4a7e58424ed970ac1c546fbeb08421be";
    private static String appid = "9b7e3b02e425676a73e37ae6aff50fe5";

    public static void main(String[] args) {
        try {
            Long d = new Date().getTime();
            System.out.println(d);
            String sig = MD5Utils.string2MD5(subAccountSid+subAccountSidtoken+d).toUpperCase();
            System.out.println("sig="+sig);
            String url = "http://"+ip+"/"+version+"/SubAccounts/"+subAccountSid+"/Enterprises/createNumberPair?sig="+sig;
            System.out.println("url="+url);
            String Authorization = Base64U.getBase64(subAccountSid+":"+d);
            JSONObject rootjson = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("appId", appid);
            jsonObject.accumulate("numberA", "18010602319");
            jsonObject.accumulate("numberB", "18380413398");
            jsonObject.accumulate("maxAge", "3600");
            rootjson.accumulate("createNumberPair", jsonObject);
            String sp = rootjson.toString();
            System.out.println(sp);
            //String t = UnicomHttpClientUtil.doPost(url, sp,Authorization);
            String t = HttpRequest.httpPostUnicomString(url, sp,Authorization);
            System.out.println(t);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
