package io.ark.core.requests;

import io.ark.core.crypto.Crypto;
import io.ark.core.model.Account;
import io.ark.core.model.Asset;
import io.ark.core.model.Transaction;
import io.ark.core.network.NetworkConnections;
import io.ark.core.network.response.v1.Fee;
import io.ark.core.requests.dto.TransactionTransport;
import io.ark.core.responses.BlockExplorerResponse;
import io.ark.core.responses.TransactionResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class TransactionManager extends Manager {

  private static final String sendTransaction = "/peer/transactions";
  private static final String getFees = "/api/blocks/getFees";
  
  private Fee fees;

  public TransactionManager(NetworkConnections connections) {
    super(connections);
    try {
      BlockExplorerResponse feesResponse = http.get(getFees, BlockExplorerResponse.class);
      this.fees = feesResponse.getFees();
    } catch (Exception e) {
      // Load fees hard coded
    }
    
  }
  
  public String sendTransaction(String address, long amount, Account sender) throws InterruptedException, ExecutionException, TimeoutException {
    return sendTransaction(address, amount, null, sender);
  }

  public String sendTransaction(String address, long amount, String vendorField, Account sender) throws InterruptedException, ExecutionException, TimeoutException {
    Transaction tx = new Transaction(address, amount, vendorField, sender.getKeyPair(), fees.getSend());

    Crypto.sign(tx, sender.getKeyPair());

    if (sender.getSecondKeyPair() != null) {
      Crypto.secondSign(tx, sender.getSecondKeyPair());
    }

    Crypto.setId(tx);

    List<Transaction> txList = Arrays.asList(tx);
    TransactionTransport txTransport = new TransactionTransport(txList.stream().map(s -> s.convert(new Asset())).collect(Collectors.toList()));
    TransactionResponse res = doPostRequest(sendTransaction, txTransport);
    
    if (!res.isSuccess()) {
      throw new RuntimeException(res.getError());
    }

    return res.getTransactionIds().get(0);
  }
  
  private TransactionResponse doPostRequest(String endpoint, Object payload) {
    try {
      return http.post(endpoint, TransactionResponse.class, payload);
    } catch (Exception e) {
      return TransactionResponse.builder().success(false).build();
    }
  }

}
