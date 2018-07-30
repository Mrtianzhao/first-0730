package net.dgg.cloud.utils;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import net.dgg.framework.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by gene on 2017/10/16.
 * desc:jpush消息推送工具
 */
public class JpushUtil {

    private static Logger logger = LoggerFactory.getLogger(JpushUtil.class);

    //加載配置文件
    static Map<String, String> configMap;
    static {
        configMap = ResourceUtils.getResource("constants").getMap();
    }

    private final static String appKey = configMap.get("jpush.appKey");

    private final static String masterSecret = configMap.get("jpush.masterSecret");
    /**
     * 保存离线的时长。秒为单位。最多支持10天（864000秒）。 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
     * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒)。
     */
    private static long timeToLive = Long.parseLong(configMap.get("jpush.timeToLive"));

    private static JPushClient jPushClient = null;

    /**
     * 所有平台，所有设备，内容为 content 的通知
     * @param content
     * @return
     */
    public static PushPayload buildPushObject_all_all_alert(String content) {
        return PushPayload.alertAll(content);
    }

    /**
     * 根据 设备终端ID 推送消息
     *
     * @param regesterIds
     *            设备终端ID集合
     * @param content
     *            内容
     * @return
     */
    public static PushPayload buildPushObject_all_all_regesterIds(List<String> regesterIds, String content) {
        return PushPayload.newBuilder().setPlatform(Platform.all())
                .setAudience(Audience.registrationId(regesterIds))
                .setNotification(Notification.alert(content))
                .build();
    }


    /**
     * 所有平台，推送目标是别名为 "alias"，通知内容为 TEST 。特别说明：这里是轨迹相关定时任务调用的方法重写。
     *
     * @param alias
     * @param content
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String alias, String content, String msgType, String name,String jobNo,String phone) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).
                setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(content)
                                .incrBadge(1)
                                .setSound("happy")
                                .addExtra("type", msgType)
                                .addExtra("name", name==null?"":name)
                                .addExtra("jobNo", jobNo == null ? "":jobNo)
                                .addExtra("phone", phone == null ? "" : phone)
                                .build())
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(content)
                        .addExtra("type", msgType)
                        .addExtra("name", name==null?"":name)
                        .addExtra("jobNo", jobNo == null ? "":jobNo)
                        .addExtra("phone", phone == null ? "" : phone)
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .setTimeToLive(timeToLive)
                        .build())
                .build();
    }



    /**
     * 平台(安卓，ios)，推送目标是别名为 "alias"，通知内容为 TEST
     *
     * @param alias
     * @param content
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String alias, String content,String msgType,String param) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).
                setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(content)
                                .incrBadge(1)
                                .setSound("happy")
                                .addExtra("type", msgType)
                                .addExtra("param",param)
                                .addExtra("pkey","CLOUD")
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(content)
                                .addExtra("type", msgType)
                                .addExtra("param",param)
                                .addExtra("pkey","CLOUD")
                                .build())
                        .build())
                /*.setMessage(Message.newBuilder()
                        .setMsgContent(content)
                        .addExtra("type", msgType)
                        .addExtra("param",param)
                        .addExtra("pkey","CLOUD")
                        .build())*/
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .setTimeToLive(timeToLive)
                        .build())
                .build();
    }

    /**
     * 所有平台，所有设备，内容为 content 的通知
     * @param msgContent 发送内容
     * @return
     */
    public static boolean sendPushAll(String msgContent) {
        jPushClient = new JPushClient(masterSecret, appKey, (int)timeToLive);
        boolean flag = false;
        try {
            //String title = "推送测试";
            PushPayload payload = buildPushObject_all_all_alert(msgContent);
            System.out.println("服务器返回数据：" + payload.toString());

            PushResult result = jPushClient.sendPush(payload);
            if (null != result) {
                logger.info("Get resul ---" + result);
                flag = true;
            }
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            flag = false;
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            flag = false;
        }
        return flag;

    }

    /**
     * 根据 设备终端ID 推送消息
     * @param regeSterIds 设备终端id
     * @param msgContent 发送内容
     * @return
     */
    public static boolean  sendPushByRegesterId(List<String> regeSterIds,String msgContent) {
        jPushClient = new JPushClient(masterSecret, appKey);
        boolean flag = false;
//      String content = "多个ID测试";
        try {
            PushPayload payload = buildPushObject_all_all_regesterIds(regeSterIds,msgContent);
            System.out.println("服务器返回数据：" + payload.toString());
            PushResult result = jPushClient.sendPush(payload);
            if (null != result) {
                logger.info("Get result ----" + result);
                flag = true;
            }
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            flag = false;
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            flag = false;
        }

        return flag;
    }

    /**
     * 所有平台，推送目标是别名为 "alias"，通知内容为 TEST
     * @param alias
     * @param msgContent
     * @return
     */
    public static boolean  sendPushByAlias(String alias, String msgContent,String msgType,String param) {
        jPushClient = new JPushClient(masterSecret, appKey);
        boolean flag = false;
//      String content = "多个ID测试";
        try {
            PushPayload payload = buildPushObject_all_alias_alert(alias,msgContent,msgType,param);
            System.out.println("服务器返回数据：" + payload.toString());
            PushResult result = jPushClient.sendPush(payload);
            if (null != result) {
                logger.info("Get result ----" + result);
                flag = true;
            }
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            flag = false;
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            flag = false;
        }
        return flag;
    }


    /**
     * 所有平台，推送目标是别名为 "alias"，通知内容为 TEST。特别说明，这里是轨迹相关定时任务调用的方法重写
     * @param alias
     * @param msgContent
     * @return
     */
    public static boolean  sendPushByAlias(String alias, String msgContent,String msgType,String name,String jobNo,String phone) {
        jPushClient = new JPushClient(masterSecret, appKey);
        boolean flag = false;
//      String content = "多个ID测试";
        try {
            PushPayload payload = buildPushObject_all_alias_alert(alias,msgContent,msgType, name, jobNo, phone);
            System.out.println("服务器返回数据：" + payload.toString());
            PushResult result = jPushClient.sendPush(payload);
            if (null != result) {
                logger.info("Get result ----" + result);
                flag = true;
            }
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            flag = false;
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            flag = false;
        }
        return flag;
    }



}
