package com.mongotest.procedure.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "SAPS_LOG")
public class SapsLog {

    @Id
    private String id;

    @Field("PROCESS_NAME")
    private String processName;

    @Field("LOG_TIME")
    private LocalDateTime logTime;

    @Field("LOG_TYPE")
    private Character logType;

    @Field("LOG_DESCP")
    private String logDescp;

    @Field("LOG_CODE")
    private String logCode;

    public SapsLog(String vPROCESSName, String vFAILEDRemarks) {}
}
