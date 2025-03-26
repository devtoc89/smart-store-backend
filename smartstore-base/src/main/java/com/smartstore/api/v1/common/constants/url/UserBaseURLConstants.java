package com.smartstore.api.v1.common.constants.url;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public class UserBaseURLConstants {
  public static final String BASE_URL = BaseURLConstants.BASE_URL + "/user";

  private UserBaseURLConstants() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}
