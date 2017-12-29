import com.google.common.io.BaseEncoding
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.VersionedChecksummedBytes
import org.spongycastle.crypto.digests.RIPEMD160Digest

object Crypto
{
    val networkVersion = 0x17

    fun base16Encode(bytes: ByteArray) = BaseEncoding.base16().lowerCase().encode(bytes)!!
    fun base16Decode(chars: String) = BaseEncoding.base16().lowerCase().decode(chars)!!

    fun sign(transaction: Transaction, passphrase: String): ECKey.ECDSASignature?
    {
        return signBytes(transaction.toBytes(), passphrase)
    }

    fun secondSign(transaction: Transaction, passphrase: String): ECKey.ECDSASignature?
    {
        return signBytes(transaction.toBytes(skipSignature = false), passphrase)
    }

    fun getKeys(passphrase: String): ECKey?
    {
        val sha256 = Sha256Hash.hash(passphrase.toByteArray())
        return ECKey.fromPrivate(sha256, true)
    }

    fun verify(transaction: Transaction): Boolean
    {
        val keys = ECKey.fromPublicOnly(base16Decode(transaction.senderPublicKey!!))
        return verifyBytes(
                transaction.toBytes(),
                base16Decode(transaction.signature!!),
                keys.pubKey)
    }

    fun secondVerify(transaction: Transaction, secondPublicKeyHex: String): Boolean
    {
        val keys = ECKey.fromPublicOnly(base16Decode(secondPublicKeyHex))
        return verifyBytes(
                transaction.toBytes(skipSignature = false),
                base16Decode(transaction.signSignature!!),
                keys.pubKey)
    }

    fun getId(transaction: Transaction): String
    {
        return base16Encode(Sha256Hash.hash(transaction.toBytes(false, false)))
    }

    fun getAddress(publicKey: ByteArray): String
    {
        val out = byteArrayOf(20)

        var digest = RIPEMD160Digest().apply {
            update(publicKey, 0, publicKey.size)
            doFinal(out, 0)
        }

        return ArkAddress(NetworkConstants.mainnet, out).toBase58()
    }

    private fun verifyBytes(bytes: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean
    {
        return ECKey.verify(Sha256Hash.hash(bytes), signature, publicKey)
    }

    private fun signBytes(bytes: ByteArray, passphrase: String): ECKey.ECDSASignature?
    {
        return getKeys(passphrase)?.sign(Sha256Hash.of(bytes))
    }

}