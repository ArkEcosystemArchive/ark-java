package io.ark.core.model;

import java.text.MessageFormat;
import lombok.Getter;

public class Peer {

  @Getter
  private String address;
  @Getter
  private int port;
  private long lastSeen;
  private long lastResponseTime;

  public Peer(String address, int port) {
    this.address = address;
    this.port = port;
  }

  public String get() {
    return MessageFormat.format("{0}:{1}", address, Integer.toString(port));
  }

}
