package net.dgg.framework.api.aliyun;

import net.dgg.framework.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云企业信息查询接口
 * <p>
 * AppKey：24615794     AppSecret：7af7770fc2c6e594bf2705f6440627c4 复制
 * AppCode：21ac62da359e4f8b85a42206530b7128
 * Created by wu on 2017-09-07.
 */
public class AliyunComApi {

    static Logger logger = LoggerFactory.getLogger(AliyunComApi.class);

    public static String API_CDETAIL_HOST = "http://cdetail.market.alicloudapi.com";

    public static String API_CALL_ITEMS_HOST = "http://callitems.market.alicloudapi.com";

    public static String API_CDETAIL_APP_CODE = "21ac62da359e4f8b85a42206530b7128";

    public static String REQUEST_GET = "GET";

    public static String REQUEST_POST = "POST";


    /**
     * 企业信息查询
     */
    public static String API_CDETAILS_PATH = "/lianzhuo/cdetails";

    /**
     * 年报信息查询
     */
    public static String API_CANNUAL_REPORT_PATH = "/lianzhuo/cannualreport";

    static {
//        Map<String, String> configMap = ResourceUtils.getResource("application").getMap();
//        API_HOST = configMap.get("api.cdetail.url");
//        API_APP_CODE = configMap.get("api.cdetail.appcode");
    }

    public static String cdetails(String keyword) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + API_CDETAIL_APP_CODE);
        Map<String, String> querys = new HashMap();
        querys.put("keyword", keyword);
        querys.put("page", "1");
        BufferedReader reader = null;
        try {
            HttpResponse response = HttpUtils.doGet(API_CDETAIL_HOST, API_CDETAILS_PATH, REQUEST_GET, headers, querys);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String line = null;
                reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                logger.info("查询返回信息为:" + stringBuffer.toString());
                return stringBuffer.toString();
            } else {
                logger.info("未查询到企业信息");
            }
        } catch (Exception e) {
            logger.error("查询请求异常:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("关闭流异常");
                }
            }
        }
        return "";
    }

    public static String cannualReport(String keyword) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + API_CDETAIL_APP_CODE);
        Map<String, String> querys = new HashMap();
        querys.put("keyword", keyword);
        querys.put("page", "1");

        BufferedReader reader = null;
        try {
            HttpResponse response = HttpUtils.doGet(API_CALL_ITEMS_HOST, API_CANNUAL_REPORT_PATH, REQUEST_GET, headers, querys);
            logger.info(response.toString());
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String line = null;
                reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                reader.close();
                logger.info("查询返回信息为:" + stringBuffer.toString());
                return stringBuffer.toString();
            } else {
                logger.info("未查询到企业信息");
            }
        } catch (Exception e) {
            logger.error("查询请求异常:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    public static void main(String[] args) {
        String result = AliyunComApi.cannualReport("成都顶呱呱投资集团有限公司");
        // result=AliyunComApi.cdetails("成都顶呱呱投资集团有限公司");
        System.out.println(result);
    }
}
