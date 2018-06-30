package at.tugraz.tc.cyfile.ui

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import at.tugraz.tc.cyfile.AppModule
import at.tugraz.tc.cyfile.BaseInstrumentedTest
import at.tugraz.tc.cyfile.R
import at.tugraz.tc.cyfile.async.AsyncModule
import at.tugraz.tc.cyfile.crypto.KeyVaultService
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger
import at.tugraz.tc.cyfile.note.NoteModule
import at.tugraz.tc.cyfile.note.NoteService
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretModule
import at.tugraz.tc.cyfile.secret.SecretPrompter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import java.util.concurrent.Executor

class SettingsActivityInstrumentedTest : BaseInstrumentedTest() {

    @Mock
    private val secretManager: SecretManager? = null

    @Rule
    @JvmField
    var testRule = IntentsTestRule(SettingsActivity::class.java, true, false)

    @Before
    fun init() {
        app.component = DaggerApplicationComponent.builder()
                .appModule(AppModule(app, NoOpLogger()))
                .noteModule(NoteModule(mock(NoteService::class.java)))
                .asyncModule(AsyncModule(mock(Executor::class.java)))
                .secretModule(SecretModule(secretManager!!, mock(SecretPrompter::class.java), mock(KeyVaultService::class.java))).build()

    }

    @Test
    @Throws(Exception::class)
    fun settingsAboutTest() {

        testRule.launchActivity(Intent())

        onView(withId(R.id.settings_about)).perform(click())

        onView(withText(R.string.about_string)).check(matches(isDisplayed()))
    }

    @Test
    @Throws(Exception::class)
    fun settingsHelpTest() {

        testRule.launchActivity(Intent())

        onView(withId(R.id.settings_help)).perform(click())

        onView(withText(R.string.help_string)).check(matches(isDisplayed()))
    }

    @Test
    @Throws(Exception::class)
    fun settingsContactTest() {

        testRule.launchActivity(Intent())

        onView(withId(R.id.settings_contact)).perform(click())

        onView(withText(R.string.contact_string)).check(matches(isDisplayed()))
    }

    @Test
    @Throws(Exception::class)
    fun settingsLicensesTest() {

        testRule.launchActivity(Intent())

        onView(withId(R.id.settings_licenses)).perform(click())

        onView(withText(R.string.licenses_string)).check(matches(isDisplayed()))
    }
}
