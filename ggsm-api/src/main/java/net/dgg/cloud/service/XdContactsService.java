package net.dgg.cloud.service;

import net.dgg.cloud.entity.XdContacts;

import java.util.Map;

/**
 * 联系人service
 * @author
 */
public interface XdContactsService {

    /**
     * 根据资源id获取联系人列表
     *
     * @param record
     * @param token
     * @return
     */
    Map<String, Object> queryContactsList(XdContacts record, String token);

    /**
     * 插入联系人信息
     *
     * @param record
     * @param token
     * @return
     */
    Map<String, Object> addContactsInfo(XdContacts record, String token);

}
