package net.dgg.cloud.service;

import net.dgg.cloud.entity.XdGeographyRecords;

import java.util.Map;

/**
 * 骑手位置记录service
 */
public interface XdGeographyRecordService {

    /***插入骑手位置信息*/
    Map<String,Object> addGeographyRecord(XdGeographyRecords record ,String token);
}
