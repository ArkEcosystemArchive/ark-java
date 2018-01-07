import Crypto.base16Decode
import Crypto.base16Encode
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Represents a Ark transaction
 * Minimal constructor requires [amount] [fee] and [type] but
 * [Transaction] should be created using [Crypto] object methods
 */
data class Transaction(var id: String? = null,
                       var timestamp: Int? = null,
                       var recipientId: String? = null,
                       var amount: Long,
                       var fee: Long,
                       var type: Byte,
                       var vendorField: String? = null,
                       var signature: String? = null,
                       var signSignature: String? = null,
                       var senderPublicKey: String? = null,
                       var requesterPublicKey: String? = null,
                       var asset: Asset = Asset())
{

    /**
     * Returns a JsonObject of this instance of transaction
     */
    fun toJson(): JsonElement? = GsonBuilder().serializeNulls().create().toJsonTree(this)

    /**
     * Populates the [senderPublicKey] and [signature] fields
     */
    fun sign(passphrase: String)
    {
        senderPublicKey = base16Encode(Crypto.getKeys(passphrase)!!.pubKey)
        signature = base16Encode(Crypto.sign(this, passphrase)!!.encodeToDER())
    }

    /**
     * Populates the signSignature field using a second [passphrase]
     */
    fun secondSign(passphrase: String)
    {
        signSignature = base16Encode(Crypto.secondSign(this, passphrase)!!.encodeToDER())
    }

    /**
     * Converts this object to it's [ByteArray] representation for [Crypto] signing and verification purposes
     */
    fun toBytes(skipSignature: Boolean = true, skipSecondSignature: Boolean = true ): ByteArray
    {
        var output = ByteArray(0)
        val buffer: ByteBuffer = prepareBuffer(ByteBuffer.allocate(1000), skipSignature, skipSecondSignature)

        with(buffer)
        {
            output = ByteArray(position())
            rewind()
            get(output)
        }

        return output
    }

    /**
     * Handles a ByteBuffer and populates it with this instance's fields
     */
    private fun prepareBuffer(buffer: ByteBuffer, skipSignature: Boolean, skipSecondSignature: Boolean) = buffer.apply{
        order(ByteOrder.LITTLE_ENDIAN)

        put(type)
        putInt(timestamp!!)
        put(base16Decode(senderPublicKey!!))

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
            1 -> put(base16Decode(asset.signature!!))
            2 -> put(asset.username!!.toByteArray())
            3 -> put(asset.votes!!.joinToString().toByteArray())
        }

        if(!skipSignature && signature != null) put(base16Decode(signature!!))

        if(!skipSecondSignature && signSignature != null) put(base16Decode(signSignature!!))
    }
}
