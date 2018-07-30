package net.dgg.cloud.dto;

/**
 * @author ytz
 * @date 2018/5/15.
 * @desc
 */
public class WxUserDto {

    /** 电话 */
    private String phoneNo;

    /** openId */
    private String openId;

    /** 资源id */
    private String resourceId;

    /** 订单id */
    private String orderId;

    /** 图形验证码 */
    private String code;

    /** 短信验证码 */
    private String msgCode;

    private Integer page;

    private Integer pageSize;


    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
