package net.dgg.cloud.constant;

/**
 * @author ytz
 * @date 2018/5/18.
 * @desc 百度鹰眼
 */
public class BaiduHawkEyeConstants {

    /**
     * ak
     */
    public static final String AK = "jKznUKWBRNGoYrNy1u0vMzxZwpZ73wrG";

    /**
     * service_id
     */
    public static final int SERVICE_ID = 200251;

    /**
     * 根据entity_name、自定义字段或活跃时间，查询符合条件的entity
     */
    public static final String LOCATION_LIST_URL = "http://yingyan.baidu.com/api/v3/entity/list";

    /**
     * 查询entity 一段时间内行驶里程的Url
     */
    public static final String GETDISTANCE_URL = "http://yingyan.baidu.com/api/v3/track/getdistance";

}
