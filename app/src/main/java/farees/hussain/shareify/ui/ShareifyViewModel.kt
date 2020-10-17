package farees.hussain.shareify.ui

import android.content.Context
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ApplicationContext
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.data.remote.response.UploadResponse
import farees.hussain.shareify.other.Constants
import farees.hussain.shareify.other.Event
import farees.hussain.shareify.other.Resource
import farees.hussain.shareify.repositories.ShareifyRepository
import farees.hussain.shareify.utils.getFileName
import farees.hussain.shareify.utils.getFilePath
import farees.hussain.shareify.utils.getFileSize
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ShareifyViewModel @ViewModelInject constructor(
    @ApplicationContext val context: Context,
    private val repository: ShareifyRepository
) : ViewModel() {
    val shoppingItems = repository.observeAllShareifyItems()

    //! variables for upload fragment
    private val _fileName = MutableLiveData<String>()
    val fileName : LiveData<String>
    get() = _fileName
    private val _fileSize = MutableLiveData<String>()
    val fileSize : LiveData<String>
    get() = _fileSize
    private val _filePath = MutableLiveData<String>()
    val filePath : LiveData<String>
    get() = _filePath

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
        val fileSizeInBytes = uri.getFileSize(context)
        val fileName1 = uri.getFileName(context)
        val filepath1 = uri.getFilePath(context)
        var fileSizeInMB = Math.round((fileSizeInBytes.toDouble()/1000000) * 100.0)/100.0
        _fileSize.postValue(fileSizeInMB.toString()+"MB")
        _fileName.postValue(fileName1)
        _filePath.postValue(filepath1!!)
        Timber.d(fileName.value)
        Timber.d(fileSizeInBytes)
        Timber.d(filePath.value)
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

//    fun getFileUrl(file: File){
//        if(!file.isFile){
//            return
//        }
//        _fileUrl.value = Event(Resource.loading(null))
//        viewModelScope.launch {
//            val response = repository.uploadFile(file)
//            _fileUrl.value = Event(response)
//        }
//    }

    fun uploadFile(){
        _fileUrl.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.uploadFile(curFileUri.value!!,context)
            _fileUrl.value = Event(response)
            Timber.d(response.data?.file)
            Timber.d(_fileUrl.value!!.getContentIfNotHandled()?.message)
            Timber.d(_fileUrl.value!!.getContentIfNotHandled()?.message.toString())
        }
    }

    fun uploadAndNavigateBack(){

    }
}