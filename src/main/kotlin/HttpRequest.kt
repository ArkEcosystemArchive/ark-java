import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlin.reflect.KFunction2

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

    fun getStatus(peer: Peer): Deferred<PeerStatus>
    {
        return async {
            val tripleResponse = request(path = "${peer.peerURL}/peer/status",
                    method = String::httpGet)
                    .header(peer.network.getHeaders())
                    .responseObject(moshiDeserializerOf<PeerStatus>())

            tripleResponse.third.get()
        }
    }

    fun postTransaction(peer: Peer, transaction: Transaction): Deferred<TransactionPostResponse>
    {
        val transactionArray = JsonArray()
        val jsonObject = JsonObject()

        transactionArray.add(transaction.toJson())
        jsonObject.add("transactions", transactionArray)

        return async {
            val tripleResponse = request(path = "${peer.peerURL}/peer/transactions", method = String::httpPost)
                    .header(peer.network.getHeaders())
                    .header(mapOf("accept" to "application/json"))
                    .header(mapOf("Content-Type" to "application/json"))
                    .body(jsonObject.toString())
                    .responseObject(moshiDeserializerOf<TransactionPostResponse>())

            tripleResponse.third.get()
        }
    }

    fun getTransactions(peer: Peer, account: Account, amount: Int): Deferred<TransactionList>
    {
        return async {
            val tripleResponse = request(path = "${peer.peerURL}/api/transactions",
                    method = String::httpGet,
                    parameters = listOf("recipientId" to account.address,
                            "senderId" to account.address,
                            "limit" to amount))
                    .header(peer.network.getHeaders())
                    .responseObject(moshiDeserializerOf<TransactionList>())

            tripleResponse.third.get()
        }
    }

    fun getFreshPeersFromUrl(url: String, timeout: Int): Deferred<PeerList>
    {
        return async {
            val tripleResponse = request(path = "$url/api/peers", method = String::httpGet)
                    .timeout(timeout)
                    .responseObject(moshiDeserializerOf<PeerList>())

            tripleResponse.third.get()
        }
    }
}