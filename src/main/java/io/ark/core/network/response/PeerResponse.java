package io.ark.core.network.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ark.core.network.response.v1.Peer;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerResponse {
  
  private boolean success;
  private String error;
  private List<Peer> peers;

}
