package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class CommentHandler extends GeneralException {

    public CommentHandler(BaseErrorCode code) {
        super(code);
    }
}
