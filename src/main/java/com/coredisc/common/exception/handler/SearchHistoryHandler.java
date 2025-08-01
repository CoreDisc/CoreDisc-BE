package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class SearchHistoryHandler extends GeneralException {
    public SearchHistoryHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
