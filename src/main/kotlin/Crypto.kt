import com.google.common.io.BaseEncoding
import com.google.gson.Gson
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.spongycastle.crypto.digests.RIPEMD160Digest

/**
 * This object handles all Cryptographic operations within the library and accordingly contains
 * all static referenced methods. Can be initialized with a [network] constant to utilize.
 * By default [network] is set to mainnet
 */
object Crypto
{
    //TODO: Explore possibility to move to init block
    var network = NetworkConstants.mainnet

    //Helper function extentions cleaning up repetitive encoding/decoding
    fun base16Encode(bytes: ByteArray) = BaseEncoding.base16().lowerCase().encode(bytes)!!
    fun base16Decode(chars: String) = BaseEncoding.base16().lowerCase().decode(chars)!!

    //TODO: Switch from Gson to Moshi to reduce dependencies
    //Allows deserialization of any Transaction object
    fun fromJson(input: String): Transaction = Gson().fromJson(input, Transaction::class.java)

    /**
     * Signs a [transaction] using the provided [passphrase]
     * Returns a [ECKey.ECDSASignature] to be included in a transaction
     */
    fun sign(transaction: Transaction, passphrase: String): ECKey.ECDSASignature?
    {
        return signBytes(transaction.toBytes(), passphrase)
    }

    /**
     * Performs a second signature on a [transaction] using the [secondPassphrase]
     * Returns a [ECKey.ECDSASignature] to be included in a transaction
     */
    fun secondSign(transaction: Transaction, secondPassphrase: String): ECKey.ECDSASignature?
    {
        return signBytes(transaction.toBytes(skipSignature = false), secondPassphrase)
    }

    /**
     * Takes a [passphrase] and returns the [ECKey] associated with it's SHA256 hash
     */
    fun getKeys(passphrase: String): ECKey?
    {
        val sha256 = Sha256Hash.hash(passphrase.toByteArray())
        return ECKey.fromPrivate(sha256, true)
    }

    /**
     * Verifies if the [transaction] is correctly created for submission to a Ark [Network]
     * Uses the first signature associated with the [transaction]
     * Returns a [Boolean] representing success of verification
     */
    fun verify(transaction: Transaction): Boolean
    {
        val keys = ECKey.fromPublicOnly(base16Decode(transaction.senderPublicKey!!))
        return verifyBytes(
                transaction.toBytes(),
                base16Decode(transaction.signature!!),
                keys.pubKey)
    }

    /**
     * Verifies if the [transaction] is correctly created for submission to a Ark [Network]
     * Uses the second signature associated with the [transaction]
     * Returns a [Boolean] representing success of verification
     */
    fun secondVerify(transaction: Transaction, secondPublicKeyHex: String): Boolean
    {
        val keys = ECKey.fromPublicOnly(base16Decode(secondPublicKeyHex))
        return verifyBytes(
                transaction.toBytes(skipSignature = false),
                base16Decode(transaction.signSignature!!),
                keys.pubKey)
    }

    /**
     * Constructs and processes a type 0(Normal) [Transaction] object using the provided fields
     */
    fun createTransaction(recipientId: String, satoshiAmount: Long, vendorField: String, passphrase: String, secondPassphrase: String? = null): Transaction
    {
        val transaction = Transaction(type = 0, recipientId = recipientId, amount = satoshiAmount, fee = 10000000, vendorField = vendorField)
        return processTransaction(transaction, passphrase)
    }

    /**
     * Returns the Id of a [transaction] as a string
     * Makes use of both first and second signatures
     */
    fun getId(transaction: Transaction): String
    {
        return base16Encode(Sha256Hash.hash(transaction.toBytes(false, false)))
    }

    /**
     * Convenience method to return the [ArkAddress]
     * of a [ECKey].
     */
    fun getAddress(ECKey: ECKey?): String
    {
        return getAddress(ECKey!!.pubKey)
    }

    /**
     * Returns the Ark address associated with a [publicKey]
     */
    fun getAddress(publicKey: ByteArray): String
    {
        val out = ByteArray(20)

        var digest = RIPEMD160Digest().apply {
            update(publicKey, 0, publicKey.size)
            doFinal(out, 0)
        }

        return ArkAddress(network, out).toBase58()
    }

    /**
     * Attempts to sign an array of [bytes] using the [ECKey] associated with
     * the [passphrase]
     */
    fun signBytes(bytes: ByteArray, passphrase: String): ECKey.ECDSASignature?
    {
        return getKeys(passphrase)?.sign(Sha256Hash.of(bytes))
    }

    /**
     * Attempts to verify an array of [bytes] using the [signature] and [publicKey]
     */
    fun verifyBytes(bytes: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean
    {
        return ECKey.verify(Sha256Hash.hash(bytes), signature, publicKey)
    }

    /**
     * Creates a Type 3 transaction used for voting for a delegate
     */
    fun createVote(votes: List<String>, passphrase: String, secondPassphrase: String? = null): Transaction
    {
        //Construct transaction
        val transaction = Transaction(type = 3, amount = 0, fee = 100000000)

        //Set type 3 parameters
        transaction.asset.votes = votes
        transaction.recipientId = Crypto.getAddress(Crypto.getKeys(passphrase))
        return processTransaction(transaction, passphrase)
    }

    /**
     * Creates a Type 2 transaction used for creating a new delegate
     * [username] of the delegate has to be provided
     */
    fun createDelegate(username: String, passphrase: String, secondPassphrase: String? = null): Transaction
    {
        //Construct transaction
        val transaction = Transaction(type = 2, amount = 0, fee = 2500000000)

        //Set type 2 parameters
        transaction.asset.username = username
        return processTransaction(transaction, passphrase)
    }

    /**
     * Creates a Type 1 transaction which requires two passphrases
     */
    fun createSecondSignature(passphrase: String, secondPassphrase: String): Transaction
    {
        //Construct transaction
        val transaction = Transaction(type = 1, amount = 0, fee = 2500000000)

        //Set type 1 parameters
        transaction.asset.signature = base16Encode(Crypto.getKeys(secondPassphrase)!!.pubKey)
        return processTransaction(transaction, passphrase, secondPassphrase)
    }

    /**
     * Handles tasks common to [transaction] creation such as id and timestamp initialization and signing
     * Returns the processed [Transaction] object
     */
    private fun processTransaction(transaction: Transaction, passphrase: String, secondPassphrase: String? = null) : Transaction
    {
        transaction.timestamp = Slot.getTime()

        //Sign it with first passphrase
        transaction.sign(passphrase)

        //Check if second passphrase exists, if so sign using that one too
        secondPassphrase?.let { transaction.secondSign(it) }

        //Extract the ID and initialize it in the object
        transaction.id = Crypto.getId(transaction)
        return transaction
    }
}