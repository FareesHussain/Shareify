package farees.hussain.shareify.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import farees.hussain.shareify.MainCoroutineRule
import farees.hussain.shareify.getOrAwaitValueTest
import farees.hussain.shareify.other.Constants.MAX_FILE_SIZE
import farees.hussain.shareify.other.Status
import farees.hussain.shareify.repositories.FakeShareifyRespository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ShareifyViewModelTest{
    private lateinit var viewModel: ShareifyViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup(){
        viewModel = ShareifyViewModel(FakeShareifyRespository())
    }

    @Test
    fun `insert shareify item with empty filename returns error`(){
        viewModel.insertShareifyItem("","www.thisIsAUrlTrustMe.com",456666, Date(233543523652),false)
        val value = viewModel.insertShareifyItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shareify item with empty fileurl returns error`(){
        viewModel.insertShareifyItem("file_name.png","",456666, Date(233543523652),false)
        val value = viewModel.insertShareifyItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert shareify item with empty date returns error`(){
        viewModel.insertShareifyItem("file_name.png","",456666, Date(0),false)
        val value = viewModel.insertShareifyItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert shareify item with expired returns error`(){
        viewModel.insertShareifyItem("file_name.png","www.thisIsAUrlTrustMe.com",456666, Date(233543523652),true)
        val value = viewModel.insertShareifyItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert shareify item with file size greater than the limit returns error`(){
        viewModel.insertShareifyItem("file_name.png","www.thisIsAUrlTrustMe.com",(MAX_FILE_SIZE+1).toLong(), Date(233543523652),false)
        val value = viewModel.insertShareifyItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert shareify item with valid input returns success`(){
        viewModel.insertShareifyItem("file_name.png","www.thisIsAUrlTrustMe.com",(MAX_FILE_SIZE-1).toLong(), Date(233543523652),false)
        val value = viewModel.insertShareifyItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}