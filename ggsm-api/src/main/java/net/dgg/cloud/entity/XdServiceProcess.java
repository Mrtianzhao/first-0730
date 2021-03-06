package net.dgg.cloud.entity;

import java.util.Date;

public class XdServiceProcess {
    /** 主键id */
    private String id;

    /** 所属业务id */
    private String typeId;

    /** 节点名称 */
    private String processName;

    /** 创建人id */
    private String createUserId;

    /** 创建时间 */
    private Date createTime;

    /** 修改人id */
    private String updateUserId;

    /** 修改时间 */
    private Date updateTime;

    /** 删除标识(0未删除，1删除) */
    private Integer delFlag;

    /** 排序值 */
    private Integer orderNo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.id
     *
     * @return the value of xd_service_process.id
     *
     * @mbggenerated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.id
     *
     * @param id the value for xd_service_process.id
     *
     * @mbggenerated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.type_id
     *
     * @return the value of xd_service_process.type_id
     *
     * @mbggenerated
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.type_id
     *
     * @param typeId the value for xd_service_process.type_id
     *
     * @mbggenerated
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId == null ? null : typeId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.process_name
     *
     * @return the value of xd_service_process.process_name
     *
     * @mbggenerated
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.process_name
     *
     * @param processName the value for xd_service_process.process_name
     *
     * @mbggenerated
     */
    public void setProcessName(String processName) {
        this.processName = processName == null ? null : processName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.create_user_id
     *
     * @return the value of xd_service_process.create_user_id
     *
     * @mbggenerated
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.create_user_id
     *
     * @param createUserId the value for xd_service_process.create_user_id
     *
     * @mbggenerated
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.create_time
     *
     * @return the value of xd_service_process.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.create_time
     *
     * @param createTime the value for xd_service_process.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.update_user_id
     *
     * @return the value of xd_service_process.update_user_id
     *
     * @mbggenerated
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.update_user_id
     *
     * @param updateUserId the value for xd_service_process.update_user_id
     *
     * @mbggenerated
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.update_time
     *
     * @return the value of xd_service_process.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.update_time
     *
     * @param updateTime the value for xd_service_process.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.del_flag
     *
     * @return the value of xd_service_process.del_flag
     *
     * @mbggenerated
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.del_flag
     *
     * @param delFlag the value for xd_service_process.del_flag
     *
     * @mbggenerated
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_service_process.order_no
     *
     * @return the value of xd_service_process.order_no
     *
     * @mbggenerated
     */
    public Integer getOrderNo() {
        return orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_service_process.order_no
     *
     * @param orderNo the value for xd_service_process.order_no
     *
     * @mbggenerated
     */
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}