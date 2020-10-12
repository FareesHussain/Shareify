package farees.hussain.shareify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import farees.hussain.shareify.data.converters.Converters

@Database(
    entities = [ShareifyItem::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ShareifyDatabase: RoomDatabase() {
    abstract fun shareifyDao(): ShareifyDao
}