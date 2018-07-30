package net.dgg.system.service;

import net.dgg.cloud.dto.XdSalesmanDto;
import net.dgg.cloud.entity.XdSalesman;
import net.dgg.system.dto.LoginDto;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author ytz
 * @date 2018/4/28.
 * @desc
 */
public interface XdSalesmanService {

    /**
     * 获取token
     *
     * @return
     */
    public String getToken();

    /**
     * 获取验证码
     *
     * @return
     */
    public String getCode();

    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    public Map<String,Object> login(LoginDto loginDto);

    /**
     * 登出
     *
     * @param token
     * @return
     */
    public Map<String,Object> signOut(String token);

    /**
     * 统计
     *
     * @param token
     * @return
     */
    public Map<String, Object> getSalesmanStatistics(XdSalesmanDto dto,String token);

    /**
     * 修改状态
     *
     * @param dto
     * @param token
     * @return
     */
    public Map<String, Object> changeState(XdSalesmanDto dto, String token);

    /**
     * 电话获取商务
     *
     * @param phone
     * @return
     */
    public XdSalesman getSalesmanByPhone(String phone);

}
