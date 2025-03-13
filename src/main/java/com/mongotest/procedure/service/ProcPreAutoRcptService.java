package com.mongotest.procedure.service;

import com.mongotest.procedure.entity.AcrcPymtMatch;
import com.mongotest.procedure.entity.AcrcPymtUpload;
import com.mongotest.procedure.entity.SapsLog;
import com.mongotest.procedure.entity.SapsProcess;
import com.mongotest.procedure.entity.ViewAcrcMatchDoc;
import com.mongotest.procedure.repository.AcrcPymtMatchRepository;
import com.mongotest.procedure.repository.AcrcPymtUploadRepository;
import com.mongotest.procedure.repository.SapsLogRepository;
import com.mongotest.procedure.repository.SapsProcessRepository;
import com.mongotest.procedure.repository.ViewAcrcMatchDocRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProcPreAutoRcptService {

    @Autowired
    private AcrcPymtUploadRepository acrcPymtUploadRepository;

    @Autowired
    private AcrcPymtMatchRepository acrcPymtMatchRepository;

    @Autowired
    private ViewAcrcMatchDocRepository viewAcrcMatchDocRepository;

    @Autowired
    private SapsLogRepository sapsLogRepository;

    @Autowired
    private SapsProcessRepository sapsProcessRepository;

    private String vPYMT_NO;
    private String vMATCH_DOC;
    private String vPOL_NO;
    private BigDecimal dMAST_MATCH_ADV_AMT;
    private BigDecimal dDET_MATCH_ADV_AMT;
    private BigDecimal dTOTAL_MATCH_ADV_AMT;
    private String vPRODUCT_TYPE;
    private String vMATCH_TYPE;
    private String vADV_MATCH_DOC;
    private String vADV_MATCH_TYPE;
    private String vPROCESS_NAME = "PROC_PRE_AUTO_RCPT";
    private String vDEBUG = "0";
    private int bNOT_FOUND = 0;
    private int SQLCODE = 0;
    private String vERROR_CODE;
    private String vERROR_MESSAGE;
    private String vFAILED_REMARKS;
    private String vSQLERR;

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

                List<AcrcPymtMatch> cPYMT_MATCH = acrcPymtMatchRepository.findByPymtNo(vPYMT_NO);
                for (AcrcPymtMatch pymtMatch : cPYMT_MATCH) {
                    vADV_MATCH_DOC = pymtMatch.getMatchDoc();
                    vADV_MATCH_TYPE = pymtMatch.getMatchType();
                    dDET_MATCH_ADV_AMT = pymtMatch.getMatchAdvAmt();

                    Optional<ViewAcrcMatchDoc> matchDoc =
                        viewAcrcMatchDocRepository.findByMatchTypeAndMatchDoc(vADV_MATCH_TYPE, vADV_MATCH_DOC);
                    if (!matchDoc.isPresent()) {
                        bNOT_FOUND = 1;
                        continue;
                    }
                    vMATCH_DOC = null;
                    vPOL_NO = null;
                    bNOT_FOUND = 0;

                    try {
                        vMATCH_DOC = matchDoc.get().getMatchDoc();
                        vPOL_NO = matchDoc.get().getPolNo();

                        if (vMATCH_DOC != null && vPOL_NO != null) {
                            Optional<ViewAcrcMatchDoc> docMatch =
                                viewAcrcMatchDocRepository.findByMatchTypeAndMatchDoc(vMATCH_TYPE, vPOL_NO);
                            if (docMatch.isPresent()) {
                                vMATCH_TYPE = docMatch.get().getMatchType();
                                vPRODUCT_TYPE = docMatch.get().getProdType();
                            } else {
                                bNOT_FOUND = 1;
                                continue;
                            }

                            acrcPymtMatchRepository.updateMatchDetails(vPYMT_NO, vPOL_NO, dDET_MATCH_ADV_AMT, dDET_MATCH_ADV_AMT.negate(), vMATCH_TYPE , vPRODUCT_TYPE);
                            acrcPymtUploadRepository.updateMatchDetails(vPYMT_NO, dDET_MATCH_ADV_AMT, dDET_MATCH_ADV_AMT.negate());
                            updateProcessStatus(vPROCESS_NAME, 'S');
                        }
                    } catch (Exception e) {
                        logError("Error in matching documents", e);
                        updateProcessStatus(vPROCESS_NAME, 'F');
                    }
                }
            } catch (Exception e) {
                logError("General Error in procedure", e);
                updateProcessStatus(vPROCESS_NAME, 'F');
                continue;
            }
        }
    }

    private void failedPreAutoRcpt() {
        if (vFAILED_REMARKS != null) {
            sapsLogRepository.save(new SapsLog(vPROCESS_NAME, vFAILED_REMARKS));
        }
    }

    private void logError(String message, Exception e) {
        vERROR_CODE = e.getClass().getSimpleName();
        vSQLERR = e.getMessage();

        String errorMessage = message + ": " + e.getMessage();
        sapsLogRepository.save(new SapsLog(vPROCESS_NAME, errorMessage));

        sapsLogRepository.save(new SapsLog(vPROCESS_NAME, "ERROR_CODE: " + vERROR_CODE + ", SQLERR: " + vSQLERR));

        procGenAccLog(vPYMT_NO, vPROCESS_NAME, errorMessage, "E", vERROR_CODE);
    }

    private void procGenAccLog(String pymtNo, String processName, String errorMessage, String logType, String errorCode) {
        System.out.println("Logging Error: ");
        System.out.println("PYMT_NO: " + pymtNo);
        System.out.println("PROCESS_NAME: " + processName);
        System.out.println("ERROR_MESSAGE: " + errorMessage);
        System.out.println("LOG_TYPE: " + logType);
        System.out.println("ERROR_CODE: " + errorCode);
    }

    private void updateProcessStatus(String processName, Character status) {
        Optional<SapsProcess> sapsProcessOpt = sapsProcessRepository.findById(processName);
        if (sapsProcessOpt.isPresent()) {
            SapsProcess sapsProcess = sapsProcessOpt.get();
            sapsProcess.setLastEndDate(LocalDateTime.now());
            sapsProcess.setLastRunStat(status);
            sapsProcessRepository.save(sapsProcess);
        }
    }
}