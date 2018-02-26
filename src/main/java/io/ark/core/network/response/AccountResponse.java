package io.ark.core.network.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ark.core.model.Account;
import io.ark.core.model.Delegate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
