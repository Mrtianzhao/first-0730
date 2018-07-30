package net.dgg.cloud.dto;

import net.dgg.cloud.constant.BaiduHawkEyeConstants;
import net.dgg.cloud.service.BaiduMapService;
import net.dgg.cloud.utils.DGGException;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @Author zhangyuzhu
 * @Description 本类目的：封装{@link BaiduMapService#getDistance(BaiduDistanceReqDto)}方法的请求参数
 * @Created data 2018/7/12 17:39
 */
public class BaiduDistanceReqDto extends BaseDto{

    /** 用户的ak，授权使用*/
    private String ak;

    /** service唯一标识*/
    private Integer serviceId;

    /** entity唯一标识*/
    private String entityName;

    /** 开始时间(UNIX 时间戳)*/
    private Long startTime;

    /** 结束时间(UNIX 时间戳)*/
    private Long endTime;

    /** 是否返回纠偏后里程 {@link PROCESSEDFLAG}*/
    private Integer processedFlag;

    /** 纠偏选项 {@link ProcessOption}*/
    private ProcessOption processOption;

    /** 里程补偿方式 {@link SUPPLEMENTMODE}*/
    private String supplementMode;


    public BaiduDistanceReqDto() {
        this(null,null,null);
    }

    public BaiduDistanceReqDto(String entityName, Long startTime, Long endTime) {
        super();
        this.entityName = entityName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ak = BaiduHawkEyeConstants.AK;
        this.serviceId = BaiduHawkEyeConstants.SERVICE_ID;
        this.processedFlag = PROCESSEDFLAG.TRUE.getKey();
        this.supplementMode = SUPPLEMENTMODE.DRIVING.getKey();
        ProcessOption po = new ProcessOption(NEEDDENOISE.TRUE.getKey(),NEEDMAPMATCH.TRUE.getKey(),RADIUSTHRESHOLD.GPS.getKey(),TRANSPORTMODE.AUTO.getKey());
        this.processOption = po;
    }

    /**
     * 纠偏选项类
     */
    class ProcessOption{

        public ProcessOption() {
            super();
        }

        public ProcessOption(Integer needDenoise, Integer needMapmatch, Integer radiusThreshold, String transportMode) {
            this.needDenoise = needDenoise;
            this.needMapmatch = needMapmatch;
            this.radiusThreshold = radiusThreshold;
            this.transportMode = transportMode;
        }

        /** 是否去噪 {@link NEEDDENOISE}*/
        private Integer needDenoise;

        /** 是否绑路 {@link NEEDMAPMATCH}*/
        private Integer needMapmatch;

        /** 定位精度过滤 {@link RADIUSTHRESHOLD}*/
        private Integer radiusThreshold;

        /** 交通方式 {@link TRANSPORTMODE}*/
        private String transportMode;

        public Integer getNeedDenoise() {
            return needDenoise;
        }

        public void setNeedDenoise(Integer needDenoise) {
            this.needDenoise = needDenoise;
        }

        public Integer getNeedMapmatch() {
            return needMapmatch;
        }

        public void setNeedMapmatch(Integer needMapmatch) {
            this.needMapmatch = needMapmatch;
        }

        public Integer getRadiusThreshold() {
            return radiusThreshold;
        }

        public void setRadiusThreshold(Integer radiusThreshold) {
            this.radiusThreshold = radiusThreshold;
        }

        public String getTransportMode() {
            return transportMode;
        }

        public void setTransportMode(String transportMode) {
            this.transportMode = transportMode;
        }
    }


    public String getAk() {
        return ak;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public String getEntityName() {
        return entityName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Integer getProcessedFlag() {
        return processedFlag;
    }

    public ProcessOption getProcessOption() {
        return processOption;
    }

    public String getSupplementMode() {
        return supplementMode;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    /**
     * 是否返回纠偏后里程
     */
    public enum PROCESSEDFLAG {
        TRUE(1,"是"),
        FALSE(0,"否");

        private Integer key;
        private String value;

        PROCESSEDFLAG(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public Integer getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 里程补偿方式
     */
    public enum SUPPLEMENTMODE {
        NOSUPPLEMENT("no_supplement","不补充，中断两点间距离不记入里程"),
        STRAIGHT("straight","使用直线距离补充"),
        DRIVING("driving","使用最短驾车路线距离补充"),
        RIDING("riding","使用最短骑行路线距离补充"),
        WALKING("walking","使用最短步行路线距离补充");

        private String key;
        private String value;

        SUPPLEMENTMODE(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 是否去噪
     */
    public enum NEEDDENOISE {
        TRUE(1,"去噪"),
        FALSE(0,"不去噪");

        private Integer key;
        private String value;

        NEEDDENOISE(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public Integer getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 是否绑路
     */
    public enum NEEDMAPMATCH {
        TRUE(1,"绑路"),
        FALSE(0,"不绑路");

        private Integer key;
        private String value;

        NEEDMAPMATCH(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public Integer getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 定位精度过滤
     */
    public enum RADIUSTHRESHOLD {
        GPSANDWIFI(100,"保留 GPS 和 Wi-Fi 定位点"),
        GPS(20,"保留 GPS 定位点"),
        NOFILTER(0,"不过滤");

        private Integer key;
        private String value;

        RADIUSTHRESHOLD(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public Integer getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 交通方式
     */
    public enum TRANSPORTMODE {
        AUTO("auto","自动"),
        DRIVING("driving","驾车"),
        RIDING("riding","骑行"),
        WALKING("walking","步行");

        private String key;
        private String value;

        TRANSPORTMODE(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 用本对象组装成发送百度需要的参数的hashMap
     * @return
     */
    public HashMap toParam(){
        HashMap hashMap = new HashMap();

        hashMap.put("ak",this.getAk());
        hashMap.put("service_id",this.getServiceId());

        if(StringUtils.isEmpty(this.getEntityName())){
            throw new DGGException("entity唯一标识为空!");
        }
        hashMap.put("entity_name",this.getEntityName());

        if(this.getStartTime() == null){
            throw new DGGException("startTime为空!");
        }
        if(this.getStartTime().longValue() < 0){
            throw new DGGException("startTime非法!");
        }
        hashMap.put("start_time",this.getStartTime());

        if(this.getEndTime() == null){
            throw new DGGException("endTime为空!");
        }
        if(this.getEndTime().longValue() < 0 || this.getEndTime().longValue() <= this.getStartTime().longValue()){
            throw new DGGException("endTime非法!");
        }
        long differenceMillis = this.getEndTime().longValue() - this.getStartTime().longValue();
        long maxMillis = 24 * 60 * 60 * 1000;
        if(differenceMillis > maxMillis){
            throw new DGGException("开始时间和结束时间必须在24小时内!");
        }
        hashMap.put("end_time",this.getEndTime());
        hashMap.put("is_processed",this.getProcessedFlag());
        hashMap.put("supplement_mode",this.getSupplementMode());
        StringBuffer processOption = new StringBuffer();
        processOption.append("transport_mode=").append(this.getProcessOption().getTransportMode())
                .append(",").append("radius_threshold=").append(this.getProcessOption().getRadiusThreshold())
                .append(",").append("need_mapmatch=").append(this.getProcessOption().getNeedMapmatch())
                .append(",").append("need_denoise=").append(this.getProcessOption().getNeedDenoise());
        hashMap.put("process_option",processOption.toString());
        return hashMap;
    }


}
