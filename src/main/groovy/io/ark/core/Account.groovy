package io.ark.core

class Account extends Object {
  String address
  String publicKey
  long balance
  long unconfirmedBalance
  String username
  long vote
  List votes
  int rate
  Network network
  long updateTimestamp

  Account(String address, Network network) {
    this.address = address
    this.network = network
  }

  public boolean applyTransaction(transaction) {
    balance -= transaction.amount + transaction.fee
    balance > -1
  }

  public boolean undoTransaction(transaction) {
    balance += transaction.amount + transaction.fee
    balance > -1
  }

  public Verification verifyTransaction(transaction) {
    Verification v = new Verification()
    if (balance < transaction.amount + transaction.fee)
      v.error.push "Account ${address} does not have enough balance: ${balance}"
    // TODO: many things
  }

  public void updateBalance() {
    def json = network.get("/api/accounts", [address: address]).account
    this.balance = json.balance as long
    this.unconfirmedBalance = json.unconfirmedBalance as long
    this.updateTimestamp = System.currentTimeMillis()
  }

}
