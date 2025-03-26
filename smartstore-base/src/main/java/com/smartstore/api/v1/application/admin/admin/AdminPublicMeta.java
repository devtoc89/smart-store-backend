package com.smartstore.api.v1.application.admin.admin;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public final class AdminPublicMeta {
  public static final String SIGNUP_FULL_PATH = Config.BASE_URL + Config.SIGNUP_PATH;
  public static final String LOGIN_FULL_PATH = Config.BASE_URL + Config.LOGIN_PATH;

  private AdminPublicMeta() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}