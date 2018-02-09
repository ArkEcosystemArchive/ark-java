package io.ark.core.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.ark.core.model.Peer;
import io.ark.core.network.NetworkConfig;
import io.ark.core.util.JsonUtils;

/**
 * TODO: PEER ROTATION NEEDED - Too many peers timing out.
 * 
 * This class is limited by the fact that it does not employ any kind of threading.
 * Migrating these over to requests eventually using an executor pool, and Futures.
 * 
 * Working on making requests a more simple ordeal to hide the nastiness.
 */
public class HttpUtils {
  private final ExecutorService executor = Executors.newFixedThreadPool(5);
  
  private Map<String, String> headers;
  private List<Peer> peers;
  private Peer currentPeer;

  public HttpUtils(NetworkConfig config) {
    peers = new ArrayList<Peer>();
    for (String address : config.getPeers()) {
      peers.add(new Peer(address, config.getPort()));
    }
    headers = new HashMap<String, String>();
    headers.put("Content-Type", "application/json");
    headers.put("nethash", config.getNethash());
    headers.put("port", Integer.toString(config.getPort()));
    headers.put("version", config.getVersion());
    pickRandomPeer(null);
  }
  
  public <T> T getFuture(String endpoint, Class<T> clazz) {
    Future<String> response = executor.submit(new GetRequest(currentPeer, headers, endpoint));
    return getResponse(response, clazz);
  }
  
  public <T> T postFuture(String endpoint, Class<T> clazz, Object payload) {
    Future<String> response = executor.submit(new PostRequest(currentPeer, headers, endpoint, payload));
    return getResponse(response, clazz);
  }
  
  private <T> T getResponse(Future<String> response, Class<T> clazz) {
    String res;
    try {
      res = response.get();
    } catch (InterruptedException | ExecutionException e) {
      // TODO : retry? fail?
      throw new RuntimeException("Request failed.", e);
    }
    return JsonUtils.getObjectFromJson(res, clazz);
  }

  private void pickRandomPeer(Peer peer) {
    if (peer != null) {
      System.out.println("Removing peer: " + peer.getAddress());
      peers.remove(peer);
    }
    Random r = new Random();
    currentPeer = peers.get(r.nextInt(peers.size()));
  }

}
