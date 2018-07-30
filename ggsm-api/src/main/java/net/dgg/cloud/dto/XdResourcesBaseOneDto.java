package net.dgg.cloud.dto;

import java.util.Date;

/**
 * 资源详情
 */
public class XdResourcesBaseOneDto {

    /** 资源id */
    private String resourcesId;

    /** 商机号 */
    private String businessCode;

    /** 客户名称 */
    private String customerName;

    /** 客户电话 */
    private String customerPhone;

    /** 接单时间 */
    private Date firstTime;

    /** 下次跟进时间*/
    private Date nextFollowTime;

    /** 业务信息 */
    private String businessName;

    /** 资源来源 */
    private String resourceChannel;

    /** 来源code */
    private String channelCode;

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

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getNextFollowTime() {
        return nextFollowTime;
    }

    public void setNextFollowTime(Date nextFollowTime) {
        this.nextFollowTime = nextFollowTime;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getResourceChannel() {
        return resourceChannel;
    }

    public void setResourceChannel(String resourceChannel) {
        this.resourceChannel = resourceChannel;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
}
