package at.tugraz.tc.cyfile;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.DaggerNoteComponent;
import at.tugraz.tc.cyfile.note.NoteComponent;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.DaggerSecretComponent;
import at.tugraz.tc.cyfile.secret.SecretComponent;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class DisplayNoteActivityTest {
    @Mock
    private NoteService noteService;

    @Mock
    private SecretManager secretManager;

    @Rule
    public IntentsTestRule<MainActivity> testRule =
            new IntentsTestRule<>(MainActivity.class);

    private List<Note> testNotes =
            Arrays.asList(new Note("1", "name1", "content1")
                    , new Note("2", "name2", "content2"));


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        CyFileApplication app = (CyFileApplication)
                InstrumentationRegistry
                        .getInstrumentation()
                        .getTargetContext()
                        .getApplicationContext();

        NoteComponent mockedComponent = DaggerNoteComponent
                .builder()
                .noteModule(new NoteModule(noteService))
                .build();

        SecretComponent mockedSecretComp
                = DaggerSecretComponent
                .builder()
                .secretModule(new SecretModule(secretManager))
                .build();

        app.setmNoteComponent(mockedComponent);
        app.setSecretVerifierComponent(mockedSecretComp);

        when(secretManager.verify(any()))
                .thenReturn(true);

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

    public void testEditNote() {
        Intent startIntent = new Intent();
        Note editedNote = testNotes.get(0);
        startIntent.putExtra(MainActivity.NOTE_ID, editedNote.getId());
        testRule.launchActivity(startIntent);

        onView(withId(R.id.TEXT_TITLE)).check(matches(withText(editedNote.getTitle())));
        assertTrue(editedNote.getTitle().length() > 0);
    }

    //TODO not done yet
    public void testSaveButton() {

    }

    public void testNotSavedBack() {

    }
}
