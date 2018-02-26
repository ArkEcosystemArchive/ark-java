package io.ark.core.network.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"address"})
@EqualsAndHashCode(of = {"address"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

  private String address;

  private long unconfirmedBalance;

  private long balance;

  private String publicKey;

  private int unconfirmedSignature;

  private int secondSignature;

  private String secondPublicKey;

}
