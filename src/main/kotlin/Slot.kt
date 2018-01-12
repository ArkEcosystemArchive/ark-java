import java.text.SimpleDateFormat
import java.util.*

/**
 * Handles the date processing for Crypto operations
 * TODO: Single use method has it's own object. Do we really need it?
 */
object Slot
{
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun getTime(date: Date = Date()): Int
    {
        //Use UTC timezone
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val beginEpoch = dateFormat.parse("2017-03-21 13:00:00")

        var returnDate = Date()

        date?.let {
            returnDate = date
        }

        return ((returnDate.time - beginEpoch.time) / 1000).toInt()
    }
}