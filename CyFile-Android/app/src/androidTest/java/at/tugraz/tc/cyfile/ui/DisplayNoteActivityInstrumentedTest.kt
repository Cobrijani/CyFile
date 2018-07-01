package at.tugraz.tc.cyfile.ui

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import at.tugraz.tc.cyfile.AppModule
import at.tugraz.tc.cyfile.BaseInstrumentedTest
import at.tugraz.tc.cyfile.MainActivity
import at.tugraz.tc.cyfile.R
import at.tugraz.tc.cyfile.async.AsyncModule
import at.tugraz.tc.cyfile.crypto.impl.NoOpCryptoService
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger
import at.tugraz.tc.cyfile.note.NoteModule
import at.tugraz.tc.cyfile.note.impl.InMemoryNoteRepository
import at.tugraz.tc.cyfile.note.impl.SecureNoteService
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretModule
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class DisplayNoteActivityInstrumentedTest : BaseInstrumentedTest() {

    @RelaxedMockK
    private lateinit var secretManager: SecretManager

    @Rule
    @JvmField
    var mainActivityActivityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Rule
    @JvmField
    var testRule = IntentsTestRule(DisplayNoteActivity::class.java, true, false)

    private val testNotes = Arrays.asList(Note("1", "name1", "content1", 0L, 0L), Note("2", "name2", "content2", 0L, 0L))


    @Before
    fun init() {
        val applicationComponent = DaggerApplicationComponent.builder()
                .noteModule(NoteModule(SecureNoteService(InMemoryNoteRepository(HashSet(testNotes)), NoOpCryptoService())))
                .secretModule(SecretModule(secretManager,
                        mockk(relaxed = true),
                        mockk(relaxed = true)))
                .appModule(AppModule(app, NoOpLogger()))
                .asyncModule(AsyncModule(mockk(relaxed = true)))
                .build()

        app.component = applicationComponent

        mainActivityActivityTestRule.launchActivity(Intent())
    }

    /**
     * Instrumented test, which will execute on an Android device.
     *
     *
     * See [testing documentation](http://d.android.com/tools/testing).
     */
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("at.tugraz.tc.cyfile", appContext.packageName)
    }

    @Test
    fun testViewNote() {
        val startIntent = Intent()
        val editedNote = testNotes[0]
        startIntent.putExtra(MainActivity.NOTE_ID, editedNote.id)

        testRule.launchActivity(startIntent)

        onView(withId(R.id.NOTE_TITLE)).check(matches(withText(editedNote.title)))
        onView(withId(R.id.NOTE_CONTENT)).check(matches(withText(editedNote.content)))
        assertTrue(editedNote.title.isNotEmpty())
    }

    @Test
    fun testDeleteButton() {
        val startIntent = Intent()
        val note = testNotes[0]
        startIntent.putExtra(MainActivity.NOTE_ID, note.id)

        testRule.launchActivity(startIntent)

        onView(withId(R.id.BTN_DEL))
                .perform(click())

        onView(withText(note.title))
                .check(doesNotExist())

    }

    @Test
    fun testSaveButton() {
        val startIntent = Intent()
        val note = testNotes[0]
        startIntent.putExtra(MainActivity.NOTE_ID, note.id)

        testRule.launchActivity(startIntent)

        onView(withId(R.id.NOTE_TITLE))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("New title"))
                .perform(closeSoftKeyboard())

        onView(withId(R.id.NOTE_CONTENT))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("Next text"))
                .perform(closeSoftKeyboard())

        onView(withId(R.id.BTN_SAVE))
                .perform(click())

        onView(withText("New title"))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withText("Next text"))
                .check(matches(isDisplayed()))
    }

    @Test
    fun testBackDialogNewNoteNothingChanged() {
        val startIntent = Intent()
        // New Note created

        testRule.launchActivity(startIntent)

        Espresso.pressBack()

        onView(withText("Are you sure you want to leave without saving?")).check(doesNotExist())
    }

    @Test
    fun testBackDialogNewNoteChanged() {
        val startIntent = Intent()

        testRule.launchActivity(startIntent)

        onView(withId(R.id.NOTE_TITLE))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("New title"))
                .perform(closeSoftKeyboard())

        onView(withId(R.id.NOTE_CONTENT))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("Next text"))
                .perform(closeSoftKeyboard())

        Espresso.pressBack()

        onView(withText("Are you sure you want to leave without saving?")).check(matches(isDisplayed()))
    }

    @Test
    fun testBackDialogExistingNoteNothingChanged() {
        val startIntent = Intent()
        val note = testNotes[0]
        startIntent.putExtra(MainActivity.NOTE_ID, note.id)

        testRule.launchActivity(startIntent)

        Espresso.pressBack()

        onView(withText("Are you sure you want to leave without saving?")).check(doesNotExist())
    }

    @Test
    fun testBackDialogExistingNoteChanged() {
        val startIntent = Intent()
        val note = testNotes[0]
        startIntent.putExtra(MainActivity.NOTE_ID, note.id)

        testRule.launchActivity(startIntent)

        onView(withId(R.id.NOTE_TITLE))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("New title"))
                .perform(closeSoftKeyboard())

        onView(withId(R.id.NOTE_CONTENT))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("Next text"))
                .perform(closeSoftKeyboard())

        Espresso.pressBack()

        onView(withText("Are you sure you want to leave without saving?")).check(matches(isDisplayed()))

    }


    @Test
    fun testNotSavedBack() {
        val startIntent = Intent()
        val note = testNotes[0]
        startIntent.putExtra(MainActivity.NOTE_ID, note.id)

        testRule.launchActivity(startIntent)

        onView(withId(R.id.NOTE_TITLE))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("New title"))
                .perform(closeSoftKeyboard())
                .perform(pressBack())

        onView(withText(R.string.yes))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withText(note.title))
                .check(matches(isDisplayed()))

    }
}
