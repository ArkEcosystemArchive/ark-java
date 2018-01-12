import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlin.reflect.KFunction2

/**
 * This object handles static operations that require interacting with the Ark Node API.
 * Other classes and objects can statically access these methods. This allows us to isolate common tasks such
 * as request construction, (de)serialization of objects, and mock testing.
 * ALl methods in this class should be accessible asynchronously and therefore return Deferred<> objects to allow a
 * wider range of library applications. All methods in this class should also make use of the generic request() method
 * to keep the code readable
 */
object HttpRequest
{
    /**
     * Assists in the creation of a HTTP request using [path] as the url
     * and [method] as the http method to send. ex. String::httpGet, String::httpPost
     * String:: is used/required for easier access to the method. May be replaced with Fuel::Get, etc, in the future
     */
    suspend fun request(
            path: String,
            parameters: List<Pair<String, Any?>>? = null,
            method: KFunction2<String, @ParameterName(name = "parameters") List<Pair<String, Any?>>?, Request>): Request
    {
        return method(path, parameters)
    }

    /**
     * Returns the status status of a given [peer]
     * Eventually returns a [PeerStatus]
     */
    fun getStatus(peer: Peer): Deferred<PeerStatus>
    {
        //Returns an object that will resolve to PeerStatus eventually
        return async {
            //Returns the Request, Response, Result<PeerStatus, FuelError>
            try
            {
                val tripleResponse = request(path = "${peer.peerURL}/peer/status",
                        method = String::httpGet)
                        .header(peer.network.getHeaders())
                        .responseObject(moshiDeserializerOf<PeerStatus>())

                //Extracts the Result(third object), and returns the PeerStatus
                tripleResponse.third.get()
            }catch (e: Exception)
            {
                PeerStatus(false)
            }
        }
    }

    /**
     * Submits a [transaction] to a [peer]
     * Eventually returns a [TransactionPostResponse]
     */
    fun postTransaction(peer: Peer, transaction: Transaction): Deferred<TransactionPostResponse>
    {
        //Constructs the approperiate JSON structure for /peer/transactions endpoint
        val transactionArray = JsonArray()
        val jsonObject = JsonObject()

        //Adds an array of transactions to a json property named "transactions"
        transactionArray.add(transaction.toJson())
        jsonObject.add("transactions", transactionArray)

        //Returns an object that will resolve to TransactionPostResponse eventually
        return async {
            //Returns the Request, Response, Result<TransactionPostResponse, FuelError>
            val tripleResponse = request(path = "${peer.peerURL}/peer/transactions", method = String::httpPost)
                    .header(peer.network.getHeaders())
                    .header(mapOf("accept" to "application/json"))
                    .header(mapOf("Content-Type" to "application/json"))
                    .body(jsonObject.toString())
                    .responseObject(moshiDeserializerOf<TransactionPostResponse>())

            //Extracts the Result(third object), and returns the TransactionPostResponse
            tripleResponse.third.get()
        }
    }

    /**
     * Retrieves the [amount] of transactions in a list from an [account] using the [peer]
     * Eventually returns a [TransactionList]
     */
    fun getTransactions(peer: Peer, account: Account, amount: Int): Deferred<TransactionList>
    {
        //Returns an object that will resolve to TransactionList eventually
        return async {
            //Returns the Request, Response, Result<TransactionList, FuelError>
            val tripleResponse = request(path = "${peer.peerURL}/api/transactions",
                    method = String::httpGet,
                    parameters = listOf("recipientId" to account.address,
                            "senderId" to account.address,
                            "limit" to amount))
                    .header(peer.network.getHeaders())
                    .responseObject(moshiDeserializerOf<TransactionList>())

            //Extracts the Result(third object), and returns the TransactionList
            tripleResponse.third.get()
        }
    }

    /**
     * Fetches a list of peers from a peerListProvider ([url]) within a [timeout] period
     * Eventually returns a [PeerList]
     */
    fun getFreshPeersFromUrl(url: String, timeout: Int): Deferred<PeerList>
    {
        //Returns an object that will resolve to PeerList eventually
        return async {
            //Returns the Request, Response, Result<PeerList, FuelError>
            val tripleResponse = request(path = "$url/api/peers", method = String::httpGet)
                    .timeout(timeout)
                    .responseObject(moshiDeserializerOf<PeerList>())

            //Extracts the Result(third object), and returns the PeerList
            tripleResponse.third.get()
        }
    }
}