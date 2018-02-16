package io.ark.core.requests.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
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
  private String vendorField;
}
