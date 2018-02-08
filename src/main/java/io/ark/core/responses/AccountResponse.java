package io.ark.core.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.ark.core.model.Account;
import io.ark.core.model.Delegate;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {
  
  private boolean success;
  private Account account;
  private long fee;
  private List<Delegate> delegates;
  private String error;
  
}
