package net.dgg.cloud.service.Impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.constant.BaiduHawkEyeConstants;
import net.dgg.cloud.constant.WxConstants;
import net.dgg.cloud.dao.WxUserMapper;
import net.dgg.cloud.dao.XdResourcesBaseMapper;
import net.dgg.cloud.dao.XdSalesmanMapper;
import net.dgg.cloud.dto.WxUserDto;
import net.dgg.cloud.dto.WxUserLocationDto;
import net.dgg.cloud.dto.XdWxResourceBaseDto;
import net.dgg.cloud.entity.WxUser;
import net.dgg.cloud.entity.XdResourcesBase;
import net.dgg.cloud.entity.XdSalesman;
import net.dgg.cloud.entity.baiduhawkeye.BaiDuResult;
import net.dgg.cloud.entity.baiduhawkeye.EntityResult;
import net.dgg.cloud.entity.baiduhawkeye.LatestLocation;
import net.dgg.cloud.service.WxUserService;
import net.dgg.cloud.utils.HttpClientUtil;
import net.dgg.cloud.utils.WechatUtil;
import net.dgg.framework.utils.PrimaryKeyUtils;
import net.dgg.framework.utils.RedisUtils;
import net.dgg.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ytz
 * @date 2018/5/15.
 * @desc
 */
@Service
public class WxUserServiceImpl implements WxUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WxUserMapper wxUserMapper;

    @Resource
    private XdResourcesBaseMapper xdResourcesBaseMapper;

    @Resource
    private XdSalesmanMapper salesmanMapper;

    @Override
    public Map<String, Object> validate(WxUserDto dto) {
        Map<String, Object> retMap = new HashMap<>();
        String openId = dto.getOpenId();
        List<WxUser> wxUser = wxUserMapper.selectByOPenid(openId);
        if (wxUser == null || wxUser.size() <= 0) {
            retMap.put("code", WxConstants.UN_BIND);
            retMap.put("mag", "用户未绑定！");
        } else {
            retMap.put("code", 0);
            retMap.put("mag", "已绑定！");
        }
        return retMap;
    }

    @Transactional
    @Override
    public Map<String, Object> bindPhoneNo(@RequestBody WxUserDto dto) {
        Map<String, Object> retMap = new HashMap<>();

        String phoneNo = dto.getPhoneNo();
        String msgCode = dto.getMsgCode();
        String openId = dto.getOpenId();
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

        String wxMsgCodePrefix = ApiConstants.WX_MSG_CODE_PREFIX;
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

        List<WxUser> wxUserList = wxUserMapper.selectByOPenid(openId);
        if (wxUserList != null && wxUserList.size() > 0) {
            WxUser user = wxUserList.get(0);
            if (user != null) {
                String userPhoneNo = user.getPhoneNo();
                String userOpenId = user.getOpenId();
                String userId = user.getId();
                if (StringUtils.isBlank(userPhoneNo) || StringUtils.isBlank(userOpenId)) {
                    wxUserMapper.deleteByPrimaryKey(userId);
                }
            }
        }

        WxUser wxUser = new WxUser();
        String id = PrimaryKeyUtils.getId();
        wxUser.setId(id);
        wxUser.setOpenId(openId);
        wxUser.setIsSubscribe(1);
        wxUser.setIsBind(1);
        wxUser.setPhoneNo(phoneNo);
        wxUser.setCreateTime(new Date());

        try {
            wxUserMapper.insert(wxUser);
            retMap.put("code", 0);
            retMap.put("msg", "绑定成功！");
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "绑定失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    @Override
    public Map<String,Object> getResourceList(WxUserDto dto, Model model) {
        Map<String, Object> retMap = new HashMap<>();
        String openId = dto.getOpenId();
        List<WxUser> wxUserList = wxUserMapper.selectByOPenid(openId);
        if (wxUserList == null || wxUserList.size() <= 0) {
            retMap.put("code", WxConstants.UN_BIND);
            retMap.put("mag", "用户未绑定！");
            return retMap;
        }
        WxUser wxUser = wxUserList.get(0);
        String phoneNo = wxUser.getPhoneNo();
        dto.setPhoneNo(phoneNo);
        Integer page = dto.getPage();
        Integer pageSize = dto.getPageSize();
        if (page != null && pageSize != null) {
            page = page <= 0 ? 1 : page;
            pageSize = pageSize <= 0 ? 10 : pageSize;
            page = (page - 1) * pageSize;

            dto.setPage(page);
            dto.setPageSize(pageSize);
        }

        try {
            List<XdWxResourceBaseDto> baseDtoList = xdResourcesBaseMapper.selectResourcesListByPhone(dto);
            int count = xdResourcesBaseMapper.selectResourcesCountByPhone(dto);

            retMap.put("totalCount", count);
            retMap.put("data", baseDtoList);
            retMap.put("code", 0);
            retMap.put("msg", "获取资源列表成功！");
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "获取资源列表失败！");
        }
        return retMap;
    }

    @Override
    public String index(InputStream inputStream) {
        try {
            Map<String, String> dataMap = WechatUtil.parseXmlByInpuStream(inputStream);
            String event = dataMap.get("Event");
            if (WxConstants.SUB_EVENT.equals(event)) {
                String toUserName = dataMap.get("ToUserName");
                String fromUserName = dataMap.get("FromUserName");
                String createTime = dataMap.get("CreateTime");
                String msgType = dataMap.get("MsgType");
                logger.info(" fromUserName : " + fromUserName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, Object> getSalesmanLocation(WxUserDto dto) {
        Map<String, Object> retMap = new HashMap<>();
        String resourceId = dto.getResourceId();
        if (StringUtils.isBlank(resourceId)) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误：id为空！");
            return retMap;
        }

        XdResourcesBase resourcesBase = xdResourcesBaseMapper.selectByPrimaryKey(resourceId);
        if (resourcesBase == null || resourcesBase.getDelFlag() == 1) {
            retMap.put("code", -1);
            retMap.put("msg", "资源状态异常！");
            return retMap;
        }

        Date nextFollowTime = resourcesBase.getNextFollowTime();
        long nextTime = nextFollowTime.getTime();
        long curTime = System.currentTimeMillis();

        long diff = nextTime - curTime;
        int minDiff = ApiConstants.SHOW_LOCATION_TIME_DIFF;
        int timeDiff = minDiff * 60 * 1000;
        double hour = (double) minDiff / 60;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String hourFormat = decimalFormat.format(hour);
        if (diff > timeDiff) {
            retMap.put("code", -1);
            retMap.put("msg", "商务暂未在线，可在" + hourFormat + "小时内查看商务位置！");
            return retMap;
        }

        String commerceId = resourcesBase.getCommerceId();
        if (StringUtils.isBlank(commerceId)) {
            retMap.put("code", -1);
            retMap.put("msg", "订单暂未分配！");
            return retMap;
        }
        XdSalesman salesman = salesmanMapper.selectByPrimaryKey(commerceId);
        if (salesman == null) {
            retMap.put("code", -1);
            retMap.put("msg", "商务暂未在线，请稍后查看！");
            return retMap;
        }
        String salesmanName = salesman.getName();

        String url = BaiduHawkEyeConstants.LOCATION_LIST_URL;
        String ak = BaiduHawkEyeConstants.AK;
        int serviceId = BaiduHawkEyeConstants.SERVICE_ID;

        Map<String, Object> query = new HashMap<>();
        query.put("ak", ak);
        query.put("service_id", serviceId);
        query.put("filter", "entity_names:" + commerceId);

        try {
            String doGet = HttpClientUtil.doGet(url, query);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            BaiDuResult baiDuResult = gson.fromJson(doGet, BaiDuResult.class);
            List<EntityResult> entities = baiDuResult.getEntities();
            if (entities != null && entities.size() > 0) {
                EntityResult item = entities.get(0);

                String createTime = item.getCreate_time();
                String modifyTime = item.getModify_time();
                String entityName = item.getEntity_name();
                LatestLocation location = item.getLatest_location();

                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                long locTime = location.getLoc_time();
                double speed = location.getSpeed();
                int direction = location.getDirection();
                double radius = location.getRadius();

                WxUserLocationDto locationDto = new WxUserLocationDto();
                locationDto.setSalesmanId(commerceId);
                locationDto.setSalesmanName(salesmanName);
                locationDto.setLongitude(longitude);
                locationDto.setLatitude(latitude);
                locationDto.setDirection(direction);
                locationDto.setLocTime(locTime);
                locationDto.setRadius(radius);
                locationDto.setSpeed(speed);

                retMap.put("code", 0);
                retMap.put("msg", "获取商务位置成功！");
                retMap.put("data", locationDto);
            } else {
                retMap.put("code", 0);
                retMap.put("msg", "商务暂未上线，请稍后查看！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "获取商务位置失败！");
        }
        return retMap;
    }
}
