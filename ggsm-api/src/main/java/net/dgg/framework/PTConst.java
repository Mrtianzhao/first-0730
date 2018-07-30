package net.dgg.framework;

/**
 * 项目常量设置
 * Created by wu on 2017/7/12.
 */
//TODO 每个属性都需要注释
public class PTConst {

    /**
     * erp用户密匙
     */
    public static final String PWD_KEY = "DGGERP962540ADMIN";

    public static String REDIS_CONF = "";

    /**
     * 资源缓存前缀
     */
    public static String RESOURCE_KEY_PREFIX = "resource_id:";

    /**
     * 所有
     */
    public static String REGEX_ALL = "*";

    /**
     * 系统表前缀
     */
    public static String PT_TABLE_PREFIX = "dgg_base_";

    public static String USER_INFO = "user_info";

    public static String USER_TOKEN = "token";

    public static String USER_TIMEOUT = "redis.expire";

    public static String USER_CACHE = "redis";

    /**
     * 返回状态码
     */
    public static String RSP_CODE = "code";

    /**
     * 返回信息
     */
    public static String RSP_MSG = "msg";

    /**
     * 返回数据
     */
    public static String RSP_DATA = "data";

    /**
     * 验证码标志
     */
    public static String VERIFY_IMG = "img";
    public static String VERIFY_SMS = "sms";

    /**
     * 数据库分页开始行
     */
    public static String START_COUNT = "startCount";

    /**
     * 数据库分页大小
     */
    public static String PAGE_NUM = "pageNum";

    /**
     * redis 数据超时变量设置
     */
    public static String TOKEN_EXPIRE = "token.expire";

    public static String IMG_VERIFY_EXPIRE = "img.verify.expire";

    public static String SMS_VERIFY_EXPIRE = "sms.verify.expire";

    /**
     * 分页查询请求条数
     */
    public static int PAGE_SIZE = 100;

    public static String CHANNEL = "channel";

    public static String APP = "app";

    /**
     * 微信公众号前端页面地址
     */
    public static final String WX_PAGE_PATH = "wx.page.path";

    /**
     * 微信公众号appid
     */
    public static final String APPID_KEY = "wx.appid";

    /**
     * 微信公众号app_secret
     */
    public static final String APP_SECRET_KEY = "wx.appsecret";

}
