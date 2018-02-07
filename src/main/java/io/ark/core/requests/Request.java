package io.ark.core.requests;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

import io.ark.core.model.Peer;

public class Request {

  private static final String PROTOCOL = "http://";
  
  protected HttpURLConnection conn;
  
  public Request(Peer peer, Map<String, String> headers, String endpoint) {
    try {
      String connectionEndpoint = getEndpoint(peer, endpoint);
      conn = (HttpURLConnection) new URL(connectionEndpoint).openConnection();
    } catch (Exception e) {
      throw new RuntimeException(MessageFormat.format("Request {0} failed", endpoint), e);
    }
  }
  
  private String getEndpoint(Peer currentPeer, String endpoint) {
    return MessageFormat.format("{0}{1}{2}", PROTOCOL, currentPeer.get(), endpoint);
  }
}
