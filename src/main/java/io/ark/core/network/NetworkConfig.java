package io.ark.core.network;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkConfig {

  private int port;
  private String version;
  private int minimumNetworkReach;
  private List<String> peers;
  private String network;
  private String nethash;

}
