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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
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
            val requestFile: RequestBody = RequestBody.create(
                context.contentResolver.getType(uri)?.let { it.toMediaTypeOrNull() },
                uri.getFilePath(context)!!
            )
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.getFileName(context))
            var mime =
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
            if(mime==null) {
                mime = "file"
            }
            val requestFile1 = RequestBody.create(
                mime.toMediaType(),
                file
            )
            val requestFile2 = RequestBody.create(
                MultipartBody.FORM,
                file
            )



            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                uri.getFileName(context),
                requestFile2
            )
            val multipartbody2: MultipartBody.Part = MultipartBody.Part.createFormData(
                "myfile",
                file.name,
                requestFile1
            )
            Timber.d("${file.name} ${file.totalSpace}")
            val response = shareifyAPI.uploadFile(multipartbody2)
            Timber.d(response.toString())
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An Unknown Error Occured", null)
            } else {
                Timber.d(response.message())
                Resource.error("An Unknown Error Occured", null)
            }
        } catch (e: Exception){
            Timber.d(e)
            Resource.error("Check Internet Connection", null)
        }
    }
}

/*
String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
 String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
                RequestBody requestBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("filelink", imageName, RequestBody.create(MediaType.parse(mime), file))
//                        .addFormDataPart("Pdlaid", pdlaId)
//                        .addFormDataPart("nextvisitdate", finalDateInput)
//                        .build();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("filelink", imageName, RequestBody.create(MediaType.parse(mime), file))
                        .addFormDataPart("Pdlaid", pdlaId)
                        .addFormDataPart("pdla_patient_history",et1.getText().toString())
                        .addFormDataPart("pdla_prescription_suggested_clinical_test",et2.getText().toString())
                        .addFormDataPart("pdla_prescription_medication",et3.getText().toString())
                        .addFormDataPart("pdla_prescription_others",et4.getText().toString())
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Contants.SERVICE_BASE_URL + Contants.UPDATE_DOCTOR_PATIENT_TODO_LIST)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .post(requestBody)
                        .build();
 */