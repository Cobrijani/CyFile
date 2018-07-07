package at.tugraz.tc.cyfile

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.GeneralLocation
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.runner.AndroidJUnit4
import io.mockk.MockKAnnotations

import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentedTest {

    protected lateinit var app: CyFileApplication

    @Before
    fun initialization() {
        MockKAnnotations.init(this)

        app = InstrumentationRegistry
                .getInstrumentation()
                .targetContext
                .applicationContext as CyFileApplication
    }


    protected fun swipeFromCenterToBottom(): ViewAction {
        return GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER,
                GeneralLocation.BOTTOM_CENTER, Press.FINGER)
    }
}