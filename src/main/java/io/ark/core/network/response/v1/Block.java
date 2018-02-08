package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ark.core.network.response.NodeResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block implements NodeResponse {

    private String id;

    private int version;

    private long timestamp;

    private String previousBlock;

    private int numberOfTransactions;

    private long totalAmount;

    private long totalFee;

    private long totalForged;

    private long reward;

    private long payloadLength;

    private String payloadHash;

    private String generatorPublicKey;

    private String generatorId;

    private String blockSignature;

    private long confirmations;

    public LocalDate getLocalDate() {
        return LocalDateTime.of(2017, 3, 21, 13, 0, 0).plusSeconds(this.timestamp).toLocalDate();
    }
}
