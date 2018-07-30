package net.dgg.cloud.service;

import net.dgg.cloud.dto.WxUserDto;
import net.dgg.cloud.entity.WxUser;
import org.springframework.ui.Model;

import java.io.InputStream;
import java.util.Map;

/**
 * @author ytz
 * @date 2018/5/15.
 * @desc 微信公众号
 */
public interface WxUserService {

    /**
     * 验证是否绑定微信号
     *
     * @param dto
     * @return
     */
    public Map<String,Object> validate(WxUserDto dto);

    /**
     * 绑定手机号
     *
     * @param dto
     * @return
     */
    public Map<String, Object> bindPhoneNo(WxUserDto dto);

    /**
     * 获取资源列表
     *
     * @param dto
     * @return
     */
    public Map<String,Object> getResourceList(WxUserDto dto,Model model);


    /**
     * 微信
     *
     * @param inputStream
     * @return
     */
    public String index(InputStream inputStream);

    /**
     * 查看商务位置
     *
     * @param dto
     * @return
     */
    public Map<String,Object> getSalesmanLocation(WxUserDto dto);

}
