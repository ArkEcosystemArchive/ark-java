import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object CryptoTest: Spek({
    given("Crypto Object")
    {
        on("Passphrase 'this is a top secret passphrase'")
        {
            it("Should generate address 'AGeYmgbg2LgGxRW2vNNJvQ88PknEJsYizC' on Mainnet")
            {

            }

            it("Should generate address 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib' on Devnet")
            {

            }
        }

        on("Transaction Creation")
        {
            it("should verify if normal transaction")
            {

            }

            it("should verify if vote transaction")
            {

            }

            it("should verify if delegate transaction")
            {

            }

            it("should fail to verify if amount is modified")
            {

            }

            it("should fail to verify if fee is modified")
            {

            }

            it("should fail to verify if recipientId is modified")
            {

            }

            it("should serialize to JSON")
            {

            }

            it("should deserialize to JSON")
            {

            }

        }
    }

})