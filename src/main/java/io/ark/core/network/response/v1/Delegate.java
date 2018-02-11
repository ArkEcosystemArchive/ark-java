package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ark.core.network.response.NodeResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"username"})
@EqualsAndHashCode(of = {"username"})
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Delegate implements NodeResponse {

  private String username;

  private String address;

  private String publicKey;

  private long vote;

  @JsonProperty("producedblocks")
  private long producedBlocks;

  @JsonProperty("missedblocks")
  private long missedBlocks;

  private short rate;

  private double approval;

  private double productivity;

}
