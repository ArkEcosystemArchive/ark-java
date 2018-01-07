/**
 * This file is responsible for containing all the response structures that this library can recieve from a
 * ark-node's api with correct types and nullity checks. Incoming data should be deserialized into these objects
 * immediately.
 */

/**
 * Contains the response from a "GET:/peer/status" HTTP request
 */
data class PeerStatus(
        val success: Boolean,
        val height: Int,
        val forgingAllowed: Boolean,
        val currentSlot: Int,
        val header: Header
)

/**
 * Contains a specific Peer's information found within a PeerList
 */
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


/**
 * Contains the response from a "GET:/api/peers" request
 */
data class PeerList(
        val success: Boolean,
        var peers: Array<PeerData>
)

/**
 * Contains a peer's header data found within a PeerStatus response
 */
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

/**
 * Contains the response from a "POST:/peer/transactions/" request
 */
data class TransactionPostResponse(
        val success: Boolean,
        val message: String,
        val error: String?,
        val transactionIds: List<String?>?
)
{
    //When an error occurs, construct without transactionIds property
    constructor(success: Boolean, message: String, error: String) : this(success, message, error, null)

    //When an error doesn't occur, construct without error property
    constructor(success: Boolean, message: String, transactionIds: List<String?>) : this(success, message, null, transactionIds)
}

/**
 * Contains the response from a "GET:/api/transactions/ request
 */
data class TransactionList(
        val success: Boolean,
        val transactions: Array<Transaction>
)
