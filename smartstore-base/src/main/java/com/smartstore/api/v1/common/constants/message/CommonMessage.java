package com.smartstore.api.v1.common.constants.message;

public class CommonMessage {

  public static final String CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG = "This is a constants class and cannot be instantiated";
  public static final String CANNOT_INITIALIZE_UTIL_CLASS_MSG = "This is a utility class and cannot be instantiated";

  private CommonMessage() {
    throw new UnsupportedOperationException(CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}
