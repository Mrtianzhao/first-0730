package net.dgg.cloud.entity;

import java.util.Date;

public class XdSalesman {
    /** 主键id */
    private String id;

    /** 商务名字 */
    private String name;

    /** 电话号码 */
    private String phoneNumber;

    /** 隐号 */
    private String xNumber;

    /** 所在城市 */
    private String city;

    /** 擅长类型 */
    private String specialty;

    /** 商务在线状态，0：离线，1：在线，2：繁忙 */
    private Integer onlineStatus;

    /** 等级称号 */
    private String salesmanGradeId;

    /** 可以接单，0：不可以接单，1;可以接单，默认为1 */
    private Integer canorder;

    /** 创建人id */
    private String createUserId;

    /** 创建时间 */
    private Date createTime;

    /** 修改人id */
    private String updateUserId;

    /** 修改时间 */
    private Date updateTime;

    /** 是否启用（0启用，1禁用） */
    private Integer isDel;

    /** 是否在职（0在职，1离职） */
    private Integer isOn;

    /** 禁止接单的截止时间 */
    private Date canorderTime;

    /** 隐号的使用截止时间 */
    private Date xNumberUseTime;

    /** 每周拒单数（周末清零） */
    private Integer reSum;

    /** 操作人id（最后一次启用禁用的人id） */
    private String operatorId;

    /** 操作人名 */
    private String operatorName;

    /** 操作时间 */
    private Date operatorTime;

    /** 操作人工号 */
    private String operatorWorkNo;

    /** 事业部 */
    private String businessDivision;

    /** 一级业态id */
    private String bigBizId;

    /** 商务工号 */
    private String employeeNo;

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getBigBizId() {
        return bigBizId;
    }

    public void setBigBizId(String bigBizId) {
        this.bigBizId = bigBizId;
    }

    public String getBusinessDivision() {
        return businessDivision;
    }

    public void setBusinessDivision(String businessDivision) {
        this.businessDivision = businessDivision;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.id
     *
     * @return the value of xd_salesman.id
     *
     * @mbggenerated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.id
     *
     * @param id the value for xd_salesman.id
     *
     * @mbggenerated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.name
     *
     * @return the value of xd_salesman.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.name
     *
     * @param name the value for xd_salesman.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.phone_number
     *
     * @return the value of xd_salesman.phone_number
     *
     * @mbggenerated
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.phone_number
     *
     * @param phoneNumber the value for xd_salesman.phone_number
     *
     * @mbggenerated
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.x_number
     *
     * @return the value of xd_salesman.x_number
     *
     * @mbggenerated
     */
    public String getxNumber() {
        return xNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.x_number
     *
     * @param xNumber the value for xd_salesman.x_number
     *
     * @mbggenerated
     */
    public void setxNumber(String xNumber) {
        this.xNumber = xNumber == null ? null : xNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.city
     *
     * @return the value of xd_salesman.city
     *
     * @mbggenerated
     */
    public String getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.city
     *
     * @param city the value for xd_salesman.city
     *
     * @mbggenerated
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.specialty
     *
     * @return the value of xd_salesman.specialty
     *
     * @mbggenerated
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.specialty
     *
     * @param specialty the value for xd_salesman.specialty
     *
     * @mbggenerated
     */
    public void setSpecialty(String specialty) {
        this.specialty = specialty == null ? null : specialty.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.online_status
     *
     * @return the value of xd_salesman.online_status
     *
     * @mbggenerated
     */
    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.online_status
     *
     * @param onlineStatus the value for xd_salesman.online_status
     *
     * @mbggenerated
     */
    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.salesman_grade_id
     *
     * @return the value of xd_salesman.salesman_grade_id
     *
     * @mbggenerated
     */
    public String getSalesmanGradeId() {
        return salesmanGradeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.salesman_grade_id
     *
     * @param salesmanGradeId the value for xd_salesman.salesman_grade_id
     *
     * @mbggenerated
     */
    public void setSalesmanGradeId(String salesmanGradeId) {
        this.salesmanGradeId = salesmanGradeId == null ? null : salesmanGradeId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.canOrder
     *
     * @return the value of xd_salesman.canOrder
     *
     * @mbggenerated
     */
    public Integer getCanorder() {
        return canorder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.canOrder
     *
     * @param canorder the value for xd_salesman.canOrder
     *
     * @mbggenerated
     */
    public void setCanorder(Integer canorder) {
        this.canorder = canorder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.create_user_id
     *
     * @return the value of xd_salesman.create_user_id
     *
     * @mbggenerated
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.create_user_id
     *
     * @param createUserId the value for xd_salesman.create_user_id
     *
     * @mbggenerated
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.create_time
     *
     * @return the value of xd_salesman.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.create_time
     *
     * @param createTime the value for xd_salesman.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.update_user_id
     *
     * @return the value of xd_salesman.update_user_id
     *
     * @mbggenerated
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.update_user_id
     *
     * @param updateUserId the value for xd_salesman.update_user_id
     *
     * @mbggenerated
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.update_time
     *
     * @return the value of xd_salesman.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.update_time
     *
     * @param updateTime the value for xd_salesman.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.is_del
     *
     * @return the value of xd_salesman.is_del
     *
     * @mbggenerated
     */
    public Integer getIsDel() {
        return isDel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.is_del
     *
     * @param isDel the value for xd_salesman.is_del
     *
     * @mbggenerated
     */
    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.is_on
     *
     * @return the value of xd_salesman.is_on
     *
     * @mbggenerated
     */
    public Integer getIsOn() {
        return isOn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.is_on
     *
     * @param isOn the value for xd_salesman.is_on
     *
     * @mbggenerated
     */
    public void setIsOn(Integer isOn) {
        this.isOn = isOn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.canOrder_time
     *
     * @return the value of xd_salesman.canOrder_time
     *
     * @mbggenerated
     */
    public Date getCanorderTime() {
        return canorderTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.canOrder_time
     *
     * @param canorderTime the value for xd_salesman.canOrder_time
     *
     * @mbggenerated
     */
    public void setCanorderTime(Date canorderTime) {
        this.canorderTime = canorderTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.x_number_use_time
     *
     * @return the value of xd_salesman.x_number_use_time
     *
     * @mbggenerated
     */
    public Date getxNumberUseTime() {
        return xNumberUseTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.x_number_use_time
     *
     * @param xNumberUseTime the value for xd_salesman.x_number_use_time
     *
     * @mbggenerated
     */
    public void setxNumberUseTime(Date xNumberUseTime) {
        this.xNumberUseTime = xNumberUseTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.re_sum
     *
     * @return the value of xd_salesman.re_sum
     *
     * @mbggenerated
     */
    public Integer getReSum() {
        return reSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.re_sum
     *
     * @param reSum the value for xd_salesman.re_sum
     *
     * @mbggenerated
     */
    public void setReSum(Integer reSum) {
        this.reSum = reSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.operator_id
     *
     * @return the value of xd_salesman.operator_id
     *
     * @mbggenerated
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.operator_id
     *
     * @param operatorId the value for xd_salesman.operator_id
     *
     * @mbggenerated
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.operator_name
     *
     * @return the value of xd_salesman.operator_name
     *
     * @mbggenerated
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.operator_name
     *
     * @param operatorName the value for xd_salesman.operator_name
     *
     * @mbggenerated
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.operator_time
     *
     * @return the value of xd_salesman.operator_time
     *
     * @mbggenerated
     */
    public Date getOperatorTime() {
        return operatorTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.operator_time
     *
     * @param operatorTime the value for xd_salesman.operator_time
     *
     * @mbggenerated
     */
    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xd_salesman.operator_work_no
     *
     * @return the value of xd_salesman.operator_work_no
     *
     * @mbggenerated
     */
    public String getOperatorWorkNo() {
        return operatorWorkNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xd_salesman.operator_work_no
     *
     * @param operatorWorkNo the value for xd_salesman.operator_work_no
     *
     * @mbggenerated
     */
    public void setOperatorWorkNo(String operatorWorkNo) {
        this.operatorWorkNo = operatorWorkNo == null ? null : operatorWorkNo.trim();
    }
}