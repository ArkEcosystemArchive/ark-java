package io.ark.core

import java.nio.*
import groovy.transform.*
import com.google.common.io.BaseEncoding
import org.bitcoinj.core.*
import org.bitcoinj.crypto.*

@Canonical
class Block extends Object {
  byte version = 0
	int height
  String previousBlock
	long totalAmount
	long totalFee
  long reward
  String payloadHash
	int	timestamp
  int numberOfTransactions
  int payloadLength
  int size
  String generatorPublicKey
  List transactions = []
  List transactionIds = []
  String blockSignature
  String id

  public byte[] getBytes(boolean includeSignature = false){
    ByteBuffer buffer = ByteBuffer.allocate(1000)
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    buffer.put version
    buffer.putInt timestamp
    buffer.putInt height
    buffer.put new BigInteger(previousBlock).bytes
    buffer.putInt numberOfTransactions
		buffer.putLong totalAmount
		buffer.putLong totalFee
		buffer.putLong reward
    buffer.putInt payloadLength
    if(!payloadHash){
      //TODO: create payloadhash from transactions
    }
    buffer.put BaseEncoding.base16().lowerCase().decode(payloadHash)
    buffer.put BaseEncoding.base16().lowerCase().decode(generatorPublicKey)

    if(includeSignature){
      buffer.put BaseEncoding.base16().lowerCase().decode(blockSignature)
    }

    byte[] outBuffer = new byte[buffer.position()]
    buffer.rewind()
    buffer.get(outBuffer)
    return outBuffer
  }

  public String sign(String passphrase){
    blockSignature = BaseEncoding.base16().lowerCase().encode Crypto.signBytes(getBytes(), passphrase).encodeToDER()
  }

  public boolean verify(){
    ECKey keys = ECKey.fromPublicOnly(BaseEncoding.base16().lowerCase().decode(generatorPublicKey))
    byte[] signature = BaseEncoding.base16().lowerCase().decode(blockSignature)
    byte[] bytes = getBytes()
    verifyBytes(bytes, signature, keys.getPubKey())
  }

  public String getId(){
    byte[] bytes = getBytes(true)
    byte[] bytesid = bytes[0..7]
    id = new BigInteger(bytesid).toString()
  }
}
