package net.dgg.cloud.service;

import net.dgg.cloud.dto.WxUserDto;

import java.util.Map;

/**
 * V2
 *
 * @author ytz
 * @date 2018/7/11.
 * @desc
 */
public interface XdClueService {

    /**
     * 下单
     *
     * @param dto
     * @return
     */
    Map<String, Object> reservation(WxUserDto dto);
}
