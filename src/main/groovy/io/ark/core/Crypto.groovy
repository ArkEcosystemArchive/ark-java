package io.ark.core

import java.nio.*
import org.bitcoinj.core.*
import org.bitcoinj.crypto.*
import com.google.common.io.BaseEncoding
import org.spongycastle.crypto.digests.RIPEMD160Digest


class Crypto {

  static networkVersion = 0x17

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
    ECKey keys = ECKey.fromPublicOnly(BaseEncoding.base16().lowerCase().decode(secondPublicKeyHex))
    byte[] signature = BaseEncoding.base16().lowerCase().decode(t.signSignature)
    byte[] bytes = getBytes(t, false)
    verifyBytes(bytes, signature, keys.getPubKey())
  }

  static boolean verifyBytes(byte[] bytes, byte[] signature, byte[] publicKey){
    ECKey.verify(Sha256Hash.hash(bytes), signature, publicKey)
  }

  static byte[] getBytes(Transaction t, boolean skipSignature = true, boolean skipSecondSignature = true){
    return t.toBytes(skipSignature, skipSecondSignature)
  }

  static String getId(Transaction t){
    BaseEncoding.base16().lowerCase().encode Sha256Hash.hash(getBytes(t, false, false))
  }

  static ECKey getKeys(String passphrase){
    byte[] sha256 = Sha256Hash.hash(passphrase.bytes)
    ECKey keys = ECKey.fromPrivate(sha256, true)
    return keys
  }

  static String getAddress(ECKey keys){
    getAddress(keys.getPubKey())
  }

  static String getAddress(publicKey){
    RIPEMD160Digest digest = new RIPEMD160Digest();
    digest.update(publicKey, 0, publicKey.length);
    byte[] out = new byte[20];
    digest.doFinal(out, 0);
    def address = new VersionedChecksummedBytes(networkVersion, out)
    return address.toBase58();
  }


}
