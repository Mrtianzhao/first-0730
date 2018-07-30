package net.dgg.cloud.dao;

import net.dgg.cloud.entity.CloundVersionControl;

import java.util.Map;

public interface CloundVersionControlMapper {

    /*查询最新版本号*/
    CloundVersionControl selectNewest(String type);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table clound_version_control
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String versionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table clound_version_control
     *
     * @mbggenerated
     */
    int insert(CloundVersionControl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table clound_version_control
     *
     * @mbggenerated
     */
    int insertSelective(CloundVersionControl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table clound_version_control
     *
     * @mbggenerated
     */
    CloundVersionControl selectByPrimaryKey(String versionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table clound_version_control
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CloundVersionControl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table clound_version_control
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CloundVersionControl record);

    /**
     * 更新设备版本信息 2017-12-20
     * @param map
     * @return
     */
    int updatePhoneModel(Map<String,Object> map);
}