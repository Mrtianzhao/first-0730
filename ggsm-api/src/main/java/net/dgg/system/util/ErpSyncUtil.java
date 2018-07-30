package net.dgg.system.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gene on 2017/8/24.
 * desci:erp同步工具
 */
public class ErpSyncUtil {

    /**
     * erp同步工具
     * @param uri  远程地址
     * @param jsonString  查询数据，json格式
     * @return
     */
    public static String sendPost(String uri,String jsonString) {
        //创建
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //方式
          HttpPost httpPost = new HttpPost(uri);

        try {
            //httpPost.addHeader("Content-type","application/json; charset=utf-8");
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            //httpPost.setHeader("Accept", "application/json; charset=utf-8");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("jsonData", jsonString));

            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf-8");

            httpPost.setEntity(urlEncodedFormEntity);
            //执行
            org.apache.http.HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            //返回结果
            String result = EntityUtils.toString(entity);

            //System.out.println(result);
            System.out.println("[info]远程调用状态："+response.getStatusLine().getStatusCode());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public  static String sendGet(String urlStr){
		StringBuilder sb = null;  
        HttpURLConnection con = null;  
        BufferedReader br = null;  
        try {  
            URL url = new URL(urlStr);  
            
            con = (HttpURLConnection) url.openConnection();  
            //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
            con.connect();  
            sb = new StringBuilder();  
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));  
            String temp;  
            while ((temp = br.readLine()) != null) {  
                sb.append(temp);  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } finally {  
            if (br != null) {  
                try {  
                    br.close();  
                } catch (IOException e) {  
                    br = null;  
                    throw new RuntimeException(e);  
                } finally {  
                    if (con != null) {  
                        con.disconnect();  
                        con = null;  
                    }  
                }  
            }  
        } 
        return sb.toString();
	}
    
}

