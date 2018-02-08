package io.ark.core.requests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
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

  private static final String PROTOCOL = "http://";

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
    
    String res;
    try {
      res = response.get();
      System.out.println(res);
    } catch (InterruptedException | ExecutionException e) {
      // TODO : retry? fail?
      throw new RuntimeException("Request failed");
    }
    
    return JsonUtils.getObjectFromJson(res, clazz);
  }

  public String post(String endpoint, String body) throws Exception {
    HttpURLConnection con;
    try {
      con = postConnection(endpoint);
    } catch (Exception e) {
      e.printStackTrace();
      pickRandomPeer(currentPeer);
      throw new RuntimeException(MessageFormat.format("POST request {0} failed", endpoint));
    }

    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(body);
    wr.flush();
    wr.close();

    int responseCode = con.getResponseCode();

    if (responseCode != 200) {
      return null;
    }

    String response = getResponse(con);
    System.out.println(response);

    return response;
  }

  public String getEndpoint(String api) {
    return MessageFormat.format("{0}{1}{2}", PROTOCOL, currentPeer.get(), api);
  }

  private String getResponse(HttpURLConnection con) throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    return response.toString();
  }

  private HttpURLConnection postConnection(String endpoint) throws Exception {
    URL url = new URL(endpoint);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");
    con.setDoOutput(true);
    setHeaders(con);
    return con;
  }

  private void setHeaders(HttpURLConnection con) {
    for (String header : headers.keySet()) {
      con.addRequestProperty(header, headers.get(header));
    }
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
