import java.text.SimpleDateFormat
import java.util.*

object Slot
{
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun getTime(date: Date = Date()): Int
    {
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val beginEpoch = dateFormat.parse("2017-03-21 13:00:00")

        var returnDate = Date()

        date?.let {
            returnDate = date
        }

        return ((returnDate.time - beginEpoch.time) / 1000).toInt()
    }
}