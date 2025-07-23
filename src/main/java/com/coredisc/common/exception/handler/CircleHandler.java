package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class CircleHandler extends GeneralException {
    public CircleHandler(BaseErrorCode code) { super(code); }
}
