import Crypto.base16Decode
import Crypto.base16Encode
import Crypto.verifyBytes
import org.bitcoinj.core.ECKey
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class Block(var previousBlock: String,
                 var version: Byte,
                 var totalAmount: Long,
                 var totalFee: Long,
                 var reward: Long,
                 var payloadHash: String?,
                 var generatorPublicKey: String,
                 var height: Int,
                 var timestamp: Int,
                 var numberOfTransactions: Int,
                 var payloadLength: Int,
                 var blockSigniture: String,
                 var id: String)
{
    private val bufferSize = 1000

    fun sign(passphrase: String)
    {
        blockSigniture = base16Encode(Crypto.signBytes(getBytes(), passphrase)!!.encodeToDER())
    }

    fun verify(): Boolean
    {
        var keys = ECKey.fromPublicOnly(base16Decode(generatorPublicKey))
        var signature = base16Decode(blockSigniture)
        var bytes = getBytes()

        return verifyBytes(bytes, signature, keys.pubKey)
    }

    fun setId()
    {
        val bytesId = getBytes().sliceArray(0..7)
        id = BigInteger(bytesId).toString()
    }

    private fun getBytes(includeSignature: Boolean = false): ByteArray
    {
        var output = ByteArray(0)
        val buffer: ByteBuffer = prepareBuffer(ByteBuffer.allocate(bufferSize), includeSignature)

        with(buffer)
        {
            output = ByteArray(position())
            rewind()
            get(output)
        }

        return output
    }

    private fun prepareBuffer(buffer: ByteBuffer, includeSignature: Boolean) = buffer.apply{
        order(ByteOrder.LITTLE_ENDIAN)

        put(version)
        putInt(timestamp)
        putInt(height)
        put(BigInteger(previousBlock).toByteArray())
        putInt(numberOfTransactions)
        putLong(totalAmount)
        putLong(totalFee)
        putLong(reward)
        putInt(payloadLength)

        //TODO: create payloadhash from transactions

        put(base16Decode(payloadHash!!))
        put(base16Decode(generatorPublicKey))

        if (includeSignature) put(base16Decode(blockSigniture))
    }
}
