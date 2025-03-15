package com.smartstore.api.v1.common.utils;

public class SQLUtils {
  private SQLUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static String escapeLike(String value) {
    return value == null ? null
        : value.replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_");
  }
}
