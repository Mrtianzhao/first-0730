package net.dgg.cloud.entity;

import java.util.Date;

public class CloundVersionControl {
    /**  */
    private String versionId;

    /** 版本号 */
    private Integer versionNo;

    /** 版本名称 */
    private String versionName;

    /** 备注说明 */
    private String description;

    /**  */
    private String path;

    /** 类型（ios,android） */
    private String type;

    /**  */
    private Integer isUpdate;

    /**  */
    private Date lastUpdate;

    /** 创建时间 */
    private Date createTime;

    /** 是否禁用（1启用，2禁用,） */
    private Integer isOpen;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.version_id
     *
     * @return the value of clound_version_control.version_id
     *
     * @mbggenerated
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.version_id
     *
     * @param versionId the value for clound_version_control.version_id
     *
     * @mbggenerated
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.version_no
     *
     * @return the value of clound_version_control.version_no
     *
     * @mbggenerated
     */
    public Integer getVersionNo() {
        return versionNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.version_no
     *
     * @param versionNo the value for clound_version_control.version_no
     *
     * @mbggenerated
     */
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.version_name
     *
     * @return the value of clound_version_control.version_name
     *
     * @mbggenerated
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.version_name
     *
     * @param versionName the value for clound_version_control.version_name
     *
     * @mbggenerated
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName == null ? null : versionName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.description
     *
     * @return the value of clound_version_control.description
     *
     * @mbggenerated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.description
     *
     * @param description the value for clound_version_control.description
     *
     * @mbggenerated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.path
     *
     * @return the value of clound_version_control.path
     *
     * @mbggenerated
     */
    public String getPath() {
        return path;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.path
     *
     * @param path the value for clound_version_control.path
     *
     * @mbggenerated
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.type
     *
     * @return the value of clound_version_control.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.type
     *
     * @param type the value for clound_version_control.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.is_update
     *
     * @return the value of clound_version_control.is_update
     *
     * @mbggenerated
     */
    public Integer getIsUpdate() {
        return isUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.is_update
     *
     * @param isUpdate the value for clound_version_control.is_update
     *
     * @mbggenerated
     */
    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.last_update
     *
     * @return the value of clound_version_control.last_update
     *
     * @mbggenerated
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.last_update
     *
     * @param lastUpdate the value for clound_version_control.last_update
     *
     * @mbggenerated
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.create_time
     *
     * @return the value of clound_version_control.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.create_time
     *
     * @param createTime the value for clound_version_control.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column clound_version_control.is_open
     *
     * @return the value of clound_version_control.is_open
     *
     * @mbggenerated
     */
    public Integer getIsOpen() {
        return isOpen;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column clound_version_control.is_open
     *
     * @param isOpen the value for clound_version_control.is_open
     *
     * @mbggenerated
     */
    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }
}