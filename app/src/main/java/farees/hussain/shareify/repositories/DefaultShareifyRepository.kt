package farees.hussain.shareify.repositories

import androidx.lifecycle.LiveData
import farees.hussain.shareify.data.local.ShareifyDao
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.data.remote.ShareifyAPI
import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Resource
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class DefaultShareifyRepository @Inject constructor(
    private val shareifyDao: ShareifyDao,
    private val shareifyAPI: ShareifyAPI
) :ShareifyRepository{
    override suspend fun insertShareifyItem(shareifyItem: ShareifyItem) {
        shareifyDao.insertFile(shareifyItem)
    }

    override suspend fun deleteShareifyItem(shareifyItem: ShareifyItem) {
        shareifyDao.deleteFile(shareifyItem)
    }

    override fun observeAllShareifyItems() = shareifyDao.observeAllFiles()

    override fun deleteAllShareifyItems() =  shareifyDao.deleteAllFiles()

    override suspend fun uploadFile(file: File): Resource<UploadResponse> {
        return try {
            val response = shareifyAPI.uploadFile(file)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An Unknown Error Occured",null)
            } else {
                Resource.error("An Unknown Error Occured",null)
            }
        } catch (e: Exception){
            Resource.error("Check Internet Connection",null)
        }
    }
}