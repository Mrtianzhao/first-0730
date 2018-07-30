package net.dgg.cloud.service.Impl;

import com.google.gson.Gson;
import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dao.*;
import net.dgg.cloud.dto.BaiduDistanceReqDto;
import net.dgg.cloud.dto.VisitRecordDto;
import net.dgg.cloud.dto.XdResourceVisitDto;
import net.dgg.cloud.entity.*;
import net.dgg.cloud.service.BaiduMapService;
import net.dgg.cloud.service.XdResourceVisitService;
import net.dgg.cloud.utils.RestUtil;
import net.dgg.framework.redis.RedisFactory;
import net.dgg.framework.utils.PrimaryKeyUtils;
import net.dgg.framework.utils.ResourceUtils;
import net.dgg.framework.utils.StringUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资源上门信息serviceImpl
 *
 * @author
 */
@Service
public class XdResourceVisitServiceImpl implements XdResourceVisitService {

    private Logger logger = LoggerFactory.getLogger(XdResourceVisitServiceImpl.class);

    @Resource
    XdResourceVisitMapper xdResourceVisitMapper;

    @Resource
    XdResourcesBaseMapper xdResourcesBaseMapper;

    @Resource
    XdSalesmanMapper salesmanMapper;

    @Resource
    private XdResourceOperationMapper xdResourceOperationMapper;

    @Resource
    private XdOnTimeMapper xdOnTimeMapper;

    @Resource
    BaiduMapService baiduMapService;

    @Resource
    private XdResourceSourceMapper resourceSourceMapper;

    @Autowired
    private RestUtil restUtil;

    /**
     * 根据资源id获取上门信息列表
     *
     * @param dto
     * @param token
     * @return
     */
    @Override
    public Map<String, Object> queryResourceVisitList(XdResourceVisitDto dto, String token) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            String resourcesId = dto.getResourcesId();
            if (StringUtils.isBlank(resourcesId)) {
                retMap.put("code", 1);
                retMap.put("msg", "资源id不能为空!");
                return retMap;
            }

            Integer page = dto.getPage();
            Integer pageSize = dto.getPageSize();
            if (page != null && pageSize != null) {
                page = page <= 0 ? 1 : page;
                pageSize = pageSize <= 0 ? 20 : pageSize;
                page = (page - 1) * pageSize;

                dto.setPage(page);
                dto.setPageSize(pageSize);
            }

            List<XdResourceVisit> resourceVisits = xdResourceVisitMapper.selectXdResourceVisitList(dto);
            Integer count = xdResourceVisitMapper.selectCount(dto);
            count = count == null ? 0 : count;

            XdResourcesBase resourcesBase = xdResourcesBaseMapper.selectByPrimaryKey(resourcesId);
            Integer status = null;
            int isCanInvalid = 1;
            if (resourcesBase != null) {
                status = resourcesBase.getResourcesStatus();
                int stateCode4 = ApiConstants.RESOURCE_STATE_CODE_4;
                int stateCode5 = ApiConstants.RESOURCE_STATE_CODE_5;
                /*if (stateCode4 == status || stateCode5 == status) {
                    isCanInvalid = 0;
                }*/

                // V2 ===================================
                Integer channel = resourcesBase.getResourceChannel();
                Integer invalidNum = resourcesBase.getInvalidNum();
                Integer resourcesStatus = resourcesBase.getResourcesStatus();
                // 是否可以反无效
                if (ApiConstants.RESOURCE_CHANNEL_1 == channel) {
                    //  抢单
                    isCanInvalid = 0;
                } else if (ApiConstants.RESOURCE_CHANNEL_0 == channel && invalidNum >= ApiConstants.INVALID_COUNT) {
                    // 分配 已反无效 2 次
                    isCanInvalid = 0;
                } else if (stateCode4 == resourcesStatus || stateCode5 == resourcesStatus) {
                    // 已成单 已下单
                    isCanInvalid = 0;
                } else {
                    // 可以
                    isCanInvalid = 1;
                }
                // V2 ===================================
            }

            String fastHost = getHeadHost();
            data.put("fastHost", fastHost);
            data.put("size", count);
            data.put("isCanInvalid", isCanInvalid);
            data.put("resourceState", status);
            data.put("pageSize", resourceVisits);

            retMap.put("code", 0);
            retMap.put("msg", "获取上门信息列表成功！");
            retMap.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", 1);
            retMap.put("msg", "请求失败！");
            return data;
        }
        return retMap;
    }

    /**
     * 插入上门信息
     *
     * @param dto
     * @param token
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addResourceVisitInfo(XdResourceVisitDto dto, String token) {
        Map<String, Object> retMap = new HashMap<>();

        try {

            String resourcesId = dto.getResourcesId();
            String visitTime = dto.getVisitTime();
            Integer visitType = dto.getVisitType();
            Integer visitSituation = dto.getVisitSituation();
            String visitImg = dto.getVisitImg();
            String visitAddress = dto.getVisitAddress();
            String nextFollowTime = dto.getNextFollowTime();
            String remark = dto.getRemark();

            if (StringUtils.isBlank(resourcesId)) {
                retMap.put("code", -1);
                retMap.put("msg", "参数错误：资源id为空!");
                return retMap;
            }
            if (StringUtils.isBlank(visitTime)) {
                retMap.put("code", -1);
                retMap.put("msg", "上门时间不能为空！");
                return retMap;
            }
            if (visitType == null) {
                retMap.put("code", -1);
                retMap.put("msg", "上门类型不能为空！");
                return retMap;
            }
            if (visitSituation == null) {
                retMap.put("code", -1);
                retMap.put("msg", "上门情况不能为空！");
                return retMap;
            }
            if (StringUtils.isBlank(visitImg)) {
                retMap.put("code", -1);
                retMap.put("msg", "上门附件不能为空！");
                return retMap;
            }
            if (StringUtils.isBlank(visitAddress)) {
                retMap.put("code", -1);
                retMap.put("msg", "上门地址不能为空！");
                return retMap;
            }
            if (StringUtils.isBlank(nextFollowTime)) {
                retMap.put("code", -1);
                retMap.put("msg", "下次跟进时间不能为空！");
                return retMap;
            }

            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            String phone = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            //
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phone);
            if (salesman == null) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }

            XdResourceVisit xdResourceVisit = new XdResourceVisit();
            String id = PrimaryKeyUtils.getId();
            String reg = ApiConstants.VISIT_TIME_DEFAULT_REG;
            Date visitDate = convertDateStr2Date(visitTime, reg);
            String salesmanId = salesman.getId();

            xdResourceVisit.setVisitId(id);
            xdResourceVisit.setResourcesId(resourcesId);
            xdResourceVisit.setVisitTime(visitDate);
            xdResourceVisit.setVisitImg(visitImg);
            xdResourceVisit.setVisitAddress(visitAddress);
            xdResourceVisit.setTalkOrderType(visitType);
            xdResourceVisit.setTalkOrderSituation(visitSituation);
            xdResourceVisit.setRemark(dto.getRemark());
            xdResourceVisit.setSalesmanId(salesmanId);
            xdResourceVisit.setCreateTime(new Date());

            XdResourcesBase base = new XdResourcesBase();
            Date nextFollowDate = convertDateStr2Date(nextFollowTime, reg);

            base.setLostTime(nextFollowDate);
            base.setNextFollowTime(nextFollowDate);
            base.setResourcesId(resourcesId);
            base.setUpdateUserId(salesmanId);
            base.setUpdateTime(new Date());

            XdResourcesBase resourcesBase = xdResourcesBaseMapper.selectByPrimaryKey(resourcesId);
            if (resourcesBase == null || resourcesBase.getDelFlag() == 1) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常！");
                return retMap;
            }

            Integer status = resourcesBase.getResourcesStatus();
            Integer delFlag = resourcesBase.getDelFlag();

            // 待跟进 跟进中 已上门
            int stateCode1 = ApiConstants.RESOURCE_STATE_CODE_1;
            int stateCode2 = ApiConstants.RESOURCE_STATE_CODE_2;
            int stateCode3 = ApiConstants.RESOURCE_STATE_CODE_3;
            int stateCode4 = ApiConstants.RESOURCE_STATE_CODE_4;
            int stateCode5 = ApiConstants.RESOURCE_STATE_CODE_5;
            int stateCode6 = ApiConstants.RESOURCE_STATE_CODE_6;
            int stateCode7 = ApiConstants.RESOURCE_STATE_CODE_7;

            if (stateCode1 != status && stateCode2 != status && stateCode3 != status && stateCode4 != status && stateCode5 != status) {
                retMap.put("code", -1);
                if (stateCode6 == status) {
                    retMap.put("msg", "资源已掉库！");
                } else if (stateCode7 == status && delFlag == 0) {
                    retMap.put("msg", "资源反无效审核中，不能录入上门信息！");
                } else {
                    retMap.put("msg", "非法操作！");
                }
                return retMap;
            }

            XdResourceOperation operation = new XdResourceOperation();
            String phoneNumber = salesman.getPhoneNumber();
            String salesmanName = salesman.getName();

            String optId = PrimaryKeyUtils.getId();
            operation.setId(optId);
            operation.setOperatingTime(new Date());
            operation.setOperatorLoginName(phoneNumber);
            operation.setOperatorName(salesmanName);
            operation.setResourcesId(resourcesId);
            operation.setCommerceId(salesmanId);

            if (status != ApiConstants.RESOURCE_STATE_CODE_4 && status != ApiConstants.RESOURCE_STATE_CODE_5) {
                if (visitSituation == 1) {
                    base.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_4);
                    base.setfOrderTime(new Date());
                    operation.setOperationRecords(salesmanName + "已成单");
                } else {
                    base.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_3);
                    operation.setOperationRecords(salesmanName + "已上门");
                }
            }

            if (status == ApiConstants.RESOURCE_STATE_CODE_4) {
                base.setfOrderTime(new Date());
                operation.setOperationRecords(salesmanName + "已成单");
            }

            if (visitSituation == 1) {
                operation.setOperationRecords(salesmanName + "已成单");
            } else {
                operation.setOperationRecords(salesmanName + "已上门");
            }

            xdResourceOperationMapper.insert(operation);
            xdResourcesBaseMapper.updateByPrimaryKeySelective(base);
            xdResourceVisitMapper.insert(xdResourceVisit);

            retMap.put("code", 0);
            retMap.put("msg", "上门信息新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "请求失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    // V2 =====================================================

    @Transactional
    @Override
    public Map<String, Object> saveOrUpdateVisitRecord(VisitRecordDto dto, String token) {
        Map<String, Object> retMap = new HashMap<>();
        Integer optType = dto.getOptType();
        // 开始上门1，我已到达2，我已成单3，下次跟进4，取消上门5，无效上门6
        int optType_1 = 1;
        int optType_2 = 2;
        int optType_3 = 3;
        int optType_4 = 4;
        int optType_5 = 5;

        try {
            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            String phone = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phone);
            if (optType == null) {
                retMap.put("code", -1);
                retMap.put("msg", "非法请求！");
                return retMap;
            }

            if (salesman == null) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }

            String resourceId = dto.getResourceId();
            if (StringUtils.isBlank(resourceId)) {
                retMap.put("code", -1);
                retMap.put("msg", "资源id不能为空！");
                return retMap;
            }
            if (optType == optType_1) {
                retMap = saveVisitRecord(dto, salesman);
            }
            if (optType == optType_2) {
                retMap = updateVisitRecordArrive(dto, salesman);
            }
            if (optType == optType_3) {
                retMap = updateVisitRecord(optType_3, dto, salesman);
            }
            if (optType == optType_4) {
                retMap = updateVisitRecord(optType_4, dto, salesman);
            }
            if (optType == optType_5) {
                retMap = updateVisitRecordCancel(dto, salesman);
            }
        } catch (Exception e) {
            logger.error(optType + "失败：", e);
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "操作失败，请重试！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return retMap;
    }

    /**
     * 保存上门记录
     *
     * @param dto      数据
     * @param salesman 商务
     * @return
     */
    private Map<String, Object> saveVisitRecord(VisitRecordDto dto, XdSalesman salesman) {
        Map<String, Object> retMap = new HashMap<>();
        Integer status = salesman.getOnlineStatus();
        boolean bool = status == null || (status != null && status == 0);
        if (bool) {
            retMap.put("code", -1);
            retMap.put("msg", "请切换为在线状态！");
            return retMap;
        }
        String resourceId = dto.getResourceId();
        String visitAddress = dto.getVisitAddress();
        String longitude = dto.getLongitude();
        String latitude = dto.getLatitude();
        Integer defaultAddress = dto.getDefaultAddress();
        String salesmanId = salesman.getId();

        // 正在上门的资源
        List<XdResourcesBase> baseList = this.xdResourcesBaseMapper.selectVisitingResource(salesmanId);
        if (baseList != null && baseList.size() > 0) {
            XdResourcesBase resourcesBase = baseList.get(0);
            String resourcesId = resourcesBase.getResourcesId();
            if (resourcesId != null && !resourcesId.equals(resourceId)) {
                retMap.put("code", -1);
                retMap.put("msg", "已有资源上门中！");
                return retMap;
            }
        }

        if (defaultAddress == null) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误！");
            return retMap;
        }

        if (defaultAddress == 0) {
            if (StringUtils.isBlank(visitAddress)) {
                retMap.put("code", -1);
                retMap.put("msg", "上门地址不能为空！");
                return retMap;
            }
            if (StringUtils.isBlank(longitude)) {
                retMap.put("code", -1);
                retMap.put("msg", "上门地址经度错误！");
                return retMap;
            }
            if (StringUtils.isBlank(latitude)) {
                retMap.put("code", -1);
                retMap.put("msg", "上门地址纬度错误！");
                return retMap;
            }

            double longitudeDou = Double.parseDouble(longitude);
            if (longitudeDou < -180 || longitudeDou > 180) {
                retMap.put("code", -1);
                retMap.put("msg", "经纬度错误！");
                return retMap;
            }
            double latitudeDou = Double.parseDouble(latitude);
            if (latitudeDou < -90 || latitudeDou > 90) {
                retMap.put("code", -1);
                retMap.put("msg", "经纬度错误！");
                return retMap;
            }

        }

        XdResourcesBase resources = this.xdResourcesBaseMapper.selectByPrimaryKey(resourceId);
        Integer resourcesStatus = resources.getResourcesStatus();
        Integer delFlag = resources.getDelFlag();
        if (ApiConstants.RESOURCE_STATE_CODE_7 == resourcesStatus && delFlag == 0) {
            retMap.put("code", -1);
            retMap.put("msg", "资源反无效审核中，操作失败！");
            return retMap;
        }

        XdResourceVisit visit = new XdResourceVisit();
        String visitId = PrimaryKeyUtils.getId();
        visit.setVisitId(visitId);
        visit.setResourcesId(resourceId);
        visit.setSalesmanId(salesmanId);
        visit.setState(1);
        visit.setCreateTime(new Date());
        visit.setDistance(0D);
        if (defaultAddress == 1) {
            String customerAddress = resources.getVisitAddress();
            String longitude1 = resources.getLongitude();
            String latitude1 = resources.getLatitude();
            visit.setVisitAddress(customerAddress);
            visit.setLongitude(longitude1);
            visit.setLatitude(latitude1);
        } else {
            visit.setLongitude(longitude);
            visit.setLatitude(latitude);
            visit.setVisitAddress(visitAddress);
        }

        resources.setCurrentVisitId(visitId);
        this.xdResourceVisitMapper.insertSelective(visit);
        this.xdResourcesBaseMapper.updateByPrimaryKeySelective(resources);
        // 结束当前在线时间
        XdOnTime xdOnTime = xdOnTimeMapper.selectLastOnTime(salesmanId);
        if (xdOnTime != null) {

            Date onTime = xdOnTime.getOnTime();
            Date curDate = new Date();

            long curTimeStamp = curDate.getTime();
            long onTimeStamp = onTime.getTime();
            long timeMinutesDiff = (curTimeStamp - onTimeStamp) / 1000 / 60;
            timeMinutesDiff = timeMinutesDiff < 0 ? 0 : timeMinutesDiff;
            xdOnTime.setOffTime(new Date());
            xdOnTime.setTimeLength(timeMinutesDiff);
            this.xdOnTimeMapper.updateByPrimaryKeySelective(xdOnTime);
        }

        // 新增繁忙数据
        XdOnTime busyOnTime = new XdOnTime();
        busyOnTime.setOnId(PrimaryKeyUtils.getId());
        busyOnTime.setOnTime(new Date());
        busyOnTime.setSalesmanId(salesmanId);
        busyOnTime.setState(ApiConstants.STATE_2);
        xdOnTimeMapper.insert(busyOnTime);

        salesman.setOnlineStatus(ApiConstants.STATE_2);
        this.salesmanMapper.updateByPrimaryKeySelective(salesman);
        // 操作记录
        String phoneNumber = salesman.getPhoneNumber();
        String salesmanName = salesman.getName();
        XdResourceOperation operation = new XdResourceOperation();
        operation.setId(PrimaryKeyUtils.getId());
        operation.setResourcesId(resourceId);
        operation.setOperatorLoginName(phoneNumber);
        operation.setOperatorName(salesmanName);
        operation.setOperationRecords(salesmanName + "开始上门");
        operation.setCommerceId(salesmanId);
        operation.setOperatingTime(new Date());
        this.xdResourceOperationMapper.insert(operation);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("salesmanState", ApiConstants.STATE_2);
        dataMap.put("visitRecordId", visitId);

        retMap.put("code", 0);
        retMap.put("msg", "上门地址保存成功！");
        retMap.put("data", dataMap);
        return retMap;
    }

    /**
     * 修改上門記錄
     *
     * @param optType  我已成单 3 ，下次跟进4
     * @param dto      数据
     * @param salesman 商务
     * @return
     */
    private Map<String, Object> updateVisitRecord(int optType, VisitRecordDto dto, XdSalesman salesman) throws Exception {
        Map<String, Object> retMap = new HashMap<>();
        String recordId = dto.getVisitRecordId();
        String dtoResourceId = dto.getResourceId();
        Integer isStandard = dto.getIsStandard();
        Integer isAddWX = dto.getIsAddWX();
        String reason = dto.getNotAddWXReason();
        String remark = dto.getRemark();
        String nextTime = dto.getNextTime();
        String visitImg = dto.getVisitImg();
        // 我已成单
        int opt_3 = 3;
        int opt_4 = 4;
        if (recordId == null || dtoResourceId == null || isStandard == null || isAddWX == null) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误！");
            return retMap;
        }
        if (isAddWX == 0 && StringUtils.isBlank(reason)) {
            retMap.put("code", -1);
            retMap.put("msg", "请填写未添加微信原因！");
            return retMap;
        }
        int reasonLen = 30;
        if (isAddWX == 0 && reason.length() > reasonLen) {
            retMap.put("code", -1);
            retMap.put("msg", "原因不能超过30字！");
            return retMap;
        }
        if (optType == opt_4 && StringUtils.isBlank(nextTime)) {
            retMap.put("code", -1);
            retMap.put("msg", "请填写下次跟进时间！");
            return retMap;
        }
        if (StringUtils.isBlank(visitImg)) {
            retMap.put("code", -1);
            retMap.put("msg", "请上传图片！");
            return retMap;
        }

        // 资源状态
        XdResourcesBase resources = this.xdResourcesBaseMapper.selectByPrimaryKey(dtoResourceId);
        if (resources == null) {
            retMap.put("code", -1);
            retMap.put("msg", "资源状态异常！");
            return retMap;
        }

        // 通知crm，jriboss
        String resSourceId = resources.getResSourceId();
        XdResourceSource source = this.resourceSourceMapper.selectByPrimaryKey(resSourceId);
        String channelCode = "";
        if (source != null) {
            channelCode = source.getSourceCode();
        }
        List<String> noticeArr = Arrays.asList(ApiConstants.NOTICE_RESOURCE_CODE);
        String phoneNumber = salesman.getPhoneNumber();
        String salesmanName = salesman.getName();
        String salesmanId = salesman.getId();
        if (optType == opt_3 && noticeArr.contains(channelCode)) {
            Integer status = resources.getResourcesStatus();
            // 成单一次之后不再调用crm，jriboss 接口
            if (ApiConstants.RESOURCE_STATE_CODE_4 != status && ApiConstants.RESOURCE_STATE_CODE_5 != status) {

                retMap = noticeCrmOrJriboss(resources, salesman);
                int code = Integer.parseInt(String.valueOf(retMap.get("code")));
                if (code != 0) {
                    // 操作记录
                    XdResourceOperation operation = new XdResourceOperation();
                    operation.setId(PrimaryKeyUtils.getId());
                    operation.setResourcesId(dtoResourceId);
                    operation.setOperatorLoginName(phoneNumber);
                    operation.setOperatorName(salesmanName);
                    operation.setCommerceId(salesmanId);
                    operation.setOperatingTime(new Date());
                    operation.setOperationRecords(String.valueOf(retMap.get("msg")));
                    this.xdResourceOperationMapper.insert(operation);
                    return retMap;
                }
            }
        }

        XdResourceVisit visit = this.xdResourceVisitMapper.selectByPrimaryKey(recordId);
        Date date = new Date();
        visit.setTalkOrderType(isStandard);
        visit.setAddWx(isAddWX);
        visit.setNotAddWxReason(reason);
        visit.setRemark(remark);
        visit.setEndTime(date);
        visit.setVisitImg(visitImg);
        if (optType == opt_3) {
            // 成单
            visit.setTalkOrderSituation(1);
            visit.setState(3);
        } else {
            // 上门
            visit.setTalkOrderSituation(0);
            visit.setState(4);
        }
        long startTime = visit.getCreateTime().getTime() / 1000;
        long endTime = date.getTime() / 1000;
        String entityName = visit.getSalesmanId();

        BigDecimal distance = getBaiDuDistance(startTime, endTime, entityName);
        visit.setDistance(distance.doubleValue());
        this.xdResourceVisitMapper.updateByPrimaryKeySelective(visit);


        Integer status = resources.getResourcesStatus();

        if (optType == opt_3) {
            if (ApiConstants.RESOURCE_STATE_CODE_5 != status) {
                resources.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_4);
            }
            Date date1 = new Date();
            resources.setfOrderTime(date1);
        }
        if (optType != opt_3) {
            Date nextDate = convertDateStr2Date(nextTime, ApiConstants.YYYY_MM_DD_HH_MM);
            resources.setNextFollowTime(nextDate);
            if (ApiConstants.RESOURCE_STATE_CODE_4 != status && ApiConstants.RESOURCE_STATE_CODE_5 != status) {
                resources.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_3);
            }
        }
        resources.setCurrentVisitId(null);
        resources.setLastFollowTime(new Date());
        resources.setLastVisitTime(new Date());
        this.xdResourcesBaseMapper.updateByPrimaryKey(resources);

        // 商务信息
        salesman.setOnlineStatus(ApiConstants.STATE_1);
        this.salesmanMapper.updateByPrimaryKeySelective(salesman);

        // 在线时长 : 结束繁忙 开始在线 0：离线，1：在线，2：繁忙
        XdOnTime xdOnTime = this.xdOnTimeMapper.selectLastBusyOnTIme(salesmanId);
        if (xdOnTime == null) {

        } else {
            Date offTime = new Date();
            Date onTime = xdOnTime.getOnTime();

            long onTimeStamp = onTime.getTime();
            long offTimeStamp = offTime.getTime();

            long diff = (offTimeStamp - onTimeStamp) / 1000 / 60;
            diff = diff < 0 ? 0 : diff;
            xdOnTime.setOffTime(offTime);
            xdOnTime.setTimeLength(diff / 2);
            this.xdOnTimeMapper.updateByPrimaryKeySelective(xdOnTime);
        }

        XdOnTime onTime = new XdOnTime();
        onTime.setOnId(PrimaryKeyUtils.getId());
        onTime.setOnTime(new Date());
        onTime.setSalesmanId(salesmanId);
        onTime.setState(1);
        this.xdOnTimeMapper.insert(onTime);


        XdResourceOperation operation = new XdResourceOperation();
        operation.setId(PrimaryKeyUtils.getId());
        operation.setResourcesId(dtoResourceId);
        operation.setOperatorLoginName(phoneNumber);
        operation.setOperatorName(salesmanName);
        operation.setCommerceId(salesmanId);
        operation.setOperatingTime(new Date());
        String optRecord = "";
        if (optType == opt_3) {
            optRecord = salesmanName + "已成单";
        } else if (optType == opt_4) {
            optRecord = salesmanName + "已上门";
        }
        operation.setOperationRecords(optRecord);
        this.xdResourceOperationMapper.insert(operation);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("salesmanState", ApiConstants.STATE_1);

//        // 通知crm，jriboss
//        String resSourceId = resources.getResSourceId();
//        XdResourceSource source = this.resourceSourceMapper.selectByPrimaryKey(resSourceId);
//        String channelCode = "";
//        if (source != null) {
//            channelCode = source.getSourceCode();
//        }
//        List<String> noticeArr = Arrays.asList(ApiConstants.NOTICE_RESOURCE_CODE);

//        if (optType == opt_3 && noticeArr.contains(channelCode)) {
//            retMap = noticeCrmOrJriboss(resources, salesman);
//        } else {
//            retMap.put("code", 0);
//            retMap.put("msg", "操作成功！");
//        }

        retMap.put("code", 0);
        retMap.put("msg", "操作成功！");
        retMap.put("data", dataMap);

        return retMap;
    }

    private Map<String, Object> noticeCrmOrJriboss(XdResourcesBase resources, XdSalesman salesman) throws Exception {
        Map<String, Object> retMap = new HashMap<>();
        String sourceId = resources.getResSourceId();
        XdResourceSource source = this.resourceSourceMapper.selectByPrimaryKey(sourceId);
        if (source == null) {
            retMap.put("code", -1);
            retMap.put("msg", "资源状态异常！");
            return retMap;
        }

        StringBuilder reqUrl = new StringBuilder();
        String sourceCode = source.getSourceCode();
        Map<String, String> configMap = ResourceUtils.getResource(ApiConstants.CONFIG_FILE_NAME).getMap();
            // CRM
        if (ApiConstants.CRM_SOURCE_CODE.equals(sourceCode)) {
            reqUrl.append(configMap.get(ApiConstants.CRM_NOTIC_HOST_KEY)).append(configMap.get(ApiConstants.CRM_NOTICEE_PATH_KEY));
            // JRIBOSS
        } else if (ApiConstants.JRIBOSS_SOURCE_CODE.equals(sourceCode)) {
            reqUrl.append(configMap.get(ApiConstants.JRIBOSS_NOTIC_HOST_KEY)).append(configMap.get(ApiConstants.JRIBOSS_NOTICEE_PATH_KEY));
            // IBOSS
        } else if (ApiConstants.IBOSS_SOURCE_CODE.equals(sourceCode)) {
            reqUrl.append(configMap.get(ApiConstants.IBOSS_NOTIC_HOST_KEY)).append(configMap.get(ApiConstants.IBOSS_NOTICEE_PATH_KEY));
        }

        HashMap<String, Object> param = new HashMap<>();
        param.put("businessCode", resources.getExtBusinessCode());
        param.put("commerceWorkno", salesman.getEmployeeNo());

        ResponseEntity<String> response = restUtil.sendPost(reqUrl.toString(), param);
        String body = response.getBody();
        Map map = com.alibaba.fastjson.JSONObject.parseObject(body);
        Object retCode = map.get("code");
        Object msg = map.get("msg");
        int code = Integer.parseInt(String.valueOf(retCode));
        if (code == 0) {
            retMap.put("code", 0);
            retMap.put("msg", "操作成功！");
        } else {
            if (ApiConstants.CRM_SOURCE_CODE.equals(sourceCode)) {
                retMap.put("code", -1);
                retMap.put("msg", "CRM成单操作失敗！【" + msg + "】");
                logger.error("crm:成单失败 -----> " + msg);
            } else {
                retMap.put("code", -1);
                retMap.put("msg", "JRIBOSS成单操作失敗！【" + msg + "】");
                logger.error("jriboss:成单失败 -----> " + msg);
            }
        }
        return retMap;
    }

    private BigDecimal getBaiDuDistance(long startTime, long endTime, String entityName) {
        BaiduDistanceReqDto baiduDistanceReqDto = new BaiduDistanceReqDto();
        baiduDistanceReqDto.setStartTime(startTime);
        baiduDistanceReqDto.setEndTime(endTime);
        baiduDistanceReqDto.setEntityName(entityName);
        BigDecimal distance = baiduMapService.getDistance(baiduDistanceReqDto);
        distance = distance == null ? new BigDecimal(0) : distance;
        return distance;
    }

    private Map<String, Object> updateVisitRecordCancel(VisitRecordDto dto, XdSalesman salesman) {
        Map<String, Object> retMap = new HashMap<>();
        String recordId = dto.getVisitRecordId();
        if (StringUtils.isBlank(recordId)) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误！");
            return retMap;
        }
        String resourceId = dto.getResourceId();
        // 资源
        XdResourcesBase resourcesBase = this.xdResourcesBaseMapper.selectByPrimaryKey(resourceId);
        resourcesBase.setCurrentVisitId(null);
        this.xdResourcesBaseMapper.updateByPrimaryKey(resourcesBase);

        XdResourceVisit visit = this.xdResourceVisitMapper.selectByPrimaryKey(recordId);
        visit.setVisitId(recordId);
        visit.setState(5);
        Date date = new Date();
        visit.setEndTime(date);

        long start = visit.getCreateTime().getTime();
        long end = date.getTime();
        String entityName = visit.getSalesmanId();
        BigDecimal distance = this.getBaiDuDistance(start, end, entityName);
        visit.setDistance(distance.doubleValue());

        this.xdResourceVisitMapper.updateByPrimaryKeySelective(visit);
        // 商务信息
        salesman.setOnlineStatus(ApiConstants.STATE_1);
        this.salesmanMapper.updateByPrimaryKeySelective(salesman);

        // 在线时长 : 结束繁忙 开始在线 0：离线，1：在线，2：繁忙
        String salesmanId = salesman.getId();
        XdOnTime xdOnTime = this.xdOnTimeMapper.selectLastBusyOnTIme(salesmanId);
        if (xdOnTime != null) {
            Date offTime = new Date();
            Date onTime = xdOnTime.getOnTime();

            long onTimeStamp = onTime.getTime();
            long offTimeStamp = offTime.getTime();

            long diff = (offTimeStamp - onTimeStamp) / 1000 / 60;
            diff = diff < 0 ? 0 : diff;
            xdOnTime.setOffTime(offTime);
            xdOnTime.setTimeLength(diff / 2);
            this.xdOnTimeMapper.updateByPrimaryKeySelective(xdOnTime);
        } else {

        }

        XdOnTime onTime = new XdOnTime();
        onTime.setOnId(PrimaryKeyUtils.getId());
        onTime.setOnTime(new Date());
        onTime.setSalesmanId(salesmanId);
        onTime.setState(1);
        this.xdOnTimeMapper.insert(onTime);

        // 操作记录
        String phoneNumber = salesman.getPhoneNumber();
        String salesmanName = salesman.getName();
        XdResourceOperation operation = new XdResourceOperation();
        operation.setId(PrimaryKeyUtils.getId());
        operation.setResourcesId(resourceId);
        operation.setOperatorLoginName(phoneNumber);
        operation.setOperatorName(salesmanName);
        operation.setOperationRecords(salesmanName + "已取消上门");
        operation.setCommerceId(salesmanId);
        operation.setOperatingTime(new Date());
        this.xdResourceOperationMapper.insert(operation);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("salesmanState", ApiConstants.STATE_1);

        retMap.put("code", 0);
        retMap.put("msg", "操作成功！");
        retMap.put("data", dataMap);

        return retMap;
    }

    private Map<String, Object> updateVisitRecordArrive(VisitRecordDto dto, XdSalesman salesman) {
        Map<String, Object> retMap = new HashMap();
        String recordId = dto.getVisitRecordId();
        if (StringUtils.isBlank(recordId)) {
            retMap.put("code", -1);
            retMap.put("msg", "上门记录id不能为空！");
            return retMap;
        }
        String resourceId = dto.getResourceId();
        XdResourceVisit visit = this.xdResourceVisitMapper.selectByPrimaryKey(recordId);
        visit.setState(2);
        Date date = new Date();
        visit.setVisitTime(date);
        long start = visit.getCreateTime().getTime();
        long end = date.getTime();
        String entityName = visit.getSalesmanId();
        BigDecimal distance = getBaiDuDistance(start, end, entityName);
        visit.setDistance(distance.doubleValue());

        this.xdResourceVisitMapper.updateByPrimaryKeySelective(visit);

        XdResourcesBase resources = this.xdResourcesBaseMapper.selectByPrimaryKey(resourceId);
        this.xdResourcesBaseMapper.updateByPrimaryKeySelective(resources);
        // 操作记录
        String phoneNumber = salesman.getPhoneNumber();
        String salesmanName = salesman.getName();
        String salesmanId = salesman.getId();
        XdResourceOperation operation = new XdResourceOperation();
        operation.setId(PrimaryKeyUtils.getId());
        operation.setResourcesId(resourceId);
        operation.setOperatorLoginName(phoneNumber);
        operation.setOperatorName(salesmanName);
        operation.setOperationRecords(salesmanName + "已到达上门地址");
        operation.setCommerceId(salesmanId);
        operation.setOperatingTime(new Date());
        this.xdResourceOperationMapper.insert(operation);

        retMap.put("code", 0);
        retMap.put("msg", "操作成功！");
        return retMap;
    }


    private Date convertDateStr2Date(String timeStr, String reg) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(reg);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 文件服务器 host
     *
     * @return
     */
    private String getHeadHost() {
        String fastHost = ResourceUtils.getResource("constants").getMap().get("fasdfs.host");
        String port = ResourceUtils.getResource("constants").getMap().get("fastdfs.tracker_http_port");
        String host = fastHost + ":" + port + "/";
        return host;
    }

}
