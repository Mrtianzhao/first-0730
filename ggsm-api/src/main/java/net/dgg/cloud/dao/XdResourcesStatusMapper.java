package net.dgg.cloud.dao;

import net.dgg.cloud.entity.XdResourcesStatus;

import java.util.List;

public interface XdResourcesStatusMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_status
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_status
     *
     * @mbggenerated
     */
    int insert(XdResourcesStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_status
     *
     * @mbggenerated
     */
    int insertSelective(XdResourcesStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_status
     *
     * @mbggenerated
     */
    XdResourcesStatus selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_status
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(XdResourcesStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_status
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(XdResourcesStatus record);

    List<XdResourcesStatus>  selectStatus();
}