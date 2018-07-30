package net.dgg.cloud.entity.baiduhawkeye;

/**
 * @author ytz
 * @date 2018/5/18.
 * @desc 实体
 */
public class EntityResult {

    /**
     * entity名称，其唯一标识	string
     */
    private String entity_name;

    /**
     * entity 可读性描述	string
     */
    private String entity_desc;

    /**
     * entity属性修改时间	格式化时间	该时间为服务端时间
     */
    private String modify_time;

    /**
     * entity创建时间	格式化时间	该时间为服务端时间
     */
    private String create_time;

    /**
     * 开发者自定义的entity属性信息	string	只有当开发者为entity创建了自定义属性字段，且赋过值，才会返回。
     */
    private String column_key;

    /**
     * 最新的轨迹点信息
     */
    private LatestLocation latest_location;

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public String getEntity_desc() {
        return entity_desc;
    }

    public void setEntity_desc(String entity_desc) {
        this.entity_desc = entity_desc;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getColumn_key() {
        return column_key;
    }

    public void setColumn_key(String column_key) {
        this.column_key = column_key;
    }

    public LatestLocation getLatest_location() {
        return latest_location;
    }

    public void setLatest_location(LatestLocation latest_location) {
        this.latest_location = latest_location;
    }
}
