package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ark.core.network.response.NodeResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"port", "ip"})
@EqualsAndHashCode(of = {"port", "ip"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer implements NodeResponse {

    @Getter
    @Setter
    private String ip;

    @Getter
    @Setter
    private int port;

    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    private String os;

    @Getter
    @Setter
    private long height;

    @Getter
    @Setter
    private Status status;

    @Getter
    @Setter
    private int delay;

    enum Status {
        OK, FORK, NEW, ERESPONSE, EAPI, ENETHASH
    }
}
