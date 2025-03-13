package com.mongotest.procedure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongotest.procedure.entity.ViewAcrcMatchdoc;

@Repository
public interface ViewAcrcMatchdocRepository extends MongoRepository<ViewAcrcMatchdoc, String> {
}
