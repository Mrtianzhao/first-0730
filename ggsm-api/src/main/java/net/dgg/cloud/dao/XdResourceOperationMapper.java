package net.dgg.cloud.dao;

import net.dgg.cloud.entity.XdResourceOperation;

public interface XdResourceOperationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resource_operation
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resource_operation
     *
     * @mbggenerated
     */
    int insert(XdResourceOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resource_operation
     *
     * @mbggenerated
     */
    int insertSelective(XdResourceOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resource_operation
     *
     * @mbggenerated
     */
    XdResourceOperation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resource_operation
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(XdResourceOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resource_operation
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(XdResourceOperation record);
}