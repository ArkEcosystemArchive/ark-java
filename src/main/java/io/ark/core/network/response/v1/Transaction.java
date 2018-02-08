package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ark.core.network.response.NodeResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements NodeResponse {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    @JsonProperty("blockid")
    private String blockId;

    @Getter
    @Setter
    private int type;

    @Getter
    @Setter
    private long timestamp;

    @Getter
    @Setter
    private long amount;

    @Getter
    @Setter
    private long fee;

    @Getter
    @Setter
    private String senderId;

    @Getter
    @Setter
    private String recipientId;

    @Getter
    @Setter
    private String senderPublicKey;

    @Getter
    @Setter
    private String signatre;

    @Getter
    @Setter
    private long confirmations;

    public LocalDate getLocalDate() {
        return LocalDateTime.of(2017, 3, 21, 13, 0, 0).plusSeconds(this.timestamp).toLocalDate();
    }
}
