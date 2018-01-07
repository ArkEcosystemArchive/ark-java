import HttpRequest.getFreshPeersFromUrl
import com.github.kittinunf.fuel.core.Request
import kotlinx.coroutines.experimental.runBlocking
import kotlin.reflect.KFunction2
import java.util.*

/**
 * Represents a single Ark Network
 */
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
    //Constant values
    private val random = Random()
    private val localhost = "127.0.0.1"
    private val defaultTimeout = 30000

    /**
     * Returns the headers required by the Ark Node API
     * to make a network request as a Map
     */
    fun getHeaders(): Map<String, Any>
    {
        return mapOf(
                "nethash" to nethash,
                "version" to version,
                "port" to port)
    }

    /**
     * Attempts to fetch a list of peers from a peerListProvider and use them to populate
     * the peers field. If that is unsuccessful then the static hardcoded peerlist is used to
     * populate the peers field
     *
     * Allows specifying how many peers to populate using the [numberOfPeers] parameter. Defaults to 20
     * Returns a boolean indicating if the warmup was successful
     */
    suspend fun warmup(numberOfPeers: Int = 20): Boolean
    {
        //If there are peers in the field, break out of method
        if (peers.isNotEmpty()) return false

        //Network operation looking for a list of peers
        val freshPeers = getFreshPeers(numberOfPeers)?.peers

        //If network operation is successful
        if (freshPeers != null)
        {
            //Add each peer to field
            for(peer in freshPeers)
            {
                verifyAndAddFreshPeer(peer)
            }
        }

        //If at this point we have no peers, that means network operation wasn't successful
        if (peers.isEmpty())
        {
            //Use the hardcoded list to populate peers
            fallBackToPeerSeed(numberOfPeers)
        }

        return true
    }

    /**
     * Populates peers based on hardcoded peerlist until [numberOfPeers]
     */
    fun fallBackToPeerSeed(numberOfPeers: Int)
    {
        //The numberOfPeers is bigger than peerseed, just use peerseed as the limit
        val limit = if (numberOfPeers < peerseed!!.size) numberOfPeers else peerseed!!.size

        for(peerseed in peerseed!!.subList(0, limit))
        {
            verifyAndAddSeedPeer(peerseed)
        }
    }


    fun getFreshPeers(limitResults: Int, timeout: Int = defaultTimeout): PeerList?
    {
        //If we don't have any providers, this method can't do anything
        if (peerListProviders.isEmpty()) return null

        var resultList: PeerList? = null

        //Shuffle the list of peer providers
        Collections.shuffle(peerListProviders)

        // Iterate through each provider until a successful list of peers is retrieved
        val urlList = peerListProviders.iterator()

        do
        {
            //Fetch the list of peers for the next provider
            runBlocking {
                resultList = getFreshPeersFromUrl(urlList.next(), timeout).await()
            }
        }while ((resultList == null || !resultList!!.success) && urlList.hasNext())

        //Sort the array by the peers delay
        Arrays.sort(resultList!!.peers, compareBy({it.delay}))

        //Remove results over the provided limit
        resultList!!.peers = resultList!!.peers.sliceArray(0..limitResults)

        return resultList
    }

    fun verifyAndAddSeedPeer(peerInfo: String)
    {
        val peer = Peer(peerInfo.split(":"), this)

        runBlocking {
            //TODO: Suspected cause of possible NPE if bad peer, catch and handle
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
            runBlocking {
                //TODO: Suspected cause of possible NPE if bad peer, catch and handle
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

    fun getRandomPeer(): Peer
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