import HttpRequest.getFreshPeersFromUrl
import com.github.kittinunf.fuel.core.Request
import kotlinx.coroutines.experimental.async
import kotlin.reflect.KFunction2
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
    private val defaultTimeout = 30000

    fun getHeaders(): Map<String, Any>
    {
        return mapOf(
                "nethash" to nethash,
                "version" to version,
                "port" to port)
    }

    suspend fun warmup(numberOfPeers: Int = 20): Boolean
    {
        if (peers.isNotEmpty()) return false

        for(peer in getFreshPeers(numberOfPeers).peers)
        {
            verifyAndAddFreshPeer(peer)
        }

        if (peers.isEmpty())
        {
            fallBackToPeerSeed(numberOfPeers)
        }

        return true
    }

    fun fallBackToPeerSeed(numberOfPeers: Int)
    {
        val limit = if (numberOfPeers < peerseed!!.size) numberOfPeers else peerseed!!.size

        for(peerseed in peerseed!!.subList(0, limit))
        {
            verifyAndAddSeedPeer(peerseed)
        }
    }

    suspend fun getFreshPeers(limitResults: Int, timeout: Int = defaultTimeout): PeerList
    {
        //Used to store return
        var resultList: PeerList?

        //Shuffle the list of peer providers
        Collections.shuffle(peerListProviders)

        // Iterate through each provider until a successful list of peers is retrieved
        val urlList = peerListProviders.listIterator()

        do
        {
            //Fetch the list of peers for the next provider
            resultList = getFreshPeersFromUrl(urlList.next(), timeout).await()
        }while ((resultList == null || !resultList.success) && urlList.hasNext())

        //Sort the array by the peers delay
        Arrays.sort(resultList!!.peers, compareBy({it.delay}))

        //Remove results over the provided limit
        resultList.peers = resultList.peers.sliceArray(0..limitResults)

        return resultList
    }

    fun verifyAndAddSeedPeer(peerInfo: String)
    {
        val peer = Peer(peerInfo.split(":"), this)

        async {
            if (peer.isOk())
            {
                peers.add(peer)
            }
        }
    }

    fun verifyAndAddFreshPeer(freshPeer: PeerData, localHostAllowed: Boolean = false)
    {
        val peer = Peer(freshPeer.ip, freshPeer.port, this)

        // If the ip isn't localhost or if localhost is allowed
        if(localHostAllowed or (!localHostAllowed and (peer.ip == localhost)))
        {
            // Check if the peer's status is OK
            async {
                if (peer.isOk())
                {
                    peers.add(peer)
                }
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

    /**
     * Forwards requests to the HttpRequest object while injecting a random peer's url into the path
     * Only the [endpoint] path is required to access the ark node's API. ex. "/peer/transactions/"
     * [method] and [parameters] remain fully configurable as seen in the parent function in the HttpRequest object
     */
    suspend fun request(
            endpoint: String,
            parameters: List<Pair<String, Any?>>? = null,
            method: KFunction2<String, @ParameterName(name = "parameters") List<Pair<String, Any?>>?, Request>)
            = HttpRequest.request(getRandomPeer().peerURL + endpoint, parameters, method)
}