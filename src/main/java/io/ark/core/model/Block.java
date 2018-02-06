package io.ark.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Block {
  private String id;
  private String version;
  private int timestamp;
  private long height;
  private String previousBlock;
  private int numberOfTransactions;
  private long totalAmount;
  private long totalFee;
  private long reward;
  private int payloadLength;
  private String payloadHash;
  private String generatorPublicKey;
  private String generatorId;
  private String blockSignature;
  private long confirmations;
  private long totalForged;
}
