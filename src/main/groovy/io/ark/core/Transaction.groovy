package io.ark.core

import java.nio.*
import com.google.common.io.BaseEncoding
import com.google.gson.Gson
import org.bitcoinj.core.*
import groovy.transform.*

enum TransactionType {
  NORMAL(0),
  SECONDSIGNATURE(1),
  DELEGATE(2),
  VOTE(3),

  /**
   * @deprecated use SECONDSIGNATURE
   */
  @Deprecated SECONDSIGNITURE(1)

  private final int value

  TransactionType(int value)
  {
    this.value = value as byte
  }

  byte getByteValue() {
    return value
  }
}

@Canonical
class Transaction extends Object {
  int timestamp
  String recipientId
  Long amount
  Long fee
  TransactionType type
  String vendorField
  String signature
  String signSignature
  String senderPublicKey
  String requesterPublicKey
  Map<String, Object> asset = [:]
  String id

  /**
   * Serializes this transaction object into a byte array
   *
   * @param skipSignature
   * @param skipSecondSignature
   * @return an array of bytes representing this object
   */
  public byte[] toBytes(boolean skipSignature = true, boolean skipSecondSignature = true){
    ByteBuffer buffer = ByteBuffer.allocate(1000)
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    buffer.put type.getByteValue()
    buffer.putInt timestamp
    buffer.put BaseEncoding.base16().lowerCase().decode(senderPublicKey)

    if(requesterPublicKey){
      buffer.put BaseEncoding.base16().lowerCase().decode(requesterPublicKey)
    }

    if(recipientId){
      buffer.put Base58.decodeChecked(recipientId)
    }
    else {
      buffer.put new byte[21]
    }

    if(vendorField){
      byte[] vbytes = vendorField.bytes
      if(vbytes.size()<65){
        buffer.put vbytes
        buffer.put new byte[64-vbytes.size()]
      }
    }
    else {
      buffer.put new byte[64]
    }

    buffer.putLong amount
    buffer.putLong fee

    if(type == TransactionType.SECONDSIGNITURE || type == TransactionType.SECONDSIGNATURE){
      buffer.put BaseEncoding.base16().lowerCase().decode(asset?.get("signature")?.get("publicKey"))
    }
    else if(type == TransactionType.DELEGATE){
      buffer.put asset.username.bytes
    }
    else if(type == TransactionType.VOTE){
      buffer.put asset.votes.join("").bytes
    }
    // TODO: multisignature
    // else if(type==4){
    //   buffer.put BaseEncoding.base16().lowerCase().decode(asset.signature)
    // }

    if(!skipSignature && signature){
      buffer.put BaseEncoding.base16().lowerCase().decode(signature)
    }
    if(!skipSecondSignature && signSignature){
      buffer.put BaseEncoding.base16().lowerCase().decode(signSignature)
    }

    def outBuffer = new byte[buffer.position()]
    buffer.rewind()
    buffer.get(outBuffer)
    return outBuffer
  }

  public Map toObject(){
    this.properties.subMap(['id', 'timestamp', 'recipientId', 'amount', 'fee', 'type', 'vendorField', 'signature', 'signSignature', 'senderPublicKey', 'requesterPublicKey', 'asset'])
  }

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
    def tx = new Transaction(type:TransactionType.NORMAL, recipientId:recipientId, amount:satoshiAmount, fee:10000000, vendorField:vendorField)
    tx.timestamp = Slot.getTime()
    tx.sign(passphrase)
    if(secondPassphrase)
      tx.secondSign(secondPassphrase)
    tx.id = Crypto.getId(tx)
    return tx
  }

  static Transaction createVote(List votes, String passphrase, String secondPassphrase = null){
    def tx = new Transaction(type:TransactionType.VOTE, amount:0, fee:100000000)
    tx.asset.votes = votes
    tx.recipientId = Crypto.getAddress(Crypto.getKeys(passphrase))
    tx.timestamp = Slot.getTime()
    tx.sign(passphrase)
    if(secondPassphrase)
      tx.secondSign(secondPassphrase)
    tx.id = Crypto.getId(tx)
    return tx
  }

  static Transaction createDelegate(String username, String passphrase, String secondPassphrase = null){
    def tx = new Transaction(type:TransactionType.DELEGATE, amount:0, fee:2500000000)
    tx.asset.username = username
    tx.timestamp = Slot.getTime()
    tx.sign(passphrase)
    if(secondPassphrase)
      tx.secondSign(secondPassphrase)
    tx.id = Crypto.getId(tx)
    return tx
  }

  static Transaction createSecondSignature(String secondPassphrase, String passphrase){
    def tx = new Transaction(type:TransactionType.SECONDSIGNATURE, amount:0, fee:500000000)
    tx.asset.signature = [publicKey:BaseEncoding.base16().lowerCase().encode(Crypto.getKeys(secondPassphrase).getPubKey())]
    tx.timestamp = Slot.getTime()
    tx.sign(passphrase)
    tx.id = Crypto.getId(tx)
    return tx
  }

  //TODO: create multisignature

  //Custom getter to map type to byte value
  byte getType()
  {
    return type.getByteValue()
  }

}
