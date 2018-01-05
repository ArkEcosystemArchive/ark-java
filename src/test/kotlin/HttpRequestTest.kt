import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.moshi.responseObject
import org.hamcrest.CoreMatchers
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert

data class IssueInfo(val id: Int, val title: String, val number: Int)

object HttpRequestTest: Spek({
    given("An HttpRequest Object")
    {
        on("fuel test get with issues")
        {

            var issues: List<IssueInfo>? = listOf()

            Fuel.get("https://api.github.com/repos/kittinunf/Fuel/issues").responseObject<List<IssueInfo>> { _, _, result ->
                val newissues = result.get()
            }

/*            HttpRequest.request("https://api.github.com/repos/kittinunf/Fuel/issues", method = String::httpGet)
                    .responseObject<List<IssueInfo>> {_, _, result ->
                        issues = result.get()
                    }*/

            it("should be good")
            {
                Assert.assertNotEquals(issues!!.size, 0)
                Assert.assertThat(issues!![0], CoreMatchers.isA(IssueInfo::class.java))
            }
        }
    }
})
