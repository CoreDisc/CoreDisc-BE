package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class BlockHandler extends GeneralException {
    public BlockHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
