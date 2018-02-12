package io.ark.core.requests;

import io.ark.core.model.Peer;
import io.ark.core.util.JsonUtils;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.Map;
import java.util.concurrent.Callable;

public class PostRequest extends Request implements Callable<String> {

  private Object payload;

  public PostRequest(Peer peer, Map<String, String> headers, String endpoint, Object payload) {
    super(peer, headers, endpoint);
    try {
      conn.setRequestMethod("POST");
    } catch (ProtocolException e) {
      // This will never happen.
    }
    conn.setDoOutput(true);
    this.payload = payload;
  }

  @Override
  public String call() throws Exception {
    doPost();
    conn.getResponseCode();
    return getResponse();
  }

  private void doPost() throws IOException {
    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
    wr.writeBytes(JsonUtils.getObjectAsJson(payload));
    wr.flush();
    wr.close();
  }

}
