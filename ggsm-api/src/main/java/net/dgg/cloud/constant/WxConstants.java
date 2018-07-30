package net.dgg.cloud.constant;

/**
 * @author ytz
 * @date 2018/5/15.
 * @desc
 */
public class WxConstants {

    /**
     * appid
     * 小顶上门：wx8f9b14d903e5382c
     * 一站式：wxa09c8878a6993878
     */
    public static final String APPID = "wxa09c8878a6993878";

    /**
     * app_secret
     * 小顶上门：69fbebea17420bf3234449411bc9bb28
     * 一站式：4f1fa36509fd130a0be7f13c8802db80
     */
    public static final String APP_SECRET = "4f1fa36509fd130a0be7f13c8802db80";

    /**
     * 自定义 TOKEN
     */
    public static final String DIY_TOKEN = "ggsm";

    /**
     * EncodingAESKey
     */
    public static final String ENCODING_AES_KEY = "y2Fpv73PGehNV1wjcEhdTx828yPJixdDKExZ24NAKK6";

    /** state */
    public static final String STATE = "ggsm";

    public static final String OPENID_REDIS_KEY = "wx_fromUserName";

    /**
     * 应用授权作用域
     */
    public static final String AUTH_SCOPE_BASE = "snsapi_base";
    public static final String AUTH_SCOPE_USERINFO = "snsapi_userinfo";

    /**
     * 基础支持 access_token 地址
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 获取code
     */
    public static final String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    /**
     * 网页授权 access_token
     */
    public static final String AUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    /**
     * 关注事件
     */
    public static final String SUB_EVENT = "subscribe";

    /**
     * 取消关注事件
     */
    public static final String UNSUB_EVENT = "unsubscribe";

    /**
     * 文本消息事件
     */
    public static final String TEXT_EVENT = "text";

    /**
     * 客戶端
     */
    public static final String USER_AGENT = "micromessenger";

    /**
     * openid key
     */
    public static final String OPENID_KEY = "wx_fromUserName";

    /**
     * 未绑定 返回码
     */
    public static final int UN_BIND = -3;

    /**
     * 域名
     * 测试：http://rwechat.xdsm.dgg.net -->https://rxdsmwechat.dgg.net
     * 正式：http://xdsmwechat.dgg.net
     *
     */
    public static final String DOMAIN_URL = "https://rxdsmwechat.dgg.net";

    /**
     * 列表页面
     */
    public static final String LIST_URL = "/index.html#/orderList";

    /**
     * 绑定页面
     */
    public static final String BIND_URL = "/index.html#/bind";

}
