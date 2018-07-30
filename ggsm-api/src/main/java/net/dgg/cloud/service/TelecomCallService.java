package net.dgg.cloud.service;

import net.dgg.cloud.entity.call.TelecomCallDto;

import java.util.Map;

/**
 * V2
 *
 * @author ytz
 * @date 2018/7/23.
 * @desc 电信拨号
 */
public interface TelecomCallService {

    /**
     * 电信拨号
     *
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> telecomCall(TelecomCallDto dto, String token);

}
