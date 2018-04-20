package at.tugraz.tc.cyfile.ui;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import at.tugraz.tc.cyfile.AppModule;
import at.tugraz.tc.cyfile.BaseInstrumentedTest;
import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.injection.ApplicationComponent;
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;
import at.tugraz.tc.cyfile.secret.SecretPrompter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisplayNoteActivityInstrumentedTest extends BaseInstrumentedTest {

    @Mock
    private NoteService noteService;

    @Mock
    private SecretManager secretManager;

    @Rule
    public IntentsTestRule<DisplayNoteActivity> testRule =
            new IntentsTestRule<>(DisplayNoteActivity.class, true, false);

    private List<Note> testNotes =
            Arrays.asList(new Note("1", "name1", "content1")
                    , new Note("2", "name2", "content2"));


    @Before
    public void init() {
        ApplicationComponent applicationComponent
                = DaggerApplicationComponent.builder()
                .noteModule(new NoteModule(noteService))
                .secretModule(new SecretModule(secretManager, mock(SecretPrompter.class)))
                .appModule(mock(AppModule.class))
                .build();

        app.setComponent(applicationComponent);
        mockRepo(testNotes);
    }

    private void mockRepo(List<Note> testNotes) {
        when(noteService.findAll())
                .thenReturn(testNotes);

        for (Note note : testNotes) {
            when(noteService.findOne(note.getId()))
                    .thenReturn(note);
        }
    }


    /**
     * Instrumented test, which will execute on an Android device.
     * <p>
     * See [testing documentation](http://d.android.com/tools/testing).
     */
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("at.tugraz.tc.cyfile", appContext.getPackageName());
    }

    @Test
    public void testEditNote() {
        Intent startIntent = new Intent();
        Note editedNote = testNotes.get(0);
        startIntent.putExtra(MainActivity.NOTE_ID, editedNote.getId());
        testRule.launchActivity(startIntent);

        onView(withId(R.id.TEXT_TITLE)).check(matches(withText(editedNote.getTitle())));
        assertTrue(editedNote.getTitle().length() > 0);
    }

    @Test
    public void testSaveButton() {
        String newTitle = "this title is new";
        Note note = noteService.findAll().get(0);
        String oldTitle = note.getTitle();
        assertNotEquals(newTitle, note.getTitle());

        Intent startIntent = new Intent();
        startIntent.putExtra(MainActivity.NOTE_ID, note.getId());
        testRule.launchActivity(startIntent);

        onView(withId(R.id.TEXT_TITLE)).perform(replaceText(newTitle));
        onView(withId(R.id.BTN_SAVE)).perform(click());

        //assert changed title in list-view
        intended(hasComponent(MainActivity.class.getName()));
        onView(withText(newTitle)).check(matches(isDisplayed()));
        onView(withText(oldTitle)).check(doesNotExist());
    }

    public void testNotSavedBack() {

    }
}
