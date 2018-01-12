/**
 * Represents a peer servicing a [network]
 * Can be initialized with a list of {ip,port} or seperated params
 */
open class Peer(val ip: String, val port: Int, val network: Network)
{
    private var protocol: String = "http://"
    private var status: String = "NEW"
    var peerURL: String = ""

    /**
     * Runs after either constructor.
     * Determines the protocol and assembles the url
     */
    init
    {
        if (port % 1000 == 443) protocol = "https://"
        peerURL = "$protocol$ip:$port"
    }

    /**
     * Secondary constructor to use a list of strings to initialize params
     */
    constructor(peerInfo: List<String>, network: Network) : this(
            ip = peerInfo[0],
            port = peerInfo[1].toInt(),
            network = network
    )

    /**
     * Convenience method to enhance readability for getStatus() being successful
     */
    suspend fun isOk() = getStatus().await().success

    /**
     * Checks the status of this [Peer] using [HttpRequest]
     */
    open fun getStatus() = HttpRequest.getStatus(this)

    /**
     * Posts a [transaction] with this [Peer] using [HttpRequest]
     * Eventually returns a [TransactionPostResponse]
     */
    open fun postTransaction(transaction: Transaction) =
            HttpRequest.postTransaction(this, transaction)


    /**
     * Gets a [TransactionList] with this [Peer] using [HttpRequest]
     */
    fun getTransactions(account: Account, amount: Int) =
            HttpRequest.getTransactions(this, account, amount)
}
