package io.ark.core.network;

import io.ark.core.config.ConfigLoader;
import lombok.Getter;

public class DevNet implements Network {

  @Getter
  private NetworkConfig networkConfig;
  @Getter
  private NetworkInfo networkInfo;

  public DevNet() {
    try {
      this.networkConfig = ConfigLoader.loadNetworkConfig(ArkNetwork.DEV);
      this.networkInfo = ConfigLoader.loadNetworkInformation(ArkNetwork.DEV);
    } catch (Exception e) {
      throw new RuntimeException("Failed to instantiate ARK_DEV_NET");
    }
  }

}
