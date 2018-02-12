package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ark.core.network.response.NodeResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fee implements NodeResponse {

  private long send;

  private long vote;

  @JsonProperty("secondsignature")
  private long secondSignature;

  private long delegate;

  @JsonProperty("multisignature")
  private long multiSignature;

}
