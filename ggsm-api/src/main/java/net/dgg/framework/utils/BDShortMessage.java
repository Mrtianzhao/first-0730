package net.dgg.framework.utils;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * Created by 王 on 2018/5/24.
 *
 * 业务名称：北京顶呱呱企业管理有限公司
 签名：【顶呱呱】
 业务代码：106900329335
 账号：dgg688
 密码：qygl21
 *
 */
public class BDShortMessage {
    private static String corp_service ="106900329335";
    private static String corp_id ="dgg688";
    private static String corp_pwd ="qygl21";

    public static void shortMessage(String mobile,String msg_content) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://client.cloud.hbsmservice.com:8080/sms_send2.do");
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        NameValuePair[] data = {
                new NameValuePair("corp_id", corp_id),
                new NameValuePair("corp_pwd", corp_pwd),
                new NameValuePair("corp_service", corp_service),
                new NameValuePair("mobile", mobile),
                new NameValuePair("msg_content", msg_content),
                new NameValuePair("corp_msg_id ","短信Id"),
                new NameValuePair("ext ","扩展小号")
        };
        post.setRequestBody(data);
        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:" + statusCode);
        for (Header h : headers) {
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString());
        System.out.println(result);
        post.releaseConnection();
    }
}
