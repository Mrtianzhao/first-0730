package net.dgg.cloud.service.Impl;

import net.dgg.cloud.dao.ConRegionMapper;
import net.dgg.cloud.entity.ConRegion;
import net.dgg.cloud.service.ConRegionService;
import net.dgg.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConRegionServiceImpl implements ConRegionService {

    @Resource
    private ConRegionMapper conRegionMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Map<String, Object> selectByPid(ConRegion conRegion, String token) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            // ==================================
           if (StringUtils.isBlank(conRegion.getPid())) {
                String pId = "1";
                conRegion.setPid(pId);
            }
             List<ConRegion> conRegions = conRegionMapper.selectByPid(conRegion.getPid());

            for (ConRegion region : conRegions) {
                List<ConRegion> conRegionsChild = conRegionMapper.selectByPid(region.getId());
                region.setConRegionChild(conRegionsChild);
            }
            map.put("conRegions", conRegions);
            data.put("code", 0);
            data.put("msg", "获取信息成功！");
            data.put("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 1);
            map.put("msg", "请求失败");
            return map;
        }
        return data;
    }

}
