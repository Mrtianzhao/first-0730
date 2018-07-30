package net.dgg.cloud.entity.call;

/**
 * V2
 *
 * @author ytz
 * @date 2018/7/23.
 * @desc
 */
public class TelecomCallDto {

    /**
     * 资源id
     */
    private String resourceId;

    /**
     * 客户电话
     */
    private String customerPhone;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
