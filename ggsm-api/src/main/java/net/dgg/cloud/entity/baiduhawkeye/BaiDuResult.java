package net.dgg.cloud.entity.baiduhawkeye;

import java.util.List;

/**
 * @author ytz
 * @date 2018/5/18.
 * @desc
 */
public class BaiDuResult {

    /**
     * 返回状态，0为成功
     */
    private int status;

    /**
     * 对status的中文描述
     */
    private String message;

    /**
     * 代表符合本次检索条件的结果总数
     */
    private String size;

    /**
     * 代表本页返回了多少条符合条件的entity
     */
    private int total;

    /**
     * entity详细信息列表
     */
    private List<EntityResult> entities;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<EntityResult> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityResult> entities) {
        this.entities = entities;
    }
}
