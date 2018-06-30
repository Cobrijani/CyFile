package at.tugraz.tc.cyfile.ui

import android.content.Intent
import android.support.test.espresso.action.ViewActions
import android.support.test.rule.ActivityTestRule

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

import java.util.concurrent.Executor

import at.tugraz.tc.cyfile.AppModule
import at.tugraz.tc.cyfile.BaseInstrumentedTest
import at.tugraz.tc.cyfile.MainActivity
import at.tugraz.tc.cyfile.R
import at.tugraz.tc.cyfile.async.AsyncModule
import at.tugraz.tc.cyfile.crypto.impl.DummyKeyVaultService
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger
import at.tugraz.tc.cyfile.note.NoteModule
import at.tugraz.tc.cyfile.note.NoteService
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretModule
import at.tugraz.tc.cyfile.secret.impl.NoOpSecretPrompter
import at.tugraz.tc.cyfile.secret.impl.OnApplicationForegroundSecretPrompter
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecretPrompter

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import at.tugraz.tc.cyfile.secret.Secret
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class PatternLockActivityInstrumentedTest : BaseInstrumentedTest() {


    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(PatternLockActivity::class.java,
            true,
            false)

    @Rule
    @JvmField
    var mainActivityActivityTestRule = ActivityTestRule(MainActivity::class.java,
            true,
            false)

    @Mock
    private val secretManager: SecretManager? = null

    @Before
    fun setup() {
        val keyVaultService = DummyKeyVaultService()
        app.component = DaggerApplicationComponent.builder()
                .appModule(AppModule(app, NoOpLogger()))
                .noteModule(NoteModule(mock(NoteService::class.java)))
                .asyncModule(AsyncModule(mock(Executor::class.java)))
                .secretModule(SecretModule(secretManager!!,
                        OnApplicationForegroundSecretPrompter(
                                PinPatternSecretPrompter(app), keyVaultService, NoOpSecretPrompter()),
                        keyVaultService)).build()
    }

    @Test
    fun correctPatternRemovesPatternView() {
        `when`(secretManager!!.verify(any<Secret>()))
                .thenReturn(true)

        `when`(secretManager.secretIsSet())
                .thenReturn(true)

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeDown())

        onView(withId(R.id.pattern_lock_view)).check(doesNotExist())

    }

    @Test
    fun falsePatternDoesNotRemoveView() {
        `when`(secretManager!!.verify(any<Secret>()))
                .thenReturn(false)
        `when`(secretManager.secretIsSet())
                .thenReturn(true)

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeDown())

        onView(withId(R.id.pattern_lock_view)).check(matches(isDisplayed()))
    }

    @Test
    fun twoCorrectPinUserInputsRemovesView() {
        `when`(secretManager!!.secretIsSet())
                .thenReturn(false)

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .check(doesNotExist())
    }

    @Test
    fun twoIncorrectPinUserInputsDoesNotRemoveView() {
        `when`(secretManager!!.secretIsSet())
                .thenReturn(false)

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeRight())
                .perform(ViewActions.click())
                .perform(ViewActions.swipeDown())
                .check(matches(isDisplayed()))
    }


    @Test
    fun setFalsePinPatternCenterBottom() {
        `when`(secretManager!!.secretIsSet())
                .thenReturn(false)

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(swipeFromCenterToBottom())
                .perform(ViewActions.click())
                .perform(swipeFromCenterToBottom())
                .check(matches(isDisplayed()))
    }
}
