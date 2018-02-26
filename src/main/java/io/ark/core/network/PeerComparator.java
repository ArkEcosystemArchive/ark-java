package io.ark.core.network;

import io.ark.core.network.response.v1.Peer;
import java.util.Comparator;

public class PeerComparator implements Comparator<Peer> {

  @Override
  public int compare(Peer one, Peer two) {
    if (version(one) > version(two)) {
      return -1;
    } else if (version(one) < version(two)) {
      return 1;
    } else if (one.getHeight() > two.getHeight()) {
      return -1;
    } else if (one.getHeight() < two.getHeight()) {
      return 1;
    } else if (one.getDelay() < two.getDelay()) {
      return -1;
    } else if (one.getDelay() > two.getDelay()) {
      return 1;
    }
    return 0;
  }

  private int version(Peer peer) {
    return Integer.valueOf(peer.getVersion().replaceAll(".", ""));
  }
  
}
