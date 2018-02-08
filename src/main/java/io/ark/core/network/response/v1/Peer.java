package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ark.core.network.response.NodeResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"port", "ip"})
@EqualsAndHashCode(of = {"port", "ip"})
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer implements NodeResponse {

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
