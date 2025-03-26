package com.smartstore.api.v1.common.filter;

import java.util.UUID;

import com.smartstore.api.v1.common.constants.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminUserHolder {
  private UUID id;
  private Role role;
}
