package io.ark.core

class Account extends Object {
  String address
  String publicKey
  long balance
  String username
  long vote
  List votes
  int rate

  public boolean applyTransaction(transaction){
    balance -= transaction.amount + transaction.fee
    balance > -1
  }

  public boolean undoTransaction(transaction){
    balance += transaction.amount + transaction.fee
    balance > -1
  }

  public Verification verifyTransaction(transaction){
    Verification v = new Verification()
    if(balance < transaction.amount + transaction.fee)
      v.error.push "Account ${address} does not have enough balance: ${balance}"
    // TODO: many things


  }
}
