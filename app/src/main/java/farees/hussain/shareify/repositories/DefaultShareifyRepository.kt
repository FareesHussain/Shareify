package farees.hussain.shareify.repositories

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import farees.hussain.shareify.data.local.ShareifyDao
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.data.remote.ShareifyAPI
import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Resource
import farees.hussain.shareify.utils.getFileName
import farees.hussain.shareify.utils.getFilePath
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
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

    override suspend fun uploadFile(uri: Uri, context: Context): Resource<UploadResponse> {
        return try {
            //todo socket timeout uploading large files
            //todo %dialog box while uploading
            val file = File(uri.getFilePath(context))
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.getFileName(context))
            var mime =
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
            if(mime==null) {
                mime = "file"
            }
            val requestFile = RequestBody.create(
                mime.toMediaType(),
                file
            )
            val multipartbody: MultipartBody.Part = MultipartBody.Part.createFormData(
                "myfile",
                file.name,
                requestFile
            )
            Timber.d("${file.name} ${file.totalSpace}")
            val response = shareifyAPI.uploadFile(multipartbody)
            Timber.d(response.toString())
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