import kotlinx.coroutines.experimental.async
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object NetworkTest: Spek({
    given("A random mainnet peer")
    {
        val mainnet = NetworkConstants.mainnet
        async {
            mainnet.warmup(2)
        }

        on("getStatus query")
        {
            it("should return a currentSlot higher than it's status")
            {

            }
        }

        on("POST transaction")
        {
            it("should return 'not enough ARK' error")
            {

            }
        }

        on("Broadcast transaction")
        {
           it("should broadcast to the max amount")
           {

           }
        }
    }

    given("A random devnet peer")
    {
        val devnet = NetworkConstants.devnet
        async {
            devnet.warmup(2)
        }

        on("getStatus query")
        {
            it("should return a currentSlot higher than it's status")
            {

            }
        }
    }
})
