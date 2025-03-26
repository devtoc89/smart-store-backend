package com.smartstore.api.v1.common.utils.file;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public class FileNameUtil {

  public static String generateFileName(String originalFileName, String domain) {
    String ext = "bin";

    int i = originalFileName.lastIndexOf('.');
    if (i > 0) {
      ext = originalFileName.substring(i + 1);
    }

    String uuid = UUID.randomUUID().toString();
    String yearMonth = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));

    return String.format("%s/%s/%s.%s", domain, yearMonth, uuid, ext);
  }

  private FileNameUtil() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }
}