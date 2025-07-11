package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class TermsHandler extends GeneralException {
    public TermsHandler(BaseErrorCode code) { super(code); }
}
