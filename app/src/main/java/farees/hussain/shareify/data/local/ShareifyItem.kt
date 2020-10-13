package farees.hussain.shareify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "shared_items")
data class ShareifyItem(
    var filename: String,
    var fileUrl: String,
    var fileSize: Long,
    var sharedDate: Date,
    var isExpired: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Long?=null
)