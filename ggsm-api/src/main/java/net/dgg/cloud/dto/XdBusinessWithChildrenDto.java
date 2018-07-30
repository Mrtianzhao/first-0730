package net.dgg.cloud.dto;

import java.util.List;

public class XdBusinessWithChildrenDto {

    /** 主键id */
    private String id;

    /** 业态名称 */
    private String businessName;

    /** 父节点 */
    private String parentId;

    private List<XdBusinessWithChildrenDto> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<XdBusinessWithChildrenDto> getChildren() {
        return children;
    }

    public void setChildren(List<XdBusinessWithChildrenDto> children) {
        this.children = children;
    }
}
