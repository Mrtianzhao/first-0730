package net.dgg.cloud.service;

import net.dgg.cloud.dto.XdContactDto;
import net.dgg.cloud.dto.XdResourceDto;
import net.dgg.cloud.dto.XdResourceOptDto;
import net.dgg.cloud.dto.XdResourcesBaseDto;
import net.dgg.cloud.entity.XdResourcesBase;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源信息service层
 * @author
 */
public interface XdResourcesBaseService {

    /**
     * 获取资源信息列表
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> queryResourcesBaseList(XdResourcesBaseDto dto, String token);

    /**
     * 剔除资源
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> removeResource(XdResourceOptDto dto, String token);

    /**
     * 反无效资源
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> invalidResources(XdResourceOptDto dto, String token);

    /**
     * 通过资源id获取资源详细信息
     * @param record
     * @param token
     * @return
     */
    Map<String, Object> queryResourcesBaseInfo(XdResourcesBase record, String token);

    /**
     * 拒单
     *
     * @param xdResourceOptDto
     * @param token
     * @return
     */
    public Map<String, Object> refuseResource(XdResourceOptDto xdResourceOptDto, String token);

    /**
     * 接单 Scramble
     *
     * @param xdResourceOptDto
     * @param token
     * @return
     */
    public Map<String, Object> acceptResource(XdResourceOptDto xdResourceOptDto, String token);

    /**
     * 抢单
     *
     * @param xdResourceOptDto
     * @param token
     * @return
     */
    public Map<String, Object> scrambleResource(XdResourceOptDto xdResourceOptDto, String token);

    /**
     * 不再显示
     *
     * @param xdResourceOptDto
     * @param token
     * @return
     */
    public Map<String, Object> hideResource(XdResourceOptDto xdResourceOptDto, String token);

    /**
     * 获取二级业态
     */
    Map<String, Object> queryStatus();

    /**
     * 修改主联系人
     *
     * @param dto
     * @param token
     * @return
     */
    public Map<String,Object> changeMainContact(XdContactDto dto,String token);

    /**
     * 商务自开发
     * @param dto
     * @param token
     * @return
     */
    Map<String,Object> selfDevelopment(XdResourceDto dto, String token);
}
