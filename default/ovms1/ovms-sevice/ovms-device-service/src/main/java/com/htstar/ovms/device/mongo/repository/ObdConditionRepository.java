package com.htstar.ovms.device.mongo.repository;

import com.htstar.ovms.device.mongo.model.ObdConditionMG;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
public interface ObdConditionRepository extends MongoRepository<ObdConditionMG, String> {

}