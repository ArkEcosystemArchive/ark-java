package io.ark.core.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDTO {
  private String id;
  private byte type;
  private int timestamp;
  private long amount;
  private long fee;
  private String recipientId;
  private String senderPublicKey;
  private String signature;
  private String signSignature;
  private Object asset;
}
