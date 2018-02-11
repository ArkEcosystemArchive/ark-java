package io.ark.core.requests;

import io.ark.core.network.NetworkConnections;

public class Manager {

  protected RequestManager http;

  public Manager(NetworkConnections connections) {
    this.http = new RequestManager(connections);
  }

}
