package com.htstar.ovms.device.service;

import com.htstar.ovms.device.protoco.ObdConditionProcoto;
import com.htstar.ovms.device.protoco.ObdGpsDataTp;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
public interface MongoDbHandleService {

    /**
     * Description: GSP数据通过协议入库
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    boolean saveGpsToMongodb(ObdGpsDataTp obdGpsDataTp);

    /**
     * Description: 工况数据通过协议入库
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    boolean saveConditionToMongodb(ObdConditionProcoto obdConditionProcoto);
}
