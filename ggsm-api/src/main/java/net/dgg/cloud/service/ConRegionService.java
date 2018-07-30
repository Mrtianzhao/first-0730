package net.dgg.cloud.service;

import net.dgg.cloud.entity.ConRegion;

import java.util.Map;

public interface ConRegionService {
    Map<String,Object> selectByPid(ConRegion conRegion, String token);
}
