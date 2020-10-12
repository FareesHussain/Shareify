package farees.hussain.shareify.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShareifyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(sharedFile: ShareifyItem)

    @Delete
    suspend fun deleteFile(sharedFile: ShareifyItem)

    @Query("select * from shared_items")
    fun observeAllFiles(): LiveData<List<ShareifyItem>>

    @Query("delete from shared_items")
    fun deleteAllFiles()
}