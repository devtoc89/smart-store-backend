package com.smartstore.api.v1.common.utils.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public class DateUtil {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final ZoneId defaultZone = ZoneId.of("Asia/Seoul");

  private DateUtil() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
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
