import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals

object NetworkTest: Spek({
    given("A random mainnet peer")
    {
        val mainnet = NetworkConstants.mainnet
        runBlocking {
            mainnet.warmup(2)
        }

        val peer = mainnet.getRandomPeer()

        on("getStatus query") {
            var status: PeerStatus? = null

            runBlocking {
                status = peer.getStatus()
            }

            it("should return a currentSlot higher than it's status") {
                assert(status!!.currentSlot > status!!.height)
            }
        }

    }

    given("A random devnet peer")
    {
        val devnet = NetworkConstants.devnet
        runBlocking {
            devnet.warmup(2)
        }

        val peer = devnet.getRandomPeer()

        on("getStatus query")
        {
            var status: PeerStatus? = null

            runBlocking {
                status = peer.getStatus()
            }

            it("should return a currentSlot higher than it's status")
            {
                assert(status!!.currentSlot > status!!.height)
            }
        }
    }

    given("A mocked peer")
    {
        val mockedPeer: Peer = mock{
            on { postTransaction(any()) } doReturn async { TransactionPostResponse(
                    true,
                    "",
                    "Account does not have enough ARK: AGeYmgbg2LgGxRW2vNNJvQ88PknEJsYizC balance: 0") }
        }

        on("POST transaction")
        {
            //TODO: Mock transactions as well? maybe...
            val transaction = Crypto.createTransaction(
                    "AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25",
                    133380000000,
                    "This is first transaction from JAVA",
                    "this is a top secret passphrase")

            var result: TransactionPostResponse? = null

            runBlocking {
                result = mockedPeer.postTransaction(transaction).await()
            }

            it("should return 'not enough ARK' error")
            {
                assertEquals("Account does not have enough ARK: AGeYmgbg2LgGxRW2vNNJvQ88PknEJsYizC balance: 0", result!!.error)
            }
        }

        on("Broadcast transaction")
        {

            it("should broadcast to the max amount")
            {

            }
        }
    }

})
