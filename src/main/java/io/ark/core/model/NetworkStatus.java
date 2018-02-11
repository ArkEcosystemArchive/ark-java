package io.ark.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NetworkStatus {

  private String epoch;
  private long height;
  private String nethash;
  private long fee;
  private int milestone;
  private int reward;
  private long supply;

}
