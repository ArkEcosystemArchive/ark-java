import com.google.common.io.BaseEncoding
import io.ark.core.groovy.Crypto
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class Transaction(var timestamp: Int,
                       var recipientId: String?,
                       var amount: Long,
                       var fee: Long,
                       var type: Byte,
                       var vendorField: String?,
                       var signature: String?,
                       var signSignature: String?,
                       var senderPublicKey: String,
                       var requesterPublicKey: String?,
                       var asset: Map<String, String>,
                       var id: String?)
{
    private val bufferSize = 1000

    private fun base16Encode(bytes: ByteArray) = BaseEncoding.base16().lowerCase().encode(bytes)
    private fun base16Decode(chars: CharSequence) = BaseEncoding.base16().lowerCase().decode(chars)

    fun sign(passphrase: String): String
    {
        senderPublicKey = base16Encode(Crypto.getKeys(passphrase).pubKey)
        signature = base16Encode(Crypto.sign(this, passphrase).encodeToDER())
    }

    fun toBytes(skipSignature: Boolean = true, skipSecondSignature: Boolean = true): ByteArray
    {
        var output: ByteArray = ByteArray(0)
        val buffer: ByteBuffer = prepareBuffer(ByteBuffer.allocate(bufferSize))

        with(buffer)
        {
            output = ByteArray(position())
            rewind()
            get(output)
        }

        return output
    }

    private fun prepareBuffer(buffer: ByteBuffer) = buffer.apply{
        order(ByteOrder.LITTLE_ENDIAN)

        put(type)
        putInt(timestamp)
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
