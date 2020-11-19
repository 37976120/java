package com.htstar.ovms.device.mongo.repository;

import com.htstar.ovms.device.mongo.model.ObdGpsDataMG;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ObdGpsDataRepository extends MongoRepository<ObdGpsDataMG, String> {


}
