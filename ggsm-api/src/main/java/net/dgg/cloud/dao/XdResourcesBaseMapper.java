package net.dgg.cloud.dao;

import net.dgg.cloud.dto.*;
import net.dgg.cloud.entity.XdResourcesBase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface XdResourcesBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_base
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String resourcesId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_base
     *
     * @mbggenerated
     */
    int insert(XdResourcesBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_base
     *
     * @mbggenerated
     */
    int insertSelective(XdResourcesBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_base
     *
     * @mbggenerated
     */
    XdResourcesBase selectByPrimaryKey(String resourcesId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_base
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(XdResourcesBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xd_resources_base
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(XdResourcesBase record);

    /**
     * 获取资源信息列表
     * @param dto
     * @return
     */
    List<XdResourcesBaseInfoDto> selectResourcesBaseList(XdResourcesBaseDto dto);

    /**
     * 统计总条数
     * @param dto
     * @return
     */
    Integer selectCount(XdResourcesBaseDto dto);

    /**
     * 剔除资源
     * @param record
     * @return
     */
    int updateByLostType(XdResourcesBase record);

    /**
     * 反无效资源
     * @param record
     * @return
     */
    int updateByResourcesStatus(XdResourcesBase record);

    /**
     * 修改下次跟进时间
     * @param record
     * @return
     */
    int updateNextTimeByResourcesId(XdResourcesBase record);

    /**
     * 根据id获取资源
     *
     * @param resourcesId
     * @return
     */
    XdResourcesBaseOneDto selectResourcesById(String resourcesId);

    /**
     * 统计商务本月待处理资源量
     *
     * @param salesmanId
     * @return
     */
    int selectPendingCountByMonth(String salesmanId);

    /**
     * 查询已分配待接收 是否超时
     *
     * @param map
     * @return
     */
    int selectBySalesmanAndState8(Map map);

    /**
     * 电话查询资源
     *
     * @param dto
     * @return
     */
    List<XdWxResourceBaseDto> selectResourcesListByPhone(WxUserDto dto);

    /**
     * 电话统计资源量
     *
     * @param dto
     * @return
     */
    int selectResourcesCountByPhone(WxUserDto dto);

    /**
     * 统计商务的月接单量
     * @param salesmanId
     * @return
     */
    int selectOrderCountByMonth(String salesmanId);

    List<XdResourcesBase> selectByPhoneAndTypeId(String customerPhone, String operationTypeId);

    /**
     * 当前商务正在上门的资源
     *
     * @param salesmanId
     * @return
     */
    List<XdResourcesBase> selectVisitingResource(@Param("salesmanId") String salesmanId);

    /**
     * ytz
     * 当前上门资源
     *
     * @param salesmanId
     * @return
     */
    XdResourcesBaseInfoDto selectCurrentVisitResource(String salesmanId);


}