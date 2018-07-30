package net.dgg.framework.utils.sms;

import net.dgg.framework.utils.JsonUtils;

import java.util.Map;

/**
 * Created by gene on 2017/9/14.
 * desc:分页查询工具
 */
public class PageUtil {

    /**
     * 开始条数，结束条数条件限制统一判断
     * @param startCount
     * @param endCount
     * @return
     */
    public static Map<String,Object> serarchContition(Integer pageSize,Integer pageNum) {

        	//前端非法数据判断
            if (pageNum == null || pageSize == null) {
                return JsonUtils.getResponseForMap(-2, "当前页数和每页条数不能为空", null);
            }
            if (pageSize <= 0) {
                //负数
                return JsonUtils.getResponseForMap(-2, "请求非法，每页条数必须大于0", null);
            } else {
                if (pageSize == 0) {
                    return JsonUtils.getResponseForMap(-2, "请求非法，每页条数不能小于1", null);
                }
                if(pageNum==0){
                	return JsonUtils.getResponseForMap(-2, "请求非法，页数不能小于1", null);
                }
                //查询数据最多为1000条
                if (pageSize*pageNum>1000) {
                    return JsonUtils.getResponseForMap(-2, "失败，查询条数不能大于1000条", null);
                } 
            }
           return null;
        }

}
