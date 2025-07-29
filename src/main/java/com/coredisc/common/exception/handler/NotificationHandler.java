package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class NotificationHandler extends GeneralException {
    public NotificationHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
