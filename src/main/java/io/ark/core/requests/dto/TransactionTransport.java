package io.ark.core.requests.dto;

import java.util.ArrayList;
import java.util.List;

import io.ark.core.model.Transaction;
import lombok.Data;

@Data
public class TransactionTransport {

  private List<TransactionDTO> transactions;

  public TransactionTransport(Transaction tx) {
    setup();
    transactions.add(tx.convert());
  }

  public TransactionTransport(List<Transaction> txs) {
    setup();
    for (Transaction tx : txs) {
      transactions.add(tx.convert());
    }
  }

  private void setup() {
    transactions = new ArrayList<TransactionDTO>();
  }

}
