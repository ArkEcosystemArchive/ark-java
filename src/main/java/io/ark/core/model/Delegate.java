package io.ark.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Delegate {
  private String username;
  private String address;
  private String publicKey;
  private long vote;
  private long producedblocks;
  private long missedblocks;
  private int rate;
  private float approval;
  private float productivity;
}
