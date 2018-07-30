package net.dgg.cloud.service.Impl;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dao.XdResourceFollowMapper;
import net.dgg.cloud.dao.XdResourceOperationMapper;
import net.dgg.cloud.dao.XdResourcesBaseMapper;
import net.dgg.cloud.dao.XdSalesmanMapper;
import net.dgg.cloud.dto.XdResourcesFollowDto;
import net.dgg.cloud.entity.XdResourceFollow;
import net.dgg.cloud.entity.XdResourceOperation;
import net.dgg.cloud.entity.XdResourcesBase;
import net.dgg.cloud.entity.XdSalesman;
import net.dgg.cloud.service.XdResourceFollowService;
import net.dgg.framework.redis.RedisFactory;
import net.dgg.framework.utils.PrimaryKeyUtils;
import net.dgg.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源跟进记录信息serviceImpl
 */
@Service
public class XdResourceFollowServiceImpl implements XdResourceFollowService {

    private Logger log = LoggerFactory.getLogger(XdResourceFollowServiceImpl.class);

    @Resource
    XdResourceFollowMapper xdResourceFollowMapper;

    @Resource
    XdSalesmanMapper salesmanMapper;

    @Resource
    XdResourcesBaseMapper xdResourcesBaseMapper;

    @Resource
    private XdResourceOperationMapper xdResourceOperationMapper;

    /**
     * 根据资源id获取该资源信息所有跟进记录
     *
     * @param dto
     * @param token
     * @return
     */
    @Override
    public Map<String, Object> queryResourcesFollowList(XdResourcesFollowDto dto, String token) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(dto.getResourceId())) {
                map.put("code", 1);
                map.put("msg", "资源id不能为空!");
                return map;
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

            /**根据资源id获取根据记录*/
            List<XdResourceFollow> resourceFollows = xdResourceFollowMapper.selectXdResourceFollowList(dto);
            /**根据资源id获取总条数*/
            int count = xdResourceFollowMapper.selectCount(dto);

            map.put("size", count);
            map.put("pageSize", resourceFollows);

            data.put("code", 0);
            data.put("msg", "获取跟进记录信息成功！");
            data.put("data", map);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("msg", "请求失败！");
            return map;
        }

        return data;
    }

    /**
     * 资源跟进信息插入
     *
     * @param dto
     * @param token
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addResourceFollow(XdResourcesFollowDto dto, String token) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        try {
            String resourceId = dto.getResourceId();
            String followTime = dto.getFollowTime();
            String remark = dto.getRemark();
            if (StringUtils.isBlank(resourceId)) {
                retMap.put("code", -1);
                retMap.put("msg", "资源id不能为空！");
                return retMap;
            }
            if (StringUtils.isBlank(followTime)) {
                retMap.put("code", -1);
                retMap.put("msg", "跟进时间不能为空！");
                return retMap;
            }
            if (StringUtils.isBlank(remark)) {
                retMap.put("code", -1);
                retMap.put("msg", "请填写跟进备注！");
                return retMap;
            }
            int defaultTextLen = ApiConstants.DEFAULT_REMARK_LEN;
            if (remark.length() > defaultTextLen) {
                retMap.put("code", -1);
                retMap.put("msg", "备注信息不能超过200字！");
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

            XdResourceFollow xdResourceFollow = new XdResourceFollow();
            String id = PrimaryKeyUtils.getId();
            String salesmanId = salesman.getId();
            String salesmanName = salesman.getName();

            xdResourceFollow.setFollowId(id);
            xdResourceFollow.setResourcesId(resourceId);
            xdResourceFollow.setCommerceId(salesmanId);
            xdResourceFollow.setCommerceName(salesmanName);
            xdResourceFollow.setCmt(remark);
            xdResourceFollow.setFollowTime(new Date());
            xdResourceFollow.setFollowType(0);

            XdResourcesBase base = new XdResourcesBase();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = simpleDateFormat.parse(followTime);

            base.setLostTime(date);
            base.setNextFollowTime(date);
            // 最新跟进时间
            base.setLastFollowTime(new Date());
            base.setResourcesId(resourceId);
            base.setUpdateUserId(salesmanId);
            base.setUpdateTime(new Date());

            XdResourcesBase resourcesBase = xdResourcesBaseMapper.selectByPrimaryKey(resourceId);
            if (resourcesBase == null || resourcesBase.getDelFlag() == 1) {
                retMap.put("code", -1);
                retMap.put("msg", "资源状态异常！");
                return retMap;
            }

            Integer status = resourcesBase.getResourcesStatus();
            Integer delFlag = resourcesBase.getDelFlag();
            // 待跟进 跟进中 已上门 已成单 已下单
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
                    retMap.put("msg", "资源反无效审核中，不能跟进！");
                } else {
                    retMap.put("msg", "非法操作！");
                }
                return retMap;
            }

            if (status != ApiConstants.RESOURCE_STATE_CODE_3 && status != ApiConstants.RESOURCE_STATE_CODE_4 && status != ApiConstants.RESOURCE_STATE_CODE_5) {
                base.setResourcesStatus(stateCode2);
            }

            XdResourceOperation operation = new XdResourceOperation();
            String optId = PrimaryKeyUtils.getId();
            String phoneNumber = salesman.getPhoneNumber();

            operation.setId(optId);
            operation.setOperatingTime(new Date());
            operation.setOperatorName(salesmanName);
            operation.setCommerceId(salesmanId);
            operation.setOperatorLoginName(phoneNumber);
            operation.setOperationRecords(remark);
            operation.setResourcesId(resourceId);

            xdResourceOperationMapper.insert(operation);
            xdResourcesBaseMapper.updateByPrimaryKeySelective(base);
            xdResourceFollowMapper.insert(xdResourceFollow);

            retMap.put("code", 0);
            retMap.put("msg", "新增跟进记录成功！");

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "请求失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return retMap;
    }
}
