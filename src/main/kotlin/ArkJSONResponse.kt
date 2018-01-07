data class PeerStatus(
        val success: Boolean,
        val height: Int,
        val forgingAllowed: Boolean,
        val currentSlot: Int,
        val header: Header
)

data class PeerData(
       val ip: String,
       val port: Int,
       val version: String,
       val errors: Int,
       val os: String,
       val height: Int,
       val status: String,
       val delay: Int
)

data class PeerList(
        val success: Boolean,
        var peers: Array<PeerData>
)

data class Header(
        val id: String,
        val height: Int,
        val version: Int,
        val totalAmount: Long,
        val totalFee: Long,
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
        val error: String?,
        val transactionIds: List<String?>?
)
{
    //When an error occurs, contruct without transactionIds property
    constructor(success: Boolean, message: String, error: String) : this(success, message, error, null)

    //When an error doesn't occur, construct without error property
    constructor(success: Boolean, message: String, transactionIds: List<String?>) : this(success, message, null, transactionIds)
}

data class TransactionList(
        val success: Boolean,
        val transactions: Array<Transaction>
)
