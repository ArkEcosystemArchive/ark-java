import java.util.*

data class Network(
        var nethash: String,
        var version: String = "1.0.1",
        var name: String,
        var broadcastMax: Int = 10,
        var port: Int,
        var prefix: Int,
        var peerseed: List<String>? = null,
        var peers: MutableList<Peer> = mutableListOf(),
        var peerListProviders: List<String>)
{
    private val random = Random()
    private val localhost = "127.0.0.1"
    private val defaultTimeout = 30000L

    fun getHeaders(): Map<String, Any>
    {
        return mapOf(
                "nethash" to nethash,
                "version" to version,
                "port" to port)
    }

    fun warmup(numberOfPeers: Int): Boolean
    {
        if (peers.isNotEmpty()) return false

        getFreshPeers(numberOfPeers).forEach { verifyAndAddFreshPeer(it) }


        return true
    }

    //TODO: Returns a JSONRESPONSE
    fun getFreshPeers(limitResults: Int, timeout: Long = defaultTimeout): List<Peer>
    {
        Collections.shuffle(peerListProviders)

        for(url in peerListProviders)
        {
            val result = getFreshPeersFromUrl(url, limitResults, timeout)

        }
    }

    //TODO: Returns a JSONRESPONSE
    fun getFreshPeersFromUrl(url: String, limitResults: Int, timeout: Long): List<Peer>
    {

    }

    fun verifyAndAddFreshPeer(freshPeer: Peer, localHostAllowed: Boolean = false)
    {
        val peer = Peer(freshPeer.ip, freshPeer.port, this)

        // If the ip isnt localhost or if localhost is allowed
        if(localHostAllowed or (!localHostAllowed and (peer.ip == localhost)))
        {
            // Check if the peer's status is OK
            if (peer.isOk())
            {
                peers.add(peer)
            }
        }
    }

    fun broadcast(transaction: Transaction): Int
    {
        for (i in 1..broadcastMax)
        {
            getRandomPeer().postTransaction(transaction)
        }

        return broadcastMax
    }

    private fun getRandomPeer(): Peer
    {
        return peers[random.nextInt(peers.size)]
    }
}