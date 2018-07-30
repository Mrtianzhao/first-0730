package net.dgg.cloud.dto;

import java.util.Date;

public class XdResourceDto {

    /** 资源id */
    private String resourcesId;

    /** 客户名称 */
    private String customerName;

    /** 客户电话 */
    private String customerPhone;

    /** 客户性别（0待定，1男，2女） */
    private Integer customerSex;

    /** 业务信息 */
    private String operationTypeId;

    /** 省 */
    private String province;

    /** 上门城市 */
    private String visitCity;

    /** 上门地址 */
    private String visitAddress;


    /** 预约上门时间 */
    private Long subscribeTime;

    /** 客户留言 */
    private String message;

    /** 资源成本 */
    private Integer resourcesCost;

    /** 是否计算成本,0：不计算，1：计算 */
    private Integer isCost;

    /** 经度 */
    private String longitude;

    /** 维度 */
    private String latitude;

    /** 所属商务id */
    private String commerceId;

    /** 资源渠道id, 二期新增20180619*/
    private String resSourceId;

    /** 一级业态名 二期新增*/
    private String bigBizId;

    public String getBigBizId() {
        return bigBizId;
    }

    public void setBigBizId(String bigBizId) {
        this.bigBizId = bigBizId;
    }

    public String getResSourceId() {
        return resSourceId;
    }

    public void setResSourceId(String resSourceId) {
        this.resSourceId = resSourceId;
    }

    public String getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(String resourcesId) {
        this.resourcesId = resourcesId;
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

    public String getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(String operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getVisitCity() {
        return visitCity;
    }

    public void setVisitCity(String visitCity) {
        this.visitCity = visitCity;
    }

    public String getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(String visitAddress) {
        this.visitAddress = visitAddress;
    }


    public Long getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResourcesCost() {
        return resourcesCost;
    }

    public void setResourcesCost(Integer resourcesCost) {
        this.resourcesCost = resourcesCost;
    }

    public Integer getIsCost() {
        return isCost;
    }

    public void setIsCost(Integer isCost) {
        this.isCost = isCost;
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

    public String getCommerceId() {
        return commerceId;
    }

    public void setCommerceId(String commerceId) {
        this.commerceId = commerceId;
    }
}
