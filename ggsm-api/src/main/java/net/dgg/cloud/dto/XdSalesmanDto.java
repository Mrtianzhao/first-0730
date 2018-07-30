package net.dgg.cloud.dto;

/**
 * @author ytz
 * @date 2018/5/11.
 * @desc 商务
 */
public class XdSalesmanDto {

    /** 商务id */
    private String salesmanId;

    /** 状态 0：离线，1：在线，2：繁忙 */
    private Integer state;

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
