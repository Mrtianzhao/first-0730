package net.dgg.cloud.constant;

import net.dgg.framework.utils.ResourceUtils;

/**
 * @author ytz
 * @date 2018/5/16.
 * @desc 常量
 */
public class ApiConstants {

    private static ApiConstants apiConstants;

    static {
        apiConstants = new ApiConstants();
    }


    /**
     * 接口： 1接单 2拒单 3抢单 4不再显示 5剔除 6反无效
     */
    public static final int ACCEPT_CODE = 1;
    public static final int REFUSE_CODE = 2;
    public static final int SCRAMBLE_CODE = 3;
    public static final int HIDE_CODE = 4;
    public static final int REMOVE_CODE = 5;
    public static final int INVALID_CODE = 6;

    /**
     * 资源状态：0待分配 1待跟进 2跟进中 3已上门 4已成单 5已下单 6公共库 7无效库 8待接收
     */
    public static final int RESOURCE_STATE_CODE_0 = 0;
    public static final int RESOURCE_STATE_CODE_1 = 1;
    public static final int RESOURCE_STATE_CODE_2 = 2;
    public static final int RESOURCE_STATE_CODE_3 = 3;
    public static final int RESOURCE_STATE_CODE_4 = 4;
    public static final int RESOURCE_STATE_CODE_5 = 5;
    public static final int RESOURCE_STATE_CODE_6 = 6;
    public static final int RESOURCE_STATE_CODE_7 = 7;
    public static final int RESOURCE_STATE_CODE_8 = 8;

    /**
     * 资源来源：0 分配 1 抢单
     */
    public static final int RESOURCE_CHANNEL_0 = 0;
    public static final int RESOURCE_CHANNEL_1 = 1;

    /**
     * 是否可以反无效 0 否 1 是
     */
    public static final int CAN_INVALID_0 = 0;
    public static final int CAN_INVALID_1 = 1;

    /**
     * 公共库类型 0剔除，1离职，2拒单，3掉库
     */
    public static final int RESOURCE_REMOVE_CODE_0 = 0;
    public static final int RESOURCE_REMOVE_CODE_1 = 1;
    public static final int RESOURCE_REMOVE_CODE_2 = 2;
    public static final int RESOURCE_REMOVE_CODE_3 = 3;

    /**
     * 拒单次数
     */
    public static final int REFUSE_COUNT = 2;

    /**
     * 反无效次数
     */
    public static final int INVALID_COUNT = 2;

    /**
     * 默认文本长度
     */
    public static final int DEFAULT_TEXT_LEN = 10;

    /**
     * 默认备注长度
     */
    public static final int DEFAULT_REMARK_LEN = 200;


    /**
     * 默认时间格式
     */
    public static final String VISIT_TIME_DEFAULT_REG = "yyyy-MM-dd HH:mm";

    /**
     * 手机存token
     */
    public static final String PHONE_TOKEN = "ggsm_token_";

    /**
     * token存手机
     */
    public static final String TOKEN_PHONE = "ggsm_phone_";

    /**
     * 用户key前缀
     */
    public static final String SALESMAN_PREFIX = "ggsm_xd_salesman_";

    /**
     * app 图片验证码前缀
     */
    public static final String IMG_CODE = "ggsm_imgCode_";

    /**
     * app 短信验证码key前缀
     */
    public static final String MSG_CODE_PREFIX = "ggsm_phoneCode_";

    /**
     * 微信图片验证码前缀++
     */
    public static final String WX_IMG_CODE = "wx_ggsm_imgCode_";

    /**
     * 微信公众号短信验证码 前缀
     */
    public static final String WX_MSG_CODE_PREFIX = "wx_ggsm_phoneCode_";

    /**
     * 默认验证码长度
     */
    public static final int DEFAULT_MSG_CODE_LEN = 4;

    /**
     * 登录失败 code
     */
    public static final int LOGIN_ERROR_CODE = -1030;

    /**
     * 拒单两次 code
     */
    public static final int REFUSE_RETURN_CODE = -2;

    /**
     * 商务状态：0：离线 1：在线 2：繁忙
     */
    public static final int STATE_0 = 0;
    public static final int STATE_1 = 1;
    public static final int STATE_2 = 2;

    /**
     * 是否在职（0在职，1离职）
     */
    public static final int SALESMAN_ENABLE = 0;
    public static final int SALESMAN_DISENABLE = 1;

    /**
     * 计算在线时间开始结束时间 格式
     */
    public static final String START_TIME = "06:00:00";
    public static final String END_TIME = "23:59:59";

    /**
     * 查看位置间隔 分钟
     */
    public static final int SHOW_LOCATION_TIME_DIFF = 120;

    /**
     * 接单拒单记录：1接单，2拒单,3未响应,4抢单
     */
    public static final int RECORD_TYPE_1 = 1;
    public static final int RECORD_TYPE_2 = 2;
    public static final int RECORD_TYPE_3 = 3;
    public static final int RECORD_TYPE_4 = 4;

    /**
     * 资源列表 0-新任务 1-待处理 2-资源池 3-成单库
     */
    public static final String LIST_SIGN_0 = "0";
    public static final String LIST_SIGN_1 = "1";
    public static final String LIST_SIGN_2 = "2";
    public static final String LIST_SIGN_3 = "3";
    public static final String LIST_SIGN_4 = "4";

    // V2 ==================================

    /**
     * 时间格式
     */
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 微信下单图片验证码前缀
     */
    public static final String WX_ORDER_IMG_PREFIX = "wx::order::img::";

    /**
     * 微信下单短信验证码前缀
     */
    public static final String WX_ORDER_MSG_PREFIX = "wx::order::msg::";

    /**
     * 微信公众号资源来源code
     */
    public static final String WX_RESOURCE_SOURCE_CODE = "xdsmgzh";

    /**
     * 3天未上们，掉库
     */
    public static final int NOT_VISIT_LOSS_DAY = apiConstants.getDay(ApiConstants.NOT_VISIT_DAY_KEY);

    /**
     * 成单后，X天未跟进掉库
     */
    public static final int AFTER_ORDER_LOSS_DAY = apiConstants.getDay(ApiConstants.AFTER_ORDER_DAY_KEY);

    /**
     * 已上门，7天未成单掉库
     */
    public static final int VISITED_NOT_ORDER_LOSS_DAY = apiConstants.getDay(ApiConstants.AFTER_VISIT_DAY_KEY);

    /**
     * crm,jriboss
     */
    public static final String CRM_SOURCE_CODE = "crm";
    public static final String JRIBOSS_SOURCE_CODE = "jriboss";
    public static final String IBOSS_SOURCE_CODE = "iboss";

    /**
     * crm，jriboss通知地址
     */
    public static final String CRM_NOTIC_HOST_KEY = "crm.order.notice.host";
    public static final String JRIBOSS_NOTIC_HOST_KEY = "jriboss.order.notice.host";
    public static final String IBOSS_NOTIC_HOST_KEY = "iboss.order.notice.host";

    /**
     * crm.order.notice.path jriboss.order.notice.path
     */
    public static final String CRM_NOTICEE_PATH_KEY = "crm.order.notice.path";
    public static final String JRIBOSS_NOTICEE_PATH_KEY = "jriboss.order.notice.path";
    public static final String IBOSS_NOTICEE_PATH_KEY = "iboss.order.notice.path";

    /**
     * 配置文件名
     */
    public static final String CONFIG_FILE_NAME = "constants";

    /**
     * crm，jriboss，iboss 掉库规则天数
     */
    public static final String NOT_VISIT_DAY_KEY = "not.visit.day";
    public static final String AFTER_ORDER_DAY_KEY = "after.order.day";
    public static final String AFTER_VISIT_DAY_KEY = "after.visit.day";

    /**
     * 需要通知的商机 来源
     */
    public static final String[] NOTICE_RESOURCE_CODE = {"crm", "jriboss","iboss"};

    private int getDay(String key) {
        return Integer.parseInt(ResourceUtils.getResource(ApiConstants.CONFIG_FILE_NAME).getValue(key));
    }

    /**
     * 电信拨号地址
     */
    public static final String TELECOM_AXB_HOST = "telecom.axb.host";
    public static final String TELECOM_AXB_PATH = "telecom.axb.path";
    public static final String TELECOM_AXB_TOKEN = "?token=1";

}
