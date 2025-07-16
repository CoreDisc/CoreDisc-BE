package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class ReportStatsHandler extends GeneralException {
    public ReportStatsHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
