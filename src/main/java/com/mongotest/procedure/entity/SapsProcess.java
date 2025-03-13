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
public class SapsProcess {

    @Id
    private String id;

    @Field("PROCESS_NAME")
    private String processName;

    @Field("LAST_END_DATE")
    private LocalDateTime lastEndDate;

    @Field("LAST_RUN_STAT")
    private Character lastRunStat;
}
