import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result

class Peer(peerInfo: String, private val headers: Map<String, Any>)
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

    fun getStatus(): PeerData
    {
        var peerInfo: PeerData? = null

        "/peer/status"
                .httpGet()
                .header(headers)
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

    fun postTransaction(transaction: Transaction)
    {

    }
}
