import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.fuel.moshi.responseObject
import com.github.kittinunf.result.Result
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.experimental.suspendCoroutine
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
        var peerInfo: PeerStatus? = null

        return async {
            request(path = "${peer.peerURL}/peer/status", method = String::httpGet)
                    .header(peer.network.getHeaders())
                    .responseObject(moshiDeserializerOf<PeerStatus>()) { _, _, result ->
                        when(result)
                        {
                            is Result.Success ->
                            {
                                peerInfo = result.component1()!!
                            }
                        }
                    }

            return@async peerInfo!!
        }
    }

    fun postTransaction(peer: Peer, transaction: Transaction): Deferred<TransactionPostResponse>
    {
        val jsonArray = JsonArray()
        val jsonObject = JsonObject()
        var transactionData: TransactionPostResponse? = null

        jsonArray.add(transaction.toJson())
        jsonObject.add("transactions", jsonArray)

        return async {
            request(path = "${peer.peerURL}/peer/transactions", method = String::httpPost)
                    .header(peer.network.getHeaders())
                    .body(jsonObject.toString())
                    .responseObject(moshiDeserializerOf<TransactionPostResponse>()) { _, _, result ->
                        when(result)
                        {
                            is Result.Success ->
                            {
                                transactionData = result.component1()!!
                            }
                        }
                    }

            return@async transactionData!!
        }
    }

    fun getTransactions(peer: Peer, account: Account, amount: Int): Deferred<TransactionList>
    {
        var transactions: TransactionList? = null

        return async {
            request(path = "${peer.peerURL}/api/transactions",
                    method = String::httpGet,
                    parameters = listOf("recipientId" to account.address, "senderId" to account.address, "limit" to amount))
                    .header(peer.network.getHeaders())
                    .responseObject(moshiDeserializerOf<TransactionList>()) { _, _, result ->
                        when(result)
                        {
                            is Result.Success -> transactions = result.component1()!!
                        }
                    }

            return@async transactions!!
        }
    }

    fun getFreshPeersFromUrl(url: String, timeout: Int): Deferred<PeerList>
    {
        var peerList: PeerList? = null

        return async {
            request(path = "$url/api/peers", method = String::httpGet)
                    .timeout(timeout)
                    .responseObject<PeerList> { _, _, result ->
                        when (result)
                        {
                            is Result.Success -> peerList = result.get()
                        }
                    }

            return@async peerList!!
        }
    }
}