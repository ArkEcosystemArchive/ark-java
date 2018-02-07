package io.ark.core;

import java.util.List;

import io.ark.core.model.Account;
import io.ark.core.network.ArkNet;
import io.ark.core.requests.AccountManager;
import io.ark.core.requests.BlockExplorer;
import io.ark.core.responses.Fees;

// TODO : Fix SLF4J error messages on start.
public class ArkJava {

	public static void main(String[] args) throws Exception {
	  ArkNet ark = new ArkNet();
	  
	  /**
	   * Creating a new account.
	   */
	  AccountManager accounts = ark.getAccountManager();
	  List<String> passphrase = accounts.getNewPassphrase();
	  Account newAccount = accounts.createAccount(passphrase);
	  
	  BlockExplorer explorer = ark.getBlockExplorer();
	  Fees fees = explorer.getFees();
	  System.out.println(fees.getDelegate());
	}
}
