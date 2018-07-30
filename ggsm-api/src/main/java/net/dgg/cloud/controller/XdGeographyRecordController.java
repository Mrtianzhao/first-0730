package net.dgg.cloud.controller;

import net.dgg.cloud.entity.XdGeographyRecords;
import net.dgg.cloud.service.XdGeographyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 骑手位置记录controller
 */
@RestController
@RequestMapping("/geographyRecord")
public class XdGeographyRecordController {

    @Autowired
    private XdGeographyRecordService XdGeographyRecordService;

    /**
     * 插入骑手位置记录信息
     */
    @RequestMapping(value = "/saveGeographyRecord")
    @ResponseBody
    public Map<String,Object> saveGeographyRecord(@RequestBody XdGeographyRecords record, @RequestHeader String token){
        return XdGeographyRecordService.addGeographyRecord(record,token);
    }
}
