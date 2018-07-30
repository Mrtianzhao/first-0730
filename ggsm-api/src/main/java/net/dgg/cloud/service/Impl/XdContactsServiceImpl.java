package net.dgg.cloud.service.Impl;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dao.XdContactsMapper;
import net.dgg.cloud.dao.XdResourcesBaseMapper;
import net.dgg.cloud.dao.XdSalesmanMapper;
import net.dgg.cloud.entity.XdContacts;
import net.dgg.cloud.entity.XdResourcesBase;
import net.dgg.cloud.entity.XdSalesman;
import net.dgg.cloud.service.XdContactsService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联系人serviceImpl
 */
@Service
public class XdContactsServiceImpl implements XdContactsService {

    private Logger log = LoggerFactory.getLogger(XdContactsServiceImpl.class);

    @Resource
    XdContactsMapper xdContactsMapper;

    @Resource
    XdSalesmanMapper salesmanMapper;

    @Resource
    private XdResourcesBaseMapper resourcesBaseMapper;

    /**
     * 根据资源id获取联系人列表
     *
     * @param record
     * @return
     */
    @Override
    public Map<String, Object> queryContactsList(XdContacts record, String token) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(record.getResourcesId())) {
                map.put("code", 1);
                map.put("msg", "资源id不能为空！");
                return map;
            } else {
                /**根据资源id获取联系人列表*/
                List<XdContacts> xdContacts = xdContactsMapper.selectContactsList(record);
                map.put("code", 0);
                map.put("msg", "获取联系人列表成功！");
                map.put("pageSize", xdContacts);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 1);
            map.put("msg", "请求失败！");
        }

        return map;
    }

    /**
     * 插入联系人信息
     *
     * @param record
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addContactsInfo(XdContacts record, String token) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            String resourcesId = record.getResourcesId();
            String customerName = record.getCustomerName();
            String customerPhoner = record.getCustomerPhoner();
            String customerAddress = record.getCustomerAddress();
            String longitude = record.getLongitude();
            String latitude = record.getLatitude();

            if (StringUtils.isBlank(resourcesId)) {
                map.put("code", -1);
                map.put("msg", "资源id不能为空！");
                return map;
            }
            if (StringUtils.isBlank(customerName)) {
                map.put("code", -1);
                map.put("msg", "客户姓名不能为空！");
                return map;
            }
            if (customerName.length() > 10) {
                map.put("code", -1);
                map.put("msg", "客户姓名不能超过10个字符！");
                return map;
            }
            if (StringUtils.isBlank(customerPhoner)) {
                map.put("code", -1);
                map.put("msg", "客户电话不能为空！");
                return map;
            }
            if (customerPhoner.length() != 11) {
                map.put("code", -1);
                map.put("msg", "客户电话格式不正确！");
                return map;
            }
            if (StringUtils.isBlank(customerAddress)) {
                map.put("code", -1);
                map.put("msg", "客户联系地址不能为空！");
                return map;
            }
            if (StringUtils.isBlank(longitude)) {
                map.put("code", -1);
                map.put("msg", "经度不能为空！");
                return map;
            }

            double longitudeDou = Double.parseDouble(longitude);
            if (longitudeDou < -180 || longitudeDou > 180){
                map.put("code", -1);
                map.put("msg", "经纬度错误！");
                return map;
            }

            if (StringUtils.isBlank(latitude)) {
                map.put("code", -1);
                map.put("msg", "纬度不能为空！");
                return map;
            }

            double latitudeDou = Double.parseDouble(latitude);
            if (latitudeDou < -90 || latitudeDou > 90) {
                map.put("code", -1);
                map.put("msg", "经纬度错误！");
                return map;
            }

            XdResourcesBase resourcesBase = resourcesBaseMapper.selectByPrimaryKey(resourcesId);
            if (resourcesBase == null || resourcesBase.getDelFlag() == 1) {
                map.put("code", -1);
                map.put("msg", "资源状态异常！");
                return map;
            }

            int stateCode7 = ApiConstants.RESOURCE_STATE_CODE_7;
            Integer status = resourcesBase.getResourcesStatus();
            Integer delFlag = resourcesBase.getDelFlag();

            if (stateCode7 == status && delFlag == 0) {
                map.put("code", -1);
                map.put("msg", "资源反无效审核中，不能添加联系人！");
                return map;
            }

            Integer isMainCon = record.getIsMainCon();
            if (isMainCon != 0 && isMainCon != 1) {
                map.put("code", -1);
                map.put("msg", "非法请求！");
                return map;
            }

            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            String phone = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phone);

            if (salesman == null) {
                map.put("code", ApiConstants.LOGIN_ERROR_CODE);
                map.put("msg", "登录异常，请重新登录！");
                return map;
            }

            Date date = new Date();
            String salesmanId = salesman.getId();
            String contactsId = PrimaryKeyUtils.getId();

            record.setContactsId(contactsId);
            record.setCommerceId(salesmanId);
            record.setCreateUserId(salesmanId);
            record.setCreateTime(date);
            record.setUpdateUserId(salesmanId);
            record.setUpdateTime(date);
            record.setDelFlag(0);

            xdContactsMapper.insert(record);

            if (isMainCon == 1) {
                xdContactsMapper.updateMainContactByResource(contactsId);
            }
            map.put("code", 0);
            map.put("msg", "联系人信息插入成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("msg", "请求失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return map;
    }
}
