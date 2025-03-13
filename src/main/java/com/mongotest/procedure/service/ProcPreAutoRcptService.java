package com.mongotest.procedure.service;

import com.mongotest.procedure.entity.AcrcPymtUpload;
import com.mongotest.procedure.repository.AcrcPymtMatchRepository;
import com.mongotest.procedure.repository.AcrcPymtUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProcPreAutoRcptService {

    @Autowired
    private AcrcPymtUploadRepository acrcPymtUploadRepository;

    @Autowired
    private AcrcPymtMatchRepository acrcPymtMatchRepository;

    private String vPYMT_NO;
    private BigDecimal dMAST_MATCH_ADV_AMT;
    private BigDecimal dTOTAL_MATCH_ADV_AMT;
    private String vFAILED_REMARKS;

    public void runPreAutoRcpt() {
        List<AcrcPymtUpload> cADV_PYMT = acrcPymtUploadRepository.findAdvPymtList();
        for (AcrcPymtUpload advPymt : cADV_PYMT) {
            vPYMT_NO = advPymt.getPymtNo();
            dMAST_MATCH_ADV_AMT = advPymt.getMatchAdvAmt() == null ? BigDecimal.ZERO : advPymt.getMatchAdvAmt();
            vFAILED_REMARKS = null;
            try {
                dTOTAL_MATCH_ADV_AMT = acrcPymtMatchRepository.sumMatchAdvAmtByPymtNo(vPYMT_NO);
                if (dTOTAL_MATCH_ADV_AMT == null) {
                    dTOTAL_MATCH_ADV_AMT = BigDecimal.ZERO;
                }
                if (dMAST_MATCH_ADV_AMT.compareTo(dTOTAL_MATCH_ADV_AMT) != 0) {
                    vFAILED_REMARKS = "Advance match amount not tally for payment no : " + vPYMT_NO;
                    failedPreAutoRcpt();
                }
                // The rest of the procedure is not yet implemented
                // You should implement the logic
            } catch (Exception e) {
                // Partial error handling
                continue;
            }
        }
    }

    private void failedPreAutoRcpt() {
        if (vFAILED_REMARKS != null) {
            // Here we could insert a log entry if needed
        }
    }
}