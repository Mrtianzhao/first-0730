package net.dgg.cloud.dto;

/**
 * @author ytz
 * @date 2018/5/2.
 * @desc 资源操作dto
 */
public class XdResourceOptDto {

    /** 资源id */
    private String resourceId;

    /** 反无效原因：反无效操作必填 */
    private String reason;

    /** 操作类型 1接单 2拒单 3抢单 4不再显示 5剔除 6反无效 refuse accept scramble hide */
    private Integer optType;

    /** 0-新任务 1-待处理 2-资源池 */
    private Integer sign;

    /** 页码 */
    private Integer page;

    /** 分页大小 */
    private Integer pageSize;


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
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
