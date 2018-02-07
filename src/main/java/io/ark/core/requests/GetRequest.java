package io.ark.core.requests;

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

}
