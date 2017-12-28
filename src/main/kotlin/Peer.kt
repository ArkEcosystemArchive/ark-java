import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import java.util.Collections.emptyMap
import java.util.concurrent.FutureTask


class Peer(peerInfo: String, headers: Triple<String, String, Int>)
{
    private var protocol: String = "http://"
    private var status: String = "NEW"

    init
    {
        val data = peerInfo.split(':')
        val ip: String = data[0]
        val port: Int = data[1].toInt()

        if (port % 1000 == 443) protocol = "https://"
    }

    fun request(method: String, path: String, body: Map<String, String> = emptyMap()): FutureTask<Request>
    {
        var result: Request? = null

        when (method)
        {
            "POST" -> result = path.httpPost()
            "PUT" -> result = path.httpPut()
            "GET" -> result = path.httpGet()
        }

        var that = this;

    return FutureTask {
            result!!.responseString { request, response, result ->

                when (result)
                {
                    is Result.Success ->
                    {
                        that.status = "OK"
                        return response.responseMessage
                    }
                }
            }
        }
    }

    fun getStatus(): Map<String, String>
    {
        return request("GET", "/peer/status").get()
    }

    fun postTransaction(transaction: Transaction)
    {

    }


}
