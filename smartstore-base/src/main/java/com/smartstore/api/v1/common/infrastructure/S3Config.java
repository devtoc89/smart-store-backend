package com.smartstore.api.v1.common.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

  @Value("${cloud.aws.s3.credentials.access-key:}")
  private String accessKey;

  @Value("${cloud.aws.s3.credentials.secret-key:}")
  private String secretKey;

  @Value("${cloud.aws.s3.credentials.region}")
  private String region;

  private boolean hasStaticKey() {
    return accessKey != null && !accessKey.isBlank() &&
        secretKey != null && !secretKey.isBlank();
  }

  @Bean
  public S3Client s3Client() {
    Region awsRegion = Region.of(region);

    if (hasStaticKey()) {
      return S3Client.builder()
          .region(awsRegion)
          .credentialsProvider(StaticCredentialsProvider.create(
              AwsBasicCredentials.create(accessKey, secretKey)))
          .build();
    }

    return S3Client.builder()
        .region(awsRegion)
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
  }

  @Bean
  public S3Presigner s3Presigner() {
    Region awsRegion = Region.of(region);

    if (hasStaticKey()) {
      return S3Presigner.builder()
          .region(awsRegion)
          .credentialsProvider(StaticCredentialsProvider.create(
              AwsBasicCredentials.create(accessKey, secretKey)))
          .build();
    }

    return S3Presigner.builder()
        .region(awsRegion)
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
  }
}
