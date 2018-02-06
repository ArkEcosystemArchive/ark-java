package io.ark.core.network;

import io.ark.core.config.ConfigLoader;
import lombok.Getter;

public class MainNet implements Network {

  @Getter
  private NetworkConfig networkConfig;
  @Getter
  private NetworkInfo networkInfo;

  public MainNet() {
    try {
      this.networkConfig = ConfigLoader.loadNetworkConfig(ArkNetwork.MAIN);
      this.networkInfo = ConfigLoader.loadNetworkInformation(ArkNetwork.MAIN);
    } catch (Exception e) {
      throw new RuntimeException("Failed to instantiate ARK_MAIN_NET");
    }
  }

}
