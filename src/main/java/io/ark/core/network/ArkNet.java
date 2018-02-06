package io.ark.core.network;

import io.ark.core.model.Account;
import io.ark.core.requests.AccountManager;
import io.ark.core.requests.BlockExplorer;
import lombok.Getter;
import lombok.Setter;

public class ArkNet {

  @Getter
  @Setter
  private Network network;

  @Getter
  private BlockExplorer blockExplorer;

  private NetworkInfo networkInfo;
  private NetworkConfig networkConfig;

  private AccountManager accountManager;

  public ArkNet() {
    this.network = new DevNet();
    this.networkInfo = network.getNetworkInfo();
    this.networkConfig = network.getNetworkConfig();
    this.accountManager = new AccountManager(networkConfig, networkInfo);
    this.blockExplorer = new BlockExplorer(networkConfig, networkInfo);
  }

  public Account createAccount(String secret) {
    return accountManager.createAccount(secret);
  }

  public Account fetchAccount(String secret) {
    return accountManager.fetchAccount(secret);
  }

  public Account fetchAccount(String secret, String secondSecret) {
    return accountManager.fetchAccount(secret, secondSecret);
  }

  public float getDelegateFee() {
    return accountManager.getDelegateFee();
  }

}
