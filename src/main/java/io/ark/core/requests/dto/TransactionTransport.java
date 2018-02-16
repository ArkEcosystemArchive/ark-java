package io.ark.core.requests.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionTransport {

  private List<TransactionDTO> transactions;

}
