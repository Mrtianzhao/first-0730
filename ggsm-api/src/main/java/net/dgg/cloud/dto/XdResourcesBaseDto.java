package net.dgg.cloud.dto;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.framework.utils.ResourceUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资源信息扩展dto
 *
 * @author
 */
public class XdResourcesBaseDto {

    /**
     * 资源状态(0待分配，1待跟进，2跟进中，3已上门，4已成单，5已下单，6公共库，7无效库，8待接收)
     */
    private String resourcesStatus;

    /**
     * 上门城市id
     */
    private String visitCity;

    /**
     * 业务信息
     */
    private String operationTypeId;

    /**
     * 接单时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date firstTime;

    /**
     * 所属商务id
     */
    private String commerceId;

    /**
     * 标识(0-新任务，1-待处理，2-资源池)
     */
    private String sign;
    /**
     * 页数
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 搜索关键字
     */
    private String searchKey;

    /**
     * 业态id list
     */
    List<String> typeList;

    /**
     * 状态 list
     */
    List<String> statuList;

    /**
     * 未上门天数上门
     */
    private int notVisitDay;

    /**
     * 成单未跟进天数
     */
    private int afterOrderDay;

    /**
     * 上门未成单天数
     */
    private int afterVisitDay;

    public XdResourcesBaseDto() {
        Map<String, String> configMap = ResourceUtils.getResource(ApiConstants.CONFIG_FILE_NAME).getMap();
        String notVisitStr = configMap.get(ApiConstants.NOT_VISIT_DAY_KEY);
        String afterOrderStr = configMap.get(ApiConstants.AFTER_ORDER_DAY_KEY);
        String afterVisitStr = configMap.get(ApiConstants.AFTER_VISIT_DAY_KEY);
        this.notVisitDay = Integer.parseInt(notVisitStr);
        this.afterOrderDay = Integer.parseInt(afterOrderStr);
        this.afterVisitDay = Integer.parseInt(afterVisitStr);
    }

    public String getResourcesStatus() {
        return resourcesStatus;
    }

    public void setResourcesStatus(String resourcesStatus) {
        this.resourcesStatus = resourcesStatus;
    }

    public String getVisitCity() {
        return visitCity;
    }

    public void setVisitCity(String visitCity) {
        this.visitCity = visitCity;
    }

    public String getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(String operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public String getCommerceId() {
        return commerceId;
    }

    public void setCommerceId(String commerceId) {
        this.commerceId = commerceId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
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

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public List<String> getStatuList() {
        return statuList;
    }

    public void setStatuList(List<String> statuList) {
        this.statuList = statuList;
    }

    public int getNotVisitDay() {
        return notVisitDay;
    }

    public void setNotVisitDay(int notVisitDay) {
        this.notVisitDay = notVisitDay;
    }

    public int getAfterOrderDay() {
        return afterOrderDay;
    }

    public void setAfterOrderDay(int afterOrderDay) {
        this.afterOrderDay = afterOrderDay;
    }

    public int getAfterVisitDay() {
        return afterVisitDay;
    }

    public void setAfterVisitDay(int afterVisitDay) {
        this.afterVisitDay = afterVisitDay;
    }
}
