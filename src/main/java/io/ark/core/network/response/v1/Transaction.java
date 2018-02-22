package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

  private String id;

  @JsonProperty("blockid")
  private String blockId;

  private int type;

  private long timestamp;

  private long amount;

  private long fee;

  private String senderId;

  private String recipientId;

  private String senderPublicKey;

  private String signatre;

  private long confirmations;

  public LocalDate getLocalDate() {
    return LocalDateTime.of(2017, 3, 21, 13, 0, 0).plusSeconds(this.timestamp).toLocalDate();
  }
}
