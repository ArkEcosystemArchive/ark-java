package io.ark.core.requests.dto;

import java.text.MessageFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BlockQueryParams {

  private Integer limit;
  private String orderBy;
  private Integer offset;
  private String generatorPublicKey;
  private Integer totalAmount;
  private Integer totalFee;
  private Integer reward;
  private String previousBlock;
  private Integer height;
  
  public String getQueryString() {
    StringBuilder sb = new StringBuilder();
    sb.append(limit != null ? getQuery("limit", limit.toString()) : "");
    sb.append(orderBy != null ? getQuery("orderBy", orderBy) : "");
    sb.append(offset != null ? getQuery("offset", offset.toString()) : "");
    sb.append(generatorPublicKey != null ? getQuery("generatorPublicKey", generatorPublicKey) : "");
    sb.append(totalAmount != null ? getQuery("totalAmount", totalAmount.toString()) : "");
    sb.append(totalFee != null ? getQuery("totalFee", totalFee.toString()) : "");
    sb.append(reward != null ? getQuery("reward", reward.toString()) : "");
    sb.append(previousBlock != null ? getQuery("previousBlock", previousBlock) : "");
    sb.append(height != null ? getQuery("height", height.toString()) : "");
    return sb.toString();
  }
  
  private String getQuery(String fieldName, String fieldValue) {
    return MessageFormat.format("?{0}={1}", fieldName, fieldValue);
  }
  
}
