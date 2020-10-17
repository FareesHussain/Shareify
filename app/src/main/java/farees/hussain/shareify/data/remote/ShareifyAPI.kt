package farees.hussain.shareify.data.remote

import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Constants.UPLOAD_ENDPOINT
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ShareifyAPI {

    @Multipart
    @POST(UPLOAD_ENDPOINT)
    suspend fun uploadFile(
        @Part uploadFile: MultipartBody.Part
    ) : Response<UploadResponse>

}