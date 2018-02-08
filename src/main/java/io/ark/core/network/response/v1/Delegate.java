package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ark.core.network.response.NodeResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"username"})
@EqualsAndHashCode(of = {"username"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Delegate implements NodeResponse {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String publicKey;

    @Getter
    @Setter
    private long vote;

    @Getter
    @Setter
    @JsonProperty("producedblocks")
    private long producedBlocks;

    @Getter
    @Setter
    @JsonProperty("missedblocks")
    private long missedBlocks;

    @Getter
    @Setter
    private short rate;

    @Getter
    @Setter
    private double approval;

    @Getter
    @Setter
    private double productivity;

}
