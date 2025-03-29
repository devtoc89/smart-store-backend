package com.smartstore.api.v1.domain.admin.entity;

import java.time.ZonedDateTime;

import com.smartstore.api.v1.common.constants.enums.Role;
import com.smartstore.api.v1.common.domain.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "admins", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "email" })
})
public class Admin extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role = Role.ADMIN;

  @Builder.Default
  @Column(nullable = false)
  private Boolean isActivated = false;

  @Builder.Default
  @Column(nullable = false)
  private Integer loginFailCount = 0;

  private ZonedDateTime lastLoginAt;

  @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private AdminToken token;
}