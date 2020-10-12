package farees.hussain.shareify.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Resource
import java.io.File


class FakeRespository : ShareifyRepository {
    private val shareifyItems = mutableListOf<ShareifyItem>()
    private val observableItems = MutableLiveData<List<ShareifyItem>>()

    private var shouldReturrnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean){
        shouldReturrnNetworkError = value
    }

    private fun refreshLiveData(){
        observableItems.postValue(shareifyItems)
    }

    override suspend fun insertShareifyItem(shareifyItem: ShareifyItem) {
        shareifyItems.add(shareifyItem)
        refreshLiveData()
    }

    override suspend fun deleteShareifyItem(shareifyItem: ShareifyItem) {
        shareifyItems.remove(shareifyItem)
        refreshLiveData()
    }

    override fun observeAllShareifyItems(): LiveData<List<ShareifyItem>> {
        return observeAllShareifyItems()
    }

    override fun deleteAllShareifyItems() {
        shareifyItems.clear()
        refreshLiveData()
    }

    override suspend fun uploadFile(file: File): Resource<UploadResponse> {
        return if(shouldReturrnNetworkError){
            Resource.error("Error", null)
        }else{
            Resource.success(UploadResponse("url"))
        }
    }
}