import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlin.reflect.KFunction2

class Peer(peerInfo: String, private val headers: Map<String, Any>)
{
    private var protocol: String = "http://"
    private var status: String = "NEW"
    private var peerURL: String? = null

    init
    {
        val data = peerInfo.split(':')
        val ip: String = data[0]
        val port: Int = data[1].toInt()

        if (port % 1000 == 443) protocol = "https://"

        peerURL = "$protocol$ip$port"
    }

    private fun request(
            path: String,
            parameters: List<Pair<String, Any?>>? = null,
            method: KFunction2<String, @ParameterName(name = "parameters") List<Pair<String, Any?>>?, Request>): Request
    {
        return method(path, parameters)
                .header(headers)
    }

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

    fun postTransaction(transaction: Transaction): TransactionData?
    {
        val jsonArray = JsonArray()
        val jsonObject = JsonObject()
        var transactionData: TransactionData? = null

        jsonArray.add(transaction.toJson())
        jsonObject.add("transactions", jsonArray)

        request(path = "/peer/transactions", method = String::httpPost)
                .body(jsonObject.toString())
                .responseObject(moshiDeserializerOf<TransactionData>()) { _, _, result ->
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
}
