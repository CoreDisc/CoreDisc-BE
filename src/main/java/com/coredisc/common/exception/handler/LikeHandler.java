package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class LikeHandler extends GeneralException {

  public LikeHandler(BaseErrorCode code) {
    super(code);
  }
}
