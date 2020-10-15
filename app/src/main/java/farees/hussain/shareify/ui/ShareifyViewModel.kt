package farees.hussain.shareify.ui

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Constants
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

    private val _fileUrl = MutableLiveData<Event<Resource<UploadResponse>>>()
    val fileUrl : LiveData<Event<Resource<UploadResponse>>> = _fileUrl

    private val _curFileUri = MutableLiveData<Uri>()
    val curFileUri : LiveData<Uri> = _curFileUri

    private val _curFileUrl = MutableLiveData<String>()
    val curFileUrl : LiveData<String> = _curFileUrl

    private val _insertShareifyItemStatus = MutableLiveData<Event<Resource<ShareifyItem>>>()
    val insertShareifyItemStatus : LiveData<Event<Resource<ShareifyItem>>> = _insertShareifyItemStatus

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
        //todo -> to set values for filename, file Uri, file path
        /*
            todo -> setting values using a extension function for Uri
         */
    }

    fun insertShareifyItem(fileName: String, fileUrl:String, filesize:Long, uploadDate: Date, isExpired: Boolean){
        if(fileName.isEmpty() || fileUrl.isEmpty() || uploadDate == Date(0)){
            _insertShareifyItemStatus.postValue(Event(Resource.error("Invalid File Format Try Another File",null)))
            return
        }
        if(isExpired){
            _insertShareifyItemStatus.postValue(Event(Resource.error("File can't be expired before uploading",null)))
            return
        }
        if(filesize>Constants.MAX_FILE_SIZE){
            _insertShareifyItemStatus.postValue(Event(Resource.error("File size is greater than 100MB", null)))
            return
        }
        val shareifyItem = ShareifyItem(
            fileName,
            fileUrl,
            filesize,
            uploadDate,
            isExpired
        )
        insertShareifyItem(shareifyItem)
        _insertShareifyItemStatus.postValue(Event(Resource.success(shareifyItem)))
    }

    fun getFileUrl(file: File){
        if(!file.isFile){
            return
        }
        _fileUrl.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.uploadFile(file)
            _fileUrl.value = Event(response)
        }
    }
}