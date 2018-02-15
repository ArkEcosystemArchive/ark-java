package io.ark.core.network;

import io.ark.core.config.ConfigLoader;
import io.ark.core.requests.AccountManager;
import io.ark.core.requests.BlockExplorer;
import io.ark.core.requests.TransactionManager;
import java.text.MessageFormat;
import lombok.Getter;

public class ArkNet {

  protected NetworkConfig networkConfig;
  protected NetworkInfo networkInfo;
  protected NetworkConnections connections;

  @Getter
  protected BlockExplorer blockExplorer;
  @Getter
  protected AccountManager accountManager;
  @Getter
  protected TransactionManager transactionManager;

  public ArkNet(ArkNetwork network) {
    try {
      this.networkConfig = ConfigLoader.loadNetworkConfig(network);
      this.networkInfo = ConfigLoader.loadNetworkInformation(network);
      this.connections = new NetworkConnections(networkConfig);
    } catch (Exception e) {
      throw new RuntimeException(MessageFormat.format("Failed to instantiate ARK_{0}_NET", network.name()), e);
    }

    this.accountManager = new AccountManager(connections, networkInfo);
    this.blockExplorer = new BlockExplorer(connections);
    this.transactionManager = new TransactionManager(connections);
  }

}
