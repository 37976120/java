package com.htstar.ovms.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.htstar.ovms.device.mongo.model.ObdConditionMG;
import com.htstar.ovms.device.mongo.model.ObdGpsDataMG;
import com.htstar.ovms.device.mongo.repository.ObdConditionRepository;
import com.htstar.ovms.device.mongo.repository.ObdGpsDataRepository;
import com.htstar.ovms.device.protoco.ConditionModel;
import com.htstar.ovms.device.protoco.GpsItemTp;
import com.htstar.ovms.device.protoco.ObdConditionProcoto;
import com.htstar.ovms.device.protoco.ObdGpsDataTp;
import com.htstar.ovms.device.service.MongoDbHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@Service
@Slf4j
public class MongoDbHandleServiceImpl implements MongoDbHandleService {

    @Autowired
    private ObdGpsDataRepository obdGpsDataRepository;

    @Autowired
    private ObdConditionRepository obdConditionRepository;
    /**
     * Description: GPS通过协议入库
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public boolean saveGpsToMongodb(ObdGpsDataTp obdGpsDataTp) {
        //原始GPS数据入MONGODB
        List<GpsItemTp> gpsItemTps =  obdGpsDataTp.getGpsItemTpList();
        if(gpsItemTps == null || gpsItemTps.isEmpty()){
            log.info("没有可入库的GPS信息");
            return true;
        }

        List<ObdGpsDataMG> obdGpsDataMGList = new ArrayList<>();
        for (GpsItemTp gpsItemTp : gpsItemTps){
            ObdGpsDataMG obdGpsDataMG = new ObdGpsDataMG();
            BeanUtil.copyProperties(gpsItemTp,obdGpsDataMG);
            obdGpsDataMG.setLocation(new Point(gpsItemTp.getLat(),gpsItemTp.getLng()));
            obdGpsDataMGList.add(obdGpsDataMG);
        }
        obdGpsDataRepository.saveAll(obdGpsDataMGList);
        return true;
    }

    @Override
    public boolean saveConditionToMongodb(ObdConditionProcoto obdConditionProcoto) {
        List<ConditionModel> conditionModelList = obdConditionProcoto.getConditionModelList();
        if(conditionModelList == null || conditionModelList.isEmpty()){
            log.info("没有可入库的工况信息");
            return true;
        }
        List<ObdConditionMG> obdConditionMGList = new ArrayList<>();
        String deviceSn = obdConditionProcoto.getDeviceSn();
        for (ConditionModel model :conditionModelList){
            ObdConditionMG obdConditionMG = new ObdConditionMG();
            obdConditionMG.setDeviceSn(deviceSn);
            obdConditionMG.setRcvTime(obdConditionProcoto.getRevTime());
            obdConditionMG.setConValue(model.getConValue());
            obdConditionMG.setPidKey(model.getPidKey());
            obdConditionMGList.add(obdConditionMG);
        }
        obdConditionRepository.saveAll(obdConditionMGList);
        return true;
    }
}
