package io.ark

import groovy.transform.CompileStatic
import java.nio.*
import org.bitcoinj.core.*
import org.bitcoinj.crypto.*
import com.google.common.io.BaseEncoding
import org.spongycastle.crypto.digests.RIPEMD160Digest


class Crypto {

  static ECKey.ECDSASignature sign(Transaction t, String passphrase){
    byte[] txbytes = getBytes(t)
    signBytes(txbytes, passphrase)
  }

  static ECKey.ECDSASignature secondSign(Transaction t, String passphrase){
    byte[] txbytes = getBytes(t, false)
    signBytes(txbytes, passphrase)
  }

  static ECKey.ECDSASignature signBytes(byte[] bytes, String passphrase){
    ECKey keys = getKeys(passphrase)
    keys.sign(Sha256Hash.of(bytes))
  }

  static boolean verify(Transaction t){
    ECKey keys = ECKey.fromPublicOnly(BaseEncoding.base16().lowerCase().decode(t.senderPublicKey))
    byte[] signature = BaseEncoding.base16().lowerCase().decode(t.signature)
    byte[] bytes = getBytes(t)
    verifyBytes(bytes, signature, keys.getPubKey())
  }

  static boolean secondVerify(Transaction t, String secondPublicKeyHex){
    ECKey keys = ECKey.fromPublicOnly BaseEncoding.base16().lowerCase().decode(secondPublicKeyHex)
    byte[] signature = BaseEncoding.base16().lowerCase().decode(t.signSignature)
    byte[] bytes = getBytes(t, false)
    verifyBytes(bytes, signature, keys.getPubKey())
  }

  static boolean verifyBytes(byte[] bytes, byte[] signature, byte[] publicKey){
    ECKey.verify(Sha256Hash.hash(bytes), signature, publicKey)
  }

  static byte[] getBytes(Transaction t, boolean skipSignature = true, boolean skipSecondSignature = true){
    ByteBuffer buffer = ByteBuffer.allocate(1000)
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    buffer.put t.type
    buffer.putInt t.timestamp
    buffer.put BaseEncoding.base16().lowerCase().decode(t.senderPublicKey)

    if(t.requesterPublicKey){
      buffer.put Base58.decodeChecked(t.requesterPublicKey)
    }

    if(t.recipientId){
      buffer.put Base58.decodeChecked(t.recipientId)
    }
    else {
      buffer.put new byte[21]
    }

    if(t.vendorField){
      byte[] vbytes = t.vendorField.bytes
      if(vbytes.size()<65){
        buffer.put vbytes
        buffer.put new byte[64-vbytes.size()]
      }
    }
    else {
      buffer.put new byte[64]
    }

    buffer.putLong t.amount
    buffer.putLong t.fee

    if(t.type==1){
      buffer.put BaseEncoding.base16().lowerCase().decode(t.asset.signature)
    }
    else if(t.type==2){
      buffer.put t.asset.username.bytes
    }
    else if(t.type==3){
      buffer.put t.asset.votes.join("").bytes
    }
    // TODO: multisignature
    // else if(t.type==4){
    //   buffer.put BaseEncoding.base16().lowerCase().decode(t.asset.signature)
    // }

    if(!skipSignature && t.signature){
      buffer.put BaseEncoding.base16().lowerCase().decode(t.signature)
    }
    if(!skipSecondSignature && t.signSignature){
      buffer.put BaseEncoding.base16().lowerCase().decode(t.signSignature)
    }

    def outBuffer = new byte[buffer.position()]
    buffer.rewind()
    buffer.get(outBuffer)
    return outBuffer
  }

  static String getId(Transaction t){
    BaseEncoding.base16().lowerCase().encode Sha256Hash.hash(getBytes(t, false, false))
  }

  static ECKey getKeys(String passphrase){
    byte[] sha256 = Sha256Hash.hash(passphrase.bytes)
    ECKey keys = ECKey.fromPrivate(sha256, true)
    return keys
  }

  static String getAddress(ECKey keys, version = 0x17){
    getAddress(keys.getPubKey(), version)
  }

  static String getAddress(publicKey, version = 0x17){
    RIPEMD160Digest digest = new RIPEMD160Digest();
    digest.update(publicKey, 0, publicKey.length);
    byte[] out = new byte[20];
    digest.doFinal(out, 0);
    def address = new VersionedChecksummedBytes(version, out)
    return address.toBase58();
  }


}
