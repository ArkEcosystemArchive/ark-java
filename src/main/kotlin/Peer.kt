class Peer(val ip: String, val port: Int, val network: Network)
{
    private var protocol: String = "http://"
    private var status: String = "NEW"
    var peerURL: String = ""

    init
    {
        if (port % 1000 == 443) protocol = "https://"
        peerURL = "$protocol$ip$port"
    }

    constructor(peerInfo: List<String>, network: Network) : this(
            ip = peerInfo[0],
            port = peerInfo[1].toInt(),
            network = network
    )

    fun isOk() = getStatus().success

    fun getStatus() = HttpRequest.getStatus(this)

    fun postTransaction(transaction: Transaction) =
            HttpRequest.postTransaction(this, transaction)

    fun getTransactions(account: Account, amount: Int) =
            HttpRequest.getTransactions(this, account, amount)
}
