package com.smartstore.api.v1.application.admin.admin.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminToken {

  @Id
  @GeneratedValue
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id", nullable = false)
  private Admin admin;

  @Column(nullable = false, unique = true, length = 512)
  private String refreshToken;

  @Column(nullable = false)
  private ZonedDateTime expiresAt;

  public boolean isExpired() {
    return ZonedDateTime.now().isAfter(this.expiresAt);
  }
}
