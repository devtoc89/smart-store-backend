package com.smartstore.api.v1.domain.common.wrapper;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DataWithChild<T, C> {
  private T data;
  private List<C> children;
}
