package net.dgg.cloud.dto;

/**
 * @author ytz
 * @date 2018/5/18.
 * @desc 查看位置
 */
public class WxUserLocationDto {

    /** 商务id */
    private String salesmanId;

    /** 商务姓名 */
    private String salesmanName;

    /** 经度 */
    private double longitude;

    /** 纬度 */
    private double latitude;

    /** 该entity最新定位时间 */
    private long locTime;

    /** 速度 单位：km/h */
    private double speed;

    /** 范围为[0,359]，0度为正北方向，顺时针 */
    private int direction;

    /** 定位精度 	单位：m */
    private double radius;

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getLocTime() {
        return locTime;
    }

    public void setLocTime(long locTime) {
        this.locTime = locTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
