package net.dgg.cloud.service.Impl;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dao.*;
import net.dgg.cloud.dto.*;
import net.dgg.cloud.entity.*;
import net.dgg.cloud.service.XdResourcesBaseService;
import net.dgg.cloud.utils.RestResponseUtil;
import net.dgg.framework.redis.RedisFactory;
import net.dgg.framework.utils.PrimaryKeyUtils;
import net.dgg.framework.utils.RedisUtils;
import net.dgg.framework.utils.ResourceUtils;
import net.dgg.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 资源信息serviceImpl
 *
 * @author
 */
@Service
public class XdResourcesBaseServiceImpl implements XdResourcesBaseService {

    private Logger log = LoggerFactory.getLogger(XdResourcesBaseServiceImpl.class);

    /**
     * 列表标识 (0-新任务，1-待处理，2-资源池)
     */
    public static final String DTO_SIGN_NEW = "0";
    public static final String DTO_SIGN_PENDING = "1";
    public static final String DTO_SIGN_COMMON = "2";

    private final String BUSINESS_CODE_PREFIX = "sj";


    @Resource
    XdResourcesBaseMapper resourcesBaseMapper;

    @Resource
    XdSalesmanMapper salesmanMapper;

    @Resource
    XdBusinessMapper xdBusinessMapper;

    @Resource
    private XdOrderRecordMapper xdOrderRecordMapper;

    @Resource
    private XdResourceOperationMapper xdResourceOperationMapper;

    @Resource
    XdResourcesStatusMapper xdResourcesStatusMapper;

    @Resource
    XdContactsMapper xdContactsMapper;

    @Resource
    private XdSalesmanGradeMapper xdSalesmanGradeMapper;

    @Resource
    private XdIsDisplayMapper xdIsDisplayMapper;

    @Resource
    private ConRegionMapper conRegionMapper;

    @Resource
    private XdResourceSourceMapper xdResourceSourceMapper;

    @Resource
    private XdResourceDistributionMapper xdResourceDistributionMapper;

    @Resource
    private XdResourceVisitMapper resourceVisitMapper;

    /**
     * 查询资源信息列表接口实现
     *
     * @param dto
     * @param token
     * @return
     */
    @Override
    public Map<String, Object> queryResourcesBaseList(XdResourcesBaseDto dto, String token) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            String sign = dto.getSign();
            if (StringUtils.isBlank(sign)) {
                retMap.put("code", -1);
                retMap.put("msg", "参数错误！");
                return retMap;
            }

            String listSign0 = ApiConstants.LIST_SIGN_0;
            String listSign1 = ApiConstants.LIST_SIGN_1;
            String listSign2 = ApiConstants.LIST_SIGN_2;
            String listSign3 = ApiConstants.LIST_SIGN_3;
            String listSign4 = ApiConstants.LIST_SIGN_4;

            if (!listSign0.equalsIgnoreCase(sign) && !listSign1.equals(sign) && !listSign2.equals(sign) && !listSign3.equals(sign) && !listSign4.equals(sign)) {
                retMap.put("code", -1);
                retMap.put("msg", "非法请求！");
                return retMap;
            }

            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            String phone = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            if (StringUtils.isBlank(phone)) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phone);
            if (salesman == null) {
                retMap.put("code", -1);
                retMap.put("msg", "用户不存在！");
                return retMap;
            }

            Integer isDel = salesman.getIsDel();
            Integer isOn = salesman.getIsOn();
            Integer canOrder = salesman.getCanorder();
            Integer reSum = salesman.getReSum();

            if (isOn == ApiConstants.SALESMAN_DISENABLE) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "商务已离职！");
                return retMap;
            }

            Integer page = dto.getPage();
            Integer pageSize = dto.getPageSize();
            int pageApp = page;
            if (page != null && pageSize != null) {
                page = page <= 0 ? 1 : page;
                pageSize = pageSize <= 0 ? 20 : pageSize;
                page = (page - 1) * pageSize;

                dto.setPage(page);
                dto.setPageSize(pageSize);
            } else {
                page = 0;
                pageSize = 10;
                dto.setPage(page);
                dto.setPageSize(pageSize);
            }
            //
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String format = simpleDateFormat.format(calendar.getTime()) + " 00:00:00";
            String commerceId = salesman.getId();

            Date nowDate = new Date();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = simpleDateFormat1.format(nowDate);

            int refuseCount = reSum;
            int retCount = ApiConstants.REFUSE_COUNT;
            int refuseReturnCode = ApiConstants.REFUSE_RETURN_CODE;
            if (!listSign3.equals(sign)) {
                // 本周拒单次数
//            int refuseCount = xdOrderRecordMapper.selectRefuseCountBySalesman(commerceId, format, nowTime);
                boolean bool = (listSign0.equals(sign) || listSign2.equals(sign)) && (refuseCount >= retCount);
                if (bool) {
                    retMap.put("code", refuseReturnCode);
                    retMap.put("msg", "拒单已超过2次，暂不能接单或抢单！");
                    retMap.put("data", new ArrayList<>());
                    return retMap;
                }
                boolean flag = canOrder == 0 && (DTO_SIGN_NEW.equals(sign) || DTO_SIGN_COMMON.equals(sign));
                if (flag) {
                    retMap.put("code", refuseReturnCode);
                    retMap.put("msg", "已禁用，暂不能接单或抢单！");
                    retMap.put("data", new ArrayList<>());
                    return retMap;
                }
            }
            dto.setCommerceId(commerceId);

            String typeId = dto.getOperationTypeId();
            String dtoStatus = dto.getResourcesStatus();
            if (!StringUtils.isBlank(typeId)) {
                String[] typeArr = typeId.split(",");
                List<String> typeList = Arrays.asList(typeArr);
                dto.setTypeList(typeList);
            }

            if (!StringUtils.isBlank(dtoStatus)) {
                String[] statuArr = dtoStatus.split(",");
                List<String> statuList = Arrays.asList(statuArr);
                dto.setStatuList(statuList);
            }

            // 已成单 已下单
            int stateCode4 = ApiConstants.RESOURCE_STATE_CODE_4;
            int stateCode5 = ApiConstants.RESOURCE_STATE_CODE_5;

            List<XdResourcesBaseInfoDto> list = new ArrayList<>();
            List<XdResourcesBaseInfoDto> baseInfoDtos = resourcesBaseMapper.selectResourcesBaseList(dto);
            Integer count = resourcesBaseMapper.selectCount(dto);
            if (baseInfoDtos != null && baseInfoDtos.size() > 0) {
                for (XdResourcesBaseInfoDto item : baseInfoDtos) {
                    if (item == null) {
                        continue;
                    }
                    Integer resourcesStatus = item.getResourcesStatus();
                    if (listSign4.equals(sign)) {
                        setValue(stateCode4,stateCode5,item);
                        Integer status = resourcesStatus;
                        setLossMinutes(item, status);
                        list.add(item);
                    } else {
                        setValue(stateCode4, stateCode5, item);
                        if (!listSign0.equals(sign) && !listSign2.equals(sign)) {
                            setIsLossToday(item);
                        }

                        // 倒计时
                        if (DTO_SIGN_PENDING.equals(sign) || DTO_SIGN_NEW.equals(sign)) {
                            Integer status = resourcesStatus;
                            setLossMinutes(item, status);
                        }
                        list.add(item);
                    }
                }
            }
            // V2 =========================================
            // 正在上门资源置顶
            if (listSign1.equals(sign) || listSign3.equals(sign) || listSign4.equals(sign)) {
                count = setVisitingReesource(dto, sign, listSign1, listSign3, salesman, pageApp, stateCode4, stateCode5, list, count);
            }
            // ==================================================


            data.put("size", count == null ? 0 : count);
            data.put("pageSize", list);
            data.put("refuseCount", ApiConstants.REFUSE_COUNT - refuseCount);
            retMap.put("code", 0);
            retMap.put("msg", "获取资源成功！");
            retMap.put("data", data);

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", 1);
            retMap.put("msg", "请求失败");
            return retMap;
        }
        return retMap;
    }

    private void setLossMinutes(XdResourcesBaseInfoDto item, Integer status) {
        if (ApiConstants.RESOURCE_STATE_CODE_1 == status || ApiConstants.RESOURCE_STATE_CODE_8 == status || ApiConstants.RESOURCE_STATE_CODE_2 == status) {
            Date nextFollowTime = item.getNextFollowTime();
            long followTimeStamp = nextFollowTime.getTime();
            long currentTimeMillis = System.currentTimeMillis();
            long min = (followTimeStamp - currentTimeMillis) / (60 * 1000);
            item.setLeftMinute(min <= 0 ? 1 : min);
        }
    }

    private Integer setVisitingReesource(XdResourcesBaseDto dto, String sign, String listSign1, String listSign3, XdSalesman salesman, int pageApp, int stateCode4, int stateCode5, List<XdResourcesBaseInfoDto> list, Integer count) throws ParseException {
        String listSign4 = "4";
        if ((listSign1.equals(sign) | listSign3.equals(sign) | listSign4.equals(sign)) && pageApp == 1) {
            String salesmanId = salesman.getId();
            XdResourcesBaseInfoDto resource = this.resourcesBaseMapper.selectCurrentVisitResource(salesmanId);
            if (resource != null) {
                String visitId = resource.getCurrentVisitId();
                XdResourceVisit visit = this.resourceVisitMapper.selectByPrimaryKey(visitId);
                if (visit != null) {
                    Integer state = visit.getState();
                    String visitAddress = visit.getVisitAddress();
                    String longitude = visit.getLongitude();
                    String latitude = visit.getLatitude();
                    resource.setVisitState(state);
                    resource.setVisitAddress(visitAddress);
                    resource.setLongitude(longitude);
                    resource.setLatitude(latitude);
                }
                // 倒计时
                if (DTO_SIGN_PENDING.equals(sign) || DTO_SIGN_NEW.equals(sign) || listSign4.equals(sign)) {
                    Integer status = resource.getResourcesStatus();
                    setLossMinutes(resource, status);
                }

                setValue(stateCode4, stateCode5, resource);
                Integer resourcesStatus = resource.getResourcesStatus();
                String channelCode = resource.getChannelCode();
                // 成单库
                if (listSign3.equals(sign)) {
                    if (resourcesStatus == stateCode4 | resourcesStatus == stateCode5) {
                        String status = dto.getResourcesStatus();
                        int anInt = Integer.parseInt(status);
                        if (anInt == resourcesStatus) {
                            list.add(0, resource);
                            count++;
                        }
                    }
                    // 待处理
                } else if (listSign1.equals(sign)){
                    if (resourcesStatus != stateCode4 && resourcesStatus != stateCode5) {
                        list.add(0, resource);
                        count++;
                    }
                    // 今日掉库
                } else {
                    boolean bool = listSign4.equals(sign) && (ApiConstants.CRM_SOURCE_CODE.equals(channelCode) || ApiConstants.JRIBOSS_SOURCE_CODE.equals(channelCode));
                    if (bool){
                        Integer state = resource.getResourcesStatus();
                        boolean flag = false;
                        // 待跟进1、跟进中2：
                        if (state == ApiConstants.RESOURCE_STATE_CODE_1 || state == ApiConstants.RESOURCE_STATE_CODE_2) {
                            Date time = resource.getAssignerTime();
                            flag = compareDate(time, ApiConstants.NOT_VISIT_LOSS_DAY);
                        }
                        // 已成单(X天)
                        if (resourcesStatus == ApiConstants.RESOURCE_STATE_CODE_4 || resourcesStatus == ApiConstants.RESOURCE_STATE_CODE_5) {
                            Date lastFollowTime = resource.getLastFollowTime();
                            flag = compareDate(lastFollowTime, ApiConstants.AFTER_ORDER_LOSS_DAY);
                        }
                        // 已上门：
                        if (resourcesStatus == ApiConstants.RESOURCE_STATE_CODE_3) {
                            Date lastVisitTime = resource.getLastVisitTime();
                            flag = compareDate(lastVisitTime, ApiConstants.VISITED_NOT_ORDER_LOSS_DAY);
                        }
                        if (flag) {
                            list.add(0, resource);
                            count++;
                        }

                    }
                }
            }
        }
        return count;
    }

    /**
     * crm系统：crm,金融iboss系统：jriboss
     */
    private static final String[] CHANNEL_CODE_ARR = {"crm", "jriboss"};

    private void setValue(int stateCode4, int stateCode5, XdResourcesBaseInfoDto resource) throws ParseException {
        Integer channel = resource.getResourceChannel();
        Integer invalidNum = resource.getInvalidNum();
        Integer resourcesStatus = resource.getResourcesStatus();
        // 是否可以反无效
        if (ApiConstants.RESOURCE_CHANNEL_1 == channel) {
            //  抢单
            resource.setIsCanInvalid(ApiConstants.CAN_INVALID_0);
        } else if (ApiConstants.RESOURCE_CHANNEL_0 == channel && invalidNum >= ApiConstants.INVALID_COUNT) {
            // 分配 已反无效 2 次
            resource.setIsCanInvalid(ApiConstants.CAN_INVALID_0);
        } else if (stateCode4 == resourcesStatus || stateCode5 == resourcesStatus) {
            // 已成单 已下单
            resource.setIsCanInvalid(ApiConstants.CAN_INVALID_0);
        } else {
            // 可以
            resource.setIsCanInvalid(ApiConstants.CAN_INVALID_1);
        }

        // 主联系人
        String resourcesId = resource.getResourcesId();
        List<XdContacts> contactList = xdContactsMapper.selectMainContactByResourceId(resourcesId);
        if (contactList != null && contactList.size() > 0) {
            XdContacts xdContacts = contactList.get(0);
            if (xdContacts != null) {
                String customerName = xdContacts.getCustomerName();
                String customerPhoner = xdContacts.getCustomerPhoner();
                resource.setCustomerPhone(customerPhoner);
                resource.setCustomerName(customerName);
            }
        }
        // V2 =========================================

    }

    private void setIsLossToday(XdResourcesBaseInfoDto resource) throws ParseException {
        Integer resourcesStatus = resource.getResourcesStatus();
        String channelCode = resource.getChannelCode();
        List<String> channelCodeArr = Arrays.asList(CHANNEL_CODE_ARR);
        if (channelCodeArr.contains(channelCode)) {
            // 待跟进1、跟进中2：
            if (resourcesStatus == ApiConstants.RESOURCE_STATE_CODE_1 || resourcesStatus == ApiConstants.RESOURCE_STATE_CODE_2) {
                Date time = resource.getAssignerTime();
                boolean bool = compareDate(time, ApiConstants.NOT_VISIT_LOSS_DAY);
                if (bool) {
                    resource.setIsTodayLoss(1);
                } else {
                    resource.setIsTodayLoss(0);
                }
            }
            // 已成单(X天)
            if (resourcesStatus == ApiConstants.RESOURCE_STATE_CODE_4 || resourcesStatus == ApiConstants.RESOURCE_STATE_CODE_5) {
                Date lastFollowTime = resource.getLastFollowTime();
                boolean bool = compareDate(lastFollowTime, ApiConstants.AFTER_ORDER_LOSS_DAY);
                if (bool) {
                    resource.setIsTodayLoss(1);
                } else {
                    resource.setIsTodayLoss(0);
                }

            }

            // 已上门：
            if (resourcesStatus == ApiConstants.RESOURCE_STATE_CODE_3) {
                Date lastVisitTime = resource.getLastVisitTime();
                boolean bool = compareDate(lastVisitTime, ApiConstants.VISITED_NOT_ORDER_LOSS_DAY);
                if (bool) {
                    resource.setIsTodayLoss(1);
                } else {
                    resource.setIsTodayLoss(0);
                }
            }
        }
    }

    private boolean compareDate(Date time, int dayNum) throws ParseException {
        if (time == null) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.DATE, dayNum);
        Date date = calendar.getTime();
        Date now = new Date();

        String beforeDay = simpleDateFormat.format(date);
        String currentDay = simpleDateFormat.format(now);
        Date before = simpleDateFormat.parse(beforeDay);
        Date current = simpleDateFormat.parse(currentDay);
        long time1 = before.getTime();
        long time2 = current.getTime();
        return time1 == time2;
    }

    /**
     * 资源剔除
     *
     * @param dto
     * @param token
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> removeResource(XdResourceOptDto dto, String token) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            String resourceId = dto.getResourceId();
            if (StringUtils.isBlank(resourceId)) {
                retMap.put("code", -1);
                retMap.put("msg", "资源id不能为空!");
                return retMap;
            }

            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            String phone = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phone);

            if (salesman == null) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }

            XdResourcesBase base = resourcesBaseMapper.selectByPrimaryKey(resourceId);
            if (base == null || base.getDelFlag() == 1) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常！");
                return retMap;
            }
            Integer status = base.getResourcesStatus();
            Integer delFlag = base.getDelFlag();
            // V2===================================
            String visitId = base.getCurrentVisitId();
            if (!StringUtils.isBlank(visitId)) {
                retMap.put("code", -1);
                retMap.put("msg", "该资源上门未结束，不能剔除！");
                return retMap;
            }
            // ===================================

            // 待跟进 跟进中 已上门
            int stateCode1 = ApiConstants.RESOURCE_STATE_CODE_1;
            int stateCode2 = ApiConstants.RESOURCE_STATE_CODE_2;
            int stateCode3 = ApiConstants.RESOURCE_STATE_CODE_3;
            int stateCode6 = ApiConstants.RESOURCE_STATE_CODE_6;
            int stateCode7 = ApiConstants.RESOURCE_STATE_CODE_7;

            if (stateCode1 != status && stateCode2 != status && stateCode3 != status) {
                retMap.put("code", -1);
                if (stateCode6 == status) {
                    retMap.put("msg", "资源已掉库！");
                } else if (stateCode7 == status && delFlag == 0) {
                    retMap.put("msg", "资源反无效审核中，不能剔除！");
                } else {
                    retMap.put("msg", "非法操作！");
                }
                return retMap;
            }

            XdResourcesBase resource = new XdResourcesBase();
            String salesmanId = salesman.getId();
            Date date = new Date();
            Date showDate = ResourceUtils.getJisuan(date);

//            resource.setResourcesId(resourceId);
//            resource.setLostType(ApiConstants.RESOURCE_REMOVE_CODE_0);
//            resource.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_6);
//            resource.setLostUserId(salesmanId);
//            resource.setLostTime(date);
//            resource.setLostDisplayTime(showDate);
//            resource.setLostState(0);
//            resource.setCommerceId(null);
//
//            resource.setUpdateUserId(salesmanId);
//            resource.setUpdateTime(date);

            base.setLostType(ApiConstants.RESOURCE_REMOVE_CODE_0);
            base.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_6);
            base.setLostUserId(salesmanId);
            base.setLostTime(date);
            base.setLostDisplayTime(showDate);
            base.setLostState(0);
//            base.setCommerceId(null);

            base.setUpdateUserId(salesmanId);
            base.setUpdateTime(date);
            resourcesBaseMapper.updateByPrimaryKey(base);

            // 记录
            XdResourceOperation xdResourceOperation = new XdResourceOperation();
            String optId = PrimaryKeyUtils.getId();
            String phoneNumber = salesman.getPhoneNumber();
            String salesmanName = salesman.getName();
            String resourcesId = base.getResourcesId();

            xdResourceOperation.setId(optId);
            xdResourceOperation.setCommerceId(salesmanId);
            xdResourceOperation.setResourcesId(resourcesId);
            xdResourceOperation.setOperatingTime(new Date());
            xdResourceOperation.setOperationRecords("剔除资源");
            xdResourceOperation.setOperatorName(salesmanName);
            xdResourceOperation.setOperatorLoginName(phoneNumber);
            xdResourceOperationMapper.insert(xdResourceOperation);

            retMap.put("code", 0);
            retMap.put("msg", "资源剔除成功！");

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "请求失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    /**
     * 返无效资源
     *
     * @param dto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> invalidResources(XdResourceOptDto dto, String token) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        try {
            String resourceId = dto.getResourceId();
            String reason = dto.getReason();
            if (StringUtils.isBlank(resourceId)) {
                retMap.put("code", -1);
                retMap.put("msg", "资源id不能为空!");
                return retMap;
            }
            if (StringUtils.isBlank(reason)) {
                retMap.put("code", -1);
                retMap.put("msg", "请输入反无效原因！!");
                return retMap;
            }
            int defaultRemarkLen = ApiConstants.DEFAULT_REMARK_LEN;
            if (reason.length() > defaultRemarkLen) {
                retMap.put("code", -1);
                retMap.put("msg", "反无效原因不能超过" + defaultRemarkLen + "字！!");
                return retMap;
            }

            XdResourcesBase resourcesBase = resourcesBaseMapper.selectByPrimaryKey(resourceId);
            if (resourcesBase == null || resourcesBase.getDelFlag() == 1) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常！");
                return retMap;
            }

            Integer status = resourcesBase.getResourcesStatus();
            Integer channel = resourcesBase.getResourceChannel();
            Integer delFlag = resourcesBase.getDelFlag();
            // V2===================================
            String visitId = resourcesBase.getCurrentVisitId();
            if (!StringUtils.isBlank(visitId)) {
                retMap.put("code", -1);
                retMap.put("msg", "该资源上门未结束，不能反无效！");
                return retMap;
            }
            // ===================================

            if (ApiConstants.RESOURCE_CHANNEL_1 == channel) {
                retMap.put("code", -1);
                retMap.put("msg", "抢单资源不能反无效！");
                return retMap;
            }

            // 待跟进 跟进中 已上门
            int stateCode1 = ApiConstants.RESOURCE_STATE_CODE_1;
            int stateCode2 = ApiConstants.RESOURCE_STATE_CODE_2;
            int stateCode3 = ApiConstants.RESOURCE_STATE_CODE_3;
            int stateCode4 = ApiConstants.RESOURCE_STATE_CODE_4;
            int stateCode5 = ApiConstants.RESOURCE_STATE_CODE_5;
            int stateCode6 = ApiConstants.RESOURCE_STATE_CODE_6;
            int stateCode7 = ApiConstants.RESOURCE_STATE_CODE_7;

            Integer invalidNum = resourcesBase.getInvalidNum();
            invalidNum = invalidNum == null ? 0 : invalidNum;
            int invalidCount = ApiConstants.INVALID_COUNT;
            if (invalidNum >= invalidCount) {
                retMap.put("code", -1);
                retMap.put("msg", "该资源已达反无效次数：2次！");
                return retMap;
            }

            if (stateCode1 != status && stateCode2 != status && stateCode3 != status) {
                retMap.put("code", -1);
                if (stateCode4 == status || stateCode5 == status) {
                    retMap.put("msg", "已成单资源，不能反无效！");
                } else if (stateCode7 == status && delFlag == 0) {
                    retMap.put("msg", "反无效审核中，请不要重复操作！");
                } else {
                    retMap.put("msg", "非法操作！");
                }
                return retMap;
            }

            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            String phone = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phone);
            if (salesman == null) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }

            XdResourcesBase resource = new XdResourcesBase();
            Date date = new Date();

            // 反无效来源：0跟进中，1已上门
            int invalidSource = 0;
            if (stateCode3 == status) {
                invalidSource = 1;
            }

            resource.setResourcesId(resourceId);
            resource.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_7);
            resource.setInvalidNum(++invalidNum);
            resource.setInvalidTime(date);
            resource.setReason(reason);
            resource.setUpdateUserId(salesman.getId());
            resource.setUpdateTime(date);
            resource.setInvalidSource(invalidSource);
            resourcesBaseMapper.updateByPrimaryKeySelective(resource);

            // 记录
            XdResourceOperation xdResourceOperation = new XdResourceOperation();
            String optId = PrimaryKeyUtils.getId();
            String salesmanName = salesman.getName();

            xdResourceOperation.setId(optId);
            xdResourceOperation.setCommerceId(salesman.getId());
            xdResourceOperation.setResourcesId(resource.getResourcesId());
            xdResourceOperation.setOperatingTime(date);
            xdResourceOperation.setOperatorLoginName(phone);
            xdResourceOperation.setOperatorName(salesmanName);
            xdResourceOperation.setOperationRecords(salesmanName + "返无效");
            xdResourceOperationMapper.insert(xdResourceOperation);

            retMap.put("code", 0);
            retMap.put("msg", "返无效资源成功！");
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "请求失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    /**
     * 通过资源id获取资源信息
     *
     * @param record
     * @param token
     * @return
     */
    @Override
    public Map<String, Object> queryResourcesBaseInfo(XdResourcesBase record, String token) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(record.getResourcesId())) {
                retMap.put("code", -1);
                retMap.put("msg", "资源id不能为空!");
                return retMap;
            }

            XdContacts contacts = new XdContacts();
            contacts.setResourcesId(record.getResourcesId());

            /**根据资源id获取联系人列表*/
            List<XdContacts> xdContacts = xdContactsMapper.selectContactsList(contacts);
            XdResourcesBaseOneDto base = resourcesBaseMapper.selectResourcesById(record.getResourcesId());

            if (null != base) {
                dataMap.put("xdContacts", xdContacts);
                dataMap.put("base", base);

                retMap.put("code", 0);
                retMap.put("msg", "获取信息成功!");
                retMap.put("data", dataMap);
            } else {
                retMap.put("code", -1);
                retMap.put("msg", "该资源信息有误，请联系工作人员！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", 1);
            retMap.put("msg", "请求失败");
        }
        return retMap;
    }

    @Transactional
    @Override
    public Map<String, Object> refuseResource(XdResourceOptDto xdResourceOptDto, String token) {
        Map<String, Object> retMap = new HashMap<>();

        int refuseCode = ApiConstants.REFUSE_CODE;
        String resourceId = xdResourceOptDto.getResourceId();
        int optType = xdResourceOptDto.getOptType();

        if (StringUtils.isBlank(resourceId)) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误：资源id为空！");
            return retMap;
        }
        if (optType != refuseCode) {
            retMap.put("code", -1);
            retMap.put("msg", "操作错误！");
            return retMap;
        }

        JedisCluster jedisCluster = null;
        try {

            jedisCluster = RedisFactory.getJedisCluster();
            String phoneNo = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            if (StringUtils.isNullOrEmpty(phoneNo)) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }
            XdSalesman xdSalesman = salesmanMapper.selectSalesmanByPhone(phoneNo);
            if (xdSalesman == null) {
                retMap.put("code", -1);
                retMap.put("msg", "用戶不存在！");
                return retMap;
            }

            // V2 ==================================
            XdResourcesBaseInfoDto baseInfoDto = this.resourcesBaseMapper.selectCurrentVisitResource(xdSalesman.getId());
            if (baseInfoDto != null) {
                retMap.put("code", -1);
                retMap.put("msg", "已有资源上们未结束，不能拒单！");
                return retMap;
            }
            // V2 ==================================

            XdResourcesBase base = resourcesBaseMapper.selectByPrimaryKey(resourceId);
            if (base == null || base.getDelFlag() == 1) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常！");
                return retMap;
            }
            Integer status = base.getResourcesStatus();
            int stateCode6 = ApiConstants.RESOURCE_STATE_CODE_6;

            if (status != ApiConstants.RESOURCE_STATE_CODE_8) {
                retMap.put("code", -1);
                if (stateCode6 == status) {
                    retMap.put("msg", "资源已掉库！");
                } else {
                    retMap.put("msg", "非法操作！");
                }
                return retMap;
            }

            String recordId = PrimaryKeyUtils.getId();
            String salesmanId = xdSalesman.getId();
            Integer reSum = xdSalesman.getReSum();

            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            String format = simpleDateFormat.format(calendar.getTime()) + " 00:00:00";
            String commerceId = salesmanId;
            int refuseCount = xdOrderRecordMapper.selectRefuseCountBySalesman(commerceId, format);
            if (refuseCount >= 2) {
                retMap.put("code", -1);
                retMap.put("msg", "本周已拒单两次！");
                return retMap;
            }*/

            int refuseCount = ApiConstants.REFUSE_COUNT;
            if (reSum >= refuseCount) {
                retMap.put("code", ApiConstants.REFUSE_RETURN_CODE);
                retMap.put("msg", "本周已拒单" + reSum + "次，不能拒单！");
                return retMap;
            }

            Date date = new Date();
            Date showTime = ResourceUtils.getJisuan(date);

            base.setLostTime(date);
            base.setResourcesStatus(stateCode6);
            base.setFirstUserId(salesmanId);
            base.setFirstTime(date);
            base.setLostType(ApiConstants.RESOURCE_REMOVE_CODE_2);
            base.setLostState(0);
            base.setLostDisplayTime(showTime);
//            base.setCommerceId(null);

            base.setNextFollowTime(showTime);
            base.setUpdateTime(date);
            base.setUpdateUserId(salesmanId);
            resourcesBaseMapper.updateByPrimaryKey(base);

            //修改该资源最后一条分配记录为接单状态
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("resourcesId", resourceId);
            params.put("nowTime", date);
            params.put("responseFlag", 3);//拒单
            int k = xdResourceDistributionMapper.updateResponseFlag(params);


            XdOrderRecord xdOrderRecord = new XdOrderRecord();
            xdOrderRecord.setId(recordId);
            xdOrderRecord.setResourcesId(resourceId);
            xdOrderRecord.setCommerceId(salesmanId);
            xdOrderRecord.setCreateUserId(salesmanId);
            xdOrderRecord.setCreateTime(date);
            xdOrderRecord.setDelFlag(0);
            xdOrderRecord.setIsType(Integer.valueOf(refuseCode));
            xdOrderRecordMapper.insert(xdOrderRecord);

            XdResourceOperation xdResourceOperation = new XdResourceOperation();
            String optId = PrimaryKeyUtils.getId();
            String salesmanName = xdSalesman.getName();
            String phoneNumber = xdSalesman.getPhoneNumber();

            xdResourceOperation.setId(optId);
            xdResourceOperation.setCommerceId(salesmanId);
            xdResourceOperation.setResourcesId(resourceId);
            xdResourceOperation.setOperatingTime(date);
            xdResourceOperation.setOperationRecords(salesmanName + "已拒单");
            xdResourceOperation.setOperatorName(salesmanName);
            xdResourceOperation.setOperatorLoginName(phoneNumber);
            xdResourceOperationMapper.insert(xdResourceOperation);

            xdSalesman.setReSum(++reSum);
            salesmanMapper.updateByPrimaryKeySelective(xdSalesman);

            //将分配记录响应状态改为拒单
            Map<String, Object> responseParams = new HashMap<String, Object>();
            responseParams.put("resourceId", resourceId);
            responseParams.put("salesmanId", salesmanId);
            responseParams.put("responseFlag", 3);//拒单
            //修改小顶分配记录为接单
            int j = xdResourceDistributionMapper.updateLastDistribution(responseParams);

            if (reSum < refuseCount) {
                int i = ApiConstants.REFUSE_COUNT - reSum;
                retMap.put("code", 0);
                retMap.put("msg", "拒单成功！本周剩余拒单次数：" + i + "次！");
            } else if (reSum >= refuseCount) {
                Calendar cal = Calendar.getInstance(Locale.CHINA);
                cal.add(Calendar.DAY_OF_MONTH, 7);
                Date dataAdd7 = cal.getTime();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

                String canRec = simpleDateFormat1.format(dataAdd7) + " 23:59:59";
                simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date canRecDate = simpleDateFormat1.parse(canRec);

                XdSalesman salesman = new XdSalesman();
                salesman.setId(salesmanId);
//                salesman.setIsDel(1);
                salesman.setCanorder(0);
                salesman.setCanorderTime(canRecDate);
                salesmanMapper.updateByPrimaryKeySelective(salesman);

                int i = ApiConstants.REFUSE_COUNT - reSum;
                retMap.put("code", 0);
                retMap.put("msg", "拒单成功！本周剩余拒单次数：" + i + "次！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(" -------------> ", e);
            retMap.put("code", -1);
            retMap.put("msg", "操作失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    @Transactional
    @Override
    public Map<String, Object> acceptResource(XdResourceOptDto xdResourceOptDto, String token) {
        Map<String, Object> retMap = new HashMap<>();
        int acceptCode = ApiConstants.ACCEPT_CODE;

        String resourceId = xdResourceOptDto.getResourceId();
        int optType = xdResourceOptDto.getOptType();
        if (StringUtils.isNullOrEmpty(resourceId) || StringUtils.isNullOrEmpty(resourceId.trim())) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误：资源id为空！");
            return retMap;
        }
        if (optType != acceptCode) {
            retMap.put("code", -1);
            retMap.put("msg", "非法请求！");
            return retMap;
        }

        try {
            JedisCluster cluster = RedisFactory.getJedisCluster();
            String phoneNo = cluster.get(ApiConstants.TOKEN_PHONE + token);
            if (StringUtils.isNullOrEmpty(phoneNo)) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }
            XdSalesman xdSalesman = salesmanMapper.selectSalesmanByPhone(phoneNo);
            if (xdSalesman == null) {
                retMap.put("code", -1);
                retMap.put("msg", "用户不存在！");
                return retMap;
            }
            Integer isDel = xdSalesman.getIsDel();
            Integer isOn = xdSalesman.getIsOn();
            Integer canOrder = xdSalesman.getCanorder();
            String gradeId = xdSalesman.getSalesmanGradeId();
            String salesmanId = xdSalesman.getId();
            String salesmanName = xdSalesman.getName();
            Integer reSum = xdSalesman.getReSum();

            if (isDel == 1 || isOn == 1 || canOrder == 0) {
                retMap.put("code", -1);
                retMap.put("msg", "用户禁用或已离职，不能接单！");
                return retMap;
            }

            if (reSum >= ApiConstants.REFUSE_COUNT) {
                retMap.put("code", -1);
                retMap.put("msg", "已拒单" + reSum + "次，不能接单！");
                return retMap;
            }
            // V2 start ==================================
            Integer onlineStatus = xdSalesman.getOnlineStatus();
            if (onlineStatus == ApiConstants.STATE_2) {
                retMap.put("code", -1);
                retMap.put("msg", "忙碌中不能接单！");
                return retMap;
            }
            XdResourcesBaseInfoDto baseInfoDto = this.resourcesBaseMapper.selectCurrentVisitResource(salesmanId);
            if (baseInfoDto != null) {
                retMap.put("code", -1);
                retMap.put("msg", "已有资源上们未结束，不能接单！");
                return retMap;
            }
            // V2 end ==================================
            XdSalesmanGrade xdSalesmanGrade = xdSalesmanGradeMapper.selectByPrimaryKey(gradeId);
            if (xdSalesmanGrade != null) {
                Integer monthOrderNum = xdSalesmanGrade.getMonthOrderNum();
                Integer storageNum = xdSalesmanGrade.getStorageNum();

                int acceptCount = xdOrderRecordMapper.selectAcceptCountByMonth(salesmanId);
                int pendingCount = resourcesBaseMapper.selectPendingCountByMonth(salesmanId);

                monthOrderNum = monthOrderNum == null ? 0 : monthOrderNum;
                storageNum = storageNum == null ? 0 : storageNum;

                if (acceptCount >= monthOrderNum) {
                    retMap.put("code", -1);
                    retMap.put("msg", "本月接单量已达上限，不能接单！");
                    return retMap;
                }

                if (pendingCount >= storageNum) {
                    retMap.put("code", -1);
                    retMap.put("msg", "待处理资源量已达上限，不能接单！");
                    return retMap;
                }

            } else {
                retMap.put("code", -1);
                retMap.put("msg", "用户暂未分配等级，不能接单！");
                return retMap;
            }

            XdResourcesBase resourcesBase = resourcesBaseMapper.selectByPrimaryKey(resourceId);
            if (resourcesBase == null || resourcesBase.getDelFlag() == 1) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常！");
                return retMap;
            }

            Integer status = resourcesBase.getResourcesStatus();
            int stateCode0 = ApiConstants.RESOURCE_STATE_CODE_0;

            if (status != ApiConstants.RESOURCE_STATE_CODE_8) {
                retMap.put("code", -1);
                if (stateCode0 == status) {
                    retMap.put("msg", "资源已掉库！");
                } else {
                    retMap.put("msg", "非法操作！");
                }
                return retMap;
            }

            // 到时未响应
            /*Date assignerTime = resourcesBase.getAssignerTime();
            Date reTime = resourcesBase.getEsReTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String retTimeStamp = simpleDateFormat.format(reTime);
            Long retTime = Long.valueOf(reTime.getTime());
            if (retTime < System.currentTimeMillis()) {
                retMap.put("code", -1);
                retMap.put("msg", "已超时，接单无效！");
                return retMap;
            }*/
            Map<String, Object> dbMap = new HashMap<>();
            dbMap.put("resourceId", resourceId);
            dbMap.put("salesmanId", salesmanId);

            int i = resourcesBaseMapper.selectBySalesmanAndState8(dbMap);
            if (i == 0) {
                retMap.put("code", -1);
                retMap.put("msg", "响应超时，不能接单！");
                return retMap;
            }

            Date date = new Date();
            // 接单拒单
            XdOrderRecord xdOrderRecord = new XdOrderRecord();
            String recordId = PrimaryKeyUtils.getId();

            xdOrderRecord.setId(recordId);
            xdOrderRecord.setResourcesId(resourceId);
            xdOrderRecord.setCommerceId(salesmanId);
            xdOrderRecord.setCreateUserId(salesmanId);
            xdOrderRecord.setCreateTime(date);
            xdOrderRecord.setDelFlag(0);
            xdOrderRecord.setIsType(Integer.valueOf(acceptCode));
            xdOrderRecordMapper.insert(xdOrderRecord);

            // 操作记录
            XdResourceOperation xdResourceOperation = new XdResourceOperation();
            String optId = PrimaryKeyUtils.getId();

            xdResourceOperation.setId(optId);
            xdResourceOperation.setCommerceId(salesmanId);
            xdResourceOperation.setResourcesId(resourceId);
            xdResourceOperation.setOperatingTime(date);
            xdResourceOperation.setOperationRecords(salesmanName + "已接单");
            xdResourceOperation.setOperatorName(salesmanName);
            xdResourceOperation.setOperatorLoginName(phoneNo);
            xdResourceOperationMapper.insert(xdResourceOperation);

            // 接单
            XdResourcesBase xdResourcesBase = new XdResourcesBase();
            xdResourcesBase.setResourcesId(resourceId);
            xdResourcesBase.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_1);
            xdResourcesBase.setFirstUserId(salesmanId);
            xdResourcesBase.setFirstTime(date);

            long nextTime = System.currentTimeMillis() + 30 * 60 * 1000;
            Date nextFollowTime = new Date(nextTime);
            xdResourcesBase.setNextFollowTime(nextFollowTime);
            xdResourcesBase.setFirstUserId(salesmanId);
            xdResourcesBase.setFirstTime(date);
            xdResourcesBase.setLostTime(nextFollowTime);

            resourcesBaseMapper.updateByPrimaryKeySelective(xdResourcesBase);

            //修改该资源最后一条分配记录为接单状态
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("resourcesId", resourceId);
            params.put("nowTime", date);
            params.put("responseFlag", 1);//未响应
            int k = xdResourceDistributionMapper.updateResponseFlag(params);


            //将分配记录的响应状态该为接单
            Map<String, Object> responseParams = new HashMap<String, Object>();
            responseParams.put("resourceId", resourceId);
            responseParams.put("salesmanId", salesmanId);
            responseParams.put("responseFlag", 1);//接单
            //修改小顶分配记录为以接单
            int j = xdResourceDistributionMapper.updateLastDistribution(responseParams);


            retMap.put("code", 0);
            retMap.put("msg", "接单成功！");

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "接单失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    @Transactional
    @Override
    public synchronized Map<String, Object> scrambleResource(XdResourceOptDto xdResourceOptDto, String token) {
        Map<String, Object> retMap = new HashMap<>();
        int scrambleCode = ApiConstants.SCRAMBLE_CODE;

        String resourceId = xdResourceOptDto.getResourceId();
        int optType = xdResourceOptDto.getOptType();
        if (StringUtils.isNullOrEmpty(resourceId) || StringUtils.isNullOrEmpty(resourceId.trim())) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误：资源id为空！");
            return retMap;
        }
        if (optType != scrambleCode) {
            retMap.put("code", -1);
            retMap.put("msg", "非法操作！");
            return retMap;
        }

        try {
            String phoneNo = getPhoneNoByToken(token);
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phoneNo);
            if (salesman == null) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }
            if (salesman.getOnlineStatus() == ApiConstants.STATE_0) {
                retMap.put("code", -1);
                retMap.put("msg", "离线状态不能抢单！");
                return retMap;
            }

            String id1 = salesman.getId();
            String gradeId = salesman.getSalesmanGradeId();
            Integer reSum = salesman.getReSum();
            if (reSum >= ApiConstants.REFUSE_COUNT) {
                retMap.put("code", -1);
                retMap.put("msg", "已拒单" + reSum + "次，不能抢单！");
                return retMap;
            }

            XdSalesmanGrade xdSalesmanGrade = xdSalesmanGradeMapper.selectByPrimaryKey(gradeId);
            if (xdSalesmanGrade != null) {
                Integer monthOrderNum = xdSalesmanGrade.getMonthOrderNum();
                Integer storageNum = xdSalesmanGrade.getStorageNum();

                int acceptCount = xdOrderRecordMapper.selectAcceptCountByMonth(id1);
                int pendingCount = resourcesBaseMapper.selectPendingCountByMonth(id1);

                monthOrderNum = monthOrderNum == null ? 0 : monthOrderNum;
                storageNum = storageNum == null ? 0 : storageNum;

                if (acceptCount >= monthOrderNum) {
                    retMap.put("code", -1);
                    retMap.put("msg", "本月接单量已达上限，不能抢单！");
                    return retMap;
                }

                if (pendingCount >= storageNum) {
                    retMap.put("code", -1);
                    retMap.put("msg", "待处理资源量已达上限，不能抢单！");
                    return retMap;
                }

            } else {
                retMap.put("code", -1);
                retMap.put("msg", "用户暂未分配等级，不能接单！");
                return retMap;
            }

            Integer isDel = salesman.getIsDel();
            Integer isOn = salesman.getIsOn();
            Integer canOrder = salesman.getCanorder();

            if (isOn == ApiConstants.SALESMAN_DISENABLE) {
                retMap.put("code", -1);
                retMap.put("msg", "商务已离职，不能抢单！");
                return retMap;
            }
            if (isDel == ApiConstants.SALESMAN_DISENABLE) {
                retMap.put("code", -1);
                retMap.put("msg", "商务已禁用，不能抢单！");
                return retMap;
            }
            if (canOrder == 0) {
                retMap.put("code", -1);
                retMap.put("msg", "商务已禁用，不能抢单！");
                return retMap;
            }
            XdResourcesBase resourcesBase = resourcesBaseMapper.selectByPrimaryKey(resourceId);
            if (resourcesBase == null) {
                retMap.put("code", -1);
                retMap.put("msg", "资源不存在或已被抢单！");
                return retMap;
            }

            Integer status = resourcesBase.getResourcesStatus();
            if (ApiConstants.RESOURCE_STATE_CODE_6 != status) {
                retMap.put("code", -1);
                retMap.put("msg", "资源不存在或已被抢单！");
                return retMap;
            }

            String salesmanId = salesman.getId();
            Date date = new Date();

            // 抢单
            int channel1 = ApiConstants.RESOURCE_CHANNEL_1;
            resourcesBase.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_1);
            resourcesBase.setResourceChannel(channel1);
            resourcesBase.setFirstUserId(salesmanId);
            resourcesBase.setFirstTime(date);
            resourcesBase.setCommerceId(salesmanId);
            resourcesBase.setUpdateTime(date);
            resourcesBase.setUpdateUserId(salesmanId);
            resourcesBase.setResourcesStatus(ApiConstants.RESOURCE_STATE_CODE_1);
            long nextTime = System.currentTimeMillis() + 30 * 60 * 1000;
            Date nextFollowTime = new Date(nextTime);
            resourcesBase.setNextFollowTime(nextFollowTime);

            resourcesBaseMapper.updateByPrimaryKeySelective(resourcesBase);

            // 记录
            XdOrderRecord xdOrderRecord = new XdOrderRecord();
            String id = PrimaryKeyUtils.getId();

            xdOrderRecord.setId(id);
            xdOrderRecord.setDelFlag(0);
            xdOrderRecord.setIsType(ApiConstants.RECORD_TYPE_4);
            xdOrderRecord.setCreateUserId(salesmanId);
            xdOrderRecord.setCreateTime(date);
            xdOrderRecord.setCommerceId(salesmanId);
            xdOrderRecord.setResourcesId(resourceId);
            xdOrderRecordMapper.insert(xdOrderRecord);

            XdResourceOperation operation = new XdResourceOperation();
            String optId = PrimaryKeyUtils.getId();
            String salesmanName = salesman.getName();
            String phoneNumber = salesman.getPhoneNumber();

            operation.setId(optId);
            operation.setOperatorName(salesmanName);
            operation.setOperatorLoginName(phoneNumber);
            operation.setResourcesId(resourceId);
            operation.setOperationRecords(salesmanName + "已抢单");
            operation.setCommerceId(salesmanId);
            operation.setOperatingTime(date);
            xdResourceOperationMapper.insert(operation);

            retMap.put("code", 0);
            retMap.put("mqg", "抢单成功！");
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "抢单失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    @Transactional
    @Override
    public Map<String, Object> hideResource(XdResourceOptDto xdResourceOptDto, String token) {
        Map<String, Object> retMap = new HashMap<>();
        int hideCode = ApiConstants.HIDE_CODE;

        String resourceId = xdResourceOptDto.getResourceId();
        int optType = xdResourceOptDto.getOptType();
        if (StringUtils.isNullOrEmpty(resourceId) || StringUtils.isNullOrEmpty(resourceId.trim())) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误：资源id为空！");
            return retMap;
        }
        if (optType != hideCode) {
            retMap.put("code", -1);
            retMap.put("msg", "操作错误！");
            return retMap;
        }

        try {
            String phoneNo = getPhoneNoByToken(token);
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phoneNo);
            if (salesman == null) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }

            XdResourcesBase base = resourcesBaseMapper.selectByPrimaryKey(resourceId);
            if (base == null || base.getDelFlag() == 1) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常！");
                return retMap;
            }
            Integer status = base.getResourcesStatus();
            if (ApiConstants.RESOURCE_STATE_CODE_6 != status) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常或已被抢单！");
                return retMap;
            }

            XdIsDisplay xdIsDisplay = new XdIsDisplay();

            String id = PrimaryKeyUtils.getId();
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.add(Calendar.MONTH, 1);
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(date) + " 23:59:59";
            Date endTime = simpleDateFormat.parse(format);

            String salesmanId = salesman.getId();

            List<XdIsDisplay> xdIsDisplayList = xdIsDisplayMapper.selectBySalesmanIdAndResourceId(salesmanId, resourceId);
            if (xdIsDisplayList != null && xdIsDisplayList.size() > 0) {
                XdIsDisplay display = xdIsDisplayList.get(0);
                if (display != null) {
                    display.setEndTime(endTime);
                    xdIsDisplayMapper.updateByPrimaryKeySelective(display);
                }
            } else {
                xdIsDisplay.setId(id);
                xdIsDisplay.setCommerceId(salesmanId);
                xdIsDisplay.setResourcesId(resourceId);
                xdIsDisplay.setEndTime(endTime);

                xdIsDisplayMapper.insert(xdIsDisplay);
            }
            retMap.put("code", 0);
            retMap.put("msg", "操作成功！");

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "操作失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return retMap;
    }

    /**
     * 获取订单状态
     *
     * @return
     */
    @Override
    public Map<String, Object> queryStatus() {
        Map<String, Object> retMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            /**获取订单状态*/
            List<XdResourcesStatus> baseStatus = xdResourcesStatusMapper.selectStatus();

            /**获取所有二级业态*/
            List<XdBusinessDto> xdBusiness = xdBusinessMapper.selectXdBusiness();

            data.put("code", 0);
            data.put("msg", "获取资源状态成功！");
            retMap.put("baseStatus", baseStatus);
            retMap.put("xdBusiness", xdBusiness);
            data.put("data", retMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "请求失败！");
            return retMap;
        }
        return data;
    }

    @Transactional
    @Override
    public Map<String, Object> changeMainContact(XdContactDto dto, String token) {
        Map<String, Object> retMap = new HashMap<>();

        String contactId = dto.getContactId();
        if (StringUtils.isBlank(contactId)) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误：联系人id为空！");
            return retMap;
        }

        XdContacts xdContacts = new XdContacts();
        xdContacts.setContactsId(contactId);
        try {
            String phoneNo = RedisUtils.get(ApiConstants.TOKEN_PHONE + token);
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phoneNo);
            if (salesman == null) {
                retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
                retMap.put("msg", "登录异常，请重新登录！");
                return retMap;
            }
            XdContacts contacts = xdContactsMapper.selectByPrimaryKey(contactId);
            if (contacts == null) {
                retMap.put("code", -1);
                retMap.put("msg", "数据异常！");
                return retMap;
            }
            String resourcesId = contacts.getResourcesId();
            XdResourcesBase resourcesBase = resourcesBaseMapper.selectByPrimaryKey(resourcesId);
            if (resourcesBase == null || resourcesBase.getDelFlag() == 1) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常！");
                return retMap;
            }
            Integer status = resourcesBase.getResourcesStatus();
            Integer delFlag = resourcesBase.getDelFlag();

            int stateCode7 = ApiConstants.RESOURCE_STATE_CODE_7;
            if (stateCode7 == status && delFlag == 0) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态反无效审核中，不能操作！");
                return retMap;
            }

            String salesmanId = salesman.getId();
            xdContacts.setUpdateUserId(salesmanId);
            xdContacts.setUpdateTime(new Date());
            xdContacts.setIsMainCon(1);

            xdContactsMapper.updateByPrimaryKeySelective(xdContacts);
            xdContactsMapper.updateMainContactByResource(contactId);
            retMap.put("code", 0);
            retMap.put("msg", "设置成功！");
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "设置失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    @Override
    public Map<String, Object> selfDevelopment(XdResourceDto dto, String token) {
        try {
            //获取商务自开发资源来源
            XdResourceSource xdResourceSource = xdResourceSourceMapper.selectByCode("swzkf");
            if (xdResourceSource == null) {
                return RestResponseUtil.getFailResult("当前关闭了商务自开发资源录入接口");
            }
            //字段验证
            Map<String, Object> map = validateResource(dto);
            if (map != null) {
                return map;
            }
            //新建商机时判断电话号码
            List<XdResourcesBase> bases = resourcesBaseMapper.selectByPhoneAndTypeId(dto.getCustomerPhone(), dto.getOperationTypeId());
            boolean repeat = false;
            for (XdResourcesBase base : bases) {
                if (base.getResourcesStatus() != 4 && base.getResourcesStatus() != 5 && base.getResourcesStatus()!=10) {
                    repeat = true;
                    break;
                }
            }
            if (repeat) {
                return RestResponseUtil.getFailResult("存在未成单的相同手机号相同业态的资源，新增失败");
            }
            Date date = new Date();
            XdResourcesBase resourcesBase = new XdResourcesBase();
            resourcesBase.setCustomerName(dto.getCustomerName());
            resourcesBase.setCustomerPhone(dto.getCustomerPhone());
            resourcesBase.setOperationTypeId(dto.getOperationTypeId());
            resourcesBase.setBigBizId(dto.getBigBizId());
            resourcesBase.setResSourceId(dto.getResSourceId());
            if (dto.getCustomerSex() == null) {
                resourcesBase.setCustomerSex(0);
            } else if (dto.getCustomerSex() == 0 || dto.getCustomerSex() == 1 || dto.getCustomerSex() == 2) {
                resourcesBase.setCustomerSex(dto.getCustomerSex());
            } else {
                resourcesBase.setCustomerSex(0);
            }
            resourcesBase.setIsCost(dto.getIsCost());
            //二期修改成把渠道资费当成资源费用的默认值，20180620
            resourcesBase.setResourcesCost(xdResourceSource.getSourcePrice());
            resourcesBase.setResSourceId(xdResourceSource.getId());
            resourcesBase.setProvince(dto.getProvince());
            resourcesBase.setVisitCity(dto.getVisitCity());
            resourcesBase.setVisitAddress(dto.getVisitAddress());
            if (dto.getSubscribeTime() != null) {
                resourcesBase.setSubscribeTime(new Date(dto.getSubscribeTime()));
            }
            resourcesBase.setMessage(dto.getMessage());
            resourcesBase.setLongitude(dto.getLongitude());
            resourcesBase.setLatitude(dto.getLatitude());
            resourcesBase.setResourcesStatus(0);
            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            String phone = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phone);
            if (salesman == null) {
                return RestResponseUtil.getFailResult("登录异常，请重新登录！");
            }
            resourcesBase.setUpdateTime(date);
            resourcesBase.setUpdateUserId(salesman.getId());
            resourcesBase.setResourcesId(PrimaryKeyUtils.getId());

            //新增资源
            //生成商机号
            resourcesBase.setBusinessCode(createBusinessCode());
            //设置资源来源为后台录入
            resourcesBase.setChannelSource(0);
            resourcesBase.setAutoDisNum(0);
            resourcesBase.setCreateUserId(salesman.getId());
            resourcesBase.setCreateTime(date);
            //判断这个商务是否满足 加单条件
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("commerceId", salesman.getId());
            param.put("nowTime", date);
            Map<String, Object> xdSalesManInfo = salesmanMapper.getAcceptInfo(param);
            //是否可接单，包括库容量的判断。//未完成
            if (xdSalesManInfo == null) {
                return RestResponseUtil.getFailResult("分配的商务不存在或不可以接单");
            }
            int storageNum = (int) xdSalesManInfo.get("storageNum");
            int monthOrderNum = (int) xdSalesManInfo.get("monthOrderNum");
            Long useNum = (Long) xdSalesManInfo.get("useNum");
            Long acceptNum = (Long) xdSalesManInfo.get("acceptNum");
            if (useNum == null) {
                storageNum = storageNum;
            } else {
                storageNum = (int) (storageNum - useNum);
            }
            if (acceptNum == null) {
                monthOrderNum = monthOrderNum;
            } else {
                monthOrderNum = (int) (monthOrderNum - acceptNum);
            }
            if (storageNum < 1) {
                return RestResponseUtil.getFailResult("商务库容量不足，新增失败");
            }
            if (monthOrderNum < 1) {
                return RestResponseUtil.getFailResult("商务月接单数已达最大值，新增失败");
            }

/*            resourcesBase.setAssignerUserId(user.getUserId());
            //二期新增20190620，为资源基础表添加分配人姓名和分配人工号信息
            resourcesBase.setAssignerUserName(user.getRemark());
            resourcesBase.setAssignerUserWorkno(user.getEmployeeNo());
            resourcesBase.setAssignerTime(date);*/

            resourcesBase.setCommerceId(salesman.getId());

            //状态为待跟进
            resourcesBase.setResourcesStatus(1);
            //是否接收了语音
            resourcesBase.setIsPush(0);
            resourcesBase.setNextFollowTime(new Date(date.getTime() + 1000 * 60 * 30));//设置30分钟后不操作调库
            // 接单时间
            resourcesBase.setFirstTime(date);
            resourcesBaseMapper.insertSelective(resourcesBase);
            //添加新增资源操作记录
            String content = "创建了商务自开发资源";
            //插入操作记录
            XdResourceOperation oper = new XdResourceOperation();
            oper.setId(PrimaryKeyUtils.getId());
            oper.setResourcesId(resourcesBase.getResourcesId());
            oper.setOperatorName(salesman.getName());
            oper.setOperatorLoginName(salesman.getPhoneNumber());
            oper.setOperationRecords(content);
            oper.setCommerceId(salesman.getId());
            oper.setOperatingTime(date);

            xdResourceOperationMapper.insert(oper);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("resourcesId", resourcesBase.getResourcesId());
            return RestResponseUtil.getSuccessResult(data);
        } catch (Exception e) {
            log.error("selfDevelopment error", e);
            return RestResponseUtil.getFailResult("系统执行异常");
        }

    }


    /**
     * 参数验证
     *
     * @param dto
     * @return
     */
    private Map<String, Object> validateResource(XdResourceDto dto) {
        if (StringUtils.isBlank(dto.getCustomerName())) {
            return RestResponseUtil.getFailResult("客户名称不可以为空");
        }
        if (dto.getCustomerName().length() > 10) {
            return RestResponseUtil.getFailResult("客户名称不可超过十个字符");
        }
        if (!isPhoneNumber(dto.getCustomerPhone())) {
            return RestResponseUtil.getFailResult("客户电话格式不正确");
        }
        if (StringUtils.isBlank(dto.getOperationTypeId())) {
            return RestResponseUtil.getFailResult("二级业态不可以为空");
        }
        if (StringUtils.isBlank(dto.getBigBizId())) {
            return RestResponseUtil.getFailResult("一级业态不可以为空");
        }

        if (StringUtils.isBlank(dto.getProvince())) {
            return RestResponseUtil.getFailResult("省份不可为空");
        }
        if (StringUtils.isBlank(dto.getVisitCity())) {
            return RestResponseUtil.getFailResult("城市不可为空");
        }
        if (dto.getVisitAddress() != null && dto.getVisitAddress().length() > 50) {
            return RestResponseUtil.getFailResult("详细地址不可以超过五十个字");
        }
        if (dto.getMessage() != null && dto.getMessage().length() > 200) {
            return RestResponseUtil.getFailResult("客户留言不可超过两百个字");
        }

        if (dto.getLatitude() == null) {
            return RestResponseUtil.getFailResult("纬度不可以为空");
        } else {
            double lat = 0;
            try {
                lat = Double.valueOf(dto.getLatitude());
            } catch (NumberFormatException e) {
                return RestResponseUtil.getFailResult("纬度的值不合法，无法转化");
            }
            if (-90 > lat || 90 < lat) {
                return RestResponseUtil.getFailResult("纬度的值不合法");
            }
        }

        if (dto.getLongitude() == null) {
            return RestResponseUtil.getFailResult("经度不可以为空");
        } else {
            double lon = 0;
            try {
                lon = Double.valueOf(dto.getLongitude());
            } catch (NumberFormatException e) {
                return RestResponseUtil.getFailResult("经度的值不合法，无法转化");
            }
            if (-180 > lon || 180 < lon) {
                return RestResponseUtil.getFailResult("经度的值不合法");
            }
        }

        //需要判断operationTypeId对应的业态是否存在。不存在需要提示
        XdBusiness xdBusiness = xdBusinessMapper.selectByPrimaryKey(dto.getOperationTypeId());
        if (xdBusiness == null || xdBusiness.getDelFlag() == 1) {
            return RestResponseUtil.getFailResult("二级业态不存在");
        }
        XdBusiness bigBusiness = xdBusinessMapper.selectByPrimaryKey(dto.getBigBizId());
        if (bigBusiness == null || bigBusiness.getDelFlag() == 1) {
            return RestResponseUtil.getFailResult("一级业态不存在");
        }

        if (xdBusiness.getParentId() == null || !xdBusiness.getParentId().equals(bigBusiness.getId())) {
            return RestResponseUtil.getFailResult("一二级业态不匹配");
        }

        //需要判断省市的关系是否正确，不存在或者不正确需要提示
        ConRegion conReion = conRegionMapper.selectByIdAndPid(dto.getVisitCity(), dto.getProvince());
        if (conReion == null) {
            return RestResponseUtil.getFailResult("省市关系不正确");
        }

        return null;
    }

    private boolean isPhoneNumber(String phoneNumStr) {
        if (phoneNumStr == null) {
            return false;
        }
        Pattern p = Pattern.compile("^[0-9]{11,12}$");
        Matcher m = p.matcher(phoneNumStr);
        return m.matches();
    }

    /**
     * 生成商机号,最后的商机号为商机号前缀+年月日+单日商机计数（计算的最大值可以是7位数）
     *
     * @return
     */
    private String createBusinessCode() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(date);
        String counterKey = BUSINESS_CODE_PREFIX + dateStr;

        JedisCluster jedisCluster = RedisFactory.getJedisCluster();
        long orderCount = jedisCluster.incr(counterKey);

        //key设置为两天失效
        if (orderCount == 1) {
            jedisCluster.expire(counterKey, 3600 * 24 * 2);
        }
        String orderCountStr = String.valueOf(orderCount);
        int length = orderCountStr.length();
        if (length < 7) {
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < (7 - length); i++) {
                sb.append("0");
            }
            orderCountStr = sb + orderCountStr;
        }
        String businessCode = counterKey + orderCountStr;

        return businessCode;
    }

    public String getPhoneNoByToken(String token) {
        JedisCluster jedisCluster = null;
        String phoneNO = "";
        try {
            jedisCluster = RedisFactory.getJedisCluster();
            phoneNO = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNO;
    }

    private String getCurrentYearMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = month < 10 ? "0" + month : month + "";
        return "" + year + monthStr;
    }


    public static void main(String[] args) {

        String s = RedisUtils.get(ApiConstants.PHONE_TOKEN + "18380413398");
        System.out.println(s);


    }
}
