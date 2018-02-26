package io.ark.core.network.request;

import io.ark.core.network.response.v1.Peer;
import java.util.Map;
import java.util.concurrent.Callable;

public class GetRequest extends Request implements Callable<String> {

  public GetRequest(Peer peer, Map<String, String> headers, String endpoint) {
    super(peer, headers, endpoint);
  }

  @Override
  public String call() throws Exception {
    conn.getResponseCode();
    return getResponse();
  }

}
