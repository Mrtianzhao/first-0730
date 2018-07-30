package net.dgg.cloud.service;

import net.dgg.cloud.entity.CloundVersionControl;

/**
 * Created by anziwen on 2017/11/20.
 */
public interface CloundVersionControlService {
    /*查询最新版本号*/
    CloundVersionControl selectNewest(String type);
}
