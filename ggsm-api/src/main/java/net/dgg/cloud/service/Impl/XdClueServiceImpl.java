package net.dgg.cloud.service.Impl;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dao.XdClueMapper;
import net.dgg.cloud.dao.XdResourceSourceMapper;
import net.dgg.cloud.dto.WxUserDto;
import net.dgg.cloud.entity.XdClue;
import net.dgg.cloud.entity.XdResourceSource;
import net.dgg.cloud.service.XdClueService;
import net.dgg.framework.utils.PrimaryKeyUtils;
import net.dgg.framework.utils.RedisUtils;
import net.dgg.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * V2
 *
 * @author ytz
 * @date 2018/7/11.
 * @desc
 */
@Service
public class XdClueServiceImpl implements XdClueService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private XdClueMapper clueMapper;

    @Resource
    private XdResourceSourceMapper resourceSourceMapper;

    @Transactional
    @Override
    public Map<String, Object> reservation(WxUserDto dto) {
        Map<String, Object> retMap = new HashMap<>();

        String phoneNo = dto.getPhoneNo();
        String msgCode = dto.getMsgCode();
        if (StringUtils.isBlank(phoneNo)) {
            retMap.put("code", -1);
            retMap.put("msg", "请输入手机号码！");
            return retMap;
        }

        if (phoneNo.length() != 11 && phoneNo.length() != 12) {
            retMap.put("code", -1);
            retMap.put("msg", "号码格式不正确！");
            return retMap;
        }

        if (StringUtils.isBlank(msgCode)) {
            retMap.put("code", -1);
            retMap.put("msg", "请输入验证码！");
            return retMap;
        }

        try {
            String wxMsgCodePrefix = ApiConstants.WX_ORDER_MSG_PREFIX;
            String msgCodeInRedis = RedisUtils.get(wxMsgCodePrefix + phoneNo);
            if (StringUtils.isBlank(msgCodeInRedis)) {
                retMap.put("code", -1);
                retMap.put("msg", "验证码失效，请重新获取！");
                return retMap;
            }
            if (!msgCodeInRedis.equals(msgCode)) {
                retMap.put("code", -1);
                retMap.put("msg", "验证码错误，请重新输入！");
                return retMap;
            }

            RedisUtils.del(wxMsgCodePrefix + phoneNo);

            List<XdClue> clueList = clueMapper.selectSourceByPhone(phoneNo);
            if (clueList != null && clueList.size() > 0) {
                retMap.put("code", -1);
                retMap.put("msg", "您已有预约服务，客服人员将稍后与您联系！");
                return retMap;
            }

            XdResourceSource source = resourceSourceMapper.selectByCode(ApiConstants.WX_RESOURCE_SOURCE_CODE);

            XdClue clue = new XdClue();
            clue.setId(PrimaryKeyUtils.getId());
            clue.setCustomerPhone(phoneNo);
            clue.setCreateTime(new Date());
            clue.setSourceId(source.getId());

            this.clueMapper.insertSelective(clue);
            retMap.put("code", 0);
            retMap.put("msg", "预约成功，客服人员将稍后与您联系！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "预约失败，请稍后重试");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }
}
