package farees.hussain.shareify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shared_items")
data class ShareifyItem(
    var name: String,
    var fileUrl: String,
    var sharedDate: String,
    var isExpired: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Long?=null
)