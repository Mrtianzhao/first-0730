package net.dgg.cloud.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class XdResourcesBaseInfoDto {

    /**
     * 资源id
     */
    private String resourcesId;

    /**
     * 商机号
     */
    private String businessCode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 客户性别（0待定，1男，2女）
     */
    private Integer customerSex;

    /**
     * 业务信息
     */
    private String businessName;

    /**
     * 上门城市
     */
    private String visitCityName;

    /**
     * 客户当前定位地址
     */
    private String customerAddress;

    /**
     * 上门地址
     */
    private String visitAddress;

    /**
     * 资源状态(0待分配，1待跟进，2跟进中，3已上门，4已成单，5已下单，6公共库，7无效库 8待接收)
     */
    private Integer resourcesStatus;

    /**
     * 状态描述
     */
    private String statusName;

    /**
     * 客户留言
     */
    private String message;

    /**
     * 接单时间
     */
    private Date firstTime;

    /**
     * 掉入公共库时间
     */
    private Date lostTime;

    /**
     * 公共库类型（0剔除，1离职，2拒单，3掉库）
     */
    private Integer lostType;

    /**
     * 公共库类型中文
     */
    private String lostName;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 维度
     */
    private String latitude;

    /**
     * 剩余分钟
     */
    private Long leftMinute;

    /**
     * 下次跟进时间
     */
    private Date nextFollowTime;

    /**
     * 分配时间
     */
    private Date assignerTime;

    /**
     * 是否可以反无效
     */
    private Integer isCanInvalid;

    /**
     * 资源来源：0分配，1抢单
     */
    private Integer resourceChannel;

    /**
     * 反无效次数
     */
    private Integer invalidNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上门状态
     */
    private Integer visitState;

    /**
     * 上门id
     */
    private String currentVisitId;

    /** 来源code */
    private String channelCode;

    /** 最新跟进时间 */
    private Date lastFollowTime;

    /** 最新上门时间 */
    private Date lastVisitTime;

    /** 是否今日掉库：否0 是1 */
    private Integer isTodayLoss;

    public String getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(String resourcesId) {
        this.resourcesId = resourcesId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Integer getCustomerSex() {
        return customerSex;
    }

    public void setCustomerSex(Integer customerSex) {
        this.customerSex = customerSex;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getVisitCityName() {
        return visitCityName;
    }

    public void setVisitCityName(String visitCityName) {
        this.visitCityName = visitCityName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(String visitAddress) {
        this.visitAddress = visitAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getLostTime() {
        return lostTime;
    }

    public void setLostTime(Date lostTime) {
        this.lostTime = lostTime;
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

    public Integer getResourcesStatus() {
        return resourcesStatus;
    }

    public void setResourcesStatus(Integer resourcesStatus) {
        this.resourcesStatus = resourcesStatus;
    }

    public Integer getLostType() {
        return lostType;
    }

    public void setLostType(Integer lostType) {
        this.lostType = lostType;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getLostName() {
        return lostName;
    }

    public void setLostName(String lostName) {
        this.lostName = lostName;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Long getLeftMinute() {
        return leftMinute;
    }

    public void setLeftMinute(Long leftMinute) {
        this.leftMinute = leftMinute;
    }

    @JsonIgnore
    public Date getNextFollowTime() {
        return nextFollowTime;
    }

    public void setNextFollowTime(Date nextFollowTime) {
        this.nextFollowTime = nextFollowTime;
    }

    public Date getAssignerTime() {
        return assignerTime;
    }

    public void setAssignerTime(Date assignerTime) {
        this.assignerTime = assignerTime;
    }

    public Integer getIsCanInvalid() {
        return isCanInvalid;
    }

    public void setIsCanInvalid(Integer isCanInvalid) {
        this.isCanInvalid = isCanInvalid;
    }

    public Integer getResourceChannel() {
        return resourceChannel;
    }

    public void setResourceChannel(Integer resourceChannel) {
        this.resourceChannel = resourceChannel;
    }

    public Integer getInvalidNum() {
        return invalidNum;
    }

    public void setInvalidNum(Integer invalidNum) {
        this.invalidNum = invalidNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVisitState() {
        return visitState;
    }

    public void setVisitState(Integer visitState) {
        this.visitState = visitState;
    }

    public String getCurrentVisitId() {
        return currentVisitId;
    }

    public void setCurrentVisitId(String currentVisitId) {
        this.currentVisitId = currentVisitId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Date getLastFollowTime() {
        return lastFollowTime;
    }

    public void setLastFollowTime(Date lastFollowTime) {
        this.lastFollowTime = lastFollowTime;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public Integer getIsTodayLoss() {
        return isTodayLoss;
    }

    public void setIsTodayLoss(Integer isTodayLoss) {
        this.isTodayLoss = isTodayLoss;
    }
}
