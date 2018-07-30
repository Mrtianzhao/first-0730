package net.dgg.cloud.dto;

/**
 * Created by 王 on 2018/4/17.
 */
public class PhTestDto {
    private String appId;  //必选	应用id
    private String aNumber; //必选	用户A的电话号码。
    private String bNumber; //必选	用户B的电话号码。
    private String xNumber;  // 必选	指定的服务号（中间号）
    private String areaCode; // 可选	区号
    private String expirationSetting; //必选	中止设定。0：未设定；1：一次呼叫后中止，2：一次成功呼叫后中止，3：指定时长后中止
  /*  可选 0 AXB 不做呼叫控制，A 和 B （N）均有权限
    1 AXB 做呼叫控制，A 和 B 有权 限，其他号码无权限
    2 AXB 的单通控制，A 无权限，B 有权限，其他号码无权限
    3 AXB 的单通控制，A 有权限，B 以及其他号码无权限
    4 AXN 的单通控制，A 无权限，B 以及其他号码有权限
    5 AXN 的单通控制，A 有权限，B 以及其他号码无权限
    6 均无权限
    默认 '1'*/
    private String callrestrict;
    private String icDisplayFlag; //可选 	显号控制  "1,1"     A->B 和B -> A     '0',显真实号码；'1'，显X号码  默认 "1,1"
    private String mappingDuration;//可选	映射关系效时长，单位是秒，到时后会映射关系自动释放。缺省为"7200"秒
    private String anucode;  //可选 	放音编码必须包含 3 个场景的编 码。按照“B->X,A->X,其他号码 ->X”的顺序填写编码，编码之 间以逗号分隔。 默认 "1,2,3"
    private String needRecord;//可选	是否需要录音
    private String callNotifyUrl;//可选	呼叫开始通知地址
    private String cdrNotifyUrl; //可选	话单推送地址
    private String userData; //可选 	用户自定义数据


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getaNumber() {
        return aNumber;
    }

    public void setaNumber(String aNumber) {
        this.aNumber = aNumber;
    }

    public String getbNumber() {
        return bNumber;
    }

    public void setbNumber(String bNumber) {
        this.bNumber = bNumber;
    }

    public String getxNumber() {
        return xNumber;
    }

    public void setxNumber(String xNumber) {
        this.xNumber = xNumber;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getExpirationSetting() {
        return expirationSetting;
    }

    public void setExpirationSetting(String expirationSetting) {
        this.expirationSetting = expirationSetting;
    }

    public String getCallrestrict() {
        return callrestrict;
    }

    public void setCallrestrict(String callrestrict) {
        this.callrestrict = callrestrict;
    }

    public String getIcDisplayFlag() {
        return icDisplayFlag;
    }

    public void setIcDisplayFlag(String icDisplayFlag) {
        this.icDisplayFlag = icDisplayFlag;
    }

    public String getMappingDuration() {
        return mappingDuration;
    }

    public void setMappingDuration(String mappingDuration) {
        this.mappingDuration = mappingDuration;
    }

    public String getAnucode() {
        return anucode;
    }

    public void setAnucode(String anucode) {
        this.anucode = anucode;
    }

    public String getNeedRecord() {
        return needRecord;
    }

    public void setNeedRecord(String needRecord) {
        this.needRecord = needRecord;
    }

    public String getCallNotifyUrl() {
        return callNotifyUrl;
    }

    public void setCallNotifyUrl(String callNotifyUrl) {
        this.callNotifyUrl = callNotifyUrl;
    }

    public String getCdrNotifyUrl() {
        return cdrNotifyUrl;
    }

    public void setCdrNotifyUrl(String cdrNotifyUrl) {
        this.cdrNotifyUrl = cdrNotifyUrl;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }
}
