package net.dgg.cloud.dao;

import net.dgg.cloud.entity.XdSalesman;

import java.util.List;
import java.util.Map;

public interface XdSalesmanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman
     *
     * @mbggenerated
     */
    int insert(XdSalesman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman
     *
     * @mbggenerated
     */
    int insertSelective(XdSalesman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman
     *
     * @mbggenerated
     */
    XdSalesman selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(XdSalesman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_salesman
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(XdSalesman record);

    /**通过商务手机号获取商务信息*/
    XdSalesman selectSalesmanByPhone(String phone);

    /**
     *
     * @param salesmanId
     * @return
     */
    List<Map> selectMouthSumBySalesman(String salesmanId);

    Map<String,Object> getAcceptInfo(Map<String, Object> commerceId);
}