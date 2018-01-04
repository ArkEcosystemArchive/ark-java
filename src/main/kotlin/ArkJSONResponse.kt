import java.util.*

data class PeerData(
        val success: Boolean,
        val height: Int,
        val forgingAllowed: Boolean,
        val currentSlot: Int,
        val header: Header
)

data class Header(
        val id: Long,
        val height: Int,
        val version: Int,
        val totalAmount: Int,
        val totalFee: Int,
        val reward: Long,
        val payloadHash: String,
        val payloadLength: Int,
        val timestamp: Int,
        val numberOfTransactions: Int,
        val previousBlock: String,
        val generatorPublicKey: String,
        val blockSignature: String
)

data class TransactionPostResponse(
        val success: Boolean,
        val message: String,
        val error: String
)

data class TransactionList(
        val success: Boolean,
        val transactions: Array<Transaction>
)
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransactionList

        if (success != other.success) return false
        if (!Arrays.equals(transactions, other.transactions)) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = success.hashCode()
        result = 31 * result + Arrays.hashCode(transactions)
        return result
    }
}