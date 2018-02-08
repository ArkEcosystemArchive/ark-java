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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block implements NodeResponse {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private int version;

    @Getter
    @Setter
    private long timestamp;

    @Getter
    @Setter
    private String previousBlock;

    @Getter
    @Setter
    private int numberOfTransactions;

    @Getter
    @Setter
    private long totalAmount;

    @Getter
    @Setter
    private long totalFee;

    @Getter
    @Setter
    private long totalForged;

    @Getter
    @Setter
    private long reward;

    @Getter
    @Setter
    private long payloadLength;

    @Getter
    @Setter
    private String payloadHash;

    @Getter
    @Setter
    private String generatorPublicKey;

    @Getter
    @Setter
    private String generatorId;

    @Getter
    @Setter
    private String blockSignature;

    @Getter
    @Setter
    private long confirmations;

    public LocalDate getLocalDate() {
        return LocalDateTime.of(2017, 3, 21, 13, 0, 0).plusSeconds(this.timestamp).toLocalDate();
    }
}
