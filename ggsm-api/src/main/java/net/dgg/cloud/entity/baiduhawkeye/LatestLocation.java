package net.dgg.cloud.entity.baiduhawkeye;

/**
 * @author ytz
 * @date 2018/5/18.
 * @desc 最新轨迹信息
 */
public class LatestLocation {

    /** 该entity最新定位时间（定位时设备的时间） */
    private long loc_time;

    /** 经度 */
    private double longitude;

    /** 纬度 */
    private double latitude;

    /** 方向 范围为[0,359]，0度为正北方向，顺时针 */
    private int direction;

    /** 高度 单位： m */
    private double height;

    /** 定位精度 单位： m */
    private double radius;

    /** 速度 单位：km/h */
    private double speed;

    /** 楼层 */
    private String floor;

    /** 对象数据名称 若无值则不返回该字段 */
    private String objectName;

    /** 开发者自定义track的属性 UNIX时间戳 只有当开发者为track创建了自定义属性字段，且赋过值，才会返回。 */
    private String columnKey;

    public long getLoc_time() {
        return loc_time;
    }

    public void setLoc_time(long loc_time) {
        this.loc_time = loc_time;
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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }
}
