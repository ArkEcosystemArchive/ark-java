import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlin.reflect.KFunction2

object HttpRequest
{
    /**
     * Assists in the creation of a HTTP request using [path] as the url
     * and [method] as the http method to send. ex. String::httpGet, String::httpPost
     * String:: is used/required for easier access to the method. May be replaced with Fuel::Get, etc, in the future
     */
    fun request(
            path: String,
            parameters: List<Pair<String, Any?>>? = null,
            method: KFunction2<String, @ParameterName(name = "parameters") List<Pair<String, Any?>>?, Request>): Request
    {
        return method(path, parameters)
    }

    fun getStatus(peerURL: String, network: Network): PeerData
    {
        var peerInfo: PeerData? = null

        request(path = "$peerURL/peer/status", method = String::httpGet)
                .header(network.getHeaders())
                .responseObject(moshiDeserializerOf<PeerData>()) { _, _, result ->
                    when(result)
                    {
                        is Result.Success ->
                        {
                            peerInfo = result.component1()!!
                        }
                    }
                }

        return peerInfo!!
    }

    fun postTransaction(peerURL: String, network: Network, transaction: Transaction): TransactionPostResponse?
    {
        val jsonArray = JsonArray()
        val jsonObject = JsonObject()
        var transactionData: TransactionPostResponse? = null

        jsonArray.add(transaction.toJson())
        jsonObject.add("transactions", jsonArray)

        request(path = "$peerURL/peer/transactions", method = String::httpPost)
                .header(network.getHeaders())
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

        return transactionData
    }

    fun getTransactions(peerURL: String, network: Network, account: Account, amount: Int): TransactionList
    {
        var transactions: TransactionList? = null

        request(path = "$peerURL/api/transactions",
                method = String::httpGet,
                parameters = listOf("recipientId" to account.address, "senderId" to account.address, "limit" to amount))
                .header(network.getHeaders())
                .responseObject(moshiDeserializerOf<TransactionList>()) { _, _, result ->
                    when(result)
                    {
                        is Result.Success -> transactions = result.component1()!!
                    }
                }

        return transactions!!
    }
}