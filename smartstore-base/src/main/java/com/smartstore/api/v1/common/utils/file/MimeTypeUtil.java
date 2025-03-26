package com.smartstore.api.v1.common.utils.file;

import java.util.Map;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

public class MimeTypeUtil {
  private static final Map<String, String> MIME_TYPES = Map.ofEntries(
      Map.entry("jpg", "image/jpeg"),
      Map.entry("jpeg", "image/jpeg"),
      Map.entry("png", "image/png"),
      Map.entry("gif", "image/gif"),
      Map.entry("webp", "image/webp"),
      Map.entry("pdf", "application/pdf"),
      Map.entry("txt", "text/plain"),
      Map.entry("csv", "text/csv"),
      Map.entry("json", "application/json"),
      Map.entry("html", "text/html"),
      Map.entry("css", "text/css"),
      Map.entry("js", "application/javascript"));

  public static String detectContentType(String fileName) {
    String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    return MIME_TYPES.getOrDefault(extension, "application/octet-stream");
  }

  private MimeTypeUtil() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }
}