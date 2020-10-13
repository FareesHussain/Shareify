package farees.hussain.shareify.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import farees.hussain.shareify.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShareifyDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    /*
          To encounter `This job has not completed yet` error
     */
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShareifyDatabase
    private lateinit var dao: ShareifyDao


    @Before
    fun setup(){
        hiltRule.inject()
        dao = database.shareifyDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertShareifyItem() = runBlockingTest {
        val shareifyItem = ShareifyItem(
            "file",
            "https://thisIsAUrlBelieveMe.com",
            2364567,
                Calendar.getInstance().time,
            false,
            1
        )
        dao.insertFile(shareifyItem)
        val allFiles = dao.observeAllFiles().getOrAwaitValue()
        assertThat(allFiles).contains(shareifyItem)
    }

    @Test
    fun deleteSharifyItem() = runBlockingTest {
        val shareifyItem = ShareifyItem(
            "file",
            "https://thisIsAUrlBelieveMe.com",
            2346,
            Calendar.getInstance().time,
            false,
            1
        )
        dao.insertFile(shareifyItem)
        dao.deleteFile(shareifyItem)
        val allFiles = dao.observeAllFiles().getOrAwaitValue()
        assertThat(allFiles).doesNotContain(shareifyItem)
    }

    @Test
    fun deleteAllFiles() = runBlockingTest {
        val shareifyItem1 = ShareifyItem(
            "file",
            "https://thisIsAUrlBelieveMe.com",
            34567890,
            Calendar.getInstance().time,
            false,
            1
        )
        val shareifyItem2 = ShareifyItem(
            "file",
            "https://thisIsAUrlBelieveMe.com",
            456789,
            Calendar.getInstance().time,
            false,
            2
        )
        val shareifyItem3 = ShareifyItem(
            "file",
            "https://thisIsAUrlBelieveMe.com",
            45678,
            Calendar.getInstance().time,
            false,
            3
        )
        dao.insertFile(shareifyItem1)
        dao.insertFile(shareifyItem2)
        dao.insertFile(shareifyItem3)
        dao.deleteAllFiles()
        val allFiles = dao.observeAllFiles().getOrAwaitValue()
        assertThat(allFiles.count()).isEqualTo(0)
    }

}