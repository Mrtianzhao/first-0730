package net.dgg.cloud.service.Impl;

import net.dgg.cloud.dao.XdBusinessMapper;
import net.dgg.cloud.dto.XdBusinessWithChildrenDto;
import net.dgg.cloud.dto.XdWxResourceBaseDto;
import net.dgg.cloud.service.XdBusinessService;
import net.dgg.cloud.utils.RestResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XdBusinessServiceImpl implements XdBusinessService {

    @Autowired
    private XdBusinessMapper xdBusinessMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, Object> getBusinessInfos(String token) {

        try {
            //查询所有有效的业态
            List<XdBusinessWithChildrenDto> dtos = xdBusinessMapper.selectAllValid();

            Map<String,XdBusinessWithChildrenDto> map = new HashMap<String,XdBusinessWithChildrenDto>();
            //将业态放入map中，用id为key
            for(XdBusinessWithChildrenDto dto:dtos){
                map.put(dto.getId(),dto);
            }

            //新建一个顶层容器
            List<XdBusinessWithChildrenDto> list = new ArrayList<XdBusinessWithChildrenDto>();

            //遍历所有有效业态，组装层级结构
            for(XdBusinessWithChildrenDto dto:dtos){
                String parentId = dto.getParentId();
                logger.info("outter:{}",parentId);
                if("0".equals(parentId)){
                    logger.info("inner:{}",parentId);
                    list.add(dto);
                }else{
                    XdBusinessWithChildrenDto parent = map.get(parentId);
                    if(parent !=null ){
                        if(parent.getChildren()!=null){
                            parent.getChildren().add(dto);
                        }else{
                            List<XdBusinessWithChildrenDto> children = new ArrayList<XdBusinessWithChildrenDto>();
                            children.add(dto);
                            parent.setChildren(children);
                        }
                    }
                }
            }

            Map<String,Object> data = new HashMap<String,Object>();
            data.put("businessInfos",list);
            return RestResponseUtil.getSuccessResult(data);
        } catch (Exception e) {
            logger.error("getBusinessInfos error",e);
            return RestResponseUtil.getFailResult("系统执行异常");
        }


    }
}
