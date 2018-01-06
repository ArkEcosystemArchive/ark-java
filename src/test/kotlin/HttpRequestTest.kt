import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given

data class IssueInfo(val id: Int, val title: String, val number: Int)

object HttpRequestTest: Spek({
    given("An HttpRequest Object")
    {
    }
})
