class Peer(val ip: String, val port: Int, val network: Network)
{
    private var protocol: String = "http://"
    private var status: String = "NEW"
    private var peerURL: String = ""

    init
    {
        if (port % 1000 == 443) protocol = "https://"
        peerURL = "$protocol$ip$port"
    }

    constructor(peerInfo: Array<String>, network: Network) : this(
            ip = peerInfo[0],
            port = peerInfo[1].toInt(),
            network = network
    )

    fun isOk() = getStatus().success

    fun getStatus() = HttpRequest.getStatus(peerURL, network)

    fun postTransaction(transaction: Transaction) =
            HttpRequest.postTransaction(peerURL, network, transaction)

    fun getTransactions(account: Account, amount: Int) =
            HttpRequest.getTransactions(peerURL, network, account, amount)
}
