package io.ark.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Fees {

  private float send;
  private float vote;
  @JsonProperty("secondsignature")
  private float secondSignature;
  private float delegate;
  private float multisignature;

}
