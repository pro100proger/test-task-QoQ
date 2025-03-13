package com.mongotest.procedure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongotest.procedure.entity.ViewAcrcMatchDoc;

@Repository
public interface ViewAcrcMatchDocRepository extends MongoRepository<ViewAcrcMatchDoc, String> {

    Optional<ViewAcrcMatchDoc> findByMatchTypeAndMatchDoc(String matchType, String matchDoc);
}
