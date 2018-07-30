package net.dgg.cloud.service.Impl;

import net.dgg.cloud.dao.CloundVersionControlMapper;
import net.dgg.cloud.entity.CloundVersionControl;
import net.dgg.cloud.service.CloundVersionControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 王 on 2018/5/23.
 */
@Service
public class CloundVersionControlServiceImpl  implements CloundVersionControlService {
    @Autowired
    CloundVersionControlMapper cloundVersionControlMapper;
    /*查询最新版本号*/
    @Override
    public CloundVersionControl selectNewest(String type) {
        return cloundVersionControlMapper.selectNewest(type);
    }
}
