package io.ark.core.model;

import lombok.Data;

@Data
public class Fees {

  private float send;
  private float vote;
  private float secondsignature;
  private float delegate;
  private float multisignature;

}
