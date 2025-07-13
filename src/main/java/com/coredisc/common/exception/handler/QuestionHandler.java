package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class QuestionHandler extends GeneralException {
    public QuestionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
