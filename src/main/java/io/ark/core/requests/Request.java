package io.ark.core.requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
      for (String header : headers.keySet()) {
        conn.addRequestProperty(header, headers.get(header));
      }
    } catch (Exception e) {
      throw new RuntimeException(MessageFormat.format("Request {0} failed", endpoint), e);
    }
  }

  protected String getResponse() {
    String inputLine;
    StringBuffer response = new StringBuffer();
    
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    } catch (IOException e) {
      return getErrorResponse();
    }

    return response.toString();
  }
  
  private String getErrorResponse() {
    String inputLine;
    StringBuffer response = new StringBuffer();
    
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error parsing response from peer", e);
    }

    return response.toString();
  }
  
  private String getEndpoint(Peer currentPeer, String endpoint) {
    return MessageFormat.format("{0}{1}{2}", PROTOCOL, currentPeer.get(), endpoint);
  }
}
