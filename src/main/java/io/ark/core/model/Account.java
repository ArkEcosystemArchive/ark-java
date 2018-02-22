package io.ark.core.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bitcoinj.core.ECKey;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
  private String address;
  private long unconfirmedBalance;
  private long balance;
  private String publicKey;
  private int unconfirmedSignature;
  private int secondSignature;
  private String secondPublicKey;
  private List<String> multiSignatures;
  private List<String> uMultiSignatures;

  private ECKey keyPair;
  private ECKey secondKeyPair;

  public static Account defaultAccount(String address) {
    return Account.builder()
        .address(address)
        .balance(0L)
        .unconfirmedBalance(0L)
        .publicKey(null)
        .unconfirmedSignature(0)
        .secondSignature(0)
        .secondPublicKey(null)
        .multiSignatures(new ArrayList<String>())
        .uMultiSignatures(new ArrayList<String>())
        .build();
  }

}
