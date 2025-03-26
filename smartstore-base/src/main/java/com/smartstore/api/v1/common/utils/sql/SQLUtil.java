package com.smartstore.api.v1.common.utils.sql;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public class SQLUtil {
  private SQLUtil() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static String escapeLike(String value) {
    return value == null ? null
        : value.replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_");
  }
}
