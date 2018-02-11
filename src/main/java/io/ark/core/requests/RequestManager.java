package io.ark.core.requests;

import static io.ark.core.util.ResponseUtils.getResponse;
import io.ark.core.network.NetworkConnections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * TODO: PEER ROTATION NEEDED - Too many peers timing out.
 * 
 * This class is limited by the fact that it does not employ any kind of threading. Migrating these
 * over to requests eventually using an executor pool, and Futures.
 * 
 * Working on making requests a more simple ordeal to hide the nastiness.
 */
public class RequestManager {
  private final ExecutorService executor = Executors.newCachedThreadPool();

  private NetworkConnections network;

  public RequestManager(NetworkConnections connections) {
    this.network = connections;
  }

  public <T> T get(String endpoint, Class<T> clazz) {
    Future<String> response =
        executor.submit(new GetRequest(network.getPeer(), network.getHeaders(), endpoint));
    return getResponse(response, clazz);
  }

  public <T> T post(String endpoint, Class<T> clazz, Object payload) {
    Future<String> response = executor
        .submit(new PostRequest(network.getPeer(), network.getHeaders(), endpoint, payload));
    return getResponse(response, clazz);
  }

}
