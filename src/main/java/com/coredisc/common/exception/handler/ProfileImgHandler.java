package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class ProfileImgHandler extends GeneralException {
    public ProfileImgHandler(BaseErrorCode code) {
        super(code);
    }
}
