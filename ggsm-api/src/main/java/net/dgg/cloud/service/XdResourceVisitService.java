package net.dgg.cloud.service;

import net.dgg.cloud.dto.VisitRecordDto;
import net.dgg.cloud.dto.XdResourceVisitDto;
import net.dgg.cloud.entity.XdResourceVisit;

import java.util.Map;

/**
 * 获取资源上门信息
 */
public interface XdResourceVisitService {

    /**
     * 根据资源id获取上门信息列表
     *
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> queryResourceVisitList(XdResourceVisitDto dto, String token);

    /**
     * 插入上门信息
     *
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> addResourceVisitInfo(XdResourceVisitDto dto, String token);

    // V2 ========================================================

    /**
     * 保存修改上门记录
     *
     * @param dto
     * @return
     */
    Map<String, Object> saveOrUpdateVisitRecord(VisitRecordDto dto, String token);
}
