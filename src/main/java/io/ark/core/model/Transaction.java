package io.ark.core.model;

import io.ark.core.crypto.Crypto;
import io.ark.core.requests.dto.TransactionDTO;
import io.ark.core.util.Time;
import lombok.Data;
import org.bitcoinj.core.ECKey;

@Data
public class Transaction {
  private String id;
  private byte type;
  private int timestamp;
  private long amount;
  private long fee;
  private String senderId;
  private String recipientId;
  private String senderPublicKey;
  private String signature;
  private String signSignature;
  private long confirmations;

  private ECKey keyPair;
  private byte[] signatureBytes;
  private byte[] signSignatureBytes;

  public Transaction(String recipientId, long amount, ECKey keyPair) {
    this.keyPair = keyPair;

    this.type = 0;
    this.amount = amount;
    this.recipientId = recipientId;
    this.timestamp = getTime();
    this.senderPublicKey = keyPair.getPublicKeyAsHex();
  }

  public TransactionDTO convert() {
    return new TransactionDTO(id, type, timestamp, amount, fee, recipientId, senderPublicKey, signature, signSignature, new Asset());
  }

  private static void validateAddress(String address, byte version) {
    if (!Crypto.validateAddress(address, version)) {
      throw new IllegalArgumentException("");
    }
  }

  private int getTime() {
    return Time.getTime();
  }

}
