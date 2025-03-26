package com.smartstore.api.v1.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CloudFrontProperties {

  @Value("${cloud.aws.cloudfront.url}")
  private String url;

  public String getUrl() {
    return url.endsWith("/") ? url : url + "/";
  }
}
