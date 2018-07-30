package net.dgg.cloud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 上门信息dto
 */
public class XdResourceVisitDto {

    /** 资源id */
    private String resourcesId;

    /** 上门时间 */
    private String visitTime;

    /** 上门类型 （0标准谈单，1非标准谈单）*/
    private Integer visitType;

    /** 上门情况 谈单情况（0上门，1成单）*/
    private Integer visitSituation;

    /** 上门地址 */
    private String visitAddress;

    /** 下次跟进时间 */
    private String nextFollowTime;

    /** 备注 */
    private String remark;

    /** 上门照片：,隔开 */
    private String visitImg;

    /** 页数 */
    private Integer page;

    /** 每页数量 */
    private Integer pageSize;

    public String getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(String resourcesId) {
        this.resourcesId = resourcesId;
    }

    public String getVisitTime() {
        return visitTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public Integer getVisitType() {
        return visitType;
    }

    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }

    public Integer getVisitSituation() {
        return visitSituation;
    }

    public void setVisitSituation(Integer visitSituation) {
        this.visitSituation = visitSituation;
    }

    public String getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(String visitAddress) {
        this.visitAddress = visitAddress;
    }

    public String getNextFollowTime() {
        return nextFollowTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setNextFollowTime(String nextFollowTime) {
        this.nextFollowTime = nextFollowTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVisitImg() {
        return visitImg;
    }

    public void setVisitImg(String visitImg) {
        this.visitImg = visitImg;
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
