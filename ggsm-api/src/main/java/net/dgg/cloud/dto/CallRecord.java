package net.dgg.cloud.dto;

/**
 * Created by anziwen on 2018/4/18.
 */
public class CallRecord {
    private String callId; //必选	通话唯一标识
    private String appId; //必选	应用id
    private String callerNum; //必选	本次呼叫的主叫号码
    private String calleeNum; //必选	本次呼叫的被叫号码
    private String servingNum; //必选	中间号
    private String yNum;   //可选 	axyb的y号码
    private String startTime; //必选	本次通话的开始时间
    private String endTime; //必选	本次通话的结束时间
    private String answerTime; //可选	被叫接听时间
    private String result;  //必选	本次通话的结果 （见第7节通话结果）
    private String releasedir; //必选 	''1" 表示主叫， "2" 表示被叫， "0" 表示平台释放
    private Integer duration; //必选	本次通话的时长，单位为秒
   /* 必选 	"1"：axb  a主叫
        "2"：axb  a被叫
        "3"：axb  转接
        "4"： ax  外呼
        "5"： ax  呼入
        "7"   axyb a主叫
        "8"  axyb  a被叫*/
    private String calltype;
    private String mappingId; //必选   映射关系ID
    private String calleeRingTime; //可选 被叫振铃时间
    private String recordUrl; // 可选  录音地址
    private String userData; //可选 	用户自定义数据

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCallerNum() {
        return callerNum;
    }

    public void setCallerNum(String callerNum) {
        this.callerNum = callerNum;
    }

    public String getCalleeNum() {
        return calleeNum;
    }

    public void setCalleeNum(String calleeNum) {
        this.calleeNum = calleeNum;
    }

    public String getServingNum() {
        return servingNum;
    }

    public void setServingNum(String servingNum) {
        this.servingNum = servingNum;
    }

    public String getyNum() {
        return yNum;
    }

    public void setyNum(String yNum) {
        this.yNum = yNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReleasedir() {
        return releasedir;
    }

    public void setReleasedir(String releasedir) {
        this.releasedir = releasedir;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getCalleeRingTime() {
        return calleeRingTime;
    }

    public void setCalleeRingTime(String calleeRingTime) {
        this.calleeRingTime = calleeRingTime;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }
}
