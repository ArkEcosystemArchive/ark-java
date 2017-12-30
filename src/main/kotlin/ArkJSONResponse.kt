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

data class TransactionData(
        val id: Long
)