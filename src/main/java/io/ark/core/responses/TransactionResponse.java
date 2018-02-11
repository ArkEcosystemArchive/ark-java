package io.ark.core.responses;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse {
  private boolean success;
  private String error;
  private List<String> transactionIds;
}
