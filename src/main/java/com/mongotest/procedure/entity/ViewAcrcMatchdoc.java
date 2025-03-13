package com.mongotest.procedure.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "VIEW_ACRC_MATCHDOC")
public class ViewAcrcMatchdoc {

    @Id
    private String id;

    @Field("MATCH_DOC")
    private String matchDoc;

    @Field("MATCH_TYPE")
    private String matchType;

    @Field("POL_NO")
    private String polNo;

    @Field("PROD_TYPE")
    private String prodType;
}
