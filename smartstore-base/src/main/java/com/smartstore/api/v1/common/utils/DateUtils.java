package com.smartstore.api.v1.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final ZoneId defaultZone = ZoneId.of("Asia/Seoul");

  private DateUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static LocalDateTime parse(String dateTime) {
    if (dateTime == null || dateTime.isEmpty()) {
      return null;
    }
    return LocalDateTime.parse(dateTime, FORMATTER);
  }

  public static ZonedDateTime parseWithZone(String dateTime, ZoneId zoneId) {
    if (dateTime == null || dateTime.isEmpty()) {
      return null;
    }
    return LocalDateTime.parse(dateTime, FORMATTER).atZone(zoneId);
  }

  public static ZonedDateTime parseWithDefaultZone(String dateTime) {
    if (dateTime == null || dateTime.isEmpty()) {
      return null;
    }
    return LocalDateTime.parse(dateTime, FORMATTER).atZone(defaultZone);
  }

  public static String formatWithZone(ZonedDateTime dateTime, ZoneId zoneId) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.withZoneSameInstant(zoneId).format(FORMATTER);
  }

  public static String formatWithDefaultZone(ZonedDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.withZoneSameInstant(defaultZone).format(FORMATTER);
  }
}
