package io.ark.core.network;

import io.ark.core.model.Bip32;
import io.ark.core.model.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkInfo {

  private String messagePrefix;
  private Bip32 bip32;
  private byte pubKeyHash;
  private String wif;
  private Client client;

}
