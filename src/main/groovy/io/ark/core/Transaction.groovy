package io.ark.core

import java.nio.*
import com.google.common.io.BaseEncoding
import com.google.gson.Gson
import org.bitcoinj.core.*
import org.bitcoinj.crypto.*
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
  String senderPublicKey
  String requesterPublicKey
  Map<String, Object> asset = [:]
  String id

  public byte[] toBytes(boolean skipSignature = true, boolean skipSecondSignature = true){
    ByteBuffer buffer = ByteBuffer.allocate(1000)
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    buffer.put type
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

    if(type==1){
      buffer.put BaseEncoding.base16().lowerCase().decode(asset.signature)
    }
    else if(type==2){
      buffer.put asset.username.bytes
    }
    else if(type==3){
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
