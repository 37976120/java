package com.htstar.ovms.device.service.impl;

import com.htstar.ovms.device.service.GpsService;
import com.htstar.ovms.device.util.ObdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@RefreshScope
@Service
public class GpsServiceImpl implements GpsService {

    @Value("${amap.convert.convertApi:http://restapi.amap.com/v3/assistant/coordinate/convert}")
    private String convertApi;

    @Value("${amap.convert.amapApi:http://restapi.amap.com/v3/geocode/regeo}")
    private String amapApi;

    @Value("${amap.convert.key:b0ca3ecb6c782fb50ea82640536b2ee9}")
    private String apiKey;

    /**
     * 正式环境要改为false，或者不写这个配置
     */
    @Value("${amap.mockStatus:fasle}")
    private boolean mockStatus;

    @Override
    public String getGpsAddr(Double lat, Double lng) {
        return ObdUtil.getLocation(mockStatus,amapApi,convertApi,apiKey,lat,lng);
    }
}
