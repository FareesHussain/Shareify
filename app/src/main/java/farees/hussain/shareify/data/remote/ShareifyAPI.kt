package farees.hussain.shareify.data.remote

import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Constants.UPLOAD_ENDPOINT
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.File

interface ShareifyAPI {

    @POST(UPLOAD_ENDPOINT)
    suspend fun uploadFile(
        @Query("myfile") uploadFile: File
    ) : Response<UploadResponse>

}