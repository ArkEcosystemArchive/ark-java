package io.ark.core.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ark.core.model.Account;
import io.ark.core.model.Delegate;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {

  private boolean success;
  private Account account;
  private long fee;
  private List<Delegate> delegates;
  private String error;
  private long balance;
  private String publicKey;

}
