package io.ark.core.requests;

import static io.ark.core.requests.AccessType.ARRAY;
import static io.ark.core.requests.AccessType.FLOAT;
import static io.ark.core.requests.AccessType.OBJECT;
import static io.ark.core.util.Constants.ARKTOSHI;

import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.ECKey;
import org.json.JSONArray;

import io.ark.core.config.Options;
import io.ark.core.crypto.Crypto;
import io.ark.core.exception.ArkNodeException;
import io.ark.core.model.Account;
import io.ark.core.model.Delegate;
import io.ark.core.network.NetworkConfig;
import io.ark.core.network.NetworkInfo;
import io.ark.core.util.JsonUtils;

public class AccountManager extends Manager {

  private static final String createAccount = "/api/accounts?address=";
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

  public Account fetchAccount(String secret) {
    return createAccount(secret);
  }

  public Account fetchAccount(String secret, String secondSecret) {
    Account account = createAccount(secret);
    account.setSecondKeyPair(Crypto.getKeys(secondSecret, new Options()));
    return account;
  }

  public Account createAccount(String secret) {
    ECKey keyPair = Crypto.getKeys(secret, new Options());
    String address = Crypto.getAddress(keyPair.getPubKey(), info.getPubKeyHash());

    Account account = null;

    try {
      account = getAccount(address);
    } catch (Exception e) {
      // TODO : errors
      e.printStackTrace();
    }

    account.setKeyPair(keyPair);
    account.setInfo(info);
    account.setTxManager(txManager);
    account.setAccManager(this);
    return account;
  }

  public float getDelegateFee() {
    float fee = http.get(getDelegateFee, "fee", float.class, FLOAT);
    return fee / ARKTOSHI;
  }

  public List<Delegate> getDelegates(String address) {
    JSONArray delegates = null;
    try {
      delegates = http.get(getDelegates + address, "delegates", JSONArray.class, ARRAY);
    } catch (ArkNodeException ex) {

    }

    if (delegates == null) {
      return null;
    }

    List<Delegate> delegateList = new ArrayList<Delegate>();
    for (int i = 0; i < delegates.length(); i++) {
      delegateList.add(JsonUtils.getObjectFromJson(delegates.getJSONObject(i), Delegate.class));
    }

    return delegateList;
  }

  private Account getAccount(String address) {
    Account account;
    try {
      account = http.get(createAccount + address, "account", Account.class, OBJECT);
    } catch (ArkNodeException e) {
      if (Crypto.validateAddress(address, info.getPubKeyHash())) {
        return Account.defaultAccount(address);
      }
      throw new RuntimeException(e);
    }

    return account;
  }

}
