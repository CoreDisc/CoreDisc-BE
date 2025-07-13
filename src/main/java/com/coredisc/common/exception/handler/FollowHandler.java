package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class FollowHandler extends GeneralException {
    public FollowHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
