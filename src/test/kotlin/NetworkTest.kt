import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.*

object NetworkTest: Spek({
    given("Mainnet")
    {
        on("warmup")
        {
            val mainnet = NetworkConstants.mainnet
            var warmupStatus: Boolean = false

            runBlocking {
                warmupStatus = mainnet.warmup(2)
            }

            it("Should warmup successfully")
            {
                assert(warmupStatus)
            }
        }
    }

    given("Devnet")
    {
        on("warmup")
        {
            val devnet = NetworkConstants.devnet
            var warmupStatus: Boolean = false

            runBlocking {
                warmupStatus = devnet.warmup(2)
            }

            it("Should warmup successfully")
            {
                assert(warmupStatus)
            }
        }
    }

    given("A mocked peer")
    {
        val transaction = Crypto.createTransaction(
                "AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25",
                133380000000,
                "This is first transaction from JAVA",
                "this is a top secret passphrase")

        val mockedPeer = MockedObjects.mockedPeer

        on("getStatus query") {
            var status: PeerStatus? = null

            runBlocking {
                status = mockedPeer.getStatus().await()
            }

            it("should return a currentSlot higher than it's status") {
                assert(status!!.currentSlot > status!!.height)
            }
        }

        context("POST transactions request")
        {
            var result: TransactionPostResponse? = null

            on("a valid transaction")
            {
                runBlocking {
                    result = mockedPeer.postTransaction(transaction).await()
                }

                it("should not return an error property")
                {
                    assertNull(result!!.error)
                }

                it("should return a list of transaction ids with the first element containing this transaction's id")
                {
                    assertEquals(transaction.id, result!!.transactionIds!![0])
                }
            }

            on("a invalid transaction")
            {
                transaction.id = null

                runBlocking {
                    result = mockedPeer.postTransaction(transaction).await()
                }

                it("should not return a list of transactionIds")
                {
                    assertNull(result!!.transactionIds)
                }

                it("should return an error String")
                {
                    assertNotNull(result!!.error)
                }
            }
        }
    }
})
