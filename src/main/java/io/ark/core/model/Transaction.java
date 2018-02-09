package io.ark.core.model;

import static io.ark.core.util.Constants.ARKTOSHI;

import org.bitcoinj.core.ECKey;

import io.ark.core.crypto.Crypto;
import io.ark.core.requests.dto.TransactionDTO;
import io.ark.core.util.Time;
import lombok.Data;

@Data
public class Transaction {

  private static final long TX_FEE = (long) (.1 * ARKTOSHI);

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

  public Transaction(String recipientId, double amount, ECKey keyPair) {
    this.keyPair = keyPair;

    this.type = 0;
    this.amount = (long) amount * ARKTOSHI;
    this.fee = TX_FEE;
    this.recipientId = recipientId;
    this.timestamp = getTime();
    this.senderPublicKey = keyPair.getPublicKeyAsHex();
  }

  public TransactionDTO convert() {
    return new TransactionDTO(id, type, timestamp, amount, fee, recipientId, senderPublicKey, signature, signSignature,
        new Asset());
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
