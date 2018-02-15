package io.ark.core.requests;

import io.ark.core.network.response.v1.Peer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

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
    String response;
    try {
      response = getGenericResponse(conn.getInputStream());
    } catch (IOException e) {
      try {
        response = getGenericResponse(conn.getErrorStream());
      } catch (IOException ex) {
        throw new RuntimeException("Could not parse response", ex);
      }
    }
    return response;
  }

  private String getGenericResponse(InputStream stream) throws IOException {
    String inputLine;
    StringBuffer response = new StringBuffer();

    try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    } catch (IOException e) {
      throw new IOException(e);
    }

    return response.toString();
  }

  private String getEndpoint(Peer currentPeer, String endpoint) {
    return MessageFormat.format("{0}{1}:{2}{3}", PROTOCOL, currentPeer.getIp(), Integer.toString(currentPeer.getPort()), endpoint);
  }
}
