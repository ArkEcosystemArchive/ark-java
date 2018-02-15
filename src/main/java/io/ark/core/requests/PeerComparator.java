package io.ark.core.requests;

import io.ark.core.network.response.v1.Peer;
import java.util.Comparator;

public class PeerComparator implements Comparator<Peer> {

  @Override
  public int compare(Peer one, Peer two) {
    if (one.getDelay() < two.getDelay()) {
      return -1;
    } else if (one.getDelay() > two.getDelay()) {
      return 1;
    }
    return 0;
  }

}
