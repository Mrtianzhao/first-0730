package net.dgg.cloud.dao;

import net.dgg.cloud.dto.XdBusinessDto;
import net.dgg.cloud.dto.XdBusinessWithChildrenDto;
import net.dgg.cloud.entity.XdBusiness;

import java.util.List;

public interface XdBusinessMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_business
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_business
     *
     * @mbggenerated
     */
    int insert(XdBusiness record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_business
     *
     * @mbggenerated
     */
    int insertSelective(XdBusiness record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_business
     *
     * @mbggenerated
     */
    XdBusiness selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_business
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(XdBusiness record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_business
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(XdBusiness record);

    /**获取所有二级业态*/
    List<XdBusinessDto> selectXdBusiness();

    List<XdBusinessWithChildrenDto> selectAllValid();
}