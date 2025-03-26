package com.smartstore.api.v1.common.constants.url;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public class BaseURLConstants {
  public static final String BASE_URL = "/api/v1";

  private BaseURLConstants() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}
