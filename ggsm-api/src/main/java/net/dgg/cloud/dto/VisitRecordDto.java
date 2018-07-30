package net.dgg.cloud.dto;

/**
 * V2
 *
 * @author ytz
 * @date 2018/7/6.
 * @desc
 */
public class VisitRecordDto {

    /**
     * 上门记录id
     */
    private String visitRecordId;

    /**
     * 资源id
     */
    private String resourceId;

    /**
     * 是否是默认地址 否0 是1
     */
    private Integer defaultAddress;

    /**
     * 上门地址
     */
    private String visitAddress;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 开始上门1，我已到达2，我已成单3，下次跟进4，取消上门5
     */
    private Integer optType;

    /**
     * 谈单类型（0标准谈单，1非标准谈单）
     */
    private Integer isStandard;

    /**
     * 谈单情况（0上门，1成单）
     */
    private Integer isDeal;

    /**
     * 是否添加微信：否0，是1
     */
    private Integer isAddWX;

    /**
     * 未添加微信原因
     */
    private String notAddWXReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 下次跟进时间
     */
    private String nextTime;

    /**
     * 上门图片地址
     */
    private String visitImg;


    public String getVisitRecordId() {
        return visitRecordId;
    }

    public void setVisitRecordId(String visitRecordId) {
        this.visitRecordId = visitRecordId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Integer defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(String visitAddress) {
        this.visitAddress = visitAddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public Integer getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(Integer isStandard) {
        this.isStandard = isStandard;
    }

    public Integer getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(Integer isDeal) {
        this.isDeal = isDeal;
    }

    public Integer getIsAddWX() {
        return isAddWX;
    }

    public void setIsAddWX(Integer isAddWX) {
        this.isAddWX = isAddWX;
    }

    public String getNotAddWXReason() {
        return notAddWXReason;
    }

    public void setNotAddWXReason(String notAddWXReason) {
        this.notAddWXReason = notAddWXReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNextTime() {
        return nextTime;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

    public String getVisitImg() {
        return visitImg;
    }

    public void setVisitImg(String visitImg) {
        this.visitImg = visitImg;
    }
}
