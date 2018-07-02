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
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger
import at.tugraz.tc.cyfile.note.NoteModule
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretModule
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsActivityInstrumentedTest : BaseInstrumentedTest() {

    @RelaxedMockK
    private lateinit var secretManager: SecretManager

    @Rule
    @JvmField
    var testRule = IntentsTestRule(SettingsActivity::class.java, true, false)

    @Before
    fun init() {
        app.component = DaggerApplicationComponent.builder()
                .appModule(AppModule(app, NoOpLogger()))
                .noteModule(NoteModule(mockk(relaxed = true)))
                .asyncModule(AsyncModule(mockk(relaxed = true)))
                .secretModule(SecretModule(secretManager,
                        mockk(relaxed = true),
                        mockk(relaxed = true))).build()

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
