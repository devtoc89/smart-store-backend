package com.smartstore.api.v1.common.utils.string;

import java.util.UUID;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public class StringUtil extends org.springframework.util.StringUtils {

  private StringUtil() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static UUID stringToUUIDOrNew(String uuidStr) {
    try {
      return uuidStr == null ? UUID.randomUUID() : UUID.fromString(uuidStr);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public static UUID stringToUUID(String uuidStr) {
    try {
      return uuidStr == null ? null : UUID.fromString(uuidStr);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public static UUID stringToUUIDOrThrow(String uuidStr) {
    try {
      return UUID.fromString(uuidStr);
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new IllegalArgumentException("유효하지 않은 UUID 형식입니다: " + uuidStr);
    }
  }

}
