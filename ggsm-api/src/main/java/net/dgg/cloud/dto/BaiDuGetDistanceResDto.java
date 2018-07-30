package net.dgg.cloud.dto;

/**
 * @Author zhangyuzhu
 * @Description 本类目的：封装调用百度地图api (方法：getdistance ) 的返回结果
 * @Created data 2018/7/12 17:43
 */
public class BaiDuGetDistanceResDto extends BaseDto{

    /**状态码(0-成功)*/
    private Integer status;

    /**请求结果提示信息*/
    private String message;

    /**距离(米)*/
    private String distance;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
