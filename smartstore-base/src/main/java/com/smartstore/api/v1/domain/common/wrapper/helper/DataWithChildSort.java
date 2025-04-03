package com.smartstore.api.v1.domain.common.wrapper.helper;

import java.util.Comparator;
import java.util.List;

import com.smartstore.api.v1.domain.common.wrapper.DataWithChild;

// TODO: 타입 제검토
public class DataWithChildSort {

  public static <T> Comparator<DataWithChild<T, ?>> dataWithChildComparator(
      Comparator<? super T> keyComparator) {
    return Comparator.comparing(DataWithChild<T, ?>::getData, keyComparator);
  }

  public static <T, C> void sort(List<DataWithChild<T, C>> list, Comparator<? super T> keyComparator, boolean recurse) {

    list.sort(dataWithChildComparator(keyComparator));

    if (recurse) {
      for (DataWithChild<T, ?> node : list) {
        List<?> children = node.getChildren();
        if (children != null && !children.isEmpty() && children.get(0) instanceof DataWithChild<?, ?>) {
          @SuppressWarnings("unchecked")
          List<DataWithChild<T, C>> typedChildren = (List<DataWithChild<T, C>>) children;

          // 재귀 정렬 (같은 comparator 사용)
          sort(typedChildren, (Comparator<T>) keyComparator, true);
        }
      }
    }
  }
}
