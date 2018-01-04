import Crypto.base16Decode
import Crypto.base16Encode
import com.google.gson.Gson
import com.google.gson.JsonElement
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

    fun toJson(): JsonElement? = Gson().toJsonTree(this)

    fun sign(passphrase: String)
    {
        senderPublicKey = base16Encode(Crypto.getKeys(passphrase)!!.pubKey)
        signature = base16Encode(Crypto.sign(this, passphrase)!!.encodeToDER())
    }

    fun secondSign(passphrase: String)
    {
        signSignature = base16Encode(Crypto.secondSign(this, passphrase)!!.encodeToDER())
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
