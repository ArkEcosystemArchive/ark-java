import com.google.common.io.BaseEncoding
import com.google.gson.Gson
import io.ark.core.groovy.Crypto
import io.ark.core.groovy.Slot
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

data class Transaction(var timestamp: Int? = null,
                       var recipientId: String? = null,
                       var amount: Long,
                       var fee: Long,
                       var type: Byte,
                       var vendorField: String? = null,
                       var signature: String? = null,
                       var signSignature: String? = null,
                       var senderPublicKey: String? = null,
                       var requesterPublicKey: String? = null,
                       var asset: Asset = Asset(),
                       var id: String? = null)
{
    private val bufferSize = 1000

    private fun base16Encode(bytes: ByteArray) = BaseEncoding.base16().lowerCase().encode(bytes)
    private fun base16Decode(chars: String?) = BaseEncoding.base16().lowerCase().decode(chars)

    fun toJson(): String = Gson().toJson(this)
    fun fromJson(input: String): Transaction = Gson().fromJson(input, Transaction::class.java)

    private fun sign(passphrase: String)
    {
        senderPublicKey = base16Encode(Crypto.getKeys(passphrase).pubKey)
        signature = base16Encode(Crypto.sign(this, passphrase).encodeToDER())
    }

    private fun secondSign(passphrase: String)
    {
        signSignature = base16Encode(Crypto.secondSign(this, passphrase).encodeToDER())
    }

    fun createTransaction(recipientId: String, satoshiAmount: Long, vendorField: String, passphrase: String, secondPassphrase: String? = null): Transaction
    {
        val transaction = Transaction(type = 0, recipientId = recipientId, amount = satoshiAmount, fee = 10000000, vendorField = vendorField)
        return processTransaction(transaction, passphrase)
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
        transaction.asset.signature = base16Encode(Crypto.getKeys(secondPassphrase).pubKey)
        return processTransaction(transaction, passphrase, secondPassphrase)
    }

    private fun processTransaction(transaction: Transaction, passphrase: String, secondPassphrase: String? = null) : Transaction
    {
        transaction.timestamp = Slot.getTime(Date())
        transaction.sign(passphrase)
        secondPassphrase?.let { transaction.secondSign(it) }
        transaction.id = Crypto.getId(transaction)
        return transaction
    }

    fun toBytes(skipSignature: Boolean = true, skipSecondSignature: Boolean = true ): ByteArray
    {
        var output = ByteArray(0)
        val buffer: ByteBuffer = prepareBuffer(ByteBuffer.allocate(bufferSize), skipSignature, skipSecondSignature)

        with(buffer)
        {
            output = ByteArray(position())
            rewind()
            get(output)
        }

        return output
    }

    private fun prepareBuffer(buffer: ByteBuffer, skipSignature: Boolean, skipSecondSignature: Boolean) = buffer.apply{
        order(ByteOrder.LITTLE_ENDIAN)

        put(type)
        timestamp?.let { putInt(it) }
        put(base16Decode(senderPublicKey))

        requesterPublicKey?.let { put(base16Decode(it)) }

        recipientId?.let { put(Base58.decodeChecked(it)) } ?: put(ByteArray(21))

        vendorField?.let{
            val vendorBytes = it.toByteArray()

            if(vendorBytes.size < 65)
            {
                put(vendorBytes)
                put(ByteArray(64 - vendorBytes.size))
            }
        } ?: put(ByteArray(64))

        putLong(amount)
        putLong(fee)

        // TODO: multisignature (type 4)
        // TODO: change type to enum from Byte
        when(type.toInt())
        {
            1 -> put(base16Decode(asset.signature))
            2 -> put(asset.username.toByteArray())
            3 -> put(asset.votes.toByteArray())
        }

        if(!skipSignature) put(base16Decode(signature!!))

        if(!skipSecondSignature) put(base16Decode(signSignature!!))
    }
}
