package farees.hussain.shareify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShareifyItem::class],
    version = 1
)
abstract class ShareifyDatabase: RoomDatabase() {
    abstract fun shareifyDao(): ShareifyDao
}