import com.google.common.io.BaseEncoding
import com.google.gson.Gson
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.spongycastle.crypto.digests.RIPEMD160Digest

object Crypto
{
    var network = NetworkConstants.mainnet

    fun base16Encode(bytes: ByteArray) = BaseEncoding.base16().lowerCase().encode(bytes)!!
    fun base16Decode(chars: String) = BaseEncoding.base16().lowerCase().decode(chars)!!
    fun fromJson(input: String): Transaction = Gson().fromJson(input, Transaction::class.java)

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

    fun createTransaction(recipientId: String, satoshiAmount: Long, vendorField: String, passphrase: String, secondPassphrase: String? = null): Transaction
    {
        val transaction = Transaction(type = 0, recipientId = recipientId, amount = satoshiAmount, fee = 10000000, vendorField = vendorField)
        return processTransaction(transaction, passphrase)
    }


    fun getId(transaction: Transaction): String
    {
        return base16Encode(Sha256Hash.hash(transaction.toBytes(false, false)))
    }

    fun getAddress(publicKey: ByteArray): String
    {
        val out = ByteArray(20)

        var digest = RIPEMD160Digest().apply {
            update(publicKey, 0, publicKey.size)
            doFinal(out, 0)
        }

        return ArkAddress(network, out).toBase58()
    }

    fun signBytes(bytes: ByteArray, passphrase: String): ECKey.ECDSASignature?
    {
        return getKeys(passphrase)?.sign(Sha256Hash.of(bytes))
    }

    fun verifyBytes(bytes: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean
    {
        return ECKey.verify(Sha256Hash.hash(bytes), signature, publicKey)
    }

    fun createVote(votes: List<String>, passphrase: String, secondPassphrase: String? = null): Transaction
    {
        val transaction = Transaction(type = 3, amount = 0, fee = 100000000)
        transaction.asset.votes = votes
        return processTransaction(transaction, passphrase)
    }

    fun createDelegate(username: String, passphrase: String, secondPassphrase: String? = null): Transaction
    {
        val transaction = Transaction(type = 2, amount = 0, fee = 2500000000)
        transaction.asset.username = username
        return processTransaction(transaction, passphrase)
    }

    fun createSecondSignature(passphrase: String, secondPassphrase: String): Transaction
    {
        val transaction = Transaction(type = 2, amount = 0, fee = 2500000000)
        transaction.asset.signature = base16Encode(Crypto.getKeys(secondPassphrase)!!.pubKey)
        return processTransaction(transaction, passphrase, secondPassphrase)
    }

    private fun processTransaction(transaction: Transaction, passphrase: String, secondPassphrase: String? = null) : Transaction
    {
        transaction.timestamp = Slot.getTime()
        transaction.sign(passphrase)
        secondPassphrase?.let { transaction.secondSign(it) }
        transaction.id = Crypto.getId(transaction)
        return transaction
    }
}