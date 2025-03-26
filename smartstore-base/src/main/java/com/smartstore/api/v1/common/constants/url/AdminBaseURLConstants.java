package com.smartstore.api.v1.common.constants.url;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public class AdminBaseURLConstants {
  public static final String BASE_URL = BaseURLConstants.BASE_URL + "/admin";

  private AdminBaseURLConstants() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}
