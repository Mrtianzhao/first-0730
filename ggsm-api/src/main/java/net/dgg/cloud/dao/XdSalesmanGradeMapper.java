package net.dgg.cloud.dao;

import net.dgg.cloud.entity.XdSalesmanGrade;

public interface XdSalesmanGradeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman_grade
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman_grade
     *
     * @mbggenerated
     */
    int insert(XdSalesmanGrade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman_grade
     *
     * @mbggenerated
     */
    int insertSelective(XdSalesmanGrade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman_grade
     *
     * @mbggenerated
     */
    XdSalesmanGrade selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman_grade
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(XdSalesmanGrade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman_grade
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(XdSalesmanGrade record);
}