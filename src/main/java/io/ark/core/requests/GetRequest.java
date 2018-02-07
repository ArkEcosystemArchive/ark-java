package io.ark.core.requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Callable;

import io.ark.core.model.Peer;

public class GetRequest extends Request implements Callable<String> {

  public GetRequest(Peer peer, Map<String, String> headers, String endpoint) {
    super(peer, headers, endpoint);
  }

  @Override
  public String call() throws Exception {
    int responseCode = conn.getResponseCode();
    
    if (responseCode != 200) {
      
    }
    
    return getResponse();
  }
  
  private String getResponse() {
    String inputLine;
    StringBuffer response = new StringBuffer();
    
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error parsing response from peer");
    }

    return response.toString();
  }


}
