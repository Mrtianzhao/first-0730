package net.dgg.cloud.service;

import net.dgg.cloud.dto.XdResourcesFollowDto;
import net.dgg.cloud.entity.XdResourceFollow;

import java.util.Map;

/**
 * 资源跟进记录信息
 * @author
 */
public interface XdResourceFollowService {

    /**
     * 根据资源id获取该资源所有跟进记录
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> queryResourcesFollowList(XdResourcesFollowDto dto, String token);

    /**
     * 插入跟进记录
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> addResourceFollow(XdResourcesFollowDto dto, String token);
}
