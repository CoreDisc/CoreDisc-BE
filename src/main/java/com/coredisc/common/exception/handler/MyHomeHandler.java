package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class MyHomeHandler extends GeneralException {
    public MyHomeHandler(BaseErrorCode code) {
        super(code);
    }
}
