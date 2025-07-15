package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class ReportStatHandler extends GeneralException {
    public ReportStatHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
