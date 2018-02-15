package io.ark.core.network;

import io.ark.core.model.SeedPeer;
import io.ark.core.network.response.v1.Peer;
import io.ark.core.requests.GetRequest;
import io.ark.core.requests.PeerComparator;
import io.ark.core.responses.PeerResponse;
import io.ark.core.util.ResponseUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.Getter;

public class NetworkConnections {

  private static final String PEERS = "/api/peers";

  @Getter
  private Map<String, String> headers;
  
  private Queue<Peer> peers;

  public NetworkConnections(NetworkConfig config) {
    headers = new HashMap<String, String>();
    headers.put("Content-Type", "application/json");
    headers.put("nethash", config.getNethash());
    headers.put("port", Integer.toString(config.getPort()));
    headers.put("version", config.getVersion());

    List<SeedPeer> seedPeers = new ArrayList<>();
    for (String address : config.getPeers()) {
      seedPeers.add(new SeedPeer(address, config.getPort()));
    }

    List<Peer> peerList = start(seedPeers);
    this.peers = new PriorityQueue<Peer>(peerList.size(), new PeerComparator());
    this.peers.addAll(peerList);
  }
  
  public Peer getPeer() {
    return peers.peek();
  }

  private List<Peer> start(List<SeedPeer> seedPeers) {
    ExecutorService executor = Executors.newFixedThreadPool(1);

    for (SeedPeer seed : seedPeers) {
      Peer peer = Peer.builder().ip(seed.getAddress()).port(seed.getPort()).build();
      Future<String> res = executor.submit(new GetRequest(peer, headers, PEERS));

      // Don't use getResponse here because we want to be able to catch the failure.
      String jsonResponse;
      try {
        jsonResponse = res.get(500, TimeUnit.MILLISECONDS);
      } catch (InterruptedException | ExecutionException | TimeoutException e) {
        continue;
      }

      PeerResponse peerResponse = ResponseUtils.getObjectFromJson(jsonResponse, PeerResponse.class);
      
      if (!peerResponse.isSuccess()) {
        continue;
      } else {
        return peerResponse.getPeers();
      }
    }
    
    return null;
  }

}
