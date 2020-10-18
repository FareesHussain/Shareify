package farees.hussain.shareify.repositories

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import farees.hussain.shareify.data.local.ShareifyDao
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.data.remote.ShareifyAPI
import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.CountingRequestBody
import farees.hussain.shareify.other.CountingRequestListener
import farees.hussain.shareify.other.Resource
import farees.hussain.shareify.utils.getFileName
import farees.hussain.shareify.utils.getFilePath
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

typealias ProgressUpdate = (progress: Double) -> Unit

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



    override suspend fun uploadFile(uri: Uri, context: Context, progress:ProgressUpdate): Resource<UploadResponse> {
        return try {
            //todo socket timeout uploading large files
            //todo %dialog box while uploading
            val file = File(uri.getFilePath(context))
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.getFileName(context))
            var mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
            val requestFile = file.asRequestBody((mime?:"file").toMediaType())
            var progress = 1.0
            val requestBody = CountingRequestBody(requestFile){bytesWritten,_->
                progress = 1.0*bytesWritten/file.length()
                progress(progress)
//                Timber.d("progress $progress $bytesWritten")
            }
            val multipartbody = MultipartBody.Part.createFormData("myfile", file.name, requestBody )
            Timber.d("${file.name} ${file.length()}")
            val response = shareifyAPI.uploadFile(multipartbody)
            Timber.d(response.toString())
            if(progress<1.0){
                Resource.loadingProgress(progress,null)
                Timber.d(progress.toString())
            }
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An Unknown Error Occured", null)
            } else {
                Resource.error("An Unknown Error Occured", null)
            }
        } catch (e: Exception){
            Timber.d(e)
            Resource.error("Check Internet Connection", null)
        }
    }
}