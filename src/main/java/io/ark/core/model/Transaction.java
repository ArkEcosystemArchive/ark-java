package io.ark.core.model;

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
  private String vendorField;

  public Transaction(String recipientId, long amount, ECKey keyPair, long fee) {
    this(recipientId, amount, null, keyPair, fee);
  }
  
  public Transaction(String recipientId, long amount, String vendorField, ECKey keyPair, long fee) {
    this.keyPair = keyPair;

    this.type = 0;
    this.amount = amount;
    this.recipientId = recipientId;
    this.timestamp = Time.getTime();
    this.senderPublicKey = keyPair.getPublicKeyAsHex();
    this.vendorField = vendorField;
    this.fee = fee;
  }

  public TransactionDTO convert(Asset asset) {
    return TransactionDTO.builder()
        .id(id)
        .type(type)
        .timestamp(timestamp)
        .amount(amount)
        .fee(fee)
        .recipientId(recipientId)
        .senderPublicKey(senderPublicKey)
        .signature(signature)
        .signSignature(signSignature)
        .asset(asset)
        .vendorField(vendorField)
        .build();
  }

}
