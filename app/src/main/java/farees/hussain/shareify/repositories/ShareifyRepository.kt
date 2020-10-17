package farees.hussain.shareify.repositories

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Resource
import java.io.File

interface ShareifyRepository {
    suspend fun insertShareifyItem(shareifyItem: ShareifyItem)
    suspend fun deleteShareifyItem(shareifyItem: ShareifyItem)
    fun observeAllShareifyItems():LiveData<List<ShareifyItem>>
    fun deleteAllShareifyItems()

    suspend fun uploadFile(uri : Uri,context: Context): Resource<UploadResponse>
}