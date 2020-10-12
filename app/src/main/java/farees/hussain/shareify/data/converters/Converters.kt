package farees.hussain.shareify.data.converters

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromLongtoDate(longValue: Long):Date{
        return Date(longValue)
    }

    @TypeConverter
    fun fromDateToLong(date: Date): Long{
        return date.time
    }
}