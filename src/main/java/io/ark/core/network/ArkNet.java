package io.ark.core.network;

import io.ark.core.requests.AccountManager;
import io.ark.core.requests.BlockExplorer;
import lombok.Getter;

public class ArkNet {
  
  private Network network;
  private NetworkInfo networkInfo;
  private NetworkConfig networkConfig;
  
  @Getter
  private BlockExplorer blockExplorer;
  
  @Getter
  private AccountManager accountManager;
  
  public ArkNet() {
    this.network = new DevNet();
    this.networkInfo = network.getNetworkInfo();
    this.networkConfig = network.getNetworkConfig();
    this.accountManager = new AccountManager(networkConfig, networkInfo);
    this.blockExplorer = new BlockExplorer(networkConfig, networkInfo);
  }

}
