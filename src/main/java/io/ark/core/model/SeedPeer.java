package io.ark.core.model;

import java.text.MessageFormat;
import lombok.Getter;

public class SeedPeer {

  @Getter
  private String address;
  @Getter
  private int port;

  public SeedPeer(String address, int port) {
    this.address = address;
    this.port = port;
  }

  public String get() {
    return MessageFormat.format("{0}:{1}", address, Integer.toString(port));
  }

}
