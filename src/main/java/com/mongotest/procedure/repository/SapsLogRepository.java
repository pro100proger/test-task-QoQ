package com.mongotest.procedure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongotest.procedure.entity.SapsLog;

@Repository
public interface SapsLogRepository extends MongoRepository<SapsLog, String> {
}
