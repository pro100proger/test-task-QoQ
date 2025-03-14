package com.mongotest.procedure.repository;

import com.mongotest.procedure.entity.AcrcPymtMatch;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AcrcPymtMatchRepository extends MongoRepository<AcrcPymtMatch, String> {

    @Aggregation(pipeline = {
        "{ $match: { 'PYMT_NO': ?0 } }",
        "{ $group: { '_id': null, 'sum': { $sum: '$MATCH_ADV_AMT' } } }"
    })
    List<SumResult> sumMatchAdvByPymtNo(String pymtNo);

    default BigDecimal sumMatchAdvAmtByPymtNo(String pymtNo) {
        List<SumResult> r = sumMatchAdvByPymtNo(pymtNo);
        if (r == null || r.isEmpty() || r.get(0).getSum() == null) {
            return null;
        }
        return BigDecimal.valueOf(r.get(0).getSum());
    }

    class SumResult {
        private Double sum;

        public Double getSum() {
            return sum;
        }

        public void setSum(Double sum) {
            this.sum = sum;
        }
    }

    List<AcrcPymtMatch> findByPymtNo(String pymtNo);

    @Query("{ 'pymtNo': ?0, 'matchDoc': ?1 }")
    @Update("{ '$inc': { 'matchAmt': ?2, 'matchAdvAmt': ?3 }, '$set': { 'matchType': ?4, 'prodType': ?5 } }")
    void updateMatchDetails(String pymtNo, String matchDoc, BigDecimal matchAmt, BigDecimal matchAdvAmt, String matchType, String prodType);
}