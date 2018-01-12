import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.experimental.async

object MockedObjects
{
    val mockedPeer: Peer = mock{
        on { postTransaction(any()) } doAnswer { invocation ->
            val parameter = invocation.getArgument<Transaction>(0)

            //TODO: Improve verification. Extract into method of Transaction.
            //TODO: Make sure this doesn't collide with tests for Crypto class (ex. createTransaction(), processTransaction())
            if(parameter.recipientId != null && parameter.id != null && parameter.timestamp != null)
            {
                async { TransactionPostResponse(
                        true,
                        "",
                        listOf(parameter.id))
                }
            }
            else
                async { TransactionPostResponse(
                        true,
                        "",
                        "ERROR")
                }
        }
        on{ getStatus() } doAnswer { invocation ->
            async {
               PeerStatus(true,
                       0,
                       true,
                       1,
                       null)
            }
        }
    }
}
