package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ark.core.network.response.NodeResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"address"})
@EqualsAndHashCode(of = {"address"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements NodeResponse {

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private long unconfirmedBalance;

    @Getter
    @Setter
    private long balance;

    @Getter
    @Setter
    private String publicKey;

    @Getter
    @Setter
    private int unconfirmedSignature;

    @Getter
    @Setter
    private int secondSignature;

    @Getter
    @Setter
    private String secondPublicKey;

}
