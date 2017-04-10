package io.ark


import com.google.common.io.BaseEncoding
import com.google.gson.Gson
import groovy.transform.*

@Canonical
class Transaction extends Object {
  int timestamp
  String recipientId
  Long amount
  Long fee
  byte type
  String vendorField
  String signature
  String signSignature
  String publicKey
  String senderPublicKey
  String requesterPublicKey
  Map<String, Object> asset = [:]
  String id

  String sign(passphrase){
    senderPublicKey = BaseEncoding.base16().lowerCase().encode(Crypto.getKeys(passphrase).getPubKey())
    signature = BaseEncoding.base16().lowerCase().encode Crypto.sign(this, passphrase).encodeToDER()
  }

  String secondSign(passphrase){
    signSignature = BaseEncoding.base16().lowerCase().encode Crypto.secondSign(this, passphrase).encodeToDER()
  }

  String toJson(){
    Gson gson = new Gson()
    gson.toJson(this)
  }

  static Transaction fromJson(json){
    Gson gson = new Gson()
    gson.fromJson(json, Transaction.class)
  }

  static Transaction createTransaction(String recipientId, long satoshiAmount, String vendorField, String passphrase, String secondPassphrase = null){
    def tx = new Transaction(type:0, recipientId:recipientId, amount:satoshiAmount, fee:10000000, vendorField:vendorField)
    tx.timestamp = Slot.getTime()
    tx.sign(passphrase)
    if(secondPassphrase)
      tx.secondSign(secondPassphrase)
    tx.id = Crypto.getId(tx)
    return tx
  }

  static Transaction createVote(ArrayList votes, String passphrase, String secondPassphrase = null){
    def tx = new Transaction(type:3, amount:0, fee:100000000)
    tx.asset.votes = votes
    tx.timestamp = Slot.getTime()
    tx.sign(passphrase)
    if(secondPassphrase)
      tx.secondSign(secondPassphrase)
    tx.id = Crypto.getId(tx)
    return tx
  }

  static Transaction createDelegate(String username, String passphrase, String secondPassphrase = null){
    def tx = new Transaction(type:2, amount:0, fee:2500000000)
    tx.asset.username = username
    tx.timestamp = Slot.getTime()
    tx.sign(passphrase)
    if(secondPassphrase)
      tx.secondSign(secondPassphrase)
    tx.id = Crypto.getId(tx)
    return tx
  }

  static Transaction createSecondSignature(secondPassphrase, passphrase){
    def tx = new Transaction(type:1, amount:0, fee:500000000)
    tx.asset.signature = BaseEncoding.base16().lowerCase().encode(Crypto.getKeys(secondPassphrase).getPubKey())
    tx.timestamp = Slot.getTime()
    tx.sign(passphrase)
    tx.id = Crypto.getId(tx)
    return tx
  }

  //TODO: create multisignature


}
