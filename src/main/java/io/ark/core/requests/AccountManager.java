package io.ark.core.requests;

import static io.ark.core.util.Constants.ARKTOSHI;

import java.util.Arrays;
import java.util.List;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import io.ark.core.config.Options;
import io.ark.core.crypto.Crypto;
import io.ark.core.model.Account;
import io.ark.core.model.Delegate;
import io.ark.core.network.NetworkConfig;
import io.ark.core.network.NetworkInfo;
import io.ark.core.responses.AccountResponse;

public class AccountManager extends Manager {

  private static final String getAccount = "/api/accounts?address=";
  private static final String getDelegateFee = "/api/accounts/delegates/fee";
  private static final String getDelegates = "/api/accounts/delegates?address=";

  // TODO : Documented on Swagger API docs, always gives 500 response.
  // private static final String getTopAccounts =
  // "/api/accounts/top?limit={0}&offset={1}";

  private TransactionManager txManager;

  public AccountManager(NetworkConfig config, NetworkInfo info) {
    super(config, info);
    this.txManager = new TransactionManager(config, info);
  }

  public List<String> createPassphrase() {
    return Crypto.getPassphrase();
  }
  
  public Account fetchAccount(String secret) {
    return createAccount(secret);
  }

  public Account fetchAccount(String secret, String secondSecret) {
    Account account = createAccount(secret);
    account.setSecondKeyPair(Crypto.getKeys(secondSecret, new Options()));
    return account;
  }

  public Account createAccount(List<String> secret) {
    return createAccount(String.join(" ", secret));
  }
  
  public Account createAccount(String secret) {
    try {
      MnemonicCode.INSTANCE.check(Arrays.asList(secret.split(" ")));
    } catch (MnemonicException e) {
      throw new RuntimeException("Passphrase is not valid BIP39 mnemonic phrase.", e);
    }
    
    ECKey keyPair = Crypto.getKeys(secret, new Options());
    String address = Crypto.getAddress(keyPair.getPubKey(), info.getPubKeyHash());
    
    Account account = getAccount(address);

    account.setKeyPair(keyPair);
    account.setInfo(info);
    account.setTxManager(txManager);
    account.setAccManager(this);
    return account;
  }

  public double getDelegateFee() {
    AccountResponse res = doRequest(getDelegateFee);
    
    if (res.isSuccess()) {
      
    }
    
    return (double) res.getFee() / ARKTOSHI;
  }

  public List<Delegate> getDelegates(String address) {
    AccountResponse res = doRequest(getDelegates + address);

    if (!res.isSuccess()) {
      
    }

    return res.getDelegates();
  }

  private Account getAccount(String address) {
    AccountResponse res = doRequest(getAccount + address);
    
    if (!res.isSuccess()) {
      if (res.getError().equals("Account not found")) {
        return Account.defaultAccount(address);
      }
    }

    return res.getAccount();
  }
  
  private AccountResponse doRequest(String endpoint) {
    return http.getFuture(endpoint, AccountResponse.class);
  }

}
