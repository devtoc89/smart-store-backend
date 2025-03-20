package com.smartstore.api.v1.common.utils;

import java.util.UUID;

public class StringUtils {

  private StringUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static UUID stringToUUID(String uuidStr) {
    try {
      return uuidStr == null ? null : UUID.fromString(uuidStr);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

}
