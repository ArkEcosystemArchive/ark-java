package io.ark.core.network;

import io.ark.core.config.ConfigLoader;
import io.ark.core.manager.AccountManager;
import io.ark.core.manager.BlockExplorer;
import io.ark.core.manager.Manager;
import io.ark.core.manager.TransactionManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
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
  
  private List<Manager> managers;

  public ArkNet(ArkNetwork network) {
    try {
      networkConfig = ConfigLoader.loadNetworkConfig(network);
      networkInfo = ConfigLoader.loadNetworkInformation(network);
      connections = new NetworkConnections(networkConfig);
    } catch (Exception e) {
      throw new RuntimeException(MessageFormat.format("Failed to instantiate ARK_{0}_NET", network.name()), e);
    }

    accountManager = new AccountManager(connections, networkInfo);
    blockExplorer = new BlockExplorer(connections);
    transactionManager = new TransactionManager(connections);
    
    managers = new ArrayList<>();
    managers.add(blockExplorer);
    managers.add(accountManager);
    managers.add(transactionManager);
  }
  
  public void shutdown() {
    for (Manager manager : managers) {
      manager.shutdown();
    }
  }

}
