package com.coredisc.common.exception.handler;

import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.exception.GeneralException;

public class NotificationReminderSettingHandler extends GeneralException {
    public NotificationReminderSettingHandler(BaseErrorCode code) { super(code); }
}
