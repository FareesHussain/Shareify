package farees.hussain.shareify.ui.fragments

import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import farees.hussain.shareify.R
import farees.hussain.shareify.launchFragmentInHiltContainer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class UploadFragmentTest{
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun urlobserverNavigatesPopBack(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<UploadFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.buNext)).perform(click())
        verify(navController).popBackStack()
    }
}