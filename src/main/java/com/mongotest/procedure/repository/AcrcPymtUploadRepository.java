package com.mongotest.procedure.repository;

import com.mongotest.procedure.entity.AcrcPymtUpload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AcrcPymtUploadRepository extends MongoRepository<AcrcPymtUpload, String> {

    @Query("{'MATCH_ADV_AMT': {$gt: 0}, '$or': [{'DEL_IND': {'$ne': 'Y'}}, {'DEL_IND': null}]}")
    List<AcrcPymtUpload> findAdvPymtList();
}