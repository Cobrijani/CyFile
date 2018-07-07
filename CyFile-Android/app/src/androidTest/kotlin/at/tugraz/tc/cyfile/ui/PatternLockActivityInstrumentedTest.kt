package at.tugraz.tc.cyfile.ui

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import at.tugraz.tc.cyfile.AppModule
import at.tugraz.tc.cyfile.BaseInstrumentedTest
import at.tugraz.tc.cyfile.MainActivity
import at.tugraz.tc.cyfile.R
import at.tugraz.tc.cyfile.async.AsyncModule
import at.tugraz.tc.cyfile.crypto.impl.DummyKeyVaultService
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger
import at.tugraz.tc.cyfile.note.NoteModule
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretModule
import at.tugraz.tc.cyfile.secret.impl.NoOpSecretPrompter
import at.tugraz.tc.cyfile.secret.impl.OnApplicationForegroundSecretPrompter
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecretPrompter
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    @RelaxedMockK
    private lateinit var secretManager: SecretManager

    @Before
    fun setup() {
        val keyVaultService = DummyKeyVaultService()
        app.component = DaggerApplicationComponent.builder()
                .appModule(AppModule(app, NoOpLogger()))
                .noteModule(NoteModule(mockk(relaxed = true)))
                .asyncModule(AsyncModule(mockk(relaxed = true)))
                .secretModule(SecretModule(secretManager,
                        OnApplicationForegroundSecretPrompter(
                                PinPatternSecretPrompter(app), keyVaultService, NoOpSecretPrompter()),
                        keyVaultService)).build()
    }

    @Test
    fun correctPatternRemovesPatternView() {
        every { secretManager.verify(any()) } returns true

        every { secretManager.secretIsSet() } returns true

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeDown())

        onView(withId(R.id.pattern_lock_view)).check(doesNotExist())

    }

    @Test
    fun falsePatternDoesNotRemoveView() {
        every { secretManager.verify(any()) } returns false

        every { secretManager.secretIsSet() } returns true

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeDown())

        onView(withId(R.id.pattern_lock_view)).check(matches(isDisplayed()))
    }

    @Test
    fun twoCorrectPinUserInputsRemovesView() {
        every { secretManager.secretIsSet() } returns false

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .check(doesNotExist())
    }

    @Test
    fun twoIncorrectPinUserInputsDoesNotRemoveView() {
        every { secretManager.secretIsSet() } returns false


        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeRight())
                .perform(ViewActions.click())
                .perform(ViewActions.swipeDown())
                .check(matches(isDisplayed()))
    }


    @Test
    fun setFalsePinPatternCenterBottom() {
        every { secretManager.secretIsSet() } returns false

        mainActivityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.pattern_lock_view))
                .perform(swipeFromCenterToBottom())
                .perform(ViewActions.click())
                .perform(swipeFromCenterToBottom())
                .check(matches(isDisplayed()))
    }
}
