package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"port", "ip"})
@EqualsAndHashCode(of = {"port", "ip"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer {

  private String ip;

  private int port;

  private String version;

  private String os;

  private long height;

  private Status status;

  private int delay;

  enum Status {
    OK, FORK, NEW, ERESPONSE, EAPI, ENETHASH
  }
}
