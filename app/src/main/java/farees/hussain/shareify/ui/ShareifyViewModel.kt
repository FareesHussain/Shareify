package farees.hussain.shareify.ui

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Event
import farees.hussain.shareify.other.Resource
import farees.hussain.shareify.repositories.ShareifyRepository
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class ShareifyViewModel @ViewModelInject constructor(
    private val repository: ShareifyRepository
) : ViewModel() {
    val shoppingItems = repository.observeAllShareifyItems()

    private val _fileUrl = MutableLiveData<Event<UploadResponse>>()
    val fileUrl : LiveData<Event<UploadResponse>> = _fileUrl

    private val _curFileUri = MutableLiveData<Uri>()
    val curFileUri : LiveData<Uri> = _curFileUri

    private val _curFileUrl = MutableLiveData<String>()
    val curFileUrl : LiveData<String> = _curFileUrl

    private val _insertShareifyItem = MutableLiveData<Event<Resource<ShareifyItem>>>()
    val insertShareifyItem : LiveData<Event<Resource<ShareifyItem>>> = _insertShareifyItem

    fun setCurFileUrl(url:String){
        _curFileUrl.postValue(url)
    }
    fun deleteShareifyItem(shareifyItem: ShareifyItem) = viewModelScope.launch {
        repository.deleteShareifyItem(shareifyItem)
    }
    fun insertShareifyItem(shareifyItem: ShareifyItem) = viewModelScope.launch {
        repository.insertShareifyItem(shareifyItem)
    }

    fun setCurFileUri(uri: Uri){
        _curFileUri.postValue(uri)
    }

    fun insertShareifyItem(fileName: String, fileUrl:String, uploadDate: Date, isExpired: Boolean){

    }

    fun getFileUrl(file: File){

    }
}