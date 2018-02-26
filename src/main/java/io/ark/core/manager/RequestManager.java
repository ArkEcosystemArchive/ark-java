package io.ark.core.manager;

import static io.ark.core.util.ResponseUtils.getResponse;
import io.ark.core.network.NetworkConnections;
import io.ark.core.network.request.GetRequest;
import io.ark.core.network.request.PostRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class RequestManager {
  private final ExecutorService executor = Executors.newCachedThreadPool();

  private NetworkConnections network;

  public RequestManager(NetworkConnections connections) {
    this.network = connections;
  }

  public <T> T get(String endpoint, Class<T> clazz) throws Exception {
    Future<String> response = executor.submit(new GetRequest(network.getPeer(), network.getHeaders(), endpoint));
    return handleResponse(response, clazz);
  }

  public <T> T post(String endpoint, Class<T> clazz, Object payload) throws Exception {
    Future<String> response = executor.submit(new PostRequest(network.getPeer(), network.getHeaders(), endpoint, payload));
    return handleResponse(response, clazz);
  }
  
  private <T> T handleResponse(Future<String> response, Class<T> clazz) throws Exception {
    try {
      return getResponse(response, clazz);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      if (e.getClass().equals(TimeoutException.class)) {
        network.nextPeer();
      }
      throw e;
    }
  }
  
  public void shutdown() {
    executor.shutdown();
  }

}
