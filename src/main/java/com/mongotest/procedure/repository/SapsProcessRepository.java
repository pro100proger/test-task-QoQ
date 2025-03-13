package com.mongotest.procedure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongotest.procedure.entity.SapsProcess;

@Repository
public interface SapsProcessRepository extends MongoRepository<SapsProcess, String> {
}
