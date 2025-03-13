package com.mongotest.procedure.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "ACRC_PYMT_UPLOAD")
public class AcrcPymtUpload {

    @Id
    private String id;

    @Field("PYMT_NO")
    private String pymtNo;

    @Field("MATCH_ADV_AMT")
    private BigDecimal matchAdvAmt;

    @Field("MATCH_AMT")
    private BigDecimal matchAmt;

    @Field("DEL_IND")
    private String delInd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPymtNo() {
        return pymtNo;
    }

    public void setPymtNo(String pymtNo) {
        this.pymtNo = pymtNo;
    }

    public BigDecimal getMatchAdvAmt() {
        return matchAdvAmt;
    }

    public void setMatchAdvAmt(BigDecimal matchAdvAmt) {
        this.matchAdvAmt = matchAdvAmt;
    }

    public BigDecimal getMatchAmt() {
        return matchAmt;
    }

    public void setMatchAmt(BigDecimal matchAmt) {
        this.matchAmt = matchAmt;
    }

    public String getDelInd() {
        return delInd;
    }

    public void setDelInd(String delInd) {
        this.delInd = delInd;
    }
}