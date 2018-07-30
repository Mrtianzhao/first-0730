package net.dgg.cloud.dto;

import java.util.Date;

/**
 * @author ytz
 * @date 2018/5/15.
 * @desc 微信公众号资源
 */
public class XdWxResourceBaseDto {

    /** 资源id */
    private String resourceId;

    /** 商机号 */
    private String businessCode;

    /** 客户姓名 */
    private String customerName;

    /** 客户电话 */
    private String customerPhone;

    /** 业态名称 */
    private String businessName;

    /** 分配时间 */
    private Date assignerTime;

    /** 状态 */
    private Integer resourcesStatus;

    /** 商务姓名 */
    private String salesmanName;

    /** 商务电话 */
    private String salesmanPhone;

    /** 下次跟进时间 */
    private Date nextFollowTime;


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Date getAssignerTime() {
        return assignerTime;
    }

    public void setAssignerTime(Date assignerTime) {
        this.assignerTime = assignerTime;
    }

    public Integer getResourcesStatus() {
        return resourcesStatus;
    }

    public void setResourcesStatus(Integer resourcesStatus) {
        this.resourcesStatus = resourcesStatus;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public String getSalesmanPhone() {
        return salesmanPhone;
    }

    public void setSalesmanPhone(String salesmanPhone) {
        this.salesmanPhone = salesmanPhone;
    }

    public Date getNextFollowTime() {
        return nextFollowTime;
    }

    public void setNextFollowTime(Date nextFollowTime) {
        this.nextFollowTime = nextFollowTime;
    }
}
