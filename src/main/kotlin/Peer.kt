import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlin.reflect.KFunction2

class Peer(val ip: String, val port: Int, val network: Network)
{
    private var protocol: String = "http://"
    private var status: String = "NEW"
    private var peerURL: String? = null

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

    private fun request(
            path: String,
            parameters: List<Pair<String, Any?>>? = null,
            method: KFunction2<String, @ParameterName(name = "parameters") List<Pair<String, Any?>>?, Request>): Request
    {
        return method(path, parameters)
                .header(network.getHeaders())
    }

    fun isOk() = getStatus().success

    fun getStatus(): PeerData
    {
        var peerInfo: PeerData? = null

        request(path = "/peer/status", method = String::httpGet)
                .responseObject(moshiDeserializerOf<PeerData>()) { _, _, result ->
                    when(result)
                    {
                        is Result.Success ->
                        {
                            status = "OK"
                            peerInfo = result.component1()!!
                        }
                    }
                }

        return peerInfo!!
    }

    fun postTransaction(transaction: Transaction): TransactionPostResponse?
    {
        val jsonArray = JsonArray()
        val jsonObject = JsonObject()
        var transactionData: TransactionPostResponse? = null

        jsonArray.add(transaction.toJson())
        jsonObject.add("transactions", jsonArray)

        request(path = "/peer/transactions", method = String::httpPost)
                .body(jsonObject.toString())
                .responseObject(moshiDeserializerOf<TransactionPostResponse>()) { _, _, result ->
                    when(result)
                    {
                        is Result.Success ->
                        {
                            status = "OK"
                            transactionData = result.component1()!!
                        }
                    }
                }

        return transactionData
    }

    fun getTransactions(account: Account, amount: Int): TransactionList
    {
        var transactions: TransactionList? = null

        request(path = "/api/transactions",
                method = String::httpGet,
                parameters = listOf("recipientId" to account.address, "senderId" to account.address, "limit" to amount))
                .responseObject(moshiDeserializerOf<TransactionList>()) { _, _, result ->
                    when(result)
                    {
                        is Result.Success -> transactions = result.component1()!!
                    }
                }

        return transactions!!
    }
}
