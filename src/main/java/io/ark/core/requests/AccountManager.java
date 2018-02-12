package io.ark.core.requests;

import io.ark.core.crypto.Crypto;
import io.ark.core.model.Account;
import io.ark.core.model.Delegate;
import io.ark.core.network.NetworkConfig;
import io.ark.core.network.NetworkInfo;
import io.ark.core.responses.AccountResponse;
import java.util.Arrays;
import java.util.List;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

public class AccountManager extends Manager {

  private static final String getBalance = "/api/accounts/getBalance?address=";
  private static final String getPublicKey = "/api/accounts/getPublicKey?address=";
  private static final String getDelegateFee = "/api/accounts/delegates/fee";
  private static final String getDelegates = "/api/accounts/delegates?address=";
  private static final String getAccount = "/api/accounts?address=";

  public AccountManager(NetworkConfig config, NetworkInfo info) {
    super(config, info);
  }

  public String createPassphrase() {
    return String.join(" ", Crypto.getPassphrase());
  }

  public long getBalance(String address) {
    AccountResponse res = doRequest(getBalance + address);

    if (res.isSuccess()) {

    }

    return res.getBalance();
  }

  public String getPublicKey(String address) {
    AccountResponse res = doRequest(getPublicKey + address);

    if (res.isSuccess()) {

    }

    return res.getPublicKey();
  }

  public long getDelegateFee() {
    AccountResponse res = doRequest(getDelegateFee);

    if (res.isSuccess()) {

    }

    return res.getFee();
  }

  public List<Delegate> getDelegates(String address) {
    AccountResponse res = doRequest(getDelegates + address);

    if (!res.isSuccess()) {

    }

    return res.getDelegates();
  }

  public Account createAccount(String secret) {
    return getAccount(secret);
  }

  public Account getAccount(String secret, String secondSecret) {
    Account account = getAccount(secret);
    account.setSecondKeyPair(Crypto.getKeys(secondSecret));
    return account;
  }

  public Account getAccount(String secret) {
    try {
      MnemonicCode.INSTANCE.check(Arrays.asList(secret.split(" ")));
    } catch (MnemonicException e) {
      throw new RuntimeException("Passphrase is not valid BIP39 mnemonic phrase.", e);
    }

    ECKey keyPair = Crypto.getKeys(secret);
    String address = Crypto.getAddress(keyPair.getPubKey(), info.getPubKeyHash());

    Account account = _getAccount(address);

    account.setKeyPair(keyPair);
    return account;
  }

  private Account _getAccount(String address) {
    AccountResponse res = doRequest(getAccount + address);

    if (!res.isSuccess()) {
      if (res.getError().equals("Account not found")) {
        return Account.defaultAccount(address);
      }
      throw new RuntimeException("Error getting account");
    }

    return res.getAccount();
  }

  private AccountResponse doRequest(String endpoint) {
    return http.getFuture(endpoint, AccountResponse.class);
  }

}
