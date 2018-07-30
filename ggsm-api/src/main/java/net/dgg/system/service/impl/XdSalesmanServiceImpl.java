package net.dgg.system.service.impl;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dao.*;
import net.dgg.cloud.dto.XdSalesmanDto;
import net.dgg.cloud.entity.XdOnTime;
import net.dgg.cloud.entity.XdResourcesBase;
import net.dgg.cloud.entity.XdSalesman;
import net.dgg.cloud.entity.XdSalesmanGrade;
import net.dgg.framework.redis.RedisFactory;
import net.dgg.framework.utils.*;
import net.dgg.system.dto.LoginDto;
import net.dgg.system.service.XdSalesmanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ytz
 * @date 2018/4/28.
 * @desc
 */
@Service
public class XdSalesmanServiceImpl implements XdSalesmanService {

    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private XdSalesmanMapper xdSalesmanMapper;

    @Resource
    private XdOnTimeMapper xdOnTimeMapper;

    @Resource
    private XdOrderRecordMapper xdOrderRecordMapper;

    @Resource
    private XdResourceVisitMapper xdResourceVisitMapper;

    @Resource
    private CallRecordMapper callRecordMapper;

    @Resource
    private XdSalesmanGradeMapper xdSalesmanGradeMapper;

    @Resource
    private XdCallNumMapper xdCallNumMapper;

    @Resource
    private XdResourcesBaseMapper xdResourcesBaseMapper;

    @Override
    public String getToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getCode() {
        return getMsgCode(ApiConstants.DEFAULT_MSG_CODE_LEN);
    }

    @Override
    public Map<String, Object> login(LoginDto loginDto) {
        Map<String, Object> retMap = new HashMap<>();
        String phoneNo = loginDto.getPhoneNo();
        String msgCode = loginDto.getMsgCode();

        if (StringUtils.isBlank(phoneNo)) {
            retMap.put("code", -1);
            retMap.put("msg", "请输入手机号！");
            return retMap;
        }
        if (phoneNo.length() != 11) {
            retMap.put("code", -1);
            retMap.put("msg", "手机格式不正确！");
            return retMap;
        }
        if (StringUtils.isBlank(msgCode)) {
            retMap.put("code", -1);
            retMap.put("msg", "请输入验证码！");
            return retMap;
        }

        JedisCluster cluster = null;
        String msgCodeInRedis = null;
        XdSalesman xdSalesman = null;
        int loginErrorCode = ApiConstants.LOGIN_ERROR_CODE;
        try {
            cluster = RedisFactory.getJedisCluster();
            msgCodeInRedis = cluster.get(ApiConstants.MSG_CODE_PREFIX + phoneNo);
            if (StringUtils.isNullOrEmpty(msgCodeInRedis)) {
                retMap.put("code", -1);
                retMap.put("msg", "验证码失效，请重新获取！");
                return retMap;
            }
            if (!msgCode.equals(msgCodeInRedis)) {
                retMap.put("code", -1);
                retMap.put("msg", "验证码错误，请重新输入！");
                return retMap;
            }
            xdSalesman = xdSalesmanMapper.selectSalesmanByPhone(phoneNo);
            if (xdSalesman == null) {
                retMap.put("code", -1);
                retMap.put("msg", "用户不存在！");
                return retMap;
            }
            Integer isOn = xdSalesman.getIsOn();
            if (isOn != null && isOn == ApiConstants.SALESMAN_DISENABLE) {
                retMap.put("code", -1);
                retMap.put("msg", "已离职，禁止登录！");
                return retMap;
            }

            String gradeId = xdSalesman.getSalesmanGradeId();
            XdSalesmanGrade grade = xdSalesmanGradeMapper.selectByPrimaryKey(gradeId);
            String gradeName = grade.getName();
            String gradePicUrl = grade.getGradePicUrl();

            Map<String, Object> dataMap = new HashMap<>();
            String salesmanId = xdSalesman.getId();
            Integer isDel = xdSalesman.getIsDel();
            String salesmanName = xdSalesman.getName();
            String phoneNumber = xdSalesman.getPhoneNumber();
            Integer canorder = xdSalesman.getCanorder();
            Integer onlineStatus = xdSalesman.getOnlineStatus();
            String xNumber = xdSalesman.getxNumber();
            String specialty = xdSalesman.getSpecialty();

            /*if (onlineStatus == ApiConstants.STATE_1 || onlineStatus == ApiConstants.STATE_2) {
                xdSalesman.setOnlineStatus(ApiConstants.STATE_0);
                onlineStatus = ApiConstants.STATE_0;
            }*/

            // V2 start =====================================

            int currentState = onlineStatus;
            if (onlineStatus == ApiConstants.STATE_1) {
                xdSalesman.setOnlineStatus(ApiConstants.STATE_0);
                onlineStatus = ApiConstants.STATE_0;
            }
            List<XdResourcesBase> list = xdResourcesBaseMapper.selectVisitingResource(salesmanId);
            boolean bool = onlineStatus == ApiConstants.STATE_2 && (list != null && list.size() > 0);
            if (bool) {
                onlineStatus = ApiConstants.STATE_2;
            } else {
                xdSalesman.setOnlineStatus(ApiConstants.STATE_0);
                onlineStatus = ApiConstants.STATE_0;
            }
            // V2 end =====================================

            xdSalesmanMapper.updateByPrimaryKey(xdSalesman);
            if (currentState != ApiConstants.STATE_2) {
                XdOnTime xdOnTime = xdOnTimeMapper.selectLastOnTime(salesmanId);
                if (xdOnTime != null) {
                    Date onTime = xdOnTime.getOnTime();
                    Date date = new Date();

                    long curMillis = date.getTime();
                    long timeLong = (curMillis - onTime.getTime()) / 1000 / 60;
                    timeLong = timeLong < 0 ? 0 : timeLong;

                    xdOnTime.setOffTime(date);
                    xdOnTime.setTimeLength(timeLong);
                    xdOnTimeMapper.updateByPrimaryKeySelective(xdOnTime);
                }

            }
            dataMap.put("userId", salesmanId);
            dataMap.put("name", salesmanName);
            dataMap.put("phoneNumber", phoneNumber);
            dataMap.put("city", xdSalesman.getCity());
            dataMap.put("isOn", isOn);
            dataMap.put("isDel", isDel);
            dataMap.put("canorder", canorder);
            dataMap.put("onlineStatus", onlineStatus);
            dataMap.put("xNumber", xNumber);
            dataMap.put("specialty", specialty);
            dataMap.put("salesmanGradeId", gradeId);
            dataMap.put("gradeName", gradeName);
            if (!StringUtils.isBlank(gradePicUrl)) {
                String HOST = getHeadHost();
                dataMap.put("gradePicUrl", HOST + gradePicUrl);
            } else {
                dataMap.put("gradePicUrl", "");
            }
            String token = getToken();
            String tokenPhoneKey = ApiConstants.TOKEN_PHONE;
            String phoneTokenKey = ApiConstants.PHONE_TOKEN;

            // 验证码
            String msgCodeKey = ApiConstants.MSG_CODE_PREFIX + phoneNo;
            RedisUtils.del(msgCodeKey);

            // token
            cluster.set(tokenPhoneKey + token, phoneNo);
            String oldToken = cluster.get(phoneTokenKey + phoneNo);
            log.info("before: " + cluster.get(tokenPhoneKey + oldToken));
            if (!StringUtils.isBlank(oldToken)) {
                cluster.del(tokenPhoneKey + oldToken);
            }
            log.info("after: " + cluster.get(tokenPhoneKey + oldToken));
            cluster.set(phoneTokenKey + phoneNo, token);
            log.info("setting: " + cluster.get(tokenPhoneKey + token));
            dataMap.put("token", token);

//            XdOnTime xdOnTime = new XdOnTime();
//            String id = PrimaryKeyUtils.getId();
//
//            xdOnTime.setOnId(id);
//            xdOnTime.setOnTime(new Date());
//            xdOnTime.setSalesmanId(salesmanId);
//            xdOnTimeMapper.insert(xdOnTime);

            retMap.put("code", 0);
            retMap.put("msg", "登录成功！");
            retMap.put("data", dataMap);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            retMap.put("code", loginErrorCode);
            retMap.put("msg", "系统错误，请稍后重试！");
            return retMap;
        }

        return retMap;
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

    @Override
    public Map<String, Object> signOut(String token) {
        Map<String, Object> retMap = new HashMap<>();
        try {

            String tokenPhoneKey = ApiConstants.TOKEN_PHONE;
            String phoneTokenKey = ApiConstants.PHONE_TOKEN;

            String phoneNo = RedisUtils.get(tokenPhoneKey + token);
            String tokenInRedis = RedisUtils.get(phoneTokenKey + phoneNo);

            RedisUtils.del(phoneTokenKey + phoneNo);
            RedisUtils.del(tokenPhoneKey + token);
            log.info("token: " + RedisUtils.get(phoneTokenKey + phoneNo));
            log.info("phoneNo: " + RedisUtils.get(tokenPhoneKey + token));

            XdSalesman salesman = xdSalesmanMapper.selectSalesmanByPhone(phoneNo);
            if (salesman == null) {
                retMap.put("code", -1);
                retMap.put("msg", "用户不存在");
                return retMap;
            }
            String salesmanId = salesman.getId();

            Integer status = salesman.getOnlineStatus();
            int state2 = 2;
            if (status != state2) {

                XdOnTime xdOnTime = xdOnTimeMapper.selectLastOnTime(salesmanId);
                int state0 = ApiConstants.STATE_0;
                if (xdOnTime != null) {
                    Date offTime = xdOnTime.getOffTime();
                    Integer onlineStatus = salesman.getOnlineStatus();

//                if (offTime == null && onlineStatus != state0) {
                    Date onTime = xdOnTime.getOnTime();
                    Date date = new Date();

                    long curMillis = date.getTime();
                    long timeLong = (curMillis - onTime.getTime()) / (1000 * 60);
                    timeLong = timeLong < 0 ? 0 : timeLong;

                    xdOnTime.setOffTime(date);
                    xdOnTime.setTimeLength(timeLong);
                    xdOnTimeMapper.updateByPrimaryKeySelective(xdOnTime);
//                }
                }

                salesman.setOnlineStatus(state0);
                xdSalesmanMapper.updateByPrimaryKey(salesman);
            }
            retMap.put("code", 0);
            retMap.put("msg", "登出成功！");
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "登出失败！");
        }
        return retMap;
    }

    @Override
    public Map<String, Object> getSalesmanStatistics(XdSalesmanDto dto, String token) {
        Map<String, Object> retMap = new HashMap<>();
        String salesmanId = dto.getSalesmanId();
        if (StringUtils.isBlank(token)) {
            retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
            retMap.put("msg", "登录异常，请重新登录！");
            return retMap;
        }

        XdSalesman salesman = null;
        if (StringUtils.isBlank(salesmanId)) {
            JedisCluster cluster = RedisFactory.getJedisCluster();
            String phoneNo = cluster.get(ApiConstants.TOKEN_PHONE + token);
            salesman = xdSalesmanMapper.selectSalesmanByPhone(phoneNo);
            if (salesman != null) {
                salesmanId = salesman.getId();
            }
        } else {
            salesman = xdSalesmanMapper.selectByPrimaryKey(salesmanId);
        }
        if (salesman == null) {
            retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
            retMap.put("msg", "登录异常，请重新登录！");
            return retMap;
        }

        try {
            Integer timeLengthSum = xdOnTimeMapper.selectSumTimeLengthByMonth(salesmanId);
            int acceptCount = xdOrderRecordMapper.selectAcceptCountByMonth(salesmanId);
            int visitCount = xdResourceVisitMapper.selectVisitCountByMonth(salesmanId);
            int callCount = xdCallNumMapper.selectCallCountByMonth(salesmanId);
            int orderCount = xdResourcesBaseMapper.selectOrderCountByMonth(salesmanId);

            XdSalesmanGrade xdSalesmanGrade = xdSalesmanGradeMapper.selectByPrimaryKey(salesman.getSalesmanGradeId());
            String gradePicUrl = null;
            String gradeName = null;
            if (xdSalesmanGrade != null) {
                gradePicUrl = xdSalesmanGrade.getGradePicUrl();
                gradeName = xdSalesmanGrade.getName();
            }

            Integer status = salesman.getOnlineStatus();

            double hours = (double) timeLengthSum / 60;
            BigDecimal bigDecimal = new BigDecimal(hours);
            double hourDouble = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("timeLength", String.valueOf(hourDouble));
            dataMap.put("acceptCount", acceptCount);
            dataMap.put("visitCount", visitCount);
            dataMap.put("callCount", callCount);
            dataMap.put("orderCount", orderCount);
            dataMap.put("salesmanState", status);
            if (!StringUtils.isBlank(gradePicUrl)) {
                String Host = getHeadHost();
                dataMap.put("gradePicUrl", Host + gradePicUrl);
            } else {
                dataMap.put("gradePicUrl", "");
            }
            if (!StringUtils.isBlank(gradeName)) {
                dataMap.put("gradeName", gradeName);
            } else {
                dataMap.put("gradeName", "");
            }

            retMap.put("code", 0);
            retMap.put("msg", "获取统计数据成功！");
            retMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "获取统计数据失败，请稍后重试！");
        }
        return retMap;
    }

    @Transactional
    @Override
    public Map<String, Object> changeState(XdSalesmanDto dto, String token) {
        Map<String, Object> retMap = new HashMap<>();
        String salesmanId = dto.getSalesmanId();
        try {

            XdSalesman salesman;
            if (StringUtils.isBlank(salesmanId)) {
                JedisCluster cluster = RedisFactory.getJedisCluster();
                String phoneNo = cluster.get(ApiConstants.TOKEN_PHONE + token);
                salesman = xdSalesmanMapper.selectSalesmanByPhone(phoneNo);
                if (StringUtils.isBlank(salesmanId) && salesman != null) {
                    salesmanId = salesman.getId();
                }
            } else {
                salesman = xdSalesmanMapper.selectByPrimaryKey(salesmanId);
            }

            if (salesman == null) {
                retMap.put("code", -1);
                retMap.put("msg", "用戶不存在！");
                return retMap;
            }
            Integer state = dto.getState();
            if (state == null) {
                retMap.put("code", -1);
                retMap.put("msg", "错误：状态异常！");
                return retMap;
            }

            Integer status = salesman.getOnlineStatus();
            if (state.equals(status)) {
                retMap.put("code", -1);
                retMap.put("msg", "不能切换到相同状态！");
                return retMap;
            }

            // 离线
            if (ApiConstants.STATE_0 == state) {
                XdOnTime xdOnTime = xdOnTimeMapper.selectLastOnTime(salesmanId);
                if (xdOnTime != null) {
                    Date onTime = xdOnTime.getOnTime();
                    Date date = new Date();

                    long curMillis = date.getTime();
                    long timeLong = (curMillis - onTime.getTime()) / 1000 / 60;
                    timeLong = timeLong < 0 ? 0 : timeLong;

                    xdOnTime.setOffTime(date);
                    xdOnTime.setTimeLength(timeLong);
                    xdOnTimeMapper.updateByPrimaryKeySelective(xdOnTime);
                }
                // 在线
            } else if (ApiConstants.STATE_1 == state) {
                XdOnTime xdOnTime = xdOnTimeMapper.selectLastOnTime(salesmanId);
                if (xdOnTime != null) {
                    Date onTime = xdOnTime.getOnTime();
                    Date date = new Date();

                    long curMillis = date.getTime();
                    long timeLong = (curMillis - onTime.getTime()) / 1000 / 60;
                    timeLong = timeLong < 0 ? 0 : timeLong;

                    xdOnTime.setOffTime(date);
                    xdOnTime.setTimeLength(timeLong);
                    xdOnTimeMapper.updateByPrimaryKeySelective(xdOnTime);
                }

                Date date = new Date();

                Map<String, String> constantMap = ResourceUtils.getResource("constants").getMap();
                String startHour = constantMap.get("online.start.hour");
                String endHour = constantMap.get("online.end.hour");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String ymd = simpleDateFormat.format(date);

                String startTime = ymd + " " + startHour;
                String endTime = ymd + " " + endHour;
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = simpleDateFormat.parse(startTime);
                Date endDate = simpleDateFormat.parse(endTime);

                long curTimeStamp = date.getTime();
                long startTimeStamp = startDate.getTime();
                long endTimeStammp = endDate.getTime();

                if (curTimeStamp < startTimeStamp || curTimeStamp > endTimeStammp) {
                    retMap.put("code", -1);
                    retMap.put("msg", "未到上线时间：" + startHour + "，或已过下线时间：" + endHour + "！");
                    return retMap;
                }

                String id = PrimaryKeyUtils.getId();
                XdOnTime onTime = new XdOnTime();
                onTime.setOnId(id);
                onTime.setSalesmanId(salesmanId);
                onTime.setOnTime(date);
                // V2  0：离线，1：在线，2：繁忙
                onTime.setState(1);
                xdOnTimeMapper.insert(onTime);

            } else if (ApiConstants.STATE_2 == state) {
                retMap.put("code", -1);
                retMap.put("msg", "切换状态失败！");
                return retMap;
            }
            salesman.setOnlineStatus(state);
            xdSalesmanMapper.updateByPrimaryKeySelective(salesman);

            retMap.put("code", 0);
            retMap.put("msg", "状态更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "状态更新失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }

    @Override
    public XdSalesman getSalesmanByPhone(String phone) {
        XdSalesman salesman = null;
        try {
            salesman = xdSalesmanMapper.selectSalesmanByPhone(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salesman;

    }

    /**
     * 随机生成数字验证码
     *
     * @param num
     * @return
     */
    private String getMsgCode(int num) {
        Random random = new Random();
        StringBuffer messCode = new StringBuffer();
        for (int i = 0; i < num; i++) {
            messCode.append(random.nextInt(10));
        }
        return messCode.toString();
    }


    public static void main(String[] args) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);
    }
}
