package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class DiscHandler extends GeneralException {
    public DiscHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
