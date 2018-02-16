package io.ark.core.requests;

import static io.ark.core.util.ResponseUtils.getResponse;
import io.ark.core.network.NetworkConnections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RequestManager {
  private final ExecutorService executor = Executors.newCachedThreadPool();

  private NetworkConnections network;

  public RequestManager(NetworkConnections connections) {
    this.network = connections;
  }

  public <T> T get(String endpoint, Class<T> clazz) throws Exception {
    Future<String> response = executor.submit(new GetRequest(network.getPeer(), network.getHeaders(), endpoint));
    return getResponse(response, clazz);
  }

  public <T> T post(String endpoint, Class<T> clazz, Object payload) throws Exception {
    Future<String> response = executor.submit(new PostRequest(network.getPeer(), network.getHeaders(), endpoint, payload));
    return getResponse(response, clazz);
  }

}
