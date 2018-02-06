package io.ark.core.model;

import static io.ark.core.util.Constants.ARKTOSHI;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.ECKey;

import io.ark.core.network.NetworkInfo;
import io.ark.core.requests.AccountManager;
import io.ark.core.requests.TransactionManager;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {
	private String address;
	private long unconfirmedBalance;
	private long balance;
	private String publicKey;
	private int unconfirmedSignature;
	private int secondSignature;
	private String secondPublicKey;
	private List<String> multisignatures;
	private List<String> u_multisignatures;
	
	private NetworkInfo info;
	private ECKey keyPair;
	private ECKey secondKeyPair;
	private TransactionManager txManager;
	private AccountManager accManager;
	
	public float getBalance() {
		return (float) balance / ARKTOSHI;
	}
	
	public String getBalanceString() {
		String symbol = info.getClient().getSymbol();
		float balance = getBalance();
		return MessageFormat.format("{0} {1}", balance, symbol);
	}
	
	public void send(String address, int amount) {
		txManager.sendTransaction(address, amount, this);
	}
	
	public List<Delegate> getDelegates() {
		return accManager.getDelegates(address);
	}
	
	public static Account defaultAccount(String address) {
		Account acc = new Account();
		acc.setAddress(address);
		acc.setBalance(0);
		acc.setUnconfirmedBalance(0);
		acc.setPublicKey(null);
		acc.setUnconfirmedBalance(0);
		acc.setSecondSignature(0);
		acc.setSecondPublicKey(null);
		acc.setMultisignatures(new ArrayList<String>());
		acc.setU_multisignatures(new ArrayList<String>());
		return acc;
	}
	
}
