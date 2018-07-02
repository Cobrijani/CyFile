package at.tugraz.tc.cyfile

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.GeneralLocation
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.PositionAssertions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import at.tugraz.tc.cyfile.async.AsyncModule
import at.tugraz.tc.cyfile.crypto.impl.DummyKeyVaultService
import at.tugraz.tc.cyfile.crypto.impl.NoOpCryptoService
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger
import at.tugraz.tc.cyfile.note.NoteModule
import at.tugraz.tc.cyfile.note.NoteRepository
import at.tugraz.tc.cyfile.note.impl.SecureNoteService
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretModule
import at.tugraz.tc.cyfile.secret.SecretPrompter
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity
import at.tugraz.tc.cyfile.ui.SettingsActivity
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * Created by kina on 11.04.18.
 */
class MainActivityInstrumentedTest : BaseInstrumentedTest() {

    @RelaxedMockK
    private lateinit var secretManager: SecretManager
    @RelaxedMockK
    private lateinit var noteRepository: NoteRepository
    @RelaxedMockK
    private lateinit var secretPrompter: SecretPrompter

    @Rule
    @JvmField
    var mActivityRule = IntentsTestRule(MainActivity::class.java, true, false)

    @Before
    fun init() {
        val applicationComponent = DaggerApplicationComponent.builder()
                .noteModule(NoteModule(SecureNoteService(noteRepository, NoOpCryptoService())))
                .asyncModule(AsyncModule(mockk(relaxed = true)))
                .secretModule(SecretModule(secretManager, secretPrompter,
                        DummyKeyVaultService()))
                .appModule(AppModule(app, NoOpLogger()))
                .build()

        app.component = applicationComponent

        every { secretManager.verify(any()) } returns true
    }

    @Test
    fun testNoNotesPresent() {

        every { noteRepository.findAll() } returns emptyList()

        mActivityRule.launchActivity(Intent())

        onView(withId(R.id.noteList))
                .check(matches(hasChildCount(0)))


    }

    private fun mockRepo(testNotes: List<Note>) {
        every { noteRepository.findAll() } returns testNotes

        for (note in testNotes) {
            every { noteRepository.findOne(note.id) } returns note
        }
    }

    @Test
    fun testSearchNotes() {
        mockRepo(Arrays.asList(Note("1", "name1", "content1", Date().time - 100, Date().time - 100), Note("2", "name2", "content2", Date().time + 100, Date().time + 100),
                Note("3", "name3", "content3", Date().time, Date().time)))


        mActivityRule.launchActivity(Intent())

        onView(withId(R.id.search_button))
                .perform(click())

        onView(withId(R.id.search_src_text))
                .perform(typeText("name3"))

        onView(withText("content1"))
                .check(doesNotExist())

        onView(withText("content2"))
                .check(doesNotExist())

        onView(withText("content3"))
                .check(matches(isDisplayed()))

    }


    @Test
    fun testNotesSortedByDateTime() {

        mockRepo(Arrays.asList(Note("1", "name1", "content1", Date().time - 100, Date().time - 100), Note("2", "name2", "content2", Date().time + 100, Date().time + 100),
                Note("3", "name3", "content3", Date().time, Date().time)))


        mActivityRule.launchActivity(Intent())


        onView(withText("name1"))
                .check(PositionAssertions.isCompletelyBelow(withText("name2")))
        onView(withText("name3"))
                .check(PositionAssertions.isCompletelyBelow(withText("name2")))
        onView(withText("name1"))
                .check(PositionAssertions.isCompletelyBelow(withText("name3")))


        //        name2 -> name3 -> name1

    }


    @Test
    fun testTwoNotesPresent() {
        mockRepo(Arrays.asList(Note("1", "name1", "content1", 0L, 0L), Note("2", "name2", "content2", 0L, 0L)))

        mActivityRule.launchActivity(Intent())

        onView(withId(R.id.noteList))
                .check(matches(hasChildCount(2)))
    }

    @Test
    fun buttonsTest() {

        every { noteRepository.findAll() } returns Arrays.asList(Note("1", "name1", "content1", 0L, 0L), Note("2", "name2", "content2", 0L, 0L))

        every { noteRepository.findOne("1") } returns Note("1", "name1", "content1", 0L, 0L)

        mActivityRule.launchActivity(Intent())

        onView(withText("name1"))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withId(R.id.noteList))
                .check(doesNotExist())
        intended(hasComponent(DisplayNoteActivity::class.java.name))
    }

    @Test
    fun testEditNote() {
        val testNotes = Arrays.asList(Note("1", "name1", "content1", 0L, 0L), Note("2", "name2", "content2", 0L, 0L))
        mockRepo(testNotes)

        mActivityRule.launchActivity(Intent())

        val title = testNotes[0].title
        onView(withText(title))
                .check(matches(isDisplayed()))

        onView(withText(title))
                .perform(click())

        onView(withId(R.id.noteList))
                .check(doesNotExist())

        intended(hasComponent(DisplayNoteActivity::class.java.name))
    }

    @Test
    @Ignore
    fun testDeleteDialogNoteSwipeLeft() {
        val testNotes = Arrays.asList(Note("1", "name1", "content1", 0L, 0L), Note("2", "name2", "content2", 0L, 0L))
        mockRepo(testNotes)

        mActivityRule.launchActivity(Intent())

        val title = testNotes[0].title
        val content = testNotes[0].content
        onView(withText(title))
                .check(ViewAssertions.matches(isDisplayed()))

        onView(withText(content))
                .perform(GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_LEFT,
                        GeneralLocation.CENTER_RIGHT, Press.THUMB))


        onView(withText("Are you sure you want to delete")).check(matches(isDisplayed()))

    }

    @Test
    fun settingsTest() {

        mActivityRule.launchActivity(Intent())

        onView(withId(R.id.action_settings)).perform(click())

        intended(hasComponent(SettingsActivity::class.java.name))
    }

}