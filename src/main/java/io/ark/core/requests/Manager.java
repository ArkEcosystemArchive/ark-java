package io.ark.core.requests;

import io.ark.core.network.NetworkConfig;
import io.ark.core.network.NetworkInfo;

public class Manager {

  protected HttpUtils http;
  protected NetworkConfig config;
  protected NetworkInfo info;

  public Manager(NetworkConfig config, NetworkInfo info) {
    this.config = config;
    this.info = info;
    this.http = new HttpUtils(config);
  }

}
