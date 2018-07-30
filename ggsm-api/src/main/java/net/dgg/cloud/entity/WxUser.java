package net.dgg.cloud.entity;

import java.util.Date;

public class WxUser {
    /**  */
    private String id;

    /**  */
    private String phoneNo;

    /**  */
    private String openId;

    /** 是否关注公众号 */
    private Integer isSubscribe;

    /** 是否绑定(0否，1是)（解绑用） */
    private Integer isBind;

    /**  */
    private Date createTime;

    /**  */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_user.id
     *
     * @return the value of wx_user.id
     *
     * @mbggenerated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_user.id
     *
     * @param id the value for wx_user.id
     *
     * @mbggenerated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_user.phone_no
     *
     * @return the value of wx_user.phone_no
     *
     * @mbggenerated
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_user.phone_no
     *
     * @param phoneNo the value for wx_user.phone_no
     *
     * @mbggenerated
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo == null ? null : phoneNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_user.open_id
     *
     * @return the value of wx_user.open_id
     *
     * @mbggenerated
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_user.open_id
     *
     * @param openId the value for wx_user.open_id
     *
     * @mbggenerated
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_user.is_subscribe
     *
     * @return the value of wx_user.is_subscribe
     *
     * @mbggenerated
     */
    public Integer getIsSubscribe() {
        return isSubscribe;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_user.is_subscribe
     *
     * @param isSubscribe the value for wx_user.is_subscribe
     *
     * @mbggenerated
     */
    public void setIsSubscribe(Integer isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_user.is_bind
     *
     * @return the value of wx_user.is_bind
     *
     * @mbggenerated
     */
    public Integer getIsBind() {
        return isBind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_user.is_bind
     *
     * @param isBind the value for wx_user.is_bind
     *
     * @mbggenerated
     */
    public void setIsBind(Integer isBind) {
        this.isBind = isBind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_user.create_time
     *
     * @return the value of wx_user.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_user.create_time
     *
     * @param createTime the value for wx_user.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_user.update_time
     *
     * @return the value of wx_user.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_user.update_time
     *
     * @param updateTime the value for wx_user.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}