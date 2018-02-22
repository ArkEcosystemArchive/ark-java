package io.ark.core;

import io.ark.core.manager.AccountManager;
import io.ark.core.model.Account;
import io.ark.core.network.ArkNet;
import io.ark.core.network.MainNet;

public class ArkJava {

  public static void main(String[] args) throws Exception {
    ArkNet a = new MainNet();
    AccountManager am = a.getAccountManager();
    Account acc = am.getAccount("pony desk bundle example poet dinosaur enrich item diet feature nurse inherit");
    a.shutdown();
  }
}
